package com.itzhang.management.mapper;

import com.itzhang.management.entity.dto.StuUserDTO;
import com.itzhang.management.entity.pojo.StuUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    void register(StuUserDTO stuUserDTO);

    Integer getUserByEmail(String stuEmail);

    StuUserDTO login(String stuEmail);

    void updateUserInfo(StuUserDTO stuUserDTO);

    StuUserDTO getUserByUserId(String userId);

    StuUser getUserByIdORName(@Param("userId") String userId, @Param("userName") String userName);
}
