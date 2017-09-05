package lab.zlren.sell.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 自定义通用mapper接口，可以加上自己的实现，这样所有继承MyMapper的mapper就都有了
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
    // todo
    // fixme 特别注意，该接口不能被扫描到，否则会出错
}
