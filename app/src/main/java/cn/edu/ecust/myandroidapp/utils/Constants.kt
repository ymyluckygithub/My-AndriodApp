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
        "avatar1", "avatar2", "avatar3", "avatar4",
        "avatar5", "avatar6", "avatar7", "avatar8"
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
    const val DB_VERSION = 2 // 升级版本支持新表

    // Database Tables
    const val TABLE_USERS = "users"
    const val TABLE_FRIENDS = "friends"
    const val TABLE_MESSAGES = "messages"
    const val TABLE_NOTIFICATIONS = "notifications"
    const val TABLE_MATCH_HISTORY = "match_history"
    const val TABLE_USER_PREFERENCES = "user_preferences"
    const val TABLE_REPORT_RECORDS = "report_records"
    const val TABLE_BLOCKED_USERS = "blocked_users"

    // User Table Columns
    const val COL_USER_ID = "id"
    const val COL_USERNAME = "username"
    const val COL_PASSWORD = "password"
    const val COL_AVATAR_RES_ID = "avatar_res_id"
    const val COL_AVATAR_PATH = "avatar_path"
    const val COL_IS_LOGGED_IN = "is_logged_in"
    const val COL_LAST_LOGIN_TIME = "last_login_time"
    const val COL_AGE = "age"
    const val COL_GENDER = "gender"
    const val COL_LATITUDE = "latitude"
    const val COL_LONGITUDE = "longitude"
    const val COL_INTERESTS = "interests"
    const val COL_CREATED_AT = "created_at"
    const val COL_UPDATED_AT = "updated_at"

    // Match History Table Columns
    const val COL_MATCH_ID = "id"
    const val COL_USER_ID_1 = "user_id_1"
    const val COL_USER_ID_2 = "user_id_2"
    const val COL_MATCH_TYPE = "match_type"
    const val COL_IS_MUTUAL = "is_mutual"
    const val COL_STATUS = "status"

    // User Preferences Table Columns
    const val COL_PREF_ID = "id"
    const val COL_PREF_USER_ID = "user_id"
    const val COL_MIN_AGE = "min_age"
    const val COL_MAX_AGE = "max_age"
    const val COL_PREFERRED_GENDER = "preferred_gender"
    const val COL_MAX_DISTANCE = "max_distance"
    const val COL_INTERESTS_FILTER = "interests_filter"

    // Report Records Table Columns
    const val COL_REPORT_ID = "id"
    const val COL_REPORTER_ID = "reporter_id"
    const val COL_REPORTED_ID = "reported_id"
    const val COL_REASON = "reason"
    const val COL_DESCRIPTION = "description"

    // Blocked Users Table Columns
    const val COL_BLOCK_ID = "id"
    const val COL_BLOCK_USER_ID = "user_id"
    const val COL_BLOCKED_USER_ID = "blocked_user_id"

    // Interest Tags
    val INTEREST_TAGS = arrayOf(
        "音乐", "电影", "运动", "旅行", "美食", "读书",
        "游戏", "摄影", "绘画", "舞蹈", "编程", "健身"
    )
}
