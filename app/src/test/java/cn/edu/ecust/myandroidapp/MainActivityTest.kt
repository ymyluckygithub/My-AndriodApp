package cn.edu.ecust.myandroidapp

import android.content.Context
import cn.edu.ecust.myandroidapp.service.MatchingService
import cn.edu.ecust.myandroidapp.service.LocationService
import cn.edu.ecust.myandroidapp.utils.Constants
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.mock

/**
 * MainActivity匹配功能测试
 * 验证匹配服务集成和基本功能
 */
class MainActivityTest {
    
    @Test
    fun testMatchingServiceIntegration() {
        val mockContext = mock(Context::class.java)
        val matchingService = MatchingService.getInstance(mockContext)
        
        // 测试随机匹配功能
        val matches = matchingService.findRandomMatch("test_user")
        assertNotNull("匹配结果不应为空", matches)
        assertTrue("应该返回匹配用户", matches.isNotEmpty())
        
        // 测试匹配统计
        val stats = matchingService.getMatchStats("test_user")
        assertNotNull("统计数据不应为空", stats)
        assertTrue("应该包含统计信息", stats.isNotEmpty())
    }
    
    @Test
    fun testLocationServiceIntegration() {
        // 测试距离计算
        val distance = LocationService.calculateDistance(
            31.2304, 121.4737, // 上海
            31.2404, 121.4837  // 附近位置
        )
        
        assertTrue("距离应该大于0", distance > 0)
        assertTrue("距离应该合理", distance < 50) // 应该小于50公里
        
        // 测试位置验证
        val mockLocation = LocationService.getMockLocation()
        assertTrue("模拟位置应该有效", LocationService.isValidLocation(mockLocation))
    }
    
    @Test
    fun testNearbyUsersFunction() {
        val mockContext = mock(Context::class.java)
        val matchingService = MatchingService.getInstance(mockContext)
        
        // 测试附近用户查找
        val nearbyUsers = matchingService.findNearbyUsers(
            "test_user",
            Constants.DEFAULT_LATITUDE,
            Constants.DEFAULT_LONGITUDE,
            Constants.DEFAULT_SEARCH_RADIUS
        )
        
        assertNotNull("附近用户结果不应为空", nearbyUsers)
        // 验证用户数据完整性
        nearbyUsers.forEach { user ->
            assertNotNull("用户ID不应为空", user.id)
            assertNotNull("用户名不应为空", user.username)
            assertTrue("纬度应该合理", user.latitude != 0.0)
            assertTrue("经度应该合理", user.longitude != 0.0)
        }
    }
    
    @Test
    fun testMatchingConstants() {
        // 验证常量配置
        assertTrue("默认搜索半径应该合理", Constants.DEFAULT_SEARCH_RADIUS > 0)
        assertTrue("最小搜索半径应该合理", Constants.MIN_SEARCH_RADIUS > 0)
        assertTrue("最大搜索半径应该合理", Constants.MAX_SEARCH_RADIUS > Constants.MIN_SEARCH_RADIUS)
        
        // 验证默认位置
        assertTrue("默认纬度应该合理", Constants.DEFAULT_LATITUDE != 0.0)
        assertTrue("默认经度应该合理", Constants.DEFAULT_LONGITUDE != 0.0)
    }
}
