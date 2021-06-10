package demo.model.vo;

import demo.model.QingResp;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class QingError implements QingResp {
    private String message;
    private Object data;
}
