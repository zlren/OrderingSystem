package lab.zlren.sell.controller;

import com.github.pagehelper.PageInfo;
import lab.zlren.sell.common.bean.OrderDTO;
import lab.zlren.sell.common.enums.ResultEnum;
import lab.zlren.sell.common.exception.SellException;
import lab.zlren.sell.pojo.OrderMaster;
import lab.zlren.sell.service.OrderMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zlren on 17/9/19.
 */
@Controller
@RequestMapping("seller/order")
@Slf4j
public class SellerOrderController {

    @Autowired
    private OrderMasterService orderMasterService;


    /**
     * 订单列表
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size) {

        PageInfo<OrderMaster> orderMasterPageInfo = this.orderMasterService.queryPageListByWhere(page, size, null);

        Map<String, Object> result = new HashMap<>();
        result.put("orderDTOPage", orderMasterPageInfo);
        result.put("currentPage", page);
        result.put("size", size);

        return new ModelAndView("order/list", result);
    }

    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    @GetMapping("cancel")
    public ModelAndView cancel(@RequestParam("orderId") String orderId) {

        Map<String, Object> map = new HashMap<>();

        OrderMaster record = new OrderMaster();
        record.setOrderId(orderId);
        OrderMaster orderMaster = this.orderMasterService.queryOne(record);

        if (orderMaster == null) {
            log.error("卖家端取消订单，查不到订单");

            map.put("msg", ResultEnum.ORDER_NOT_EXIST.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error", map);
        }

        this.orderMasterService.cancel(new OrderDTO(orderMaster));

        map.put("msg", "订单取消成功");
        map.put("url", "/sell/seller/order/list");
        return new ModelAndView("common/success", map);
    }

    /**
     * 订单详情
     *
     * @param orderId
     * @param map
     * @return
     */
    @GetMapping("/detail")
    public ModelAndView detail(@RequestParam("orderId") String orderId, Map<String, Object> map) {

        OrderMaster record = new OrderMaster();
        OrderMaster orderMaster;
        try {
            record.setOrderId(orderId);
            orderMaster = this.orderMasterService.queryOne(record);
        } catch (SellException e) {
            log.error("【卖家端查询订单详情】发生异常{}", e);
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error", map);
        }

        map.put("orderDTO", new OrderDTO(orderMaster));
        return new ModelAndView("order/detail", map);
    }


    /**
     * 完结订单
     *
     * @param orderId
     * @param map
     * @return
     */
    @GetMapping("/finish")
    public ModelAndView finished(@RequestParam("orderId") String orderId, Map<String, Object> map) {

        try {
            OrderMaster record = new OrderMaster();
            record.setOrderId(orderId);
            OrderMaster orderMaster = this.orderMasterService.queryOne(record);
            this.orderMasterService.finish(new OrderDTO(orderMaster));
        } catch (SellException e) {
            log.error("【卖家端完结订单】发生异常{}", e);
            map.put("msg", e.getMessage());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error", map);
        }

        map.put("msg", "订单完结");
        map.put("url", "/sell/seller/order/list");
        return new ModelAndView("common/success");
    }

}
