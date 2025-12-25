package com.itzhang.management.service.Impl;

import com.itzhang.management.entity.dto.StuUserDTO;
import com.itzhang.management.exception.BaseException;
import com.itzhang.management.mapper.UserMapper;
import com.itzhang.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

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
}
