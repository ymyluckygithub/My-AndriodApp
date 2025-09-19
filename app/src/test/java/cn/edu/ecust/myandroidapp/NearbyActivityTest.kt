package cn.edu.ecust.myandroidapp

import android.content.Context
import cn.edu.ecust.myandroidapp.adapter.NearbyUserAdapter
import cn.edu.ecust.myandroidapp.model.User
import cn.edu.ecust.myandroidapp.service.LocationService
import cn.edu.ecust.myandroidapp.service.MatchingService
import cn.edu.ecust.myandroidapp.utils.Constants
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.mock

/**
 * NearbyActivity功能测试
 * 验证附近的人界面的核心功能
 */
class NearbyActivityTest {
    
    @Test
    fun testNearbyUsersLoading() {
        val mockContext = mock(Context::class.java)
        val matchingService = MatchingService.getInstance(mockContext)
        
        // 测试获取附近用户
        val nearbyUsers = matchingService.findNearbyUsers(
            "test_user",
            Constants.DEFAULT_LATITUDE,
            Constants.DEFAULT_LONGITUDE,
            Constants.DEFAULT_SEARCH_RADIUS.toDouble()
        )
        
        assertNotNull("附近用户结果不应为空", nearbyUsers)
        assertTrue("应该返回附近用户", nearbyUsers.isNotEmpty())
        
        // 验证用户数据完整性
        nearbyUsers.forEach { user ->
            assertNotNull("用户ID不应为空", user.id)
            assertNotNull("用户名不应为空", user.username)
            assertTrue("年龄应该合理", user.age in 18..50)
            assertNotNull("性别不应为空", user.gender)
            assertTrue("位置信息应该有效", user.latitude != 0.0 && user.longitude != 0.0)
        }
    }
    
    @Test
    fun testDistanceCalculationAndFormatting() {
        // 测试距离计算
        val distance1 = LocationService.calculateDistance(
            31.2304, 121.4737, // 上海
            31.2404, 121.4837  // 附近位置
        )
        assertTrue("距离应该大于0", distance1 > 0)
        assertTrue("距离应该合理", distance1 < 50)
        
        // 测试距离格式化
        val formatted1 = LocationService.formatDistance(0.5)
        assertEquals("500米应该显示为500m", "500m", formatted1)
        
        val formatted2 = LocationService.formatDistance(2.3)
        assertEquals("2.3公里应该显示为2.3km", "2.3km", formatted2)
        
        val formatted3 = LocationService.formatDistance(15.7)
        assertEquals("15.7公里应该显示为15km", "15km", formatted3)
    }
    
    @Test
    fun testNearbyUserAdapter() {
        val mockContext = mock(Context::class.java)
        
        // 创建测试用户列表
        val testUsers = listOf(
            User(
                id = "user1",
                username = "张三",
                age = 25,
                gender = "男",
                interests = "音乐,电影",
                latitude = 31.2304,
                longitude = 121.4737
            ),
            User(
                id = "user2", 
                username = "李四",
                age = 23,
                gender = "女",
                interests = "运动,旅行",
                latitude = 31.2404,
                longitude = 121.4837
            )
        )
        
        val currentLat = 31.2254
        val currentLon = 121.4687
        
        val adapter = NearbyUserAdapter(
            mockContext,
            testUsers,
            currentLat,
            currentLon
        ) { user ->
            // 点击回调测试
        }
        
        // 测试适配器基本功能
        assertEquals("用户数量应该正确", 2, adapter.count)
        assertEquals("第一个用户应该正确", "张三", adapter.getItem(0).username)
        assertEquals("第二个用户应该正确", "李四", adapter.getItem(1).username)
        
        // 测试距离筛选
        val filteredUsers = adapter.filterByDistance(5.0)
        assertTrue("筛选后应该有用户", filteredUsers.isNotEmpty())
        
        // 测试获取用户距离
        val distance = adapter.getUserDistance(testUsers[0])
        assertTrue("距离应该大于0", distance > 0)
    }
    
    @Test
    fun testDistanceFiltering() {
        val mockContext = mock(Context::class.java)
        val matchingService = MatchingService.getInstance(mockContext)
        
        val currentLat = Constants.DEFAULT_LATITUDE
        val currentLon = Constants.DEFAULT_LONGITUDE
        
        // 获取所有附近用户
        val allUsers = matchingService.findNearbyUsers(
            "test_user",
            currentLat,
            currentLon,
            Constants.MAX_SEARCH_RADIUS.toDouble()
        )
        
        // 测试不同距离的筛选
        val users5km = allUsers.filter { user ->
            LocationService.calculateDistance(
                currentLat, currentLon,
                user.latitude, user.longitude
            ) <= 5.0
        }
        
        val users10km = allUsers.filter { user ->
            LocationService.calculateDistance(
                currentLat, currentLon,
                user.latitude, user.longitude
            ) <= 10.0
        }
        
        // 验证筛选逻辑
        assertTrue("5km范围内用户数应该小于等于10km范围", users5km.size <= users10km.size)
        assertTrue("10km范围内用户数应该小于等于总用户数", users10km.size <= allUsers.size)
    }
    
    @Test
    fun testUserSortingByDistance() {
        val currentLat = 31.2304
        val currentLon = 121.4737
        
        val testUsers = listOf(
            User("user1", "远用户", 25, "男", "", 31.3304, 121.5737), // 较远
            User("user2", "近用户", 23, "女", "", 31.2314, 121.4747), // 较近
            User("user3", "中用户", 27, "男", "", 31.2504, 121.4937)  // 中等
        )
        
        // 按距离排序
        val sortedUsers = testUsers.sortedBy { user ->
            LocationService.calculateDistance(
                currentLat, currentLon,
                user.latitude, user.longitude
            )
        }
        
        // 验证排序结果
        assertEquals("最近的应该是近用户", "近用户", sortedUsers[0].username)
        assertEquals("最远的应该是远用户", "远用户", sortedUsers[2].username)
        
        // 验证距离递增
        val distance1 = LocationService.calculateDistance(
            currentLat, currentLon,
            sortedUsers[0].latitude, sortedUsers[0].longitude
        )
        val distance2 = LocationService.calculateDistance(
            currentLat, currentLon,
            sortedUsers[1].latitude, sortedUsers[1].longitude
        )
        val distance3 = LocationService.calculateDistance(
            currentLat, currentLon,
            sortedUsers[2].latitude, sortedUsers[2].longitude
        )
        
        assertTrue("距离应该递增", distance1 <= distance2)
        assertTrue("距离应该递增", distance2 <= distance3)
    }
}
