package com.ite.pickon.domain.user.dto;


import lombok.Data;

import java.sql.Date;

@Data
public class UserVO {
    private Long user_id;
    private String name;
    private String username;
    private String password;
    private String phone_number;
    private String role;
    private String status;
    private Date created_at;

}