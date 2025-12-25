package com.itzhang.management.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StuUserVO implements Serializable {
    private Long id;
    private String userId;//用户唯一标识
    private String stuEmail;//邮箱
    private String userName;//用户名
    private String userPhone;//手机号
    private String userSex;//性别
    private String userBirthday;//生日
    private Integer isVip;//是否是会员：0-普通用户，1-会员
    private String token;//登录凭证
    private String loginTime;//登录时间
}
