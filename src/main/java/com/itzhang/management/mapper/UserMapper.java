package com.itzhang.management.mapper;

import com.itzhang.management.entity.dto.UserQueryDTO;
import com.itzhang.management.entity.dto.StuUserDTO;
import com.itzhang.management.entity.pojo.StuUser;
import com.itzhang.management.entity.vo.StuUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    void register(StuUserDTO stuUserDTO);

    Integer getUserByEmail(String stuEmail);

    StuUserDTO login(String stuEmail);

    void updateUserInfo(StuUserDTO stuUserDTO);

    StuUserDTO getUserByUserId(String userId);

    StuUser getUserByIdORName(@Param("userId") String userId, @Param("userName") String userName);

    StuUser getUserById(@Param("userId") String userId);

    List<StuUserVO> queryUserList(UserQueryDTO userQueryDTO);

    Integer queryTotal(UserQueryDTO userQueryDTO);
}
