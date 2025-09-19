package cn.edu.ecust.myandroidapp.service

import android.content.Context
import android.util.Log
import cn.edu.ecust.myandroidapp.model.User
import cn.edu.ecust.myandroidapp.model.MatchHistory
import cn.edu.ecust.myandroidapp.model.Friend
import cn.edu.ecust.myandroidapp.utils.Constants
import cn.edu.ecust.myandroidapp.utils.MockDataGenerator
import java.util.UUID

class MatchingService private constructor(context: Context) {
    
    private val appContext = context.applicationContext
    private val tag = "MatchingService"
    
    companion object {
        @Volatile
        private var INSTANCE: MatchingService? = null
        
        fun getInstance(context: Context): MatchingService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MatchingService(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    // 查找随机匹配用户
    fun findRandomMatch(userId: String): List<User> {
        Log.d(tag, "Finding random match for user: $userId")
        
        val allUsers = generateMockUsers()
        val excludedUsers = getMatchedUsers(userId)
        
        // 过滤掉已匹配的用户和自己
        val availableUsers = allUsers.filter { user ->
            user.id != userId && user.id !in excludedUsers
        }
        
        // 随机选择5个用户
        val matchedUsers = availableUsers.shuffled().take(5)
        Log.d(tag, "Found ${matchedUsers.size} potential matches")
        
        return matchedUsers
    }
    
    // 生成模拟用户数据
    private fun generateMockUsers(): List<User> {
        return listOf(
            User(
                id = "mock_user_1",
                username = "张三",
                age = 25,
                gender = "男",
                interests = "音乐,电影,运动",
                latitude = 31.2304,
                longitude = 121.4737
            ),
            User(
                id = "mock_user_2", 
                username = "李四",
                age = 23,
                gender = "女",
                interests = "旅行,美食,摄影",
                latitude = 31.2404,
                longitude = 121.4837
            ),
            User(
                id = "mock_user_3",
                username = "王五",
                age = 27,
                gender = "男", 
                interests = "读书,编程,游戏",
                latitude = 31.2204,
                longitude = 121.4637
            ),
            User(
                id = "mock_user_4",
                username = "赵六",
                age = 24,
                gender = "女",
                interests = "舞蹈,绘画,音乐",
                latitude = 31.2504,
                longitude = 121.4937
            ),
            User(
                id = "mock_user_5",
                username = "钱七",
                age = 26,
                gender = "男",
                interests = "健身,运动,旅行",
                latitude = 31.2104,
                longitude = 121.4537
            ),
            User(
                id = "mock_user_6",
                username = "孙八",
                age = 22,
                gender = "女",
                interests = "美食,电影,读书",
                latitude = 31.2604,
                longitude = 121.5037
            ),
            User(
                id = "mock_user_7",
                username = "周九",
                age = 28,
                gender = "男",
                interests = "摄影,旅行,音乐",
                latitude = 31.2004,
                longitude = 121.4437
            ),
            User(
                id = "mock_user_8",
                username = "吴十",
                age = 25,
                gender = "女",
                interests = "游戏,编程,电影",
                latitude = 31.2704,
                longitude = 121.5137
            )
        )
    }
    
    // 获取已匹配的用户ID列表（模拟）
    private fun getMatchedUsers(userId: String): List<String> {
        // 简单模拟：假设用户已经匹配过一些用户
        return when (userId) {
            "current_user" -> listOf("mock_user_1", "mock_user_3")
            else -> emptyList()
        }
    }
    
    // 保存匹配记录
    fun saveMatchResult(userId: String, matchedUserId: String, isAccepted: Boolean): MatchHistory {
        val matchHistory = MatchHistory(
            id = UUID.randomUUID().toString(),
            userId1 = userId,
            userId2 = matchedUserId,
            matchType = MatchHistory.TYPE_RANDOM,
            isMutual = false,
            status = if (isAccepted) MatchHistory.STATUS_ACCEPTED else MatchHistory.STATUS_REJECTED
        )
        
        Log.d(tag, "Match result saved: ${matchHistory.id}, accepted: $isAccepted")
        
        // 保存匹配记录到数据库
        // TODO: 集成数据库DAO保存匹配记录
        
        // 如果用户点击了“喜欢”，则将对方添加到好友列表
        if (isAccepted) {
            // 模拟生成好友信息
            val friend = Friend(
                id = matchedUserId,
                username = "模拟好友",
                nickname = "",
                avatarResId = 0,
                avatarPath = "",
                isOnline = false,
                lastSeenTime = 0L,
                signature = ""
            )
            
            // TODO: 集成数据库DAO保存好友信息
            Log.d(tag, "Friend added: ${friend.id}")
        }
        
        return matchHistory
    }
    
    // 查找附近的用户
    fun findNearbyUsers(userId: String, latitude: Double, longitude: Double, radius: Int): List<User> {
        Log.d(tag, "Finding nearby users for user: $userId, radius: ${radius}km")

        val nearbyUsers = MockDataGenerator.generateNearbyUsers(latitude, longitude, radius)
        val excludedUsers = getMatchedUsers(userId)

        // 过滤掉已匹配的用户和自己
        val availableUsers = nearbyUsers.filter { user ->
            user.id != userId && user.id !in excludedUsers
        }

        Log.d(tag, "Found ${availableUsers.size} nearby users")
        return availableUsers
    }

    // 计算两点之间的距离（使用LocationService）
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        return LocationService.calculateDistance(lat1, lon1, lat2, lon2)
    }

    // 获取匹配历史
    fun getMatchHistory(userId: String): List<MatchHistory> {
        Log.d(tag, "Getting match history for user: $userId")
        return MockDataGenerator.generateMockMatchHistory(userId)
    }

    // 获取匹配统计
    fun getMatchStats(userId: String): Map<String, Int> {
        // 模拟统计数据
        return mapOf(
            "total_matches" to (10..50).random(),
            "successful_matches" to (5..25).random(),
            "pending_matches" to (0..5).random()
        )
    }

    // 检查两个用户是否已经匹配过
    fun isAlreadyMatched(userId1: String, userId2: String): Boolean {
        val matchedUsers = getMatchedUsers(userId1)
        return userId2 in matchedUsers
    }
}
