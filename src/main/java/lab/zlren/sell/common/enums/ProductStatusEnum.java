package lab.zlren.sell.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 商品状态
 */
@Getter
@AllArgsConstructor
public enum ProductStatusEnum {

    UP(0, "在架"), dOWN(1, "下架");

    private Integer code;
    private String message;
}
