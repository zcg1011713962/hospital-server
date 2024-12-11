package com.hs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hs.db.entity.User;
import com.hs.db.mapper.UserMapper;
import com.hs.entity.LogicResponse;
import com.hs.entity.bo.UserBO;
import com.hs.entity.dto.UserDTO;
import com.hs.enums.ErrorCode;
import com.hs.enums.UserType;
import com.hs.service.PhoneService;
import com.hs.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PhoneService phoneService;

    @Override
    public CompletableFuture<LogicResponse<UserBO>> addUser(UserDTO userDTO) {
        return CompletableFuture.supplyAsync(()->{
            // 病人
            if(UserType.PATIENT.getType().equals(userDTO.getUserType())){
                // 校验短信验证码
                if(userDTO.getCode() == null || !phoneService.checkCode(userDTO.getPhoneNum(), userDTO.getCode())){
                    return LogicResponse.<UserBO>builder().status(ErrorCode.CODE_ERROR).msg(ErrorCode.CODE_ERROR.getDesc()).build();
                }
            }
            // 该类型用户手机号是否存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone_num", userDTO.getPhoneNum());
            queryWrapper.eq("user_type",  userDTO.getUserType());
            User u = userMapper.selectOne(queryWrapper);
            if (u != null) {
                return LogicResponse.<UserBO>builder().status(ErrorCode.USER_EXIST).msg(ErrorCode.USER_EXIST.getDesc()).build();
            }

            User user = new User();
            BeanUtils.copyProperties(userDTO, user);

            int retVal = userMapper.insert(user);
            UserBO userBO = new UserBO();
            if(retVal > 0){
                BeanUtils.copyProperties(user, userBO);
                return LogicResponse.<UserBO>builder().status(ErrorCode.SUCCESS).data(userBO).build();
            }
            return LogicResponse.<UserBO>builder().status(ErrorCode.FAILED).data(userBO).build();
        });
    }

    @Override
    public CompletableFuture<LogicResponse<UserBO>> searchUser(UserDTO userDTO) {
        return null;
    }


}
