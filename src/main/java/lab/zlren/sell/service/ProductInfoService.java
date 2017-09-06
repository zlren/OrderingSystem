package lab.zlren.sell.service;

import lab.zlren.sell.common.bean.OrderDTO;
import lab.zlren.sell.common.enums.ProductStatusEnum;
import lab.zlren.sell.common.enums.ResultEnum;
import lab.zlren.sell.common.exception.SellException;
import lab.zlren.sell.pojo.OrderDetail;
import lab.zlren.sell.pojo.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by zlren on 17/9/4.
 */
@Service
@Slf4j
public class ProductInfoService extends BaseService<ProductInfo> {

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

}
