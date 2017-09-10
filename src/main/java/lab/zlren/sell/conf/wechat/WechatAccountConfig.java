package lab.zlren.sell.conf.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by zlren on 17/9/7.
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatAccountConfig {

    private String mpAppId;
    private String mpAppSecret;
    private String mchId; // 商户号
    private String mchKey; // 商户密钥
    private String keyPah; // 商户证书路径
    private String notifyUrl;
}
