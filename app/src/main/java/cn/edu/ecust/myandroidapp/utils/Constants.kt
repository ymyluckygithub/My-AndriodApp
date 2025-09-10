package cn.edu.ecust.myandroidapp.utils

object Constants {
    
    // Intent Keys
    const val EXTRA_USER = "extra_user"
    const val EXTRA_FRIEND = "extra_friend"
    const val EXTRA_MESSAGE = "extra_message"
    const val EXTRA_CHAT_TITLE = "extra_chat_title"
    
    // Request Codes
    const val REQUEST_CODE_LOGIN = 1001
    const val REQUEST_CODE_CONTACT = 1002
    const val REQUEST_CODE_CHAT = 1003
    const val REQUEST_CODE_PICK_IMAGE = 1004
    
    // Default Values
    const val DEFAULT_AVATAR_COUNT = 8 // 默认头像数量
    const val MAX_MESSAGE_LENGTH = 500 // 最大消息长度
    const val MESSAGE_LOAD_COUNT = 20 // 每次加载消息数量
    
    // Avatar Resource IDs
    val DEFAULT_AVATARS = arrayOf(
        "avatar_1", "avatar_2", "avatar_3", "avatar_4",
        "avatar_5", "avatar_6", "avatar_7", "avatar_8"
    )
    
    // Menu Item IDs
    const val MENU_SETTINGS = 1
    const val MENU_HELP = 2
    const val MENU_ABOUT = 3
    const val MENU_LOGOUT = 4
    
    // Animation Duration
    const val ANIMATION_DURATION_SHORT = 200L
    const val ANIMATION_DURATION_MEDIUM = 300L
    const val ANIMATION_DURATION_LONG = 500L
    
    // Network
    const val CONNECTION_TIMEOUT = 10000L // 连接超时时间
    const val READ_TIMEOUT = 15000L // 读取超时时间
    
    // Database
    const val DB_NAME = "chat_app.db"
    const val DB_VERSION = 1
}
