package com.ebay.managesystem.service;

import com.ebay.managesystem.request.AdminAddUserRequest;
import com.ebay.managesystem.support.Result;

public interface UserInfoService {
    Result<Void> adminAddUser(AdminAddUserRequest request);

    Result<String> getUserResource(String resource);
}
