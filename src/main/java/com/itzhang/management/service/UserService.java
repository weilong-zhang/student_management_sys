package com.itzhang.management.service;

import com.itzhang.management.entity.dto.UserQueryDTO;
import com.itzhang.management.entity.dto.StuUserDTO;
import com.itzhang.management.entity.pojo.StuUser;
import com.itzhang.management.entity.result.PageResult;

public interface UserService {
    void register(StuUserDTO stuUserDTO);

    StuUserDTO login(StuUserDTO stuUserDTO);

    void updateUserInfo(StuUserDTO stuUserDTO);

    StuUser getUserById(String userId);

    PageResult queryUserList(UserQueryDTO userQueryDTO);
}
