package com.ebay.managesystem.repository;

import com.ebay.managesystem.constant.RoleConstant;
import com.ebay.managesystem.entity.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class UserInfoRepository {
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<String>> roleUriListMap = new ConcurrentHashMap() {{
        put(RoleConstant.ROLE_ADMIN, new CopyOnWriteArrayList<>(Arrays.asList("/admin/addUser")));
        put(RoleConstant.ROLE_USER, new CopyOnWriteArrayList<>(Arrays.asList("/user/*")));
    }};


    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private File userInfoFile = new File(System.getProperty("user.home"), "userInfo.data");

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 初始化数据库
     */
    @PostConstruct
    public void init() {
        if (!userInfoFile.exists()) {
            writeUserInfo("{}");
        }
    }


    /**
     * 判断角色是否能够访问uri
     *
     * @return
     */
    public boolean isRoleUriAccessible(String role, String requestURI) {
        CopyOnWriteArrayList<String> uriList = roleUriListMap.get(role);
        for (String uri : uriList) {
            if (antPathMatcher.match(uri, requestURI)) {
                return true;
            }
        }
        return false;
    }


    public void adminAddUser(UserInfo userInfo) {
        try {
            String s = readUserInfo();
            Map map = objectMapper.readValue(s, Map.class);
            map.put(userInfo.getUserId(), userInfo.getEndpoint());
            writeUserInfo(objectMapper.writeValueAsString(map));
        } catch (Exception e) {
            log.error("error admin add user", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 存储用户数据
     *
     * @param str
     */
    public void writeUserInfo(String str) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(userInfoFile)) {
            byte[] bytes = str.getBytes();
            fileOutputStream.write(bytes);
        } catch (Exception e) {
            log.error("error write", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取用户数据
     */
    public String readUserInfo() {
        try (FileInputStream fileInputStream = new FileInputStream(userInfoFile);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            int length;
            byte[] buffer = new byte[1024];
            while ((length = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            return byteArrayOutputStream.toString();
        } catch (Exception e) {
            log.error("error read", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * db 查询用户是否能访问resoruce
     *
     * @param userId
     * @param resource
     * @return
     */
    public boolean isUserIdDataAccessible(Long userId, String resource) {
        try {
            String s = readUserInfo();
            Map map = objectMapper.readValue(s, Map.class);
            List<String> resourceList = (List<String>) map.get(userId.toString());
            return !CollectionUtils.isEmpty(resourceList) && resourceList.contains(resource);
        } catch (Exception e) {
            log.error("error invoke is user id data accessible");
            throw new RuntimeException(e);
        }

    }
}
