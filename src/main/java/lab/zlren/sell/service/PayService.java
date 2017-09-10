package lab.zlren.sell.service;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lab.zlren.sell.common.bean.OrderDTO;
import lab.zlren.sell.common.exception.SellException;
import lab.zlren.sell.pojo.OrderMaster;
import lab.zlren.sell.util.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zlren on 17/9/10.
 */
@Service
@Slf4j
public class PayService {

    private static final String ORDER_NAME = "微信点餐订单";

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private OrderMasterService orderMasterService;


    public PayResponse create(OrderDTO orderDTO) {

        PayRequest payRequest = new PayRequest();
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOrderName(ORDER_NAME);
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);

        return this.bestPayService.pay(payRequest);
    }

    /**
     * 回调
     *
     * @param notifyData
     * @return
     */
    public PayResponse notify(String notifyData) {

        // 验证签名
        // 支付状态 前两点sdk做了

        PayResponse payResponse = this.bestPayService.asyncNotify(notifyData);

        // 收到支付的异步通知
        // 成功的话修改订单的支付状态
        String orderId = payResponse.getOrderId();
        OrderMaster record = new OrderMaster();
        record.setOrderId(orderId);
        OrderMaster orderMaster = this.orderMasterService.queryOne(record);

        if (orderMaster == null) {
            // 订单不存在
            throw new SellException("订单不存在");
        }

        // 金额是否一致
        // 注意比较的时候要考虑类型！！ 0.1 0.10
        // 这里的实现方式是相减后小于一定的阈值，如果小于就认为相等
        if (MathUtil.equal(orderMaster.getOrderAmount().doubleValue(), payResponse.getOrderAmount())) {
            throw new SellException("订单金额不一致");
        }

        // 修改支付状态
        this.orderMasterService.pay(new OrderDTO(orderMaster));

        return payResponse;
    }
}
