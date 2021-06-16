package demo.controller;

import com.google.gson.Gson;
import demo.common.Constants;
import demo.model.QingResp;
import demo.model.vo.QingRequest;
import demo.service.IQingConfigService;
import demo.service.ISpiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    private IQingConfigService qingConfigService;


    @Autowired
    private ISpiService service;

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
     * @return ok
     */
    @GetMapping("spi")
    public ResponseEntity<QingResp> qingSpi(QingRequest req) {
        // 业务逻辑
        switch (req.getAction()) {
            case Constants.ACTION_CREATE:
                return service.create(req);
            case Constants.ACTION_RENEW:
                return service.renew(req);
            case Constants.ACTION_UPGRADE:
                return service.upgrade(req);
            default:
                return new ResponseEntity<>(QingResp.error("未知的action"), HttpStatus.BAD_REQUEST);
        }
    }

}
