package cn.edu.ecust.myandroidapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Friend(
    val id: String, // 好友ID
    val username: String, // 好友用户名
    val nickname: String = "", // 好友昵称
    val avatarResId: Int = 0, // 头像资源ID
    val avatarPath: String = "", // 头像路径
    val isOnline: Boolean = false, // 在线状态
    val lastSeenTime: Long = 0L, // 最后在线时间
    val signature: String = "" // 个性签名
) : Parcelable {
    
    fun getDisplayName(): String { // 获取显示名称（昵称优先）
        return if (nickname.isNotEmpty()) nickname else username
    }
    
    fun getDisplayAvatar(): Any { // 获取显示头像
        return if (avatarResId != 0) avatarResId else avatarPath
    }
    
    fun getStatusText(): String { // 获取状态文本
        return if (isOnline) "在线" else "离线"
    }
}
