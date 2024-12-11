package com.hs.service.impl;

import com.hs.entity.LogicResponse;
import com.hs.enums.ErrorCode;
import com.hs.service.PhoneService;
import com.hs.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class PhoneServiceImpl implements PhoneService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public CompletableFuture<LogicResponse<String>> sendCode(String phoneNum) {
        return CompletableFuture.supplyAsync(()->{
            // 校验手机号码

            // 生成校验码
            Random random = new Random();
            String code = String.valueOf(100000 + random.nextInt(900000));
            // 发送短信验证码

            // 存储验证码
            redisUtil.set("code_" + phoneNum, code, 60);
            return LogicResponse.<String>builder().status(ErrorCode.SUCCESS).data(code).build();
        });
    }

    @Override
    public Boolean checkCode(String phoneNum, String code) {
        String saveCode = (String) redisUtil.get("code_" + phoneNum);
        return StringUtils.isNoneBlank(saveCode) && saveCode.equals(code);
    }
}
