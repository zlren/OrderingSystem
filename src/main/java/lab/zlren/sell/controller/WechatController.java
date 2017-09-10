package lab.zlren.sell.controller;

import lab.zlren.sell.common.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;

/**
 * 利用了第三方sdk
 * Created by zlren on 17/9/7.
 */
@Controller
@RequestMapping("wechat")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService;


    @GetMapping("auth")
    public String auth(@RequestParam("returnUrl") String returnUrl) {

        String url = "http://zlren.nat300.top/sell/wechat/userinfo";
        String redirectUrl = this.wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_USER_INFO,
                URLEncoder.encode(returnUrl));
        log.info("授权获取code部分， {}", redirectUrl);

        return "redirect:" + redirectUrl;
    }


    @GetMapping("userinfo")
    public String getUserInfo(@RequestParam("code") String code,
                              @RequestParam("state") String returnUrl) {

        String openid;

        try {
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
            openid = wxMpOAuth2AccessToken.getOpenId();

        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error("微信网页授权异常 {}", e);
            throw new SellException("微信网页授权异常");
        }

        return "redirect:" + returnUrl + "?openid=" + openid;
    }
}
