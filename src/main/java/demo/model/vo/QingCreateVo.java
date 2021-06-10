package demo.model.vo;

import demo.model.QingResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author Alex
 */
@Accessors(chain = true)
 @Data
public class QingCreateVo implements QingResp {
    private String instanceId;
    private QingUserInfo userInfo;
}
