package cn.edu.ecust.myandroidapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Message(
    val id: String, // 消息ID
    val content: String, // 消息内容
    val senderId: String, // 发送者ID
    val senderName: String, // 发送者名称
    val receiverId: String, // 接收者ID
    val timestamp: Long, // 发送时间戳
    val messageType: MessageType = MessageType.TEXT, // 消息类型
    val isRead: Boolean = false // 是否已读
) : Parcelable {
    
    enum class MessageType {
        TEXT, // 文本消息
        IMAGE, // 图片消息
        SYSTEM // 系统消息
    }
    
    fun getFormattedTime(): String { // 获取格式化时间
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    fun getFormattedDate(): String { // 获取格式化日期
        val sdf = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    fun isSentByMe(currentUserId: String): Boolean { // 判断是否为当前用户发送
        return senderId == currentUserId
    }
}
