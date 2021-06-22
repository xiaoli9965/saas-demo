package demo.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.google.gson.Gson;
import demo.common.Constants;
import demo.model.QingResp;
import demo.service.IQingConfigService;
import demo.service.ISpiService;
import demo.utils.DevUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Alex
 */
@Slf4j
@WebFilter(urlPatterns = "/test/*")
public class QingSpiFilter implements Filter {
    @Autowired
    private IQingConfigService qingConfigService;

    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

//    static String formatIso8601Date(Date date) {
//        df.setTimeZone(TimeZone.getTimeZone("GMT"));
//        return df.format(date);
//    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String timestamp = req.getParameter("time_stamp");
        String signature = req.getParameter("signature");

        if (StrUtil.isBlank(timestamp)) {
            respJson(response, QingResp.error("请求无效"));
            return;
        }
        timestamp = URLDecoder.decode(timestamp, Constants.URL_ENCODE);
        LocalDateTime reqDate = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        Duration duration = Duration.between(reqDate, LocalDateTime.now(ZoneOffset.UTC));

        //验证消息有效期
        if (duration.toMillis() > Constants.MESSAGE_VALIDITY_PERIOD) {
            // 如果时间误差大于3秒则认为请求过期,不处理.  对接方可自行选择是否需要处理
            log.error("消息过期: {}", new Gson().toJson(req.getParameterMap()));
            respJson(response, QingResp.error("消息过期"));
            return;
        }

        if (signature == null) {
            signature = req.getHeader(Constants.QING_HEAD_SIGNATURE_KEY);
        }

        // 验证签名
        boolean isOk = qingConfigService.checkSignature(signature, req);
        if (!isOk) {
            log.error("签名无效: {}", new Gson().toJson(req.getParameterMap()));
            respJson(response, QingResp.error("签名无效"));
            return;
        }
        chain.doFilter(request, response);
    }


    private void respJson(ServletResponse response, Object obj) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(new Gson().toJson(obj));
    }

}
