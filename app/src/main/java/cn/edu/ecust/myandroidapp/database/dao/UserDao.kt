package cn.edu.ecust.myandroidapp.database.dao

import cn.edu.ecust.myandroidapp.model.User

interface UserDao {
    
    // 插入用户
    fun insertUser(user: User): Long
    
    // 根据用户名获取用户
    fun getUserByUsername(username: String): User?
    
    // 根据用户ID获取用户
    fun getUserById(userId: String): User?
    
    // 更新用户信息
    fun updateUser(user: User): Int
    
    // 删除用户
    fun deleteUser(userId: String): Int
    
    // 获取所有用户
    fun getAllUsers(): List<User>
    
    // 根据年龄范围获取用户
    fun getUsersByAgeRange(minAge: Int, maxAge: Int): List<User>
    
    // 根据性别获取用户
    fun getUsersByGender(gender: String): List<User>
    
    // 根据位置范围获取用户
    fun getUsersByLocation(latitude: Double, longitude: Double, radius: Double): List<User>
    
    // 根据兴趣获取用户
    fun getUsersByInterests(interests: List<String>): List<User>
    
    // 更新用户位置
    fun updateUserLocation(userId: String, latitude: Double, longitude: Double): Int
    
    // 更新用户登录状态
    fun updateUserLoginStatus(userId: String, isLoggedIn: Boolean, lastLoginTime: Long): Int
    
    // 检查用户名是否存在
    fun isUsernameExists(username: String): Boolean
}
