package lab.zlren.sell.service;

import com.github.pagehelper.PageInfo;
import lab.zlren.sell.common.bean.OrderDTO;
import lab.zlren.sell.common.enums.OrderStatusEnum;
import lab.zlren.sell.common.enums.PayStatusEnum;
import lab.zlren.sell.common.enums.ResultEnum;
import lab.zlren.sell.common.exception.SellException;
import lab.zlren.sell.pojo.OrderDetail;
import lab.zlren.sell.pojo.OrderMaster;
import lab.zlren.sell.pojo.ProductInfo;
import lab.zlren.sell.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * Created by zlren on 17/9/4.
 */
@Service
@Slf4j
public class OrderMasterService extends BaseService<OrderMaster> {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private OrderDetailService orderDetailService;

    // /**
    //  * 按照买家的openid分页查找
    //  *
    //  * @param openid
    //  * @param page
    //  * @param size
    //  * @return
    //  */
    // public PageInfo<OrderMaster> queryPageListByBuyerOpenid(String openid, Integer page, Integer size) {
    //     OrderMaster orderMaster = new OrderMaster();
    //     orderMaster.setBuyerOpenid(openid);
    //     return this.queryPageListByWhere(page, size, orderMaster);
    // }

    /**
     * 创建订单
     * 订单传过来的参数只有商品id和数量
     *
     * @param orderDTO
     * @return
     */
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        String orderId = KeyUtil.genUniqueKey(); // 本订单的id

        // 查询商品（数量，价格）
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) { // 只有商品id和数量
            ProductInfo record = new ProductInfo();
            record.setProductId(orderDetail.getProductId());
            ProductInfo productInfo = this.productInfoService.queryOne(record); // 查到完整的商品信息

            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            // 计算订单总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);

            BeanUtils.copyProperties(productInfo, orderDetail); // 属性拷贝

            // 一个订单master有很多的商品
            // 这里一个订单master下的一种商品是一个detail
            orderDetail.setDetailId(KeyUtil.genUniqueKey()); // 每个detail的id都不一样
            orderDetail.setOrderId(orderId); // 但是它们同属于一单，所以orderId是一样的

            this.orderDetailService.save(orderDetail);
        }

        // 需要返回的数据
        orderDTO.setOrderId(orderId);

        // 写入订单数据库OrderMaster
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster); // 属性拷贝的时候，会覆盖原来的属性值，这里是orderId和orderAmount
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        this.save(orderMaster);

        // 扣库存
        this.productInfoService.decreaseStock(orderDTO.getOrderDetailList());

        return orderDTO;
    }


    /**
     * 查询订单
     *
     * @param orderId
     * @return
     */
    @Transactional
    public OrderDTO queryByOrderId(String orderId) {

        OrderMaster orderMasterRecord = new OrderMaster();
        orderMasterRecord.setOrderId(orderId);
        OrderMaster orderMaster = this.queryOne(orderMasterRecord);

        if (orderMaster == null) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }

        OrderDetail orderDetailRecord = new OrderDetail();
        orderDetailRecord.setOrderId(orderId);
        List<OrderDetail> orderDetailList = this.orderDetailService.queryListByWhere(orderDetailRecord);

        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SellException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }

        OrderDTO result = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, result);
        result.setOrderDetailList(orderDetailList);

        return result;
    }


    /**
     * 根据买家openid查
     *
     * @param page
     * @param size
     * @param openId
     * @return
     */
    @Transactional
    public PageInfo<OrderMaster> queryOrderListByOpenId(Integer page, Integer size, String openId) {

        OrderMaster record = new OrderMaster();
        record.setBuyerOpenid(openId);
        PageInfo<OrderMaster> orderMasterPageInfo = this.queryPageListByWhere(page, size, record);

        // PageInfo<OrderDTO> result = new PageInfo<>();
        // List<OrderDTO> orderDTOList = orderMasterPageInfo.getList().stream().map(OrderDTO::new)
        //         .collect(Collectors.toList());
        //
        // BeanUtils.copyProperties(orderMasterPageInfo, result);
        // result.setList(orderDTOList);

        return orderMasterPageInfo;
    }


    /**
     * 取消订单
     *
     * @param orderDTO
     * @return
     */
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {

        // 判断、修改订单状态
        OrderMaster record = new OrderMaster();
        record.setOrderId(orderDTO.getOrderId());
        OrderMaster orderMaster = this.queryOne(record);

        if (!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("取消订单失败，orderId = {}， orderStatus = {}", orderMaster.getOrderId(), orderMaster.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        this.update(orderMaster);

        // 返回库存
        this.productInfoService.increaseStock(orderDTO);

        // 如果已支付需要退款
        if (record.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            // todo 退款操作
            log.info("订单已支付，需退款");
        }

        return orderDTO;
    }


    /**
     * 完结订单
     *
     * @param orderDTO
     * @return
     */
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {

        // 判断订单状态
        OrderMaster record = new OrderMaster();
        record.setOrderId(orderDTO.getOrderId());
        OrderMaster orderMaster = this.queryOne(record);

        if (!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("订单状态错误");
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // 修改订单状态
        orderMaster.setOrderStatus(OrderStatusEnum.FINISH.getCode());
        this.update(orderMaster);

        return orderDTO;
    }


    /**
     * 支付
     *
     * @param orderDTO
     * @return
     */
    @Transactional
    public OrderDTO pay(OrderDTO orderDTO) {

        // 判断订单状态
        OrderMaster record = new OrderMaster();
        record.setOrderId(orderDTO.getOrderId());
        OrderMaster orderMaster = this.queryOne(record);

        if (!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("订单状态错误");
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        if (!orderMaster.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("订单支付状态错误");
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        // 修改订单状态
        orderMaster.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        this.update(orderMaster);

        return orderDTO;
    }

}
