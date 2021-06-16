package demo.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Alex
 */
@Data
@Accessors(chain = true)
public class QingResp {
    private Boolean success;
    private String message;

    public static QingResp ok(){
        return new QingResp().setSuccess(true);
    }
    public static QingResp error(String msg){
        return new QingResp().setSuccess(false).setMessage(msg);
    }
}
