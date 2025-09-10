package cn.edu.ecust.myandroidapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cn.edu.ecust.myandroidapp.adapter.AvatarAdapter
import cn.edu.ecust.myandroidapp.model.User
import cn.edu.ecust.myandroidapp.utils.Constants
import cn.edu.ecust.myandroidapp.utils.PreferenceManager
import cn.edu.ecust.myandroidapp.widget.CustomTitleBar

//测试
class LoginActivity : AppCompatActivity() {
    
    private lateinit var titleBar: CustomTitleBar
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var gvAvatars: GridView
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var avatarAdapter: AvatarAdapter
    private lateinit var preferenceManager: PreferenceManager
    
    private var selectedAvatarResId = R.drawable.avatar_1 // 默认选择第一个头像
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginActivity", "onCreate started")
        try {
            Log.d("LoginActivity", "Setting content view")
            setContentView(R.layout.activity_login)

            Log.d("LoginActivity", "Initializing views")
            initViews()

            Log.d("LoginActivity", "Initializing avatar grid")
            initAvatarGrid()

            Log.d("LoginActivity", "Setting up listeners")
            setupListeners()

            Log.d("LoginActivity", "Getting preference manager")
            preferenceManager = PreferenceManager.getInstance(this)

            Log.d("LoginActivity", "Checking login status")
            // 检查是否已登录
            if (preferenceManager.isLoggedIn()) {
                Log.d("LoginActivity", "User already logged in, navigating to main")
                navigateToMain()
            }
            Log.d("LoginActivity", "onCreate completed successfully")
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error in onCreate", e)
            e.printStackTrace()
            Toast.makeText(this, "初始化失败: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun initViews() { // 初始化视图
        titleBar = findViewById(R.id.title_bar)
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        gvAvatars = findViewById(R.id.gv_avatars)
        btnLogin = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progress_bar)
        
        titleBar.setTitle("用户登录")
        titleBar.setBackButtonVisible(false)
    }
    
    private fun initAvatarGrid() { // 初始化头像网格
        val avatarList = listOf(
            R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3, R.drawable.avatar_4,
            R.drawable.avatar_5, R.drawable.avatar_6, R.drawable.avatar_7, R.drawable.avatar_8
        )
        
        avatarAdapter = AvatarAdapter(this, avatarList) { avatarResId ->
            selectedAvatarResId = avatarResId
        }
        gvAvatars.adapter = avatarAdapter
    }
    
    private fun setupListeners() { // 设置监听器
        btnLogin.setOnClickListener {
            performLogin()
        }
    }
    
    private fun performLogin() { // 执行登录
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        
        if (validateInput(username, password)) {
            showLoading(true)
            
            // 模拟登录过程
            Handler(Looper.getMainLooper()).postDelayed({
                val user = User(
                    username = username,
                    password = password,
                    avatarResId = selectedAvatarResId,
                    isLoggedIn = true,
                    lastLoginTime = System.currentTimeMillis()
                )
                
                preferenceManager.saveUser(user)
                showLoading(false)
                navigateToMain()
            }, 2000) // 2秒加载动画
        }
    }
    
    private fun validateInput(username: String, password: String): Boolean { // 验证输入
        if (username.isEmpty()) {
            etUsername.error = "请输入用户名"
            return false
        }
        if (password.isEmpty()) {
            etPassword.error = "请输入密码"
            return false
        }
        if (password.length < 6) {
            etPassword.error = "密码至少6位"
            return false
        }
        return true
    }
    
    private fun showLoading(show: Boolean) { // 显示加载状态
        if (show) {
            progressBar.visibility = View.VISIBLE
            btnLogin.isEnabled = false
            btnLogin.text = "登录中..."
        } else {
            progressBar.visibility = View.GONE
            btnLogin.isEnabled = true
            btnLogin.text = "登录"
        }
    }
    
    private fun navigateToMain() { // 跳转到主界面
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Constants.EXTRA_USER, preferenceManager.getUser())
        startActivity(intent)
        finish()
    }
}
