package demo.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.google.gson.Gson;
import demo.common.Constants;
import demo.model.QingResp;
import demo.service.IQingConfigService;
import demo.service.ISpiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Alex
 */
@Slf4j
@WebFilter(urlPatterns = "/test/*")
public class QingSpiFilter implements Filter {
    @Autowired
    private IQingConfigService qingConfigService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String timestamp = req.getParameter("timestamp");
        String signature = req.getParameter("signature");


        if (StrUtil.isBlank(timestamp) ) {
            respJson(response, QingResp.error("请求无效"));
            return;
        }

        //验证消息有效期
        if ((LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() - Long.parseLong(timestamp)) > 3000) {
            // 如果时间误差大于3秒则认为请求过期,不处理.  对接者可自行选择是否需要处理
            log.error("消息过期: {}", new Gson().toJson(req.getParameterMap()));
            respJson(response, QingResp.error("消息过期"));
        }
        //验证消息有效期
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
        chain.doFilter(request,response);
    }


    private void respJson(ServletResponse response, Object obj) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(new Gson().toJson(obj));
    }

}
