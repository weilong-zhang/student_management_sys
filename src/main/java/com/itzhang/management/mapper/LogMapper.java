package com.itzhang.management.mapper;

import com.itzhang.management.entity.pojo.SysLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {
    void insertLog(SysLog sysLog);
}
