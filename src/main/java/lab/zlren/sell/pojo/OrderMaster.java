package lab.zlren.sell.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lab.zlren.sell.util.serializer.Date2LongSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "order_master")
public class OrderMaster {
    @Id
    @Column(name = "order_id")
    private String orderId;

    /**
     * 买家名字
     */
    @Column(name = "buyer_name")
    private String buyerName;

    /**
     * 买家电话
     */
    @Column(name = "buyer_phone")
    private String buyerPhone;

    /**
     * 买家地址
     */
    @Column(name = "buyer_address")
    private String buyerAddress;

    /**
     * 买家微信openid
     */
    @Column(name = "buyer_openid")
    private String buyerOpenid;

    /**
     * 订单总金额
     */
    @Column(name = "order_amount")
    private BigDecimal orderAmount;

    /**
     * 订单状态, 默认为新下单
     */
    @Column(name = "order_status")
    private Integer orderStatus;

    /**
     * 支付状态, 默认未支付
     */
    @Column(name = "pay_status")
    private Integer payStatus;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    // 不是表字段
    @Transient
    private List<OrderDetail> orderDetailList;
}