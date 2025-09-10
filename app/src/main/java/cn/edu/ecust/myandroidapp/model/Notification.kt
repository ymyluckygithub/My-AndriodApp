package cn.edu.ecust.myandroidapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Notification(
    val id: String, // 通知ID
    val title: String, // 通知标题
    val content: String, // 通知内容
    val senderId: String, // 发送者ID
    val senderName: String, // 发送者名称
    val senderAvatar: Int = 0, // 发送者头像
    val timestamp: Long, // 通知时间戳
    val type: NotificationType = NotificationType.MESSAGE, // 通知类型
    val isRead: Boolean = false, // 是否已读
    val relatedData: String = "" // 相关数据（如好友ID等）
) : Parcelable {
    
    enum class NotificationType {
        MESSAGE, // 消息通知
        FRIEND_REQUEST, // 好友请求
        SYSTEM // 系统通知
    }
    
    fun getFormattedTime(): String { // 获取格式化时间
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        
        return when {
            diff < 60000 -> "刚刚" // 1分钟内
            diff < 3600000 -> "${diff / 60000}分钟前" // 1小时内
            diff < 86400000 -> "${diff / 3600000}小时前" // 1天内
            else -> {
                val sdf = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
                sdf.format(Date(timestamp))
            }
        }
    }
    
    fun getTypeText(): String { // 获取类型文本
        return when (type) {
            NotificationType.MESSAGE -> "消息"
            NotificationType.FRIEND_REQUEST -> "好友请求"
            NotificationType.SYSTEM -> "系统通知"
        }
    }
    
    fun getDisplayContent(): String { // 获取显示内容
        return when (type) {
            NotificationType.MESSAGE -> content
            NotificationType.FRIEND_REQUEST -> "请求添加您为好友"
            NotificationType.SYSTEM -> content
        }
    }
}
