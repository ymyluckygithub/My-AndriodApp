package cn.edu.ecust.myandroidapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.edu.ecust.myandroidapp.adapter.NearbyUserAdapter
import cn.edu.ecust.myandroidapp.model.User
import cn.edu.ecust.myandroidapp.service.LocationService
import cn.edu.ecust.myandroidapp.service.MatchingService
import cn.edu.ecust.myandroidapp.utils.Constants
import cn.edu.ecust.myandroidapp.utils.LocationHelper
import cn.edu.ecust.myandroidapp.utils.PreferenceManager
import cn.edu.ecust.myandroidapp.widget.CustomTitleBar

class NearbyActivity : AppCompatActivity() {
    
    private lateinit var titleBar: CustomTitleBar
    private lateinit var seekBarDistance: SeekBar
    private lateinit var tvDistanceLabel: TextView
    private lateinit var tvUserCount: TextView
    private lateinit var lvNearbyUsers: ListView
    
    private lateinit var matchingService: MatchingService
    private lateinit var preferenceManager: PreferenceManager
    private var currentUser: User? = null
    private var currentLat: Double = Constants.DEFAULT_LATITUDE
    private var currentLon: Double = Constants.DEFAULT_LONGITUDE
    
    private val allNearbyUsers = mutableListOf<User>()
    private val filteredUsers = mutableListOf<User>()
    private var nearbyAdapter: NearbyUserAdapter? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("NearbyActivity", "onCreate started")
        
        try {
            setContentView(R.layout.activity_nearby)
            
            // 初始化服务
            matchingService = MatchingService.getInstance(this)
            preferenceManager = PreferenceManager.getInstance(this)
            currentUser = preferenceManager.getUser()
            
            initViews()
            setupTitleBar()
            setupDistanceFilter()
            checkLocationAndLoadUsers()
            
            Log.d("NearbyActivity", "onCreate completed successfully")
        } catch (e: Exception) {
            Log.e("NearbyActivity", "Error in onCreate", e)
            Toast.makeText(this, "初始化失败: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    private fun initViews() { // 初始化视图
        titleBar = findViewById(R.id.title_bar)
        seekBarDistance = findViewById(R.id.seek_bar_distance)
        tvDistanceLabel = findViewById(R.id.tv_distance_label)
        tvUserCount = findViewById(R.id.tv_user_count)
        lvNearbyUsers = findViewById(R.id.lv_nearby_users)
    }
    
    private fun setupTitleBar() { // 设置标题栏
        titleBar.setTitle("附近的人")
        titleBar.setBackButtonVisible(true)
        titleBar.setOnBackClickListener {
            finish()
        }
    }
    
    private fun setupDistanceFilter() { // 设置距离筛选
        seekBarDistance.max = Constants.MAX_SEARCH_RADIUS
        seekBarDistance.progress = Constants.DEFAULT_SEARCH_RADIUS
        updateDistanceLabel(Constants.DEFAULT_SEARCH_RADIUS)
        
        seekBarDistance.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val distance = if (progress < Constants.MIN_SEARCH_RADIUS) {
                    Constants.MIN_SEARCH_RADIUS
                } else {
                    progress
                }
                updateDistanceLabel(distance)
                if (fromUser) {
                    filterUsersByDistance(distance.toDouble())
                }
            }
            
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
    
    private fun updateDistanceLabel(distance: Int) { // 更新距离标签
        tvDistanceLabel.text = "搜索半径：${distance}km"
    }
    
    private fun checkLocationAndLoadUsers() { // 检查位置权限并加载用户
        if (LocationService.hasLocationPermission(this)) {
            getCurrentLocationAndLoadUsers()
        } else {
            LocationHelper.showLocationPermissionDialog(this) {
                LocationService.requestLocationPermission(this)
            }
        }
    }
    
    private fun getCurrentLocationAndLoadUsers() { // 获取当前位置并加载用户
        LocationHelper.getCurrentLocationWithUI(this) { location ->
            if (location != null) {
                currentLat = location.latitude
                currentLon = location.longitude
                loadNearbyUsers()
            } else {
                // 使用默认位置
                Toast.makeText(this, "使用默认位置搜索附近用户", Toast.LENGTH_SHORT).show()
                loadNearbyUsers()
            }
        }
    }
    
    private fun loadNearbyUsers() { // 加载附近用户
        try {
            currentUser?.let { user ->
                val nearbyUsers = matchingService.findNearbyUsers(
                    user.id,
                    currentLat,
                    currentLon,
                    Constants.MAX_SEARCH_RADIUS
                )
                
                allNearbyUsers.clear()
                allNearbyUsers.addAll(nearbyUsers)
                
                // 初始筛选
                val currentDistance = seekBarDistance.progress.toDouble()
                filterUsersByDistance(currentDistance)
                
                Log.d("NearbyActivity", "Loaded ${nearbyUsers.size} nearby users")
            } ?: run {
                Toast.makeText(this, "用户信息获取失败", Toast.LENGTH_SHORT).show()
                finish()
            }
        } catch (e: Exception) {
            Log.e("NearbyActivity", "Error loading nearby users", e)
            Toast.makeText(this, "加载附近用户失败", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun filterUsersByDistance(maxDistance: Double) { // 根据距离筛选用户
        filteredUsers.clear()
        
        allNearbyUsers.forEach { user ->
            val distance = LocationService.calculateDistance(
                currentLat, currentLon,
                user.latitude, user.longitude
            )
            if (distance <= maxDistance) {
                filteredUsers.add(user)
            }
        }
        
        // 按距离排序
        filteredUsers.sortBy { user ->
            LocationService.calculateDistance(
                currentLat, currentLon,
                user.latitude, user.longitude
            )
        }
        
        updateUserList()
        updateUserCount()
    }
    
    private fun updateUserList() { // 更新用户列表
        if (nearbyAdapter == null) {
            nearbyAdapter = NearbyUserAdapter(
                this,
                filteredUsers,
                currentLat,
                currentLon
            ) { user ->
                onUserClick(user)
            }
            lvNearbyUsers.adapter = nearbyAdapter
        } else {
            nearbyAdapter?.notifyDataSetChanged()
        }
    }
    
    private fun updateUserCount() { // 更新用户数量
        tvUserCount.text = "找到 ${filteredUsers.size} 个附近用户"
    }
    
    private fun onUserClick(user: User) { // 处理用户点击
        try {
            // 这里将来可以跳转到用户详情或聊天界面
            // val intent = Intent(this, UserProfileActivity::class.java)
            // intent.putExtra("user", user)
            // startActivity(intent)
            
            // 临时显示用户信息
            val distance = LocationService.calculateDistance(
                currentLat, currentLon,
                user.latitude, user.longitude
            )
            val message = "${user.username}，${user.age}岁，距离${LocationService.formatDistance(distance)}"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            
            Log.d("NearbyActivity", "User clicked: ${user.username}")
        } catch (e: Exception) {
            Log.e("NearbyActivity", "Error handling user click", e)
            Toast.makeText(this, "操作失败", Toast.LENGTH_SHORT).show()
        }
    }
    
    // 处理权限请求结果
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        LocationHelper.handleLocationPermissionResult(
            this, requestCode, permissions, grantResults,
            onGranted = {
                Toast.makeText(this, "位置权限已授权", Toast.LENGTH_SHORT).show()
                getCurrentLocationAndLoadUsers()
            },
            onDenied = {
                Toast.makeText(this, "位置权限被拒绝，使用默认位置", Toast.LENGTH_SHORT).show()
                loadNearbyUsers()
            }
        )
    }
}
