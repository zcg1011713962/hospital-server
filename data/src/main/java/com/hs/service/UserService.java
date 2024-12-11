package com.hs.service;


import com.hs.db.entity.User;
import com.hs.entity.LogicResponse;
import com.hs.entity.bo.UserBO;
import com.hs.entity.dto.UserDTO;

import java.util.concurrent.CompletableFuture;

public interface UserService {
    /**
     * 添加用户
     * @param userDTO
     */
    CompletableFuture<LogicResponse<UserBO>> addUser(UserDTO userDTO);


    CompletableFuture<LogicResponse<UserBO>> searchUser(UserDTO userDTO);

}
