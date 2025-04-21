package com.java6.controller.response;

import com.java6.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String password;
    private String fullName;
    private String email;
    private Role role;
}
