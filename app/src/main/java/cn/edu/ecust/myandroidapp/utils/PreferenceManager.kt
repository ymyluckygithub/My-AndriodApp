package cn.edu.ecust.myandroidapp.utils

import android.content.Context
import android.content.SharedPreferences
import cn.edu.ecust.myandroidapp.model.User

class PreferenceManager private constructor(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREF_NAME = "ChatAppPrefs"
        private const val KEY_USERNAME = "username"
        private const val KEY_AVATAR_RES_ID = "avatar_res_id"
        private const val KEY_AVATAR_PATH = "avatar_path"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_LAST_LOGIN_TIME = "last_login_time"
        
        @Volatile
        private var INSTANCE: PreferenceManager? = null
        
        fun getInstance(context: Context): PreferenceManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferenceManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    fun saveUser(user: User) { // 保存用户信息
        sharedPreferences.edit().apply {
            putString(KEY_USERNAME, user.username)
            putInt(KEY_AVATAR_RES_ID, user.avatarResId)
            putString(KEY_AVATAR_PATH, user.avatarPath)
            putBoolean(KEY_IS_LOGGED_IN, user.isLoggedIn)
            putLong(KEY_LAST_LOGIN_TIME, user.lastLoginTime)
            apply()
        }
    }
    
    fun getUser(): User { // 获取用户信息
        return User(
            username = sharedPreferences.getString(KEY_USERNAME, "") ?: "",
            password = "", // 密码不存储，仅登录时使用
            avatarResId = sharedPreferences.getInt(KEY_AVATAR_RES_ID, 0),
            avatarPath = sharedPreferences.getString(KEY_AVATAR_PATH, "") ?: "",
            isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false),
            lastLoginTime = sharedPreferences.getLong(KEY_LAST_LOGIN_TIME, 0L)
        )
    }
    
    fun clearUser() { // 清除用户信息
        sharedPreferences.edit().clear().apply()
    }
    
    fun isLoggedIn(): Boolean { // 检查登录状态
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
}
