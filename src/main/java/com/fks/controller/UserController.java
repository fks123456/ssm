package com.fks.controller;

import com.fks.domain.User;
import com.fks.service.IUserService;
import com.fks.util.MD5Utils;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api("用户管理")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/getAllUser")
    public List<User> getAllUser() {
        return userService.findAllUser();
    }

    @RequestMapping("/save")
    @RequiresPermissions("user/save")
    public String save(User user) {

        if(user.getPassword() != null && !"".equals(user.getPassword().trim())) {
            user.setPassword(MD5Utils.encrypt(user.getPassword().trim()));
        }

        if(userService.save(user)) {
            return "保存成功";
        } else {
            return "保存失败";
        }
    }

    @RequestMapping("/changPassword")
    public String changPassword(String newPassword) {
        String encrypt = MD5Utils.encrypt(newPassword);
        return encrypt;
    }

    @RequestMapping("/isPermission")
    public boolean isPermission(String permission) {

        Subject subject = SecurityUtils.getSubject();

        return subject.isPermitted(permission);
    }

    @ApiOperation(value = "登录接口",notes = "用户名-密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", paramType = "query", dataType = "string", required = false),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", dataType = "string", required = false)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "登陆成功"),
            @ApiResponse(code = 1, message = "用户名和密码不能为空"),
            @ApiResponse(code = 2, message = "密码错误"),
            @ApiResponse(code = 3, message = "用户名不存在"),
            @ApiResponse(code = 4, message = "登陆失败")
    })
    @PostMapping("/login")
    private Map<String, Object> login(String name, String password) {
        Map<String, Object> result = new HashMap<>();

        if(name == null || password == null || "".equals(name.trim()) || "".equals(password.trim())) {
            result.put("code", "1");
            result.put("msg", "用户名和密码不能为空");
        } else {
            UsernamePasswordToken token = new UsernamePasswordToken(name.trim(), password.trim());
            Subject subject = SecurityUtils.getSubject();

            try {
                subject.login(token);
                result.put("code", "0");
                result.put("msg", "登陆成功");
            } catch (IncorrectCredentialsException e) {
                result.put("code", "2");
                result.put("msg", "密码错误");
                e.printStackTrace();
            } catch (UnknownAccountException e) {
                result.put("code", "3");
                result.put("msg", "用户名不存在");
                e.printStackTrace();
            } catch (Exception e) {
                result.put("code", "4");
                result.put("msg", "登陆失败");
                e.printStackTrace();
            }
        }

        return result;
    }

    @RequestMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        return "退出成功";
    }
}
