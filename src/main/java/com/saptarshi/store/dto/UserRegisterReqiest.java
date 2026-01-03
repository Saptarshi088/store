package com.saptarshi.store.dto;

import lombok.Data;

@Data
public class UserRegisterReqiest {
    private String name;
    private String email;
    private String password;
}
