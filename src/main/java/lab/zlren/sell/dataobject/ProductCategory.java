package lab.zlren.sell.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 类目表
 * Created by zlren on 17/9/3.
 */
@Entity
@DynamicUpdate
@Data // 注解在类上：提供类所有属性的 getting 和 setting 方法，此外还提供了 equals、canEqual、hashCode、toString 方法
public class ProductCategory {
    @Id
    @GeneratedValue
    private Integer categoryId;
    private String categoryName;
    private Integer categoryType;
    private Date createTime;
    private Date updateTime;
}
