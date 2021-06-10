package demo.service;

import demo.model.QingResp;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface ISpiService {

    /**
     * 创建实例
     *
     * @return
     */
    ResponseEntity<QingResp> create(HttpServletRequest req);

    /**
     * 续费实例
     *
     * @return
     */
    ResponseEntity<QingResp>  renew(HttpServletRequest req);

    /**
     * 升级实例
     *
     * @return
     */
    ResponseEntity<QingResp>  upgread(HttpServletRequest req);
}
