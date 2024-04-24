package com.ebay.managesystem.request;

import lombok.Data;

import java.util.List;

@Data
public class AdminAddUserRequest {
    private Long userId;
    private List<String> endpoint;
}
