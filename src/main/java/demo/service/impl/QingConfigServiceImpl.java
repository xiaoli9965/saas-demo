package demo.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import demo.common.Constants;
import demo.service.IQingConfigService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Alex
 */
@Service
public class QingConfigServiceImpl implements IQingConfigService {
    private static final String QING_SECRET_APP_KEY = "2UNHjKQE9u6A4np4kI++o5iXPcn3cA6yZjDEdHN3iXfDFwCRaKtCQHSLT9HdeF+1";

    @Override
    public String getQinAppSecretKey() {
        // 1. 从数据库加载 QING_SECRET_APP_KEY
        // 2. 返回
        return QING_SECRET_APP_KEY;
    }

    @Override
    public Boolean updateQinAppSecretKey() {
        // 1. 请求青云更新接口,获得新的 QING_SECRET_APP_KEY
        // 2. 入库
        return true;
    }

    @Override
    public Boolean checkSignature(String reqSignature, HttpServletRequest req) {
        if (reqSignature == null || "".equals(reqSignature)) {
            return false;
        }

        Map<String, String[]> parameterMap = req.getParameterMap();

        // 排序参数名, 字典顺序
        List<String> paramsNames = parameterMap.keySet()
                .stream()
                .filter(k -> !Constants.QING_REQ_SIGNATURE_KEY.equals(k))
                .sorted()
                .collect(Collectors.toList());

        // 拼接签名串
        StringBuilder builder = new StringBuilder(req.getMethod()).append("\n");
        for (String name : paramsNames) {
            for (String val : parameterMap.get(name)) {
                builder.append(name).append("=").append(val).append("&");
            }
        }
        String toSign = builder.deleteCharAt(builder.length() - 1).toString();

        HMac mac = new HMac(HmacAlgorithm.HmacSHA256, getQinAppSecretKey().getBytes());
        String encodeSignature = Base64.encode(mac.digest(toSign));

        return encodeSignature.equals(reqSignature);
    }
}
