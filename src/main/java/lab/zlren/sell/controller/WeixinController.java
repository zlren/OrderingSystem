package lab.zlren.sell.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zlren on 17/9/7.
 */
@RestController
@RequestMapping("weixin")
@Slf4j
public class WeixinController {

    @GetMapping("auth")
    public void auth(@RequestParam("code") String code) {

        log.info("进入auth方法");

        // 拿到codey以后按照规则去拼一个url，访问这个url的目的是去访问它可以换取access_token，与之一起的可以拿到用户的openid
        String url = "" + code + "";


        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class); // 这里有access_toke和openid

        log.info(response);
    }
}
