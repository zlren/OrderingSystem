package lab.zlren.sell.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by zlren on 17/9/5.
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {

    PRODUCT_NOT_EXIST(10, "商品不存在"),
    STOCK_NOT_ENOUGH(11, "库存不够"),
    ORDER_NOT_EXIST(12, "订单不存在"),
    ORDER_DETAIL_NOT_EXIST(13, "订单详情不存在"),
    ORDER_STATUS_ERROR(14, "订单状态错误"),
    PARAMETER_ERROR(15, "参数不正确");

    private Integer code;
    private String message;
}
