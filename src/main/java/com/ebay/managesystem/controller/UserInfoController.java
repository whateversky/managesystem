package com.ebay.managesystem.controller;

import com.ebay.managesystem.annotation.ValidateResource;
import com.ebay.managesystem.request.AdminAddUserRequest;
import com.ebay.managesystem.service.UserInfoService;
import com.ebay.managesystem.support.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class UserInfoController {
    @Resource
    private UserInfoService userInfoService;

    @PostMapping("/admin/addUser")
    public Result<Void> adminAddUser(@RequestBody AdminAddUserRequest request) {
        return userInfoService.adminAddUser(request);
    }

    @GetMapping("/user/{resource}")
    @ValidateResource(resourceKey = "resource")
    public Result<String> getUserResource(@PathVariable("resource") String resource) {
        return userInfoService.getUserResource(resource);
    }


}
