package lab.zlren.sell.service;

import lab.zlren.sell.pojo.SellerInfo;
import org.springframework.stereotype.Service;

/**
 * Created by zlren on 17/9/4.
 */
@Service
public class SellerInfoService extends BaseService<SellerInfo> {


    /**
     * 根据openid查找
     *
     * @param openId
     * @return
     */
    SellerInfo findByOpenId(String openId) {

        SellerInfo record = new SellerInfo();
        record.setOpenid(openId);

        return this.queryOne(record);
    }

}
