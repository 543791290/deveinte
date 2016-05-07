package com.haothink.utils;

import org.junit.Test;

/**
 * Created by wanghao on 16-4-26.
 */
public class RedisTest {

    @Test
    public void testSetString(){
        String result = RedisUtil.set("wanghao","hello world");
        System.out.println(result);
        System.out.println(RedisUtil.get("wanghao"));
        System.out.println(RedisUtil.exists("wanghao"));

    }
}
