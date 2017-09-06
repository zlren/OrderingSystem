package lab.zlren.sell.common.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lab.zlren.sell.common.enums.ResultEnum;
import lab.zlren.sell.common.exception.SellException;
import lab.zlren.sell.pojo.OrderDetail;
import lab.zlren.sell.pojo.OrderMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 买家创建订单的数据
 * Created by zlren on 17/9/5.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class OrderDTO {

    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    List<OrderDetail> orderDetailList;

    public OrderDTO(OrderMaster orderMaster) {
        BeanUtils.copyProperties(orderMaster, this);
    }

    public OrderDTO(OrderForm orderForm) {
        this.buyerName = orderForm.getName();
        this.buyerAddress = orderForm.getAddress();
        this.buyerPhone = orderForm.getPhone();
        this.buyerOpenid = orderForm.getOpenid();

        List<OrderDetail> orderDetailList;
        try {
            orderDetailList = new Gson().fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>() {
                    }.getType());
        } catch (Exception e) {
            log.error("转换错误, items = {}", orderForm.getItems());
            throw new SellException(ResultEnum.PARAMETER_ERROR);
        }
        this.setOrderDetailList(orderDetailList);
    }
}
