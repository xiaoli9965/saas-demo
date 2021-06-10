package demo.service;

import javax.servlet.http.HttpServletRequest;

public interface IQingConfigService {

    /**
     * 比对签名
     *
     * @param reqSignature 请求过来签名
     * @param req          请求参数
     * @return 比对结果
     */
    Boolean checkSignature(String reqSignature, HttpServletRequest req);

    /**
     * 获取青云app key
     *
     * @return key
     */
    String getQinAppSecretKey();

    /**
     * 更新青云app key
     * @return ok
     */
    Boolean updateQinAppSecretKey();

}
