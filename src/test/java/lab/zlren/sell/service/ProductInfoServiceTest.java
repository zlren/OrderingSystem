package lab.zlren.sell.service;

import lab.zlren.sell.pojo.OrderDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoServiceTest {

    @Autowired
    private ProductInfoService productInfoService;

    @Test
    public void increaseStock() throws Exception {

        List<OrderDetail> orderDetailList = new ArrayList<>();

        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setProductId("1");
        orderDetail1.setProductQuantity(3);

        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setProductId("2");
        orderDetail2.setProductQuantity(4);

        orderDetailList.add(orderDetail1);
        orderDetailList.add(orderDetail2);
        this.productInfoService.increaseStock(orderDetailList);

    }

    @Test
    public void decreaseStock() throws Exception {
    }

}