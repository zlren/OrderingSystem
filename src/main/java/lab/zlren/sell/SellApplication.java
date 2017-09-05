package lab.zlren.sell;

import lab.zlren.sell.pojo.ProductInfo;
import lab.zlren.sell.service.ProductInfoService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SpringBootApplication
@MapperScan(basePackages = "lab.zlren.sell.mapper")
public class SellApplication {

    public static void main(String[] args) {
        SpringApplication.run(SellApplication.class, args);
    }

    @Autowired
    private ProductInfoService productInfoService;

    @RequestMapping("/test")
    public String test() {

        List<ProductInfo> productInfoList = this.productInfoService.queryAll();
        productInfoList.forEach(System.out::print);

        return "hello";
    }
}
