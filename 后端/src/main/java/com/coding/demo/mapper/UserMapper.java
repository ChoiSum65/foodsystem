package com.coding.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.coding.demo.domain.User;

@Repository
public interface UserMapper {
	
	@Insert("INSERT INTO tab_users(name, password, user_type, role, status, create_time, update_time) VALUES(#{user.name}, #{user.password}, #{user.user_type}, #{user.role}, #{user.status}, #{user.create_time}, #{user.update_time})")
	int saveUser(@Param("user") User user);
	
	@Select("SELECT * FROM tab_users WHERE name=#{name}")
	User selectUser(@Param("name") String name);
	
	@Select("SELECT * FROM tab_users WHERE id=#{id}")
	User selectById(@Param("id") Integer id);
	
	@Update("UPDATE tab_users SET password=#{password}, update_time=NOW() WHERE id=#{id}")
	int updatePassword(@Param("id") Integer id, @Param("password") String password);
	
	@Update("UPDATE tab_users SET role=#{role}, user_type=#{userType}, update_time=NOW() WHERE id=#{id}")
	int updatePermission(@Param("id") Integer id, @Param("role") Integer role, @Param("userType") Integer userType);
	
	@Select("SELECT * FROM tab_users WHERE name LIKE CONCAT('%', #{keyword}, '%')")
	List<User> searchByKeyword(@Param("keyword") String keyword);
	
	@Select("SELECT COUNT(*) FROM tab_users")
	int countAllUsers();
	
	@Select("SELECT COUNT(*) FROM tab_users WHERE user_type=#{userType}")
	int countUsersByType(@Param("userType") Integer userType);
	
	@Select("SELECT COUNT(*) FROM tab_users WHERE status=#{status}")
	int countUsersByStatus(@Param("status") Integer status);
}

