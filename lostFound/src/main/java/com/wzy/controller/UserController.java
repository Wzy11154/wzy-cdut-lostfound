package com.wzy.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzy.common.BaseContext;
import com.wzy.common.Constants;
import com.wzy.common.Result;
import com.wzy.controller.dto.UserDto;
import com.wzy.controller.dto.UserPasswordDto;
import com.wzy.entity.Users;
import com.wzy.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

//    @GetMapping
//    public Result findAll(){
//        List<Users> list = userService.list();
//        return Result.success(list);
//    }

    @GetMapping("username/{username}")
    public Result selectOne(@PathVariable String username) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return Result.success(userService.getOne(queryWrapper));
    }

    @GetMapping("/pages")
    public Result page(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam(required = false) String username,
                       @RequestParam(required = false) String email, @RequestParam(required = false) String phone){
        IPage<Users> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Strings.isNotEmpty(username),Users::getUsername,username);
        queryWrapper.like(Strings.isNotEmpty(email),Users::getEmail,email);
        queryWrapper.like(Strings.isNotEmpty(phone),Users::getPhone,phone);
        return Result.success(userService.page(page,queryWrapper));
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto, HttpServletRequest request){



        String username = userDto.getUsername();
        String password = userDto.getPassword();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"用户名或密码不能为空");
        }

        UserDto login = userService.login(userDto);
        String nickname = login.getNickname();
        request.getSession().setAttribute("nickname",nickname);

//        System.out.println(Thread.currentThread().getId());

//        BaseContext.setCurrentNickname((String) request.getSession().getAttribute("nickname"));

        return Result.success(login);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDto userDto){
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        return Result.success(userService.register(userDto));
    }

    @PostMapping
    public Result save(@RequestBody Users user){
        return Result.success(userService.saveOrUpdate(user));
    }

    @GetMapping
    public Result findAll(){
        return Result.success(userService.list());
    }

    @PostMapping("/password")
    public Result updatePasswd(@RequestBody UserPasswordDto userPasswordDto){
        userService.updatePassword(userPasswordDto);
        return Result.success();
    }

}
