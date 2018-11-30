package com.foxwho.demo.controller;

import com.foxwho.demo.consts.JwtConsts;
import com.foxwho.demo.exception.HttpAesException;
import com.foxwho.demo.model.User;
import com.foxwho.demo.util.Password;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Api(value = "注册管理", description = "注册管理")
public class RegisterController extends BaseController {
    /**
     * 注册用户 默认开启白名单
     *
     * @param user
     */
    @ApiOperation(value = "注册用户")
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        User bizUser = userRepository.findByUsername(user.getUsername());
        if (null != bizUser) {
            throw new HttpAesException("用户已经存在");
        }
        System.out.println("Password  as " + user.getPassword());
        user.setPassword(Password.md5(user.getPassword()));
        user.setRole(JwtConsts.ROLE_DEFAULT);
        System.out.println("Password.md5 as " + user.getPassword());
        return userRepository.save(user);
    }
}
