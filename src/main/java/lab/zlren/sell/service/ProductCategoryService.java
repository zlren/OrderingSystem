package lab.zlren.sell.service;

import lab.zlren.sell.pojo.ProductCategory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by zlren on 17/9/4.
 */
@Service
public class ProductCategoryService extends BaseService<ProductCategory> {


    /**
     * 根据categoryType列表查询
     *
     * @param list
     * @return
     */
    @Transactional
    public List<ProductCategory> queryByCategoryTypeIn(List<Integer> list) {
        Example example = new Example(ProductCategory.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andIn("categoryType", list);

        return this.getMapper().selectByExample(example);
    }

}
