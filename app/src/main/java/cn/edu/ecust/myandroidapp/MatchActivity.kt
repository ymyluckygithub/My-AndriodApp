package cn.edu.ecust.myandroidapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.edu.ecust.myandroidapp.model.User
import cn.edu.ecust.myandroidapp.service.MatchingService
import cn.edu.ecust.myandroidapp.utils.Constants
import cn.edu.ecust.myandroidapp.utils.ImageLoader
import cn.edu.ecust.myandroidapp.utils.PreferenceManager
import cn.edu.ecust.myandroidapp.widget.CustomTitleBar

class MatchActivity : AppCompatActivity() {
    
    private lateinit var titleBar: CustomTitleBar
    private lateinit var ivUserAvatar: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var tvUserAge: TextView
    private lateinit var tvUserGender: TextView
    private lateinit var tvUserInterests: TextView
    private lateinit var tvUserDistance: TextView
    private lateinit var btnAccept: Button
    private lateinit var btnReject: Button
    private lateinit var tvMatchStatus: TextView
    
    private lateinit var matchingService: MatchingService
    private lateinit var preferenceManager: PreferenceManager
    private var currentUser: User? = null
    private var currentMatch: User? = null
    private val matchQueue = mutableListOf<User>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MatchActivity", "onCreate started")
        
        try {
            setContentView(R.layout.activity_match)
            
            // 初始化服务
            matchingService = MatchingService.getInstance(this)
            preferenceManager = PreferenceManager.getInstance(this)
            currentUser = preferenceManager.getUser()
            
            initViews()
            setupTitleBar()
            setupButtons()
            loadMatches()
            
            Log.d("MatchActivity", "onCreate completed successfully")
        } catch (e: Exception) {
            Log.e("MatchActivity", "Error in onCreate", e)
            Toast.makeText(this, "初始化失败: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    private fun initViews() { // 初始化视图
        titleBar = findViewById(R.id.title_bar)
        ivUserAvatar = findViewById(R.id.iv_user_avatar)
        tvUserName = findViewById(R.id.tv_user_name)
        tvUserAge = findViewById(R.id.tv_user_age)
        tvUserGender = findViewById(R.id.tv_user_gender)
        tvUserInterests = findViewById(R.id.tv_user_interests)
        tvUserDistance = findViewById(R.id.tv_user_distance)
        btnAccept = findViewById(R.id.btn_accept)
        btnReject = findViewById(R.id.btn_reject)
        tvMatchStatus = findViewById(R.id.tv_match_status)
    }
    
    private fun setupTitleBar() { // 设置标题栏
        titleBar.setTitle("随机匹配")
        titleBar.setBackButtonVisible(true)
        titleBar.setOnBackClickListener {
            finish()
        }
    }
    
    private fun setupButtons() { // 设置按钮
        btnAccept.setOnClickListener {
            handleAccept()
        }
        
        btnReject.setOnClickListener {
            handleReject()
        }
    }
    
    private fun loadMatches() { // 加载匹配用户
        try {
            currentUser?.let { user ->
                val matches = matchingService.findRandomMatch(user.id)
                if (matches.isNotEmpty()) {
                    matchQueue.clear()
                    matchQueue.addAll(matches)
                    loadNextMatch()
                } else {
                    showNoMatchesMessage()
                }
            } ?: run {
                Toast.makeText(this, "用户信息获取失败", Toast.LENGTH_SHORT).show()
                finish()
            }
        } catch (e: Exception) {
            Log.e("MatchActivity", "Error loading matches", e)
            Toast.makeText(this, "加载匹配用户失败", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun loadNextMatch() { // 加载下一个匹配
        if (matchQueue.isEmpty()) {
            // 重新加载匹配用户
            loadMatches()
            return
        }
        
        currentMatch = matchQueue.removeFirstOrNull()
        currentMatch?.let { user ->
            displayUserCard(user)
            updateMatchStatus()
        } ?: showNoMatchesMessage()
    }
    
    private fun displayUserCard(user: User) { // 显示用户卡片
        try {
            // 设置头像
            ImageLoader.loadAvatar(this, ivUserAvatar, user.getDisplayAvatar())
            
            // 设置用户信息
            tvUserName.text = user.username
            tvUserAge.text = "${user.age}岁"
            tvUserGender.text = user.gender
            
            // 设置兴趣
            val interests = user.getInterestList()
            tvUserInterests.text = if (interests.isNotEmpty()) {
                "兴趣：${interests.joinToString(", ")}"
            } else {
                "暂无兴趣信息"
            }
            
            // 计算并显示距离
            currentUser?.let { currentUser ->
                val distance = matchingService.calculateDistance(
                    currentUser.latitude, currentUser.longitude,
                    user.latitude, user.longitude
                )
                tvUserDistance.text = "距离：${String.format("%.1f", distance)}km"
            } ?: run {
                tvUserDistance.text = "距离：未知"
            }
            
        } catch (e: Exception) {
            Log.e("MatchActivity", "Error displaying user card", e)
            Toast.makeText(this, "显示用户信息失败", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun handleAccept() { // 处理接受
        currentMatch?.let { user ->
            try {
                // 保存匹配结果
                val matchResult = matchingService.saveMatchResult(
                    currentUser?.id ?: "", 
                    user.id, 
                    true
                )
                
                Toast.makeText(this, "匹配成功！已添加到好友列表", Toast.LENGTH_SHORT).show()
                Log.d("MatchActivity", "Match accepted: ${matchResult.id}")
                
                // 加载下一个匹配
                loadNextMatch()
                
            } catch (e: Exception) {
                Log.e("MatchActivity", "Error handling accept", e)
                Toast.makeText(this, "操作失败，请重试", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun handleReject() { // 处理拒绝
        currentMatch?.let { user ->
            try {
                // 保存匹配结果
                val matchResult = matchingService.saveMatchResult(
                    currentUser?.id ?: "", 
                    user.id, 
                    false
                )
                
                Toast.makeText(this, "已跳过此用户", Toast.LENGTH_SHORT).show()
                Log.d("MatchActivity", "Match rejected: ${matchResult.id}")
                
                // 加载下一个匹配
                loadNextMatch()
                
            } catch (e: Exception) {
                Log.e("MatchActivity", "Error handling reject", e)
                Toast.makeText(this, "操作失败，请重试", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun updateMatchStatus() { // 更新匹配状态
        val remainingCount = matchQueue.size
        tvMatchStatus.text = "剩余 $remainingCount 个匹配用户"
    }
    
    private fun showNoMatchesMessage() { // 显示无匹配消息
        tvMatchStatus.text = "暂时没有更多匹配用户"
        tvUserName.text = "暂无匹配"
        tvUserAge.text = ""
        tvUserGender.text = ""
        tvUserInterests.text = "请稍后再试，或者调整您的匹配偏好"
        tvUserDistance.text = ""
        ivUserAvatar.setImageResource(R.drawable.ic_default_avatar)
        
        // 禁用按钮
        btnAccept.isEnabled = false
        btnReject.isEnabled = false
        
        Toast.makeText(this, "暂时没有更多匹配用户", Toast.LENGTH_SHORT).show()
    }
}
