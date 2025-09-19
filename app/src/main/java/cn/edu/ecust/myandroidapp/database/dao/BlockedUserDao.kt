package cn.edu.ecust.myandroidapp.database.dao

import cn.edu.ecust.myandroidapp.model.BlockedUser

interface BlockedUserDao {
    
    // 插入屏蔽记录
    fun insertBlockedUser(blockedUser: BlockedUser): Long
    
    // 删除屏蔽记录
    fun deleteBlockedUser(userId: String, blockedUserId: String): Int
    
    // 获取用户的屏蔽列表
    fun getBlockedUsers(userId: String): List<BlockedUser>
    
    // 获取屏蔽的用户ID列表
    fun getBlockedUserIds(userId: String): List<String>
    
    // 检查用户是否被屏蔽
    fun isBlocked(userId: String, targetUserId: String): Boolean
    
    // 清空用户的屏蔽列表
    fun clearBlockedUsers(userId: String): Int
}
