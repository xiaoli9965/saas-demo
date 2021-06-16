package demo.service;

import demo.model.QingResp;
import demo.model.vo.QingRequest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface ISpiService {

    /**
     * 创建实例
     *
     */
    ResponseEntity<QingResp> create(QingRequest req);

    /**
     * 续费实例
     *
     */
    ResponseEntity<QingResp>  renew(QingRequest req);

    /**
     * 升级实例
     *
     */
    ResponseEntity<QingResp>  upgrade(QingRequest req);
}
