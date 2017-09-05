package lab.zlren.sell.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by zlren on 17/9/4.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO<T> {
    private Integer code;
    private String msg;
    private T data; // 返回的具体内容
}
