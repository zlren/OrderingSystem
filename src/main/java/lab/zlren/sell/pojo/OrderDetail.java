package lab.zlren.sell.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lab.zlren.sell.util.serializer.Date2LongSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "order_detail")
@Data
@NoArgsConstructor
public class OrderDetail {

    @Id
    @Column(name = "detail_id")
    private String detailId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "product_id")
    private String productId;

    /**
     * 商品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 当前价格,单位分
     */
    @Column(name = "product_price")
    private BigDecimal productPrice;

    /**
     * 数量
     */
    @Column(name = "product_quantity")
    private Integer productQuantity;

    /**
     * 小图
     */
    @Column(name = "product_icon")
    private String productIcon;

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


    public OrderDetail(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}