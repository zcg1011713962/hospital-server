package com.hs.controller;

import com.hs.entity.BaseResponse;
import com.hs.entity.bo.UserBO;
import com.hs.entity.dto.UserDTO;
import com.hs.entity.vo.UserVO;
import com.hs.enums.ErrorCode;
import com.hs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user/add")
    public Mono<BaseResponse<UserVO>> addUser(@RequestBody UserDTO userDTO) {
        log.debug("新增用户:{}", userDTO);

        CompletableFuture<BaseResponse<UserVO>> future = userService.addUser(userDTO).thenApply(logicResponse -> {
            if (logicResponse.getStatus() == ErrorCode.SUCCESS) {
                UserBO userBO = logicResponse.getData();
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(userBO, userVO);

                return BaseResponse.<UserVO>builder()
                        .status(ErrorCode.SUCCESS.getCode())
                        .data(userVO)
                        .build();
            }
            return BaseResponse.<UserVO>builder().status(logicResponse.getStatus().getCode()).message(logicResponse.getMsg()).build();
        }).exceptionally((e)->{
            log.error("新增用户异常", e);
            return BaseResponse.<UserVO>builder().status(ErrorCode.FAILED.getCode()).message(ErrorCode.FAILED.getDesc()).build();
        });
        return Mono.fromFuture(future);
    }

    @PostMapping("search")
    public Mono<BaseResponse<String>> searchUser(@RequestBody UserDTO userDTO) {
        return Mono.fromFuture(CompletableFuture.completedFuture(BaseResponse.<String>builder().status(ErrorCode.SUCCESS.getCode()).build()));
    }


}
