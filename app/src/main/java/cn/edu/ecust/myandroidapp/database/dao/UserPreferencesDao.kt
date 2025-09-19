package cn.edu.ecust.myandroidapp.database.dao

import cn.edu.ecust.myandroidapp.model.UserPreferences

interface UserPreferencesDao {
    
    // 插入用户偏好
    fun insertUserPreferences(preferences: UserPreferences): Long
    
    // 根据用户ID获取偏好设置
    fun getUserPreferences(userId: String): UserPreferences?
    
    // 更新用户偏好
    fun updateUserPreferences(preferences: UserPreferences): Int
    
    // 删除用户偏好
    fun deleteUserPreferences(userId: String): Int
    
    // 检查用户是否有偏好设置
    fun hasUserPreferences(userId: String): Boolean
    
    // 获取默认偏好设置
    fun getDefaultPreferences(userId: String): UserPreferences
}
