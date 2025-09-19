package cn.edu.ecust.myandroidapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.edu.ecust.myandroidapp.adapter.FriendAdapter
import cn.edu.ecust.myandroidapp.adapter.NotificationAdapter
import cn.edu.ecust.myandroidapp.model.Friend
import cn.edu.ecust.myandroidapp.model.Notification
import cn.edu.ecust.myandroidapp.model.User
import cn.edu.ecust.myandroidapp.utils.Constants
import cn.edu.ecust.myandroidapp.utils.ImageLoader
import cn.edu.ecust.myandroidapp.utils.PreferenceManager
import cn.edu.ecust.myandroidapp.utils.LocationHelper
import cn.edu.ecust.myandroidapp.service.LocationService
import cn.edu.ecust.myandroidapp.service.MatchingService
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {

    private lateinit var titleBar: LinearLayout
    private lateinit var ivUserAvatar: ImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvWelcome: TextView
    private lateinit var btnContacts: Button
    private lateinit var btnMessages: Button
    private lateinit var btnRandomMatch: Button
    private lateinit var btnNearbyPeople: Button
    private lateinit var lvFriends: ListView
    private lateinit var lvNotifications: ListView
    private lateinit var searchView: SearchView
    private lateinit var tvUnreadCount: TextView
    private lateinit var btnClearNotifications: Button
    private lateinit var btnMenu: ImageButton
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var matchingService: MatchingService
    private var currentUser: User? = null
    private val friendList = mutableListOf<Friend>()
    private val notificationList = mutableListOf<Notification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate started")
        try {
            Log.d("MainActivity", "Setting content view")
            setContentView(R.layout.activity_main)

            Log.d("MainActivity", "Getting preference manager")
            preferenceManager = PreferenceManager.getInstance(this)

            Log.d("MainActivity", "Getting matching service")
            matchingService = MatchingService.getInstance(this)

            Log.d("MainActivity", "Initializing views")
            initViews()

            Log.d("MainActivity", "Setting up title bar")
            setupTitleBar()

            Log.d("MainActivity", "Setting up buttons")
            setupButtons()

            Log.d("MainActivity", "Setting up matching buttons")
            setupMatchingButtons()

            Log.d("MainActivity", "Loading user info")
            loadUserInfo()

            Log.d("MainActivity", "Loading friend data")
            loadFriendData()

            Log.d("MainActivity", "Loading notification data")
            loadNotificationData()

            Log.d("MainActivity", "Setting up friend list")
            setupFriendList()

            Log.d("MainActivity", "Setting up notification list")
            setupNotificationList()

            Log.d("MainActivity", "onCreate completed successfully")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate", e)
            e.printStackTrace()
            Toast.makeText(this, "初始化失败: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initViews() { // 初始化视图
        try {
            Log.d("MainActivity", "Finding title_bar")
            titleBar = findViewById(R.id.title_bar)

            Log.d("MainActivity", "Finding iv_user_avatar")
            ivUserAvatar = findViewById(R.id.iv_user_avatar)

            Log.d("MainActivity", "Finding tv_username")
            tvUsername = findViewById(R.id.tv_username)

            Log.d("MainActivity", "Finding tv_welcome")
            tvWelcome = findViewById(R.id.tv_welcome)

            Log.d("MainActivity", "Finding btn_contacts")
            btnContacts = findViewById(R.id.btn_contacts)

            Log.d("MainActivity", "Finding btn_messages")
            btnMessages = findViewById(R.id.btn_messages)

            Log.d("MainActivity", "Finding btn_random_match")
            btnRandomMatch = findViewById(R.id.btn_random_match)

            Log.d("MainActivity", "Finding btn_nearby_people")
            btnNearbyPeople = findViewById(R.id.btn_nearby_people)

            Log.d("MainActivity", "Finding lv_friends")
            lvFriends = findViewById(R.id.lv_friends)

            Log.d("MainActivity", "Finding lv_notifications")
            lvNotifications = findViewById(R.id.lv_notifications)

            Log.d("MainActivity", "Finding search_view")
            searchView = findViewById(R.id.search_view)

            Log.d("MainActivity", "Finding tv_unread_count")
            tvUnreadCount = findViewById(R.id.tv_unread_count)

            Log.d("MainActivity", "Finding btn_clear_notifications")
            btnClearNotifications = findViewById(R.id.btn_clear_notifications)

            Log.d("MainActivity", "Finding btn_menu")
            btnMenu = findViewById(R.id.btn_menu)

            Log.d("MainActivity", "All views found successfully")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error finding views", e)
            throw e
        }
    }

    private fun setupTitleBar() { // 设置标题栏
        // 现在使用普通LinearLayout，不需要特殊设置
        Log.d("MainActivity", "Title bar setup completed (using LinearLayout)")
    }

    private fun setupButtons() { // 设置按钮点击事件
        btnContacts.setOnClickListener {
            openContacts()
        }

        btnMessages.setOnClickListener {
            showToast("消息功能")
        }

        btnClearNotifications.setOnClickListener {
            clearAllNotifications()
        }

        btnMenu.setOnClickListener {
            Log.d("MainActivity", "Menu button clicked")
            showPopupMenu(it) // 显示弹出菜单
        }
    }

    private fun setupMatchingButtons() { // 设置匹配功能按钮
        btnRandomMatch.setOnClickListener {
            Log.d("MainActivity", "Random match button clicked")
            showToast("开始随机匹配...")
            startRandomMatch()
        }

        btnNearbyPeople.setOnClickListener {
            Log.d("MainActivity", "Nearby people button clicked")
            if (LocationService.hasLocationPermission(this)) {
                showToast("查找附近的人...")
                startNearbyPeople()
            } else {
                LocationHelper.showLocationPermissionDialog(this) {
                    LocationService.requestLocationPermission(this)
                }
            }
        }
    }

    private fun startRandomMatch() { // 开始随机匹配
        try {
            // 跳转到MatchActivity
            val intent = Intent(this, MatchActivity::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error starting random match", e)
            showToast("匹配功能暂时不可用")
        }
    }

    private fun startNearbyPeople() { // 开始查找附近的人
        try {
            // 跳转到NearbyActivity
            val intent = Intent(this, NearbyActivity::class.java)
            startActivity(intent)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error starting nearby people", e)
            showToast("附近的人功能暂时不可用")
        }
    }

    private fun setupFriendList() { // 设置好友列表
        friendAdapter = FriendAdapter(this, friendList) { friend ->
            openChat(friend)
        }
        lvFriends.adapter = friendAdapter

        // 设置搜索功能
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                friendAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                friendAdapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun setupNotificationList() { // 设置通知列表
        notificationAdapter = NotificationAdapter(
            this,
            notificationList,
            { notification -> handleNotificationClick(notification) },
            { notification -> handleNotificationLongClick(notification) }
        )
        lvNotifications.adapter = notificationAdapter

        // 适配器初始化后更新UI
        notificationAdapter.notifyDataSetChanged()
        updateUnreadCount()
        Log.d("MainActivity", "Notification list setup completed")
    }

    private fun loadUserInfo() { // 加载用户信息
        // 从Intent获取用户信息
        currentUser = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.EXTRA_USER, User::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constants.EXTRA_USER)
        }

        // 如果Intent中没有，从SharedPreferences获取
        if (currentUser == null) {
            currentUser = preferenceManager.getUser()
        }

        currentUser?.let { user ->
            tvUsername.text = user.username
            tvWelcome.text = "欢迎回来，${user.username}！"
            ImageLoader.loadAvatar(this, ivUserAvatar, user.getDisplayAvatar())
        }
    }

    private fun loadFriendData() { // 加载好友数据（模拟数据）
        friendList.clear()
        friendList.addAll(listOf(
            Friend("1", "张三", "小张", R.drawable.avatar1, "", true, System.currentTimeMillis(), "今天天气真好！"),
            Friend("2", "李四", "小李", R.drawable.avatar2, "", false, System.currentTimeMillis() - 3600000, "忙碌的一天"),
            Friend("3", "王五", "", R.drawable.avatar3, "", true, System.currentTimeMillis(), "学习使我快乐"),
            Friend("4", "赵六", "小赵", R.drawable.avatar4, "", false, System.currentTimeMillis() - 7200000, ""),
            Friend("5", "钱七", "", R.drawable.avatar5, "", true, System.currentTimeMillis(), "代码改变世界"),
            Friend("6", "孙八", "小孙", R.drawable.avatar6, "", true, System.currentTimeMillis(), "热爱生活"),
            Friend("7", "周九", "", R.drawable.avatar7, "", false, System.currentTimeMillis() - 1800000, "努力工作"),
            Friend("8", "吴十", "小吴", R.drawable.avatar8, "", true, System.currentTimeMillis(), "保持微笑")
        ))
        Log.d("MainActivity", "Friend data loaded: ${friendList.size} friends")
    }

    private fun loadNotificationData() { // 加载通知数据（模拟数据）
        notificationList.clear()
        notificationList.addAll(listOf(
            Notification(
                id = "1",
                title = "张三",
                content = "你好，在吗？",
                senderId = "1",
                senderName = "张三",
                senderAvatar = R.drawable.avatar1,
                timestamp = System.currentTimeMillis() - 300000,
                type = Notification.NotificationType.MESSAGE,
                isRead = false,
                relatedData = "1"
            ),
            Notification(
                id = "2",
                title = "李四",
                content = "明天一起吃饭吧",
                senderId = "2",
                senderName = "李四",
                senderAvatar = R.drawable.avatar2,
                timestamp = System.currentTimeMillis() - 1800000,
                type = Notification.NotificationType.MESSAGE,
                isRead = false,
                relatedData = "2"
            ),
            Notification(
                id = "3",
                title = "王五",
                content = "",
                senderId = "3",
                senderName = "王五",
                senderAvatar = R.drawable.avatar3,
                timestamp = System.currentTimeMillis() - 3600000,
                type = Notification.NotificationType.FRIEND_REQUEST,
                isRead = false,
                relatedData = "3"
            ),
            Notification(
                id = "4",
                title = "系统通知",
                content = "欢迎使用聊天应用！",
                senderId = "system",
                senderName = "系统",
                senderAvatar = R.drawable.ic_default_avatar,
                timestamp = System.currentTimeMillis() - 7200000,
                type = Notification.NotificationType.SYSTEM,
                isRead = true,
                relatedData = ""
            )
        ))
        Log.d("MainActivity", "Notification data loaded: ${notificationList.size} notifications")
        // 适配器初始化后会自动更新UI和未读计数
    }

    private fun handleNotificationClick(notification: Notification) { // 处理通知点击
        when (notification.type) {
            Notification.NotificationType.MESSAGE -> {
                // 跳转到聊天界面
                val friend = friendList.find { it.id == notification.relatedData }
                if (friend != null) {
                    openChat(friend)
                } else {
                    showToast("好友不存在")
                }
            }
            Notification.NotificationType.FRIEND_REQUEST -> {
                showToast("好友请求功能")
            }
            Notification.NotificationType.SYSTEM -> {
                showToast("系统通知：${notification.content}")
            }
        }

        // 标记为已读
        markNotificationAsRead(notification)
    }

    private fun handleNotificationLongClick(notification: Notification) { // 处理通知长按
        android.app.AlertDialog.Builder(this)
            .setTitle("通知操作")
            .setItems(arrayOf("标记为已读", "删除通知")) { _, which ->
                when (which) {
                    0 -> markNotificationAsRead(notification)
                    1 -> deleteNotification(notification)
                }
            }
            .show()
    }

    private fun markNotificationAsRead(notification: Notification) { // 标记通知为已读
        val index = notificationList.indexOf(notification)
        if (index != -1) {
            notificationList[index] = notification.copy(isRead = true)
            notificationAdapter.notifyDataSetChanged()
            updateUnreadCount()
        }
    }

    private fun deleteNotification(notification: Notification) { // 删除通知
        notificationList.remove(notification)
        notificationAdapter.notifyDataSetChanged()
        updateUnreadCount()
        showToast("通知已删除")
    }

    private fun clearAllNotifications() { // 清除所有通知
        android.app.AlertDialog.Builder(this)
            .setTitle("清除通知")
            .setMessage("确定要清除所有通知吗？")
            .setPositiveButton("确定") { _, _ ->
                notificationList.clear()
                notificationAdapter.notifyDataSetChanged()
                updateUnreadCount()
                showToast("所有通知已清除")
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun updateUnreadCount() { // 更新未读数量
        val unreadCount = notificationAdapter.getUnreadCount()
        tvUnreadCount.text = if (unreadCount > 0) "${unreadCount}条未读" else "无未读"
        tvUnreadCount.visibility = if (unreadCount > 0) android.view.View.VISIBLE else android.view.View.GONE
    }

    private fun openChat(friend: Friend) { // 打开聊天界面
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra(Constants.EXTRA_FRIEND, friend)
        intent.putExtra(Constants.EXTRA_CHAT_TITLE, friend.getDisplayName())
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // 创建菜单
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // 菜单项点击
        return when (item.itemId) {
            R.id.menu_contacts -> {
                openContacts()
                true
            }
            R.id.menu_settings -> {
                showToast("设置")
                true
            }
            R.id.menu_help -> {
                showToast("帮助")
                true
            }
            R.id.menu_about -> {
                showToast("关于")
                true
            }
            R.id.menu_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openContacts() { // 打开通讯录
        val intent = Intent(this, ContactActivity::class.java)
        startActivity(intent)
    }

    private fun logout() { // 退出登录
        preferenceManager.clearUser()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun showPopupMenu(view: android.view.View) { // 显示弹出菜单
        Log.d("MainActivity", "Creating popup menu")
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_contacts -> {
                    openContacts()
                    true
                }
                R.id.menu_settings -> {
                    showToast("设置功能")
                    true
                }
                R.id.menu_help -> {
                    showToast("帮助功能")
                    true
                }
                R.id.menu_about -> {
                    showToast("关于")
                    true
                }
                R.id.menu_logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun showToast(message: String) { // 显示提示
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
                showToast("位置权限已授权，可以查找附近的人")
                startNearbyPeople()
            },
            onDenied = {
                showToast("位置权限被拒绝，无法使用附近的人功能")
            }
        )
    }
}