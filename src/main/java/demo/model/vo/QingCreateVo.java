package demo.model.vo;

import demo.model.QingResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashMap;

/**
 * @author Alex
 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class QingCreateVo extends QingResp {
    private String instanceId;
    private QingAppInfo appInfo;

}
