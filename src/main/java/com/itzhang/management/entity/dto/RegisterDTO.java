package com.itzhang.management.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @param
 * @Descriptionc 注册时使用，传输关键信息
 * @return null
 * @Author weiloong_zhang
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegisterDTO implements Serializable {
    private String email;
    private String verificationCode;
    private String password;
}
