package com.ebay.managesystem.entity;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    private Long userId;
    private List<String> endpoint;
}
