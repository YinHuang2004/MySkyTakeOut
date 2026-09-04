package com.campus_delivery.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate(){
        System.out.println(redisTemplate);
        //string数据操作
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //hash类型的数据操作
        HashOperations hashOperations = redisTemplate.opsForHash();
        //list类型的数据操作
        ListOperations listOperations = redisTemplate.opsForList();
        //set类型数据操作
        SetOperations setOperations = redisTemplate.opsForSet();
        //zset类型数据操作
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
    }
    @Test

    void testString(){
        //set get setex setnx
        redisTemplate.opsForValue().set("name","jack");
        String name =(String) redisTemplate.opsForValue().get("name");
        System.out.println(name);
        redisTemplate.opsForValue().set("code","12345",60, TimeUnit.MINUTES);
        redisTemplate.opsForValue().setIfAbsent(name,"tom");
    }
}