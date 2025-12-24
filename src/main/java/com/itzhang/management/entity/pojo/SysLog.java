package com.itzhang.management.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysLog {
    private String userId;
    private String userName;
    private String moduleName;
    private String operation;
    private String operationId;
    private String requestParams;
    private String responseResult;
    private String requestIp;
    private String exceptionMsg;
    private LocalDateTime createTime;
}
