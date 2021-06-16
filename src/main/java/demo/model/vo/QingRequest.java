package demo.model.vo;

import lombok.Data;

/**
 * @author Alex
 */
@Data
public class QingRequest {
    private String action;
    private String userId;
    private String appId;
    private String spec;
    private String period;
    private String cloudInfo;
    private String signature;
    private String timestamp;

    private Boolean err;
}
