package com.coding.demo.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coding.demo.domain.User;
import com.coding.demo.mapper.UserMapper;
import com.coding.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Override
    public User login(String name, String password) {
        User user = userMapper.selectUser(name);
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public int register(User user) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectUser(user.getName());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 设置默认值
        user.setUser_type(0); // 默认为免费用户
        user.setRole(0); // 默认为普通角色
        user.setStatus(1); // 默认为启用状态
        user.setCreate_time(LocalDateTime.now());
        user.setUpdate_time(LocalDateTime.now());
        
        return userMapper.saveUser(user);
    }

    @Override
    public int resetPassword(Integer id, String newPassword) {
        return userMapper.updatePassword(id, newPassword);
    }

    @Override
    public int updateUserPermission(Integer id, Integer role, Integer userType) {
        return userMapper.updatePermission(id, role, userType);
    }

    @Override
    public List<User> searchUsers(String keyword) {
        return userMapper.searchByKeyword(keyword);
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public Object getUserStatistics() {
        // 获取用户统计数据
        int totalUsers = userMapper.countAllUsers();
        int freeUsers = userMapper.countUsersByType(0);
        int paidUsers = userMapper.countUsersByType(1);
        int activeUsers = userMapper.countUsersByStatus(1);
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalUsers", totalUsers);
        statistics.put("freeUsers", freeUsers);
        statistics.put("paidUsers", paidUsers);
        statistics.put("activeUsers", activeUsers);
        statistics.put("inactiveUsers", totalUsers - activeUsers);
        
        return statistics;
    }
    
    @Override
    public User selectUser(String name) {
        return userMapper.selectUser(name);
    }
}