package cn.edu.ecust.myandroidapp.database.dao

import cn.edu.ecust.myandroidapp.model.MatchHistory

interface MatchHistoryDao {
    
    // 插入匹配记录
    fun insertMatch(match: MatchHistory): Long
    
    // 根据ID获取匹配记录
    fun getMatchById(matchId: String): MatchHistory?
    
    // 获取用户的匹配历史
    fun getMatchHistory(userId: String): List<MatchHistory>
    
    // 获取两个用户之间的匹配记录
    fun getMatchBetweenUsers(userId1: String, userId2: String): MatchHistory?
    
    // 更新匹配状态
    fun updateMatchStatus(matchId: String, status: String): Int
    
    // 删除匹配记录
    fun deleteMatch(matchId: String): Int
    
    // 获取用户已匹配的用户ID列表
    fun getMatchedUserIds(userId: String): List<String>
    
    // 获取用户成功匹配的记录
    fun getSuccessfulMatches(userId: String): List<MatchHistory>
    
    // 获取用户待处理的匹配
    fun getPendingMatches(userId: String): List<MatchHistory>
    
    // 检查两个用户是否已匹配
    fun isAlreadyMatched(userId1: String, userId2: String): Boolean
    
    // 获取匹配统计数据
    fun getMatchStats(userId: String): Map<String, Int>
}
