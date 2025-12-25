package com.itzhang.management.mapper;

import com.itzhang.management.entity.dto.StuUserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void register(StuUserDTO stuUserDTO);

    Integer getUserByEmail(String stuEmail);
}
