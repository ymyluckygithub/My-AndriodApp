package cn.edu.ecust.myandroidapp.utils

import cn.edu.ecust.myandroidapp.model.User
import cn.edu.ecust.myandroidapp.model.MatchHistory
import java.util.UUID

object MockDataGenerator {
    
    // 生成模拟附近用户数据
    fun generateNearbyUsers(centerLat: Double, centerLon: Double, radius: Int): List<User> {
        val nearbyUsers = mutableListOf<User>()
        
        // 根据半径生成不同距离的用户
        val radiusInDegrees = radius / 111.0 // 大约1度 = 111公里
        
        val mockUsers = listOf(
            "王小明" to "男",
            "李小红" to "女", 
            "张小华" to "男",
            "刘小美" to "女",
            "陈小强" to "男",
            "杨小丽" to "女"
        )
        
        mockUsers.forEachIndexed { index, (name, gender) ->
            // 在指定半径内随机生成位置
            val randomLat = centerLat + (Math.random() - 0.5) * radiusInDegrees
            val randomLon = centerLon + (Math.random() - 0.5) * radiusInDegrees
            
            nearbyUsers.add(
                User(
                    id = "nearby_user_${index + 1}",
                    username = name,
                    age = (20..35).random(),
                    gender = gender,
                    interests = getRandomInterests(),
                    latitude = randomLat,
                    longitude = randomLon
                )
            )
        }
        
        return nearbyUsers
    }
    
    // 生成随机兴趣标签
    private fun getRandomInterests(): String {
        val allInterests = Constants.INTEREST_TAGS
        val selectedCount = (2..4).random()
        return allInterests.toList().shuffled().take(selectedCount).joinToString(",")
    }
    
    // 生成模拟匹配历史
    fun generateMockMatchHistory(userId: String): List<MatchHistory> {
        val matchHistory = mutableListOf<MatchHistory>()
        
        // 生成5-10条匹配记录
        val recordCount = (5..10).random()
        
        repeat(recordCount) { index ->
            matchHistory.add(
                MatchHistory(
                    id = UUID.randomUUID().toString(),
                    userId1 = userId,
                    userId2 = "mock_user_${index + 1}",
                    matchType = if (index % 2 == 0) MatchHistory.TYPE_RANDOM else MatchHistory.TYPE_NEARBY,
                    isMutual = (index % 3 == 0), // 1/3概率互相匹配
                    status = when (index % 3) {
                        0 -> MatchHistory.STATUS_ACCEPTED
                        1 -> MatchHistory.STATUS_REJECTED
                        else -> MatchHistory.STATUS_PENDING
                    },
                    createdAt = System.currentTimeMillis() - (index * 24 * 60 * 60 * 1000) // 每天一条记录
                )
            )
        }
        
        return matchHistory
    }
    
    // 生成模拟统计数据
    fun generateMockStats(): Map<String, Any> {
        return mapOf(
            "daily_matches" to (5..15).random(),
            "weekly_matches" to (20..80).random(),
            "total_users" to (100..500).random(),
            "success_rate" to (30..70).random(),
            "popular_interests" to listOf("音乐", "电影", "旅行", "美食", "运动").shuffled().take(3)
        )
    }
    
    // 生成模拟用户偏好数据
    fun generateDefaultPreferences(userId: String): Map<String, Any> {
        return mapOf(
            "min_age" to 18,
            "max_age" to 35,
            "preferred_gender" to "any",
            "max_distance" to 50,
            "interests_filter" to ""
        )
    }
}
