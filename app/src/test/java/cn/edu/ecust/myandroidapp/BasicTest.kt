package cn.edu.ecust.myandroidapp

import cn.edu.ecust.myandroidapp.model.User
import cn.edu.ecust.myandroidapp.service.LocationService
import cn.edu.ecust.myandroidapp.utils.Constants
import org.junit.Test
import org.junit.Assert.*

/**
 * 基本功能测试
 * 验证应用的核心功能
 */
class BasicTest {
    
    @Test
    fun testUserModel() {
        // 测试用户模型
        val user = User("test_id", "testuser")
        assertEquals("用户ID应正确", "test_id", user.id)
        assertEquals("用户名应正确", "testuser", user.username)
    }
    
    @Test
    fun testLocationService() {
        // 测试位置服务
        val distance = LocationService.calculateDistance(
            31.2304, 121.4737, // 上海
            39.9042, 116.4074  // 北京
        )
        assertTrue("距离应大于0", distance > 0)
        
        val formattedDistance = LocationService.formatDistance(distance)
        assertNotNull("格式化距离不应为空", formattedDistance)
        assertTrue("格式化距离应包含单位", formattedDistance.contains("km"))
    }
    
    @Test
    fun testConstants() {
        // 测试常量
        assertTrue("搜索半径应在合理范围内", Constants.DEFAULT_SEARCH_RADIUS > 0)
        assertTrue("最大搜索半径应大于默认值", Constants.MAX_SEARCH_RADIUS >= Constants.DEFAULT_SEARCH_RADIUS)
        assertNotNull("兴趣标签不应为空", Constants.INTEREST_TAGS)
        assertTrue("兴趣标签应有内容", Constants.INTEREST_TAGS.isNotEmpty())
    }
    
    @Test
    fun testInterestTags() {
        // 测试兴趣标签
        val tags = Constants.INTEREST_TAGS
        assertTrue("应有足够的兴趣标签", tags.size >= 5)
        
        // 检查是否包含常见标签
        val commonTags = listOf("音乐", "电影", "运动", "旅行", "美食")
        commonTags.forEach { tag ->
            assertTrue("应包含常见标签: $tag", tags.contains(tag))
        }
    }
}
