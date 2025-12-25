package com.itzhang.management.controller;

import com.itzhang.management.aop.annotation.LogOperation;
import com.itzhang.management.entity.dto.EmailDTO;
import com.itzhang.management.entity.dto.RegisterDTO;
import com.itzhang.management.entity.dto.StuUserDTO;
import com.itzhang.management.entity.result.Result;
import com.itzhang.management.entity.vo.StuUserVO;
import com.itzhang.management.service.UserService;
import com.itzhang.management.utils.CryptoUtil;
import com.itzhang.management.utils.EmailUtil;
import com.itzhang.management.utils.JwtUtils;
import com.itzhang.management.utils.RandomCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
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
    @Autowired
    private CryptoUtil cryptoUtil;
    @Autowired
    private UserService userService;

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

    /**
     * @param registerDTO
     * @return com.itzhang.management.entity.result.Result
     * @Description 使用Email注册
     * @Author weiloong_zhang
     */
    @LogOperation(module = "用户模块", operation = "邮箱注册")
    @PostMapping("/register/byEmail")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        log.info("开始使用邮箱注册");

        //参数校验--实体类校验
        if (registerDTO == null) {
            return Result.error("参数不存在");
        }

        //参数校验--邮箱
        if (registerDTO.getEmail() == null || registerDTO.getEmail().equals("")) {
            return Result.error("邮箱不能为空");
        }

        //参数校验--验证码
        if (registerDTO.getVerificationCode() == null || registerDTO.getVerificationCode().equals("")) {
            return Result.error("验证码不能为空");
        }

        //参数校验--密码
        if (registerDTO.getPassword() == null || registerDTO.getPassword().equals("")) {
            return Result.error("密码不能为空");
        }

        //密码加密
        String password = cryptoUtil.encrypt(registerDTO.getPassword());

        //参数存在，开始校验验证码
        String emailKey = "email:code:" + registerDTO.getEmail();
        String verificationCode = (String) redisTemplate.opsForValue().get(emailKey);

        if (!verificationCode.equals(registerDTO.getVerificationCode())) {
            return Result.error("验证码错误");
        }

        //验证码校验成功，开始注册
        //生成一个随机的游客名称
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        StringBuilder sb = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            sb.append(letters.charAt(random.nextInt(letters.length())));
        }

        String userName = "游客" + sb.toString();

        StuUserDTO stuUserDTO = StuUserDTO.builder()
                .userId(UUID.randomUUID().toString().replace("-", ""))
                .stuEmail(registerDTO.getEmail())
                .userName(userName)
                .userPassword(password)
                .build();

        //开始注册
        userService.register(stuUserDTO);

        return Result.success();
    }

    /**
     * @param stuUserDTO
     * @return com.itzhang.management.entity.result.Result
     * @Description 用户登录
     * @Author weiloong_zhang
     */
    @PostMapping("/login")
    public Result login(@RequestBody StuUserDTO stuUserDTO) {
        log.info("开始用户登录");

        //开始校验入参是否存在
        if (stuUserDTO == null) {
            return Result.error("账号密码不存在");
        }

        //参数校验--邮箱
        if (stuUserDTO.getStuEmail() == null || stuUserDTO.getStuEmail().equals("")) {
            return Result.error("邮箱不能为空");
        }

        //参数校验--密码
        if (stuUserDTO.getUserPassword() == null || stuUserDTO.getUserPassword().equals("")) {
            return Result.error("密码不能为空");
        }

        //开始登录
        StuUserDTO user = userService.login(stuUserDTO);

        //开始获取token
        //通过用户手机号
        Map<String, Object> claim = new HashMap<>();
        claim.put("userId", user.getUserId());

        String token = JwtUtils.generateJwt(claim);

        //登陆时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);

        StuUserVO userVO = StuUserVO.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .stuEmail(user.getStuEmail())
                .userName(user.getUserName())
                .userPhone(user.getUserPhone() != null ? user.getUserPhone() : "")
                .userSex(user.getUserSex())
                .userBirthday(user.getUserBirthday())
                .isVip(user.getIsVip())
                .token(token)
                .loginTime(formattedDate)
                .build();

        return Result.success(userVO);
    }
}
