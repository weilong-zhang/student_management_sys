package com.itzhang.management.service.Impl;

import com.itzhang.management.entity.dto.StuUserDTO;
import com.itzhang.management.mapper.UserMapper;
import com.itzhang.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
    * @Description 注册操作
    * @param stuUserDTO
    * @return java.lang.Integer
    * @Author weiloong_zhang
    */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(StuUserDTO stuUserDTO) {
        log.info("开始使用邮箱注册");

        userMapper.register(stuUserDTO);
    }
}
