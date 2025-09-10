package cn.edu.ecust.myandroidapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String, // 用户名
    val password: String = "", // 密码（仅登录时使用）
    val avatarResId: Int = 0, // 头像资源ID
    val avatarPath: String = "", // 头像路径
    val isLoggedIn: Boolean = false, // 登录状态
    val lastLoginTime: Long = 0L // 最后登录时间
) : Parcelable {
    
    companion object {
        fun createEmpty() = User("", "", 0, "", false, 0L) // 创建空用户对象
    }
    
    fun getDisplayAvatar(): Any { // 获取显示头像（资源ID或路径）
        return if (avatarResId != 0) avatarResId else avatarPath
    }
}
