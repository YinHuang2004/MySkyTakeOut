package com.campus_delivery.service;

import com.campus_delivery.dto.UserLoginDTO;
import com.campus_delivery.entity.User;

public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}