package com.hs.controller;

import com.hs.entity.BaseResponse;
import com.hs.entity.bo.UserBO;
import com.hs.entity.dto.UserDTO;
import com.hs.entity.vo.UserVO;
import com.hs.enums.ErrorCode;
import com.hs.service.PhoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    @GetMapping("/phone/sendCode")
    public Mono<BaseResponse<String>> sendCode(@RequestParam String phoneNum) {
        log.debug("发送手机验证码:{}", phoneNum);
        CompletableFuture<BaseResponse<String>> future = phoneService.sendCode(phoneNum).thenApply((logicResponse) -> {
            return BaseResponse.<String>builder().status(ErrorCode.SUCCESS.getCode()).data(logicResponse.getData()).build();
        });
        return Mono.fromFuture(future);
    }

}
