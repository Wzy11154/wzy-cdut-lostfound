package com.wzy.common;

/**
 * 基于ThreadLocal封装工具类，用户获取和保存当前登录用户id
 */
public class BaseContext {
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param nickname
     */
    public static void setCurrentNickname(String nickname){
        threadLocal.set(nickname);
    }

    /**
     * 获取值
     * @return
     */
    public static String getCurrentNickname(){
        return threadLocal.get();
    }
}
