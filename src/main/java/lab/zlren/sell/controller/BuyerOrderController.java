package lab.zlren.sell.controller;

import com.github.pagehelper.PageInfo;
import lab.zlren.sell.common.bean.OrderDTO;
import lab.zlren.sell.common.bean.OrderForm;
import lab.zlren.sell.common.enums.ResultEnum;
import lab.zlren.sell.common.exception.SellException;
import lab.zlren.sell.common.vo.ResultVO;
import lab.zlren.sell.pojo.OrderMaster;
import lab.zlren.sell.service.OrderMasterService;
import lab.zlren.sell.util.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zlren on 17/9/6.
 */
@RestController
@RequestMapping("buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderMasterService orderMasterService;


    /**
     * 创建订单
     *
     * @param orderForm
     * @param bindingResult
     * @return
     */
    @PostMapping("create")
    public ResultVO createOrder(@Valid OrderForm orderForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("订单参数校验失败 {}", orderForm);
            throw new SellException(ResultEnum.PARAMETER_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }


        OrderDTO orderDTO = new OrderDTO(orderForm);

        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            // 购物车为空，抛异常
            throw new SellException("购物车为空");
        }

        this.orderMasterService.create(orderDTO);

        Map<String, String> result = new HashMap<>();
        result.put("orderId", orderDTO.getOrderId());

        return ResultVOUtil.success(result);
    }


    /**
     * 查询订单列表
     *
     * @param openid
     * @param page
     * @param size
     * @return
     */
    @GetMapping("list")
    public ResultVO orderListByOpenId(@RequestParam("openid") String openid,
                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {

        if (StringUtils.isEmpty(openid)) {
            log.error("openid为空");
            throw new SellException(ResultEnum.PARAMETER_ERROR);
        }

        log.info("三个参数{}, {}, {}", openid, page, size);

        PageInfo<OrderMaster> orderMasterPageInfo = this.orderMasterService.queryOrderListByOpenId(page, size, openid);
        return ResultVOUtil.success(orderMasterPageInfo.getList());
    }

}
