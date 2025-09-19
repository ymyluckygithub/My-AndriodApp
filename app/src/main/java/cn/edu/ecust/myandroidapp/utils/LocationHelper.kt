package cn.edu.ecust.myandroidapp.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cn.edu.ecust.myandroidapp.service.LocationService

object LocationHelper {
    
    // 显示位置权限说明对话框
    fun showLocationPermissionDialog(activity: Activity, onPositive: () -> Unit) {
        AlertDialog.Builder(activity)
            .setTitle("位置权限")
            .setMessage("Random Chat需要访问您的位置信息来查找附近的用户。请允许位置权限以获得更好的匹配体验。")
            .setPositiveButton("授权") { _, _ ->
                onPositive()
            }
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(activity, "位置功能将无法使用", Toast.LENGTH_SHORT).show()
            }
            .setCancelable(false)
            .show()
    }
    
    // 显示位置权限被拒绝的对话框
    fun showLocationPermissionDeniedDialog(activity: Activity) {
        AlertDialog.Builder(activity)
            .setTitle("位置权限被拒绝")
            .setMessage("位置权限对于查找附近用户功能是必需的。您可以在设置中手动开启位置权限。")
            .setPositiveButton("去设置") { _, _ ->
                openAppSettings(activity)
            }
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    // 打开应用设置页面
    private fun openAppSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivity(intent)
    }
    
    // 获取位置并显示加载状态
    fun getCurrentLocationWithUI(activity: Activity, callback: (Location?) -> Unit) {
        if (!LocationService.hasLocationPermission(activity)) {
            showLocationPermissionDialog(activity) {
                LocationService.requestLocationPermission(activity)
            }
            return
        }
        
        // 显示获取位置中的提示
        Toast.makeText(activity, "正在获取位置信息...", Toast.LENGTH_SHORT).show()
        
        LocationService.getCurrentLocation(activity) { location ->
            if (location != null) {
                Toast.makeText(activity, "位置获取成功", Toast.LENGTH_SHORT).show()
                callback(location)
            } else {
                Toast.makeText(activity, "位置获取失败，将使用默认位置", Toast.LENGTH_SHORT).show()
                // 使用模拟位置作为备选
                callback(LocationService.getMockLocation())
            }
        }
    }
    
    // 处理权限请求结果
    fun handleLocationPermissionResult(
        activity: Activity,
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) {
        if (requestCode == LocationService.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && 
                grantResults.any { it == android.content.pm.PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(activity, "位置权限已授权", Toast.LENGTH_SHORT).show()
                onGranted()
            } else {
                showLocationPermissionDeniedDialog(activity)
                onDenied()
            }
        }
    }
    
    // 保存用户位置到偏好设置
    fun saveUserLocation(context: Context, location: Location) {
        val prefs = context.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putFloat("latitude", location.latitude.toFloat())
            putFloat("longitude", location.longitude.toFloat())
            putLong("timestamp", location.time)
            apply()
        }
    }
    
    // 从偏好设置获取用户位置
    fun getSavedUserLocation(context: Context): Location? {
        val prefs = context.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
        val latitude = prefs.getFloat("latitude", 0f)
        val longitude = prefs.getFloat("longitude", 0f)
        val timestamp = prefs.getLong("timestamp", 0L)
        
        if (latitude != 0f && longitude != 0f) {
            val location = Location("saved")
            location.latitude = latitude.toDouble()
            location.longitude = longitude.toDouble()
            location.time = timestamp
            return location
        }
        return null
    }
    
    // 检查保存的位置是否过期（超过1小时）
    fun isSavedLocationExpired(context: Context): Boolean {
        val prefs = context.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
        val timestamp = prefs.getLong("timestamp", 0L)
        val currentTime = System.currentTimeMillis()
        val oneHour = 60 * 60 * 1000L // 1小时的毫秒数
        
        return (currentTime - timestamp) > oneHour
    }
    
    // 获取位置或使用缓存
    fun getLocationWithCache(activity: Activity, callback: (Location?) -> Unit) {
        val savedLocation = getSavedUserLocation(activity)
        
        if (savedLocation != null && !isSavedLocationExpired(activity)) {
            // 使用缓存的位置
            callback(savedLocation)
        } else {
            // 获取新的位置
            getCurrentLocationWithUI(activity) { location ->
                if (location != null) {
                    saveUserLocation(activity, location)
                }
                callback(location)
            }
        }
    }
}
