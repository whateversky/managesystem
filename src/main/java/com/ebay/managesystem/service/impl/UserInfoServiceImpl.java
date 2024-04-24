package com.ebay.managesystem.service.impl;

import com.ebay.managesystem.entity.UserInfo;
import com.ebay.managesystem.service.UserInfoService;
import com.ebay.managesystem.repository.UserInfoRepository;
import com.ebay.managesystem.request.AdminAddUserRequest;
import com.ebay.managesystem.support.Result;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoRepository userInfoRepository;

    @Override
    public Result<Void> adminAddUser(AdminAddUserRequest request) {
        if (Objects.isNull(request.getUserId())) {
            return Result.error("userId cannot be null");
        }
        if (CollectionUtils.isEmpty(request.getEndpoint())) {
            return Result.error("endpoint cannot be empty");
        }
        // 请求转对象
        UserInfo userInfo = buildUserInfo(request);
        userInfoRepository.adminAddUser(userInfo);
        return Result.ok();
    }

    private UserInfo buildUserInfo(AdminAddUserRequest request) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(request.getUserId());
        userInfo.setEndpoint(request.getEndpoint());
        return userInfo;
    }

    @Override
    public Result<String> getUserResource(String resource) {
        return Result.ok(resource);
    }
}
