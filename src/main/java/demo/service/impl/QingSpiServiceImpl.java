package demo.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.google.gson.Gson;
import demo.model.QingResp;
import demo.model.vo.QingCreateVo;
import demo.model.vo.QingStatusVo;
import demo.model.vo.QingUserInfo;
import demo.service.IQingConfigService;
import demo.service.ISpiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author Alex
 */
@Slf4j
@Service
public class QingSpiServiceImpl implements ISpiService {


    @Override
    public ResponseEntity<QingResp> create(HttpServletRequest req) {
        log.info("用户开通: {}", new Gson().toJson(req.getParameterMap()));

        QingCreateVo create = new QingCreateVo();
        create.setInstanceId("i-" + RandomUtil.randomString(8));

        QingUserInfo userInfo = new QingUserInfo()
                .setAdminUrl("http://admin.saas.com")
                .setAuthUrl("http://auth.saas.com")
                .setFrontEndUrl("http://console.saas.com")
                .setUsername("saas-" + RandomUtil.randomString(8))
                .setPassword(RandomUtil.randomString(12));

        create.setUserInfo(userInfo);
        return new ResponseEntity<>(create, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QingResp> renew(HttpServletRequest req) {
        log.info("用户续费: {}", new Gson().toJson(req.getParameterMap()));
        return new ResponseEntity<>(new QingStatusVo(true), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<QingResp> upgread(HttpServletRequest req) {
        log.info("用户升级: {}", new Gson().toJson(req.getParameterMap()));
        return new ResponseEntity<>(new QingStatusVo(true), HttpStatus.OK);
    }

}
