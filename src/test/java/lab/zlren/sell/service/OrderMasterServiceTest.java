package lab.zlren.sell.service;

import lab.zlren.sell.common.bean.OrderDTO;
import lab.zlren.sell.pojo.OrderDetail;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterServiceTest {


    @Autowired
    private OrderMasterService orderMasterService;

    String BUYER_OPENID = "1101110";

    private final String ORDER_ID = "1497183332311989948";

    @Test
    public void create() throws Exception {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("任子龙");
        orderDTO.setBuyerAddress("BUPT");
        orderDTO.setBuyerPhone("15652914810");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

        // 购物车
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("1");
        o1.setProductQuantity(1);

        OrderDetail o2 = new OrderDetail();
        o2.setProductId("2");
        o2.setProductQuantity(2);

        orderDetailList.add(o1);
        orderDetailList.add(o2);

        orderDTO.setOrderDetailList(orderDetailList);

        OrderDTO result = this.orderMasterService.create(orderDTO);
        log.info("创建订单 result = {}", result);
        Assert.assertNotNull(result);
    }


    @Test
    public void queryByOrderId() throws Exception {

        // OrderDTO orderDTO = this.orderMasterService.queryByOrderId("1504667442910962335");
        // orderDTO.getOrderDetailList().forEach(orderDetail -> log.info("{}", orderDetail.getProductName()));
        // log.info("{}", orderDTO);
    }


    @Test
    public void queryOrderListByOpenId() throws Exception {

        // PageInfo<OrderDTO> orderDTOPageInfo = this.orderMasterService.queryOrderListByOpenId(1, 5, BUYER_OPENID);
        log.info("呵呵哒");
    }


    @Test
    public void cancel() throws Exception {

        // OrderDTO orderDTO = this.orderMasterService.queryByOrderId("1504667442910962335");
        // OrderDTO result = this.orderMasterService.cancel(orderDTO);
    }


    @Test
    public void finish() throws Exception {
        //
        // OrderDTO orderDTO = this.orderMasterService.queryByOrderId("1504667442910962335");
        // OrderDTO result = this.orderMasterService.finish(orderDTO);
    }


    @Test
    public void pay() throws Exception {

        // OrderDTO orderDTO = this.orderMasterService.queryByOrderId("1504667442910962335");
        // OrderDTO result = this.orderMasterService.pay(orderDTO);
    }

}