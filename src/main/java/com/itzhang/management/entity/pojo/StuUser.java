package com.itzhang.management.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StuUser implements Serializable {
    /** 主键ID */
    private Long id;

    /** 用户唯一标识 */
    private String userId;

    /** 邮箱 */
    private String stuEmail;

    /** 用户名 */
    private String userName;

    /** 密码（加密） */
    private String userPassword;

    /** 手机号 */
    private String userPhone;

    /** 性别 */
    private String userSex;

    /** 生日 */
    private String userBirthday;

    /** 微信小程序 openID */
    private String wechatId;

    /** 是否是会员：0-普通用户，1-会员 */
    private Integer isVip;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
