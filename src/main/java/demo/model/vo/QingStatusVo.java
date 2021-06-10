package demo.model.vo;

import demo.model.QingResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Alex
 */
 @Data
@AllArgsConstructor
public class QingStatusVo implements QingResp {
    private Boolean success;
}
