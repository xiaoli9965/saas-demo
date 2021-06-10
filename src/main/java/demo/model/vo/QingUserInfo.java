package demo.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class QingUserInfo {

    /**
     * 前台地址
     */
    private String frontEndUrl;
    /**
     * 管理地址
     */
    private String adminUrl;
    /**
     * 分配的账号
     */
    private String username;
    /**
     * 分配的密码
     */
    private String password;
    /**
     * 免登授权接口
     */
    private String authUrl;

}
