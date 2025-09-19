package cn.edu.ecust.myandroidapp

import android.content.Context
import cn.edu.ecust.myandroidapp.model.MatchHistory
import cn.edu.ecust.myandroidapp.model.User
import cn.edu.ecust.myandroidapp.service.MatchingService
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.mock

/**
 * MatchActivity功能测试
 * 验证匹配界面的核心功能
 */
class MatchActivityTest {
    
    @Test
    fun testMatchingServiceIntegration() {
        val mockContext = mock(Context::class.java)
        val matchingService = MatchingService.getInstance(mockContext)
        
        // 测试获取随机匹配
        val matches = matchingService.findRandomMatch("test_user")
        assertNotNull("匹配结果不应为空", matches)
        assertTrue("应该返回匹配用户", matches.isNotEmpty())
        
        // 验证匹配用户数据完整性
        matches.forEach { user ->
            assertNotNull("用户ID不应为空", user.id)
            assertNotNull("用户名不应为空", user.username)
            assertTrue("年龄应该合理", user.age in 18..50)
            assertNotNull("性别不应为空", user.gender)
            assertTrue("位置信息应该有效", user.latitude != 0.0 && user.longitude != 0.0)
        }
    }
    
    @Test
    fun testMatchResultSaving() {
        val mockContext = mock(Context::class.java)
        val matchingService = MatchingService.getInstance(mockContext)
        
        // 测试保存接受的匹配结果
        val acceptedMatch = matchingService.saveMatchResult("user1", "user2", true)
        assertNotNull("匹配记录不应为空", acceptedMatch)
        assertEquals("用户1ID应该正确", "user1", acceptedMatch.userId1)
        assertEquals("用户2ID应该正确", "user2", acceptedMatch.userId2)
        assertEquals("状态应该是已接受", MatchHistory.STATUS_ACCEPTED, acceptedMatch.status)
        assertEquals("匹配类型应该是随机", MatchHistory.TYPE_RANDOM, acceptedMatch.matchType)
        
        // 测试保存拒绝的匹配结果
        val rejectedMatch = matchingService.saveMatchResult("user1", "user3", false)
        assertNotNull("匹配记录不应为空", rejectedMatch)
        assertEquals("状态应该是已拒绝", MatchHistory.STATUS_REJECTED, rejectedMatch.status)
    }
    
    @Test
    fun testDistanceCalculation() {
        val mockContext = mock(Context::class.java)
        val matchingService = MatchingService.getInstance(mockContext)
        
        // 测试距离计算
        val distance = matchingService.calculateDistance(
            31.2304, 121.4737, // 上海
            31.2404, 121.4837  // 附近位置
        )
        
        assertTrue("距离应该大于0", distance > 0)
        assertTrue("距离应该合理", distance < 50) // 应该小于50公里
    }
    
    @Test
    fun testUserDataDisplay() {
        // 测试用户数据显示逻辑
        val testUser = User(
            id = "test_user",
            username = "测试用户",
            age = 25,
            gender = "男",
            interests = "音乐,电影,运动",
            latitude = 31.2304,
            longitude = 121.4737
        )
        
        // 验证用户信息
        assertEquals("用户名应该正确", "测试用户", testUser.username)
        assertEquals("年龄应该正确", 25, testUser.age)
        assertEquals("性别应该正确", "男", testUser.gender)
        
        // 验证兴趣列表
        val interests = testUser.getInterestList()
        assertEquals("兴趣数量应该正确", 3, interests.size)
        assertTrue("应该包含音乐", interests.contains("音乐"))
        assertTrue("应该包含电影", interests.contains("电影"))
        assertTrue("应该包含运动", interests.contains("运动"))
        
        // 验证位置信息
        assertTrue("纬度应该有效", testUser.latitude != 0.0)
        assertTrue("经度应该有效", testUser.longitude != 0.0)
    }
    
    @Test
    fun testMatchQueueLogic() {
        val mockContext = mock(Context::class.java)
        val matchingService = MatchingService.getInstance(mockContext)
        
        // 获取匹配用户列表
        val matches = matchingService.findRandomMatch("test_user")
        val matchQueue = matches.toMutableList()
        
        // 测试队列操作
        val originalSize = matchQueue.size
        assertTrue("队列应该不为空", originalSize > 0)
        
        // 模拟取出一个用户
        val firstMatch = matchQueue.removeFirstOrNull()
        assertNotNull("应该能取出用户", firstMatch)
        assertEquals("队列大小应该减1", originalSize - 1, matchQueue.size)
        
        // 测试队列为空的情况
        matchQueue.clear()
        assertTrue("队列应该为空", matchQueue.isEmpty())
        val emptyResult = matchQueue.removeFirstOrNull()
        assertNull("空队列应该返回null", emptyResult)
    }
}
