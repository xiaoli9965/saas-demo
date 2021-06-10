package demo.controller;

import com.google.gson.Gson;
import demo.common.Constants;
import demo.model.QingResp;
import demo.model.vo.QingError;
import demo.service.IQingConfigService;
import demo.service.ISpiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * 青云SAAS SPI接口调用示例
 * <p>
 * 编写SPI前请详细阅读对接指南,
 * 1. 青云SPI接口有时效性要求,需要在规定时间内作出响应,请自行判断使用同步还是异步方式
 * 2. 响应状态码必须为200,响应体需为JSON
 *
 * @author Alex
 */
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private ISpiService service;
    @Autowired
    private IQingConfigService qingConfigService;

    /**
     * 验证签名 栗子🌰
     */
    @GetMapping("/spi/signature")
    public ResponseEntity<String> qingSpiSignature(HttpServletRequest req,
                                                   @RequestParam(required = false, name = "signature") String signature) {
        if (signature == null) {
            // signature参数也可以通过请求头获取
            signature = req.getHeader(Constants.QING_HEAD_SIGNATURE_KEY);
        }
        boolean isOk = qingConfigService.checkSignature(signature, req);
        if (!isOk) {
            return new ResponseEntity<>("签名验证失败", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("签名验证成功", HttpStatus.OK);
    }

    /**
     * 简易版spi实现
     *
     * @param action    action
     * @param req       请求对象
     * @param signature 签名
     * @return ok
     */
    @GetMapping("/spi")
    public ResponseEntity<QingResp> qingSpi(String action, HttpServletRequest req,
                                            @RequestParam(name = "timestamp") Long timestamp,
                                            @RequestParam(name = "signature") String signature) throws InterruptedException {

        //验证消息有效期
        if ((LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() - timestamp) > 3000) {
            // 如果时间误差大于3秒则认为请求过期,不处理.  对接者可自行选择是否需要处理
            log.error("消息过期: {}", new Gson().toJson(req.getParameterMap()));
            QingResp resp = new QingError().setMessage("消息过期").setData(req.getParameterMap());
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        //验证消息有效期
        if (signature == null) {
            signature = req.getHeader(Constants.QING_HEAD_SIGNATURE_KEY);
        }
        // 验证签名
        boolean isOk = qingConfigService.checkSignature(signature, req);
        if (!isOk) {
            log.error("签名无效: {}", new Gson().toJson(req.getParameterMap()));
            QingResp resp = new QingError().setMessage("签名无效").setData(req.getParameterMap());
            return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }

        // 业务逻辑
        switch (action) {
            case Constants.ACTION_CREATE:
                return service.create(req);
            case Constants.ACTION_RENEW:
                return service.renew(req);
            case Constants.ACTION_UPGRADE:
                return service.upgread(req);
            default:
                log.error("未知的action: {}", new Gson().toJson(req.getParameterMap()));
                QingResp resp = new QingError().setMessage("未知的action").setData(req.getParameterMap());
                return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
        }
    }

}
