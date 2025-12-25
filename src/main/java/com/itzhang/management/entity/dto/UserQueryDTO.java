package com.itzhang.management.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserQueryDTO implements Serializable {
    private Integer page;
    private Integer offset;
    private Integer pageSize;
    private String stuEmail;
    private String userName;
    private String userPhone;
}
