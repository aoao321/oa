package com.aoao.constant;


public class RedisKeyConstants {

    /**
     * token KEY前缀
     */
    private static final String TOKEN_KEY_PREFIX = "token:";


    /**
     * 构建token KEY
     */
    public static String buildTokenKey(Long userId) {
        return TOKEN_KEY_PREFIX + userId;

    }


}

