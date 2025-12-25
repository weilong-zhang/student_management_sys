package com.itzhang.management.service;

import com.itzhang.management.entity.dto.StuUserDTO;

public interface UserService {
    void register(StuUserDTO stuUserDTO);

    StuUserDTO login(StuUserDTO stuUserDTO);

    void updateUserInfo(StuUserDTO stuUserDTO);
}
