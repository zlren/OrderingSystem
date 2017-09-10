package lab.zlren.sell.conf.wechat;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by zlren on 17/9/10.
 */
@Component
public class WeChatPayConfig {

    @Autowired
    private WechatAccountConfig wechatAccountConfig;

    @Bean
    public BestPayServiceImpl bestPayService() {
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayH5Config(wxPayH5Config());

        return bestPayService;
    }

    @Bean
    public WxPayH5Config wxPayH5Config() {
        WxPayH5Config wxPayH5Config = new WxPayH5Config();
        wxPayH5Config.setAppId(this.wechatAccountConfig.getMpAppId());
        wxPayH5Config.setAppSecret(this.wechatAccountConfig.getMpAppSecret());
        wxPayH5Config.setMchId(this.wechatAccountConfig.getMchId());
        wxPayH5Config.setMchKey(this.wechatAccountConfig.getMchKey());
        wxPayH5Config.setKeyPath(this.wechatAccountConfig.getKeyPah());
        wxPayH5Config.setNotifyUrl(this.wechatAccountConfig.getNotifyUrl());

        return wxPayH5Config;
    }
}
