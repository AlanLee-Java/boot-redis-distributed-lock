package com.alanlee;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@SpringBootApplication
@RestController
@Slf4j
public class BootRedisDistributedLockApplication {

    @Autowired
    private RedisLockRegistry redisLockRegistry;

    public static void main(String[] args) {
        SpringApplication.run(BootRedisDistributedLockApplication.class, args);
    }

    @GetMapping("/test")
    public String test() throws InterruptedException {
        log.info("========== 进入请求 ==========");
        Lock lock = redisLockRegistry.obtain("test");
        boolean b1 = lock.tryLock(3, TimeUnit.SECONDS);
        log.info("b1 is : {}", b1);

        TimeUnit.SECONDS.sleep(5);

        boolean b2 = lock.tryLock(3, TimeUnit.SECONDS);
        log.info("b2 is : {}", b2);

        lock.unlock();
        lock.unlock();
        return "========== 请求成功 ==========";
    }

}
