package com.coding.demo.service;

import java.util.List;

import com.coding.demo.domain.User;

public interface UserService {
    
    /**
     * 用户登录
     * @param name 用户名
     * @param password 密码
     * @return 用户信息
     */
    User login(String name, String password);
    
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    int register(User user);
    
    /**
     * 重置用户密码
     * @param id 用户ID
     * @param newPassword 新密码
     * @return 更新结果
     */
    int resetPassword(Integer id, String newPassword);
    
    /**
     * 更新用户权限
     * @param id 用户ID
     * @param role 角色权限
     * @param userType 用户类型
     * @return 更新结果
     */
    int updateUserPermission(Integer id, Integer role, Integer userType);
    
    /**
     * 搜索用户
     * @param keyword 关键词
     * @return 用户列表
     */
    List<User> searchUsers(String keyword);
    
    /**
     * 获取用户详情
     * @param id 用户ID
     * @return 用户信息
     */
    User getUserById(Integer id);
    
    /**
     * 获取用户统计数据
     * @return 用户统计信息
     */
    Object getUserStatistics();
    
    /**
     * 根据用户名查询用户
     * @param name 用户名
     * @return 用户信息
     */
    User selectUser(String name);
}