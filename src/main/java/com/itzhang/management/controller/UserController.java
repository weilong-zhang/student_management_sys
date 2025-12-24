package com.itzhang.management.controller;

import com.itzhang.management.aop.annotation.LogOperation;
import com.itzhang.management.entity.dto.EmailDTO;
import com.itzhang.management.entity.result.Result;
import com.itzhang.management.utils.EmailUtil;
import com.itzhang.management.utils.RandomCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/stu/user")
@Slf4j
public class UserController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RandomCodeUtil randomCodeUtil;
    @Autowired
    private EmailUtil emailUtil;

    /**
     * @param emailDTO
     * @return com.itzhang.management.entity.result.Result
     * @Description 注册时发送邮箱验证码
     * @Author weiloong_zhang
     */
    @LogOperation(module = "用户模块", operation = "发送邮箱验证码")
    @PostMapping("/email/send/verification/code")
    public Result sendEmail(@RequestBody EmailDTO emailDTO) {
        log.info("开始生成并发送邮箱验证码");
        //验证邮箱是否存在
        String email = emailDTO.getEmail() != null ? emailDTO.getEmail() : "".trim();

        if (email.equals("")) {
            return Result.error("邮箱不能为空");
        }

        //验证邮箱格式是否正确
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (!email.matches(emailRegex)) {
            return Result.error("邮箱格式不正确");
        }

        //判断是否发送过于频繁
        String limitKey = "email:limit:" + email;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(limitKey))) {
            return Result.error("发送过于频繁，请稍后再试");
        }
        redisTemplate.opsForValue().set(limitKey, "1", 60, TimeUnit.SECONDS);

        //开始生成邮箱验证码
        String verificationCode = randomCodeUtil.getRandomVerCode(6);

        //开始发送验证码
        Boolean isSend = emailUtil.sendCodeEmail(email, verificationCode);

        //判断是否发送成功
        if (!isSend) {
            return Result.error("邮箱验证码发送失败");
        }

        String emailKey = "email:code:" + email;
        //发送成功将验证码保存在缓存中，并设置五分钟过期
        redisTemplate.opsForValue().set(emailKey, verificationCode, 5 * 60, TimeUnit.SECONDS);

        return Result.success();
    }
}
