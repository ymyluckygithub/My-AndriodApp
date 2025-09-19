package cn.edu.ecust.myandroidapp.service

import android.content.Context
import cn.edu.ecust.myandroidapp.model.MatchHistory
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.mock

/**
 * 简单的MatchingService测试
 * 验证基本功能是否正常工作
 */
class MatchingServiceTest {
    
    @Test
    fun testFindRandomMatch() {
        // 模拟Context
        val mockContext = mock(Context::class.java)
        val matchingService = MatchingService.getInstance(mockContext)
        
        // 测试随机匹配功能
        val matches = matchingService.findRandomMatch("test_user")
        
        // 验证返回结果
        assertNotNull("匹配结果不应为空", matches)
        assertTrue("应该返回匹配用户", matches.isNotEmpty())
        assertTrue("匹配用户数量应该合理", matches.size <= 5)
        
        // 验证用户数据完整性
        matches.forEach { user ->
            assertNotNull("用户ID不应为空", user.id)
            assertNotNull("用户名不应为空", user.username)
            assertTrue("年龄应该合理", user.age in 18..50)
            assertNotNull("性别不应为空", user.gender)
        }
    }
    
    @Test
    fun testFindNearbyUsers() {
        val mockContext = mock(Context::class.java)
        val matchingService = MatchingService.getInstance(mockContext)
        
        // 测试附近用户查找
        val nearbyUsers = matchingService.findNearbyUsers(
            "test_user", 
            31.2304, // 上海纬度
            121.4737, // 上海经度
            10 // 10公里半径
        )
        
        // 验证返回结果
        assertNotNull("附近用户结果不应为空", nearbyUsers)
        
        // 验证位置数据
        nearbyUsers.forEach { user ->
            assertTrue("纬度应该合理", user.latitude != 0.0)
            assertTrue("经度应该合理", user.longitude != 0.0)
        }
    }
    
    @Test
    fun testCalculateDistance() {
        val mockContext = mock(Context::class.java)
        val matchingService = MatchingService.getInstance(mockContext)
        
        // 测试距离计算
        val distance = matchingService.calculateDistance(
            31.2304, 121.4737, // 上海
            31.2404, 121.4837  // 稍微偏移的位置
        )
        
        assertTrue("距离应该大于0", distance > 0)
        assertTrue("距离应该合理", distance < 100) // 应该小于100公里
    }
    
    @Test
    fun testSaveMatchResult() {
        val mockContext = mock(Context::class.java)
        val matchingService = MatchingService.getInstance(mockContext)
        
        // 测试保存匹配结果
        val matchHistory = matchingService.saveMatchResult(
            "user1", 
            "user2", 
            true
        )
        
        // 验证匹配记录
        assertNotNull("匹配记录不应为空", matchHistory)
        assertEquals("用户1ID应该正确", "user1", matchHistory.userId1)
        assertEquals("用户2ID应该正确", "user2", matchHistory.userId2)
        assertEquals("匹配类型应该正确", MatchHistory.TYPE_RANDOM, matchHistory.matchType)
        assertEquals("状态应该正确", MatchHistory.STATUS_ACCEPTED, matchHistory.status)
    }
    
    @Test
    fun testGetMatchStats() {
        val mockContext = mock(Context::class.java)
        val matchingService = MatchingService.getInstance(mockContext)
        
        // 测试获取匹配统计
        val stats = matchingService.getMatchStats("test_user")
        
        // 验证统计数据
        assertNotNull("统计数据不应为空", stats)
        assertTrue("应该包含总匹配数", stats.containsKey("total_matches"))
        assertTrue("应该包含成功匹配数", stats.containsKey("successful_matches"))
        assertTrue("应该包含待处理匹配数", stats.containsKey("pending_matches"))
        
        // 验证数据合理性
        val totalMatches = stats["total_matches"] ?: 0
        val successfulMatches = stats["successful_matches"] ?: 0
        assertTrue("总匹配数应该大于等于成功匹配数", totalMatches >= successfulMatches)
    }
}
