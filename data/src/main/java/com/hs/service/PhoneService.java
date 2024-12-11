package com.hs.service;

import com.hs.entity.LogicResponse;

import java.util.concurrent.CompletableFuture;

public interface PhoneService {

    CompletableFuture<LogicResponse<String>> sendCode(String phoneNum);

    Boolean checkCode(String phoneNum, String code);


}
