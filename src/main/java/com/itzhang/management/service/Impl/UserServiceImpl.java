package com.itzhang.management.service.Impl;

import com.itzhang.management.entity.dto.StuUserDTO;
import com.itzhang.management.exception.BaseException;
import com.itzhang.management.mapper.UserMapper;
import com.itzhang.management.service.UserService;
import com.itzhang.management.utils.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private CryptoUtil cryptoUtil;
    @Autowired
    private UserMapper userMapper;

    /**
     * @param stuUserDTO
     * @return java.lang.Integer
     * @Description 注册操作
     * @Author weiloong_zhang
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(StuUserDTO stuUserDTO) {
        log.info("开始使用邮箱注册");

        //幂等性检测
        Integer total = userMapper.getUserByEmail(stuUserDTO.getStuEmail());

        if (total > 0) {
            log.error("邮箱已存在");
            throw new BaseException("邮箱已存在");
        }

        //幂等性二次检测，异常捕捉，防止重复提交导致的并发
        try {
            userMapper.register(stuUserDTO);
        } catch (DuplicateKeyException e) {
            throw new BaseException("注册失败，邮箱已存在");
        }
    }

    /**
     * @param stuUserDTO
     * @return com.itzhang.management.entity.dto.StuUserDTO
     * @Description 用户登录
     * @Author weiloong_zhang
     */
    @Override
    public StuUserDTO login(StuUserDTO stuUserDTO) {
        //开始抓取关键信息
        String stuEmail = stuUserDTO.getStuEmail();
        String userPassword = stuUserDTO.getUserPassword();

        //开始获取用户信息
        StuUserDTO user = userMapper.login(stuEmail);

        if (user == null){
            log.error("用户不存在");
            throw new BaseException("用户不存在");
        }

        //开始校验密码
        String passwordStr = user.getUserPassword() != null ? user.getUserPassword() : "";

        Boolean isPassword = cryptoUtil.matches(userPassword, passwordStr);

        if (!isPassword){
            log.error("密码错误");
            throw new BaseException("密码错误");
        }

        return user;
    }
}
