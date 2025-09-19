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
import cn.edu.ecust.myandroidapp.adapter.InterestAdapter
import cn.edu.ecust.myandroidapp.model.User
import cn.edu.ecust.myandroidapp.utils.Constants
import cn.edu.ecust.myandroidapp.utils.PreferenceManager
import cn.edu.ecust.myandroidapp.utils.LocationHelper
import cn.edu.ecust.myandroidapp.service.LocationService
import cn.edu.ecust.myandroidapp.widget.CustomTitleBar


class LoginActivity : AppCompatActivity() {
    
    private lateinit var titleBar: CustomTitleBar
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var spinnerAge: Spinner
    private lateinit var spinnerGender: Spinner
    private lateinit var gvInterests: GridView
    private lateinit var gvAvatars: GridView
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var avatarAdapter: AvatarAdapter
    private lateinit var interestAdapter: InterestAdapter
    private lateinit var preferenceManager: PreferenceManager

    private var selectedAvatarResId = R.drawable.avatar1 // 默认选择第一个头像
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginActivity", "onCreate started")
        try {
            Log.d("LoginActivity", "Setting content view")
            setContentView(R.layout.activity_login)

            Log.d("LoginActivity", "Initializing views")
            initViews()

            Log.d("LoginActivity", "Initializing spinners")
            initSpinners()

            Log.d("LoginActivity", "Initializing interest grid")
            initInterestGrid()

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
        spinnerAge = findViewById(R.id.spinner_age)
        spinnerGender = findViewById(R.id.spinner_gender)
        gvInterests = findViewById(R.id.gv_interests)
        gvAvatars = findViewById(R.id.gv_avatars)
        btnLogin = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progress_bar)

        titleBar.setTitle("用户注册")
        titleBar.setBackButtonVisible(false)
    }

    private fun initSpinners() { // 初始化下拉选择器
        // 年龄选择器
        val ageList = (18..50).map { "${it}岁" }.toTypedArray()
        val ageAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ageList)
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAge.adapter = ageAdapter
        spinnerAge.setSelection(7) // 默认选择25岁

        // 性别选择器
        val genderList = arrayOf("男", "女")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderList)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = genderAdapter
    }

    private fun initInterestGrid() { // 初始化兴趣网格
        interestAdapter = InterestAdapter(this, Constants.INTEREST_TAGS)
        gvInterests.adapter = interestAdapter
    }

    private fun initAvatarGrid() { // 初始化头像网格
        val avatarList = listOf(
//            R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3, R.drawable.avatar_4,
//            R.drawable.avatar_5, R.drawable.avatar_6, R.drawable.avatar_7, R.drawable.avatar_8
            R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4,
            R.drawable.avatar5, R.drawable.avatar6, R.drawable.avatar7, R.drawable.avatar8
        )
        
        avatarAdapter = AvatarAdapter(this, avatarList) { avatarResId ->
            selectedAvatarResId = avatarResId
        }
        gvAvatars.adapter = avatarAdapter
    }
    
    private fun setupListeners() { // 设置监听器
        btnLogin.setOnClickListener {
            performRegistration()
        }
    }
    
    private fun performRegistration() { // 执行注册
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val age = getSelectedAge()
        val gender = getSelectedGender()
        val interests = interestAdapter.getSelectedInterests()

        if (validateInput(username, password, age, interests)) {
            showLoading(true)

            // 请求位置权限并获取位置
            LocationHelper.getCurrentLocationWithUI(this) { location ->
                // 模拟注册过程
                Handler(Looper.getMainLooper()).postDelayed({
                    val user = User(
                        username = username,
                        password = password,
                        avatarResId = selectedAvatarResId,
                        age = age,
                        gender = gender,
                        interests = interests.joinToString(","),
                        latitude = location?.latitude ?: Constants.DEFAULT_LATITUDE,
                        longitude = location?.longitude ?: Constants.DEFAULT_LONGITUDE,
                        isLoggedIn = true,
                        lastLoginTime = System.currentTimeMillis()
                    )

                    preferenceManager.saveUser(user)
                    showLoading(false)
                    Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                }, 2000) // 2秒加载动画
            }
        }
    }

    private fun getSelectedAge(): Int { // 获取选中的年龄
        val ageText = spinnerAge.selectedItem.toString()
        return ageText.replace("岁", "").toInt()
    }

    private fun getSelectedGender(): String { // 获取选中的性别
        return spinnerGender.selectedItem.toString()
    }

    private fun validateInput(username: String, password: String, age: Int, interests: List<String>): Boolean { // 验证输入
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
        if (age < 18 || age > 50) {
            Toast.makeText(this, "年龄必须在18-50岁之间", Toast.LENGTH_SHORT).show()
            return false
        }
        if (interests.isEmpty()) {
            Toast.makeText(this, "请至少选择一个兴趣", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    
    private fun showLoading(show: Boolean) { // 显示加载状态
        if (show) {
            progressBar.visibility = View.VISIBLE
            btnLogin.isEnabled = false
            btnLogin.text = "注册中..."
        } else {
            progressBar.visibility = View.GONE
            btnLogin.isEnabled = true
            btnLogin.text = "注册"
        }
    }
    
    private fun navigateToMain() { // 跳转到主界面
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Constants.EXTRA_USER, preferenceManager.getUser())
        startActivity(intent)
        finish()
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
                Toast.makeText(this, "位置权限已授权，可以查找附近的人", Toast.LENGTH_SHORT).show()
            },
            onDenied = {
                Toast.makeText(this, "位置权限被拒绝，将使用默认位置", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
