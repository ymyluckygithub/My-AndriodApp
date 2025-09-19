package cn.edu.ecust.myandroidapp.service

import android.location.Location
import org.junit.Test
import org.junit.Assert.*

/**
 * LocationService测试类
 * 验证位置计算和工具方法
 */
class LocationServiceTest {
    
    @Test
    fun testCalculateDistance() {
        // 测试距离计算（上海到北京的大概距离）
        val shanghaiLat = 31.2304
        val shanghaiLon = 121.4737
        val beijingLat = 39.9042
        val beijingLon = 116.4074
        
        val distance = LocationService.calculateDistance(
            shanghaiLat, shanghaiLon,
            beijingLat, beijingLon
        )
        
        // 上海到北京大约1000-1200公里
        assertTrue("距离应该在合理范围内", distance > 1000 && distance < 1300)
    }
    
    @Test
    fun testCalculateSimpleDistance() {
        // 测试简单距离计算
        val lat1 = 31.2304
        val lon1 = 121.4737
        val lat2 = 31.2404
        val lon2 = 121.4837
        
        val distance = LocationService.calculateSimpleDistance(lat1, lon1, lat2, lon2)
        
        assertTrue("简单距离应该大于0", distance > 0)
        assertTrue("简单距离应该小于50公里", distance < 50)
    }
    
    @Test
    fun testIsWithinRadius() {
        val centerLat = 31.2304
        val centerLon = 121.4737
        val nearbyLat = 31.2404
        val nearbyLon = 121.4837
        val farLat = 39.9042
        val farLon = 116.4074
        
        // 测试附近的点（应该在50公里内）
        assertTrue("附近的点应该在半径内", 
            LocationService.isWithinRadius(centerLat, centerLon, nearbyLat, nearbyLon, 50.0))
        
        // 测试远距离的点（应该不在50公里内）
        assertFalse("远距离的点应该不在半径内",
            LocationService.isWithinRadius(centerLat, centerLon, farLat, farLon, 50.0))
    }
    
    @Test
    fun testFormatDistance() {
        // 测试距离格式化
        assertEquals("500m", LocationService.formatDistance(0.5))
        assertEquals("1.5km", LocationService.formatDistance(1.5))
        assertEquals("15km", LocationService.formatDistance(15.0))
        assertEquals("150km", LocationService.formatDistance(150.0))
    }
    
    @Test
    fun testGetMockLocation() {
        val mockLocation = LocationService.getMockLocation()
        
        assertNotNull("模拟位置不应为空", mockLocation)
        assertEquals("模拟位置纬度应该正确", 31.2304, mockLocation.latitude, 0.0001)
        assertEquals("模拟位置经度应该正确", 121.4737, mockLocation.longitude, 0.0001)
        assertTrue("模拟位置时间应该合理", mockLocation.time > 0)
    }
    
    @Test
    fun testIsValidLocation() {
        // 测试有效位置
        val validLocation = Location("test")
        validLocation.latitude = 31.2304
        validLocation.longitude = 121.4737
        assertTrue("有效位置应该通过验证", LocationService.isValidLocation(validLocation))
        
        // 测试无效位置
        assertFalse("空位置应该无效", LocationService.isValidLocation(null))
        
        val invalidLocation1 = Location("test")
        invalidLocation1.latitude = 0.0
        invalidLocation1.longitude = 0.0
        assertFalse("0,0位置应该无效", LocationService.isValidLocation(invalidLocation1))
        
        val invalidLocation2 = Location("test")
        invalidLocation2.latitude = 91.0 // 超出范围
        invalidLocation2.longitude = 121.4737
        assertFalse("超出范围的纬度应该无效", LocationService.isValidLocation(invalidLocation2))
        
        val invalidLocation3 = Location("test")
        invalidLocation3.latitude = 31.2304
        invalidLocation3.longitude = 181.0 // 超出范围
        assertFalse("超出范围的经度应该无效", LocationService.isValidLocation(invalidLocation3))
    }
    
    @Test
    fun testDistanceCalculationAccuracy() {
        // 测试距离计算的准确性
        // 使用已知的两个点进行测试
        val point1Lat = 31.2304 // 上海人民广场
        val point1Lon = 121.4737
        val point2Lat = 31.2431 // 上海火车站
        val point2Lon = 121.4633
        
        val distance = LocationService.calculateDistance(point1Lat, point1Lon, point2Lat, point2Lon)
        
        // 这两个点之间的距离大约是2-3公里
        assertTrue("距离应该在合理范围内", distance > 1.0 && distance < 5.0)
    }
}
