package cn.edu.ecust.myandroidapp.service

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.math.*

object LocationService {
    
    private const val TAG = "LocationService"
    const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    
    // 权限检查
    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    // 请求位置权限
    fun requestLocationPermission(activity: Activity) {
        Log.d(TAG, "Requesting location permission")
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
    
    // 获取当前位置
    fun getCurrentLocation(activity: Activity, callback: (Location?) -> Unit) {
        Log.d(TAG, "Getting current location")
        
        if (!hasLocationPermission(activity)) {
            Log.w(TAG, "Location permission not granted")
            requestLocationPermission(activity)
            callback(null)
            return
        }
        
        try {
            val fusedLocationClient: FusedLocationProviderClient = 
                LocationServices.getFusedLocationProviderClient(activity)
            
            // 首先尝试获取最后已知位置
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        Log.d(TAG, "Got last known location: ${location.latitude}, ${location.longitude}")
                        callback(location)
                    } else {
                        // 如果没有最后已知位置，请求当前位置
                        getCurrentLocationFresh(fusedLocationClient, callback)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Failed to get last location", exception)
                    getCurrentLocationFresh(fusedLocationClient, callback)
                }
        } catch (e: SecurityException) {
            Log.e(TAG, "Security exception when getting location", e)
            callback(null)
        }
    }
    
    // 获取实时位置
    private fun getCurrentLocationFresh(
        fusedLocationClient: FusedLocationProviderClient,
        callback: (Location?) -> Unit
    ) {
        try {
            val cancellationTokenSource = CancellationTokenSource()
            
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        Log.d(TAG, "Got fresh location: ${location.latitude}, ${location.longitude}")
                    } else {
                        Log.w(TAG, "Fresh location is null")
                    }
                    callback(location)
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Failed to get fresh location", exception)
                    callback(null)
                }
        } catch (e: SecurityException) {
            Log.e(TAG, "Security exception when getting fresh location", e)
            callback(null)
        }
    }
    
    // 计算两点之间的距离（使用Haversine公式）
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0 // 地球半径（公里）
        
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        
        return earthRadius * c
    }
    
    // 简单的直线距离计算（用于快速估算）
    fun calculateSimpleDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val deltaLat = lat1 - lat2
        val deltaLon = lon1 - lon2
        return sqrt(deltaLat * deltaLat + deltaLon * deltaLon) * 111.0 // 大约1度 = 111公里
    }
    
    // 检查位置是否在指定半径内
    fun isWithinRadius(
        centerLat: Double, centerLon: Double,
        targetLat: Double, targetLon: Double,
        radiusKm: Double
    ): Boolean {
        val distance = calculateDistance(centerLat, centerLon, targetLat, targetLon)
        return distance <= radiusKm
    }
    
    // 格式化距离显示
    fun formatDistance(distanceKm: Double): String {
        return when {
            distanceKm < 1.0 -> "${(distanceKm * 1000).toInt()}m"
            distanceKm < 10.0 -> String.format("%.1fkm", distanceKm)
            else -> "${distanceKm.toInt()}km"
        }
    }
    
    // 获取模拟位置（用于测试）
    fun getMockLocation(): Location {
        val mockLocation = Location("mock")
        mockLocation.latitude = 31.2304 // 上海纬度
        mockLocation.longitude = 121.4737 // 上海经度
        mockLocation.accuracy = 10.0f
        mockLocation.time = System.currentTimeMillis()
        return mockLocation
    }
    
    // 验证位置数据有效性
    fun isValidLocation(location: Location?): Boolean {
        return location != null &&
                location.latitude != 0.0 &&
                location.longitude != 0.0 &&
                location.latitude >= -90.0 && location.latitude <= 90.0 &&
                location.longitude >= -180.0 && location.longitude <= 180.0
    }

    fun formatDistance(distance: Double): String { // 格式化距离显示
        return when {
            distance < 1.0 -> "${(distance * 1000).toInt()}m"
            distance < 10.0 -> String.format("%.1fkm", distance)
            else -> "${distance.toInt()}km"
        }
    }
}
