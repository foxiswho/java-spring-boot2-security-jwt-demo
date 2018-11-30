package com.foxwho.demo.controller;

import com.foxwho.demo.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/index")
@Api(value = "用户管理", description = "用户管理")
public class IndexController extends BaseController {
    /**
     * 获取用户列表
     *
     * @return
     */
    @ApiOperation(value = "查询用户列表")
    @GetMapping("/list")
    @PreAuthorize("hasRole('USER')")
    public Map<String, Object> userList() {
        List<User> users = userRepository.findAll();
        logger.info("users: {}", users);
        Map<String, Object> map = new HashMap();
        map.put("users", users);
        return map;
    }

    @ApiOperation(value = "查询用户权限")
    @GetMapping("/authorityList")
    public List<String> authorityList() {
        List<String> authentication = getAuthentication();
        return authentication;
    }

    @ApiOperation(value = "当前用户")
    @GetMapping("/my")
    public User my() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.info("users: {}", authentication);
        return null;
    }
}
