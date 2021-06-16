package demo.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import demo.model.QingResp;
import demo.model.vo.QingAppInfo;
import demo.model.vo.QingCreateVo;
import demo.model.vo.QingRequest;
import demo.model.vo.QingInstanceInfo;
import demo.service.ISpiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author Alex
 */
@Slf4j
@Service
public class QingSpiServiceImpl implements ISpiService {


    @Override
    public ResponseEntity<QingResp> create(QingRequest req) {
        log.info("用户开通: {}", req);
        log.info("套餐名: {} >>> {}", req.getSpec(), Base64.decodeStr(req.getSpec()));

        if (req.getErr()) {
            return new ResponseEntity<>(QingResp.error("开通失败"), HttpStatus.OK);
        }
        QingCreateVo create = new QingCreateVo();
        create.setInstanceId("i-" + RandomUtil.randomString(8));
        create.setSuccess(true);
        QingAppInfo appInfo = new QingAppInfo();
        appInfo.setFrontEnd(new QingInstanceInfo()
                .setUrl("https://console.saas.com")
                .setUsername("fn-" + RandomUtil.randomString(8))
                .setPassword(RandomUtil.randomString(8)
                ));
        appInfo.setAdmin(new QingInstanceInfo()
                .setUrl("https://admin.saas.com")
                .setUsername("ad-" + RandomUtil.randomString(10))
                .setPassword(RandomUtil.randomString(10)
                ));
        appInfo.setAuthUrl("https://auth.saas.com");
        create.setAppInfo(appInfo);
        return new ResponseEntity<>(create, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QingResp> renew(QingRequest req) {
        log.info("用户续费: {}", req);
        return new ResponseEntity<>(QingResp.ok(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QingResp> upgrade(QingRequest req) {
        log.info("用户升级: {}", req);
        return new ResponseEntity<>(QingResp.ok(), HttpStatus.OK);
    }

}
