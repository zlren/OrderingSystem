package lab.zlren.sell.common.exception;

import lab.zlren.sell.common.enums.ResultEnum;

/**
 * Created by zlren on 17/9/5.
 */
public class SellException extends RuntimeException {

    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public SellException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public SellException(String message) {
        super(message);
    }
}
