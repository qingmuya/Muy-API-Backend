package com.qingmuy.muyinterface.controller;


import com.qingmuy.muyapiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request) {
/*        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");
        // 实际情况为到数据库查询
        if (!accessKey.equals("dingzhen")) {
            throw new RuntimeException("无权限");
        }
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }
        // 通过secretKey加密后对比
        String serverSign = SignUtils.genSign(body, "zhenzhu");
        if (!sign.equals(serverSign)) {
            throw new RuntimeException("无权限");
        }*/
        return "POST 用户名字是" + user.getUsername();
    }
}
