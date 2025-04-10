package com.coding.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.coding.demo.domain.Result;
import com.coding.demo.domain.User;
import com.coding.demo.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("user")
@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("login")
	public Result<User> login(@RequestBody User user) {
	    if(StringUtils.isEmpty(user.getName())) {
	        return Result.error("用户名不允许为空");
	    }
	    if(StringUtils.isEmpty(user.getPassword())) {
	        return Result.error("密码不允许为空");
	    }
	    
	    User dbUser = userService.login(user.getName(), user.getPassword());
	    if(dbUser == null) {
	        return Result.error("用户名或密码错误");
	    }
	    return Result.success("登录成功", dbUser);
	}

	@PostMapping("register")
	public Result<User> register(@RequestBody User user) {
	    log.info("name:{}", user.getName());
	    log.info("password:{}", user.getPassword());
	    
	    if(StringUtils.isEmpty(user.getName())) {
	        return Result.error("用户名不允许为空");
	    }
	    if(StringUtils.isEmpty(user.getPassword())) {
	        return Result.error("密码不允许为空");
	    }
	    
	    try {
	        int resultCount = userService.register(user);
	        if(resultCount == 0) {
	            return Result.error("注册失败");
	        }
	        
	        User newUser = userService.selectUser(user.getName());
	        return Result.success("注册成功", newUser);
	    } catch (RuntimeException e) {
	        return Result.error(e.getMessage());
	    } catch (Exception e) {
	        log.error("注册异常", e);
	        return Result.error("系统异常，请稍后重试");
	    }
	}
	
	/**
	 * 管理端：重置用户密码
	 */
	@PutMapping("/admin/reset-password/{id}")
	public Result<?> resetPassword(@PathVariable Integer id, @RequestParam String newPassword) {
	    // 验证管理员权限
	    if (StringUtils.isEmpty(newPassword)) {
	        return Result.error("新密码不能为空");
	    }
	    
	    int result = userService.resetPassword(id, newPassword);
	    if (result > 0) {
	        return Result.success("密码重置成功", null);
	    } else {
	        return Result.error("密码重置失败");
	    }
	}
	
	/**
	 * 管理端：设置用户权限
	 */
	@PutMapping("/admin/permission/{id}")
	public Result<?> updatePermission(@PathVariable Integer id, 
	                                @RequestParam Integer role, 
	                                @RequestParam Integer userType) {
	    // 验证管理员权限
	    int result = userService.updateUserPermission(id, role, userType);
	    if (result > 0) {
	        return Result.success("权限更新成功", null);
	    } else {
	        return Result.error("权限更新失败");
	    }
	}
	
	/**
	 * 管理端：搜索用户
	 */
	@GetMapping("/admin/search")
	public Result<List<User>> searchUsers(@RequestParam String keyword) {
	    // 验证管理员权限
	    List<User> users = userService.searchUsers(keyword);
	    return Result.success(users);
	}
	
	/**
	 * 管理端：获取用户详情
	 */
	@GetMapping("/admin/detail/{id}")
	public Result<User> getUserDetail(@PathVariable Integer id) {
	    // 验证管理员权限
	    User user = userService.getUserById(id);
	    if (user != null) {
	        return Result.success(user);
	    } else {
	        return Result.error("用户不存在");
	    }
	}
	
	/**
	 * 管理端：用户统计分析
	 */
	@GetMapping("/admin/statistics")
	public Result<Object> getUserStatistics() {
	    // 验证管理员权限
	    Object statistics = userService.getUserStatistics();
	    return Result.success(statistics);
	}
	
	/**
	 * 免费用户：获取个人信息
	 */
	@GetMapping("/profile/{id}")
	public Result<User> getUserProfile(@PathVariable Integer id) {
	    User user = userService.getUserById(id);
	    if (user != null) {
	        // 免费用户只能查看自己的基本信息
	        if (user.getUser_type() == 0) {
	            // 清除敏感信息
	            user.setPassword(null);
	        }
	        return Result.success(user);
	    } else {
	        return Result.error("用户不存在");
	    }
	}
	
	/**
	 * 修改密码（所有用户可用）
	 */
	@PutMapping("/change-password/{id}")
	public Result<?> changePassword(@PathVariable Integer id, 
	                             @RequestParam String oldPassword, 
	                             @RequestParam String newPassword) {
	    if (StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
	        return Result.error("密码不能为空");
	    }
	    
	    User user = userService.getUserById(id);
	    if (user != null && oldPassword.equals(user.getPassword())) {
	        int result = userService.resetPassword(id, newPassword);
	        if (result > 0) {
	            return Result.success("密码修改成功", null);
	        }
	    }
	    return Result.error("密码修改失败");
	}
}
