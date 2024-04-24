package com.ebay.managesystem.support;

import lombok.Data;

@Data
public class UserInfoToken {
    private Long userId;
    private String accountName;
    private String role;
}
