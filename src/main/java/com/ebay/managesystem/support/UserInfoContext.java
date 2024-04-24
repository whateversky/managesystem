package com.ebay.managesystem.support;

public class UserInfoContext {
    private static final ThreadLocal<UserInfoToken> userInfoHolder = new ThreadLocal<>();

    public static void setUserInfoToken(UserInfoToken userInfoToken) {
        userInfoHolder.set(userInfoToken);
    }

    public static void remove() {
        userInfoHolder.remove();

    }

    public static Long getUserId() {
        UserInfoToken userInfoToken = userInfoHolder.get();
        return userInfoToken.getUserId();
    }
}
