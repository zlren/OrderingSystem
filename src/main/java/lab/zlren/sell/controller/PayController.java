package lab.zlren.sell.controller;

import com.lly835.bestpay.model.PayResponse;
import lab.zlren.sell.common.bean.OrderDTO;
import lab.zlren.sell.common.exception.SellException;
import lab.zlren.sell.pojo.OrderMaster;
import lab.zlren.sell.service.OrderMasterService;
import lab.zlren.sell.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created by zlren on 17/9/7.
 */
@Controller
@RequestMapping("pay")
public class PayController {

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private PayService payService;

    @GetMapping("create")
    public ModelAndView create(@RequestParam("orderId") String orderId, @RequestParam("returnUrl") String returnUrl,
                               Map<String, Object> map) {

        OrderMaster record = new OrderMaster();
        record.setOrderId(orderId);
        OrderMaster orderMaster = this.orderMasterService.queryOne(record);

        if (orderMaster == null) {
            throw new SellException("订单不存在");
        }

        // 发起支付
        PayResponse payResponse = this.payService.create(new OrderDTO(orderMaster));
        map.put("payResponse", payResponse);
        map.put("returnUrl", returnUrl);

        return new ModelAndView("pay/create", map);
    }


    @PostMapping("notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        this.payService.notify(notifyData);

        // 我们处理好以后，要把结果返回给微信处理结果，否则微信会不断地调异步通知的方法
        return new ModelAndView("pay/success");
    }

}
