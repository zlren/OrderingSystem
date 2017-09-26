package lab.zlren.sell.controller;


import lab.zlren.sell.common.constant.CookieConstant;
import lab.zlren.sell.common.constant.RedisConstant;
import lab.zlren.sell.conf.wechat.ProjectUrlConfig;
import lab.zlren.sell.pojo.SellerInfo;
import lab.zlren.sell.service.SellerInfoService;
import lab.zlren.sell.util.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 卖家用户
 */
@Controller
@RequestMapping("seller")
public class SellerUserController {

    @Autowired
    private SellerInfoService sellerInfoService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid,
                              HttpServletResponse response,
                              Map<String, Object> map) {

        // 1. openid去和数据库里的数据匹配
        SellerInfo record = new SellerInfo();
        record.setOpenid(openid);
        SellerInfo sellerInfo = sellerInfoService.queryOne(record);
        if (sellerInfo == null) {
            map.put("msg", "登录失败");
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error");
        }

        // 2. 设置token至redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;

        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), openid, expire, TimeUnit
                .SECONDS);

        // 3. 设置token至cookie
        CookieUtil.set(response, CookieConstant.TOKEN, token, expire);

        return new ModelAndView("redirect:" + projectUrlConfig.getSell() + "/sell/seller/order/list");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,
                               HttpServletResponse response,
                               Map<String, Object> map) {
        // 1. 从cookie里查询
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);

        if (cookie != null) {
            // 2. 清除redis
            redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX, cookie
                    .getValue()));

            // 3. 清除cookie
            CookieUtil.set(response, CookieConstant.TOKEN, null, 0);
        }

        map.put("msg", "登出成功");
        map.put("url", "/sell/seller/order/list");
        return new ModelAndView("common/success", map);
    }
}
