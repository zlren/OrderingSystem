package lab.zlren.sell.service;

import lab.zlren.sell.common.bean.OrderDTO;
import lab.zlren.sell.common.enums.ProductStatusEnum;
import lab.zlren.sell.common.enums.ResultEnum;
import lab.zlren.sell.common.exception.SellException;
import lab.zlren.sell.pojo.OrderDetail;
import lab.zlren.sell.pojo.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zlren on 17/9/4.
 */
@Service
@Slf4j
public class ProductInfoService extends BaseService<ProductInfo> {

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 查询所有在架商品
     *
     * @return
     */
    public List<ProductInfo> queryUpAll() {
        ProductInfo record = new ProductInfo();
        record.setProductStatus(ProductStatusEnum.UP.getCode());
        return this.queryListByWhere(record);
    }


    /**
     * 增加库存
     *
     * @param orderDetailList
     */
    @Transactional
    public void increaseStock(List<OrderDetail> orderDetailList) {
        orderDetailList.forEach(orderDetail -> { // 这个orderDetail里面只有商品id和对应的数量

            ProductInfo record = new ProductInfo();
            record.setProductId(orderDetail.getProductId());
            ProductInfo productInfo = this.queryOne(record);

            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            productInfo.setProductStock(productInfo.getProductStock() + orderDetail.getProductQuantity());
            this.update(productInfo);
        });
    }

    /**
     * 增加库存
     *
     * @param orderDTO
     */
    @Transactional
    public void increaseStock(OrderDTO orderDTO) {
        this.increaseStock(orderDTO.getOrderDetailList());
    }


    /**
     * 根据orderId返回库存
     *
     * @param orderId
     */
    @Transactional
    public void increaseStock(String orderId) {
        OrderDetail orderDetailRecord = new OrderDetail();
        orderDetailRecord.setOrderId(orderId);
        increaseStock(this.orderDetailService.queryListByWhere(orderDetailRecord).stream()
                .map(orderDetail -> new OrderDetail(orderDetail.getProductId(), orderDetail.getProductQuantity()))
                .collect(Collectors.toList()));
    }


    /**
     * 减少库存
     *
     * @param orderDetailList
     */
    @Transactional
    public void decreaseStock(List<OrderDetail> orderDetailList) {
        orderDetailList.forEach(orderDetail -> {
            ProductInfo record = new ProductInfo();
            record.setProductId(orderDetail.getProductId());
            ProductInfo productInfo = this.queryOne(record);

            if (productInfo == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            // 库存不够，抛异常
            if (productInfo.getProductStock() < orderDetail.getProductQuantity()) {
                throw new SellException(ResultEnum.STOCK_NOT_ENOUGH);
            }

            productInfo.setProductStock(productInfo.getProductStock() - orderDetail.getProductQuantity());
            this.update(productInfo);
        });
    }


    /**
     * 上架
     *
     * @param productId
     */
    @Transactional
    public void onSale(String productId) {

        ProductInfo record = new ProductInfo();
        record.setProductId(productId);
        record.setProductStatus(0);

        this.update(record);
    }


    /**
     * 下架
     *
     * @param productId
     */
    @Transactional
    public void offSale(String productId) {

        ProductInfo record = new ProductInfo();
        record.setProductId(productId);
        record.setProductStatus(1);

        this.update(record);
    }


}
