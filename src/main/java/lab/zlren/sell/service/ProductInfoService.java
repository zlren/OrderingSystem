package lab.zlren.sell.service;

import lab.zlren.sell.common.ProductStatusEnum;
import lab.zlren.sell.pojo.ProductInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zlren on 17/9/4.
 */
@Service
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

}
