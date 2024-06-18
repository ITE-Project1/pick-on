package com.ite.pickon.domain.user.dto;


import com.ite.pickon.domain.user.UserStatus;
import lombok.*;

import java.sql.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private Long user_id;
    private String name;
    private String username;
    private String password;
    private String phone_number;
    private String role;
    private UserStatus status;
    private Date created_at;
}