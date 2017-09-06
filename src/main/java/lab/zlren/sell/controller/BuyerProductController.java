package lab.zlren.sell.controller;

import lab.zlren.sell.common.vo.ProductInfoVO;
import lab.zlren.sell.common.vo.ProductVO;
import lab.zlren.sell.common.vo.ResultVO;
import lab.zlren.sell.pojo.ProductCategory;
import lab.zlren.sell.pojo.ProductInfo;
import lab.zlren.sell.service.ProductCategoryService;
import lab.zlren.sell.service.ProductInfoService;
import lab.zlren.sell.util.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zlren on 17/9/4.
 */
@RestController
@RequestMapping("buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("list")
    public ResultVO list() {

        // 查询所有的上架商品
        List<ProductInfo> productUpInfoList = this.productInfoService.queryUpAll();

        // 查询所有的类目
        List<Integer> collect = productUpInfoList.stream().map(ProductInfo::getCategoryType).collect(Collectors
                .toList());
        List<ProductCategory> productCategoryList = this.productCategoryService.queryByCategoryTypeIn(collect);

        // 数据拼装
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productUpInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);

                    productInfoVOList.add(productInfoVO);
                }
            }

            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }

        return ResultVOUtil.success(productVOList);
    }
}
