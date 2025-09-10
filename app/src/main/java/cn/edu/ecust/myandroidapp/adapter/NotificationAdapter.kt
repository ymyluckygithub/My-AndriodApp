package cn.edu.ecust.myandroidapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import cn.edu.ecust.myandroidapp.R
import cn.edu.ecust.myandroidapp.model.Notification
import cn.edu.ecust.myandroidapp.utils.ImageLoader

class NotificationAdapter(
    private val context: Context,
    private val notificationList: List<Notification>,
    private val onNotificationClick: (Notification) -> Unit,
    private val onNotificationLongClick: (Notification) -> Unit
) : BaseAdapter() {
    
    override fun getCount(): Int = notificationList.size
    
    override fun getItem(position: Int): Notification = notificationList[position]
    
    override fun getItemId(position: Int): Long = position.toLong()
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
        
        val notification = notificationList[position]
        
        val ivAvatar = view.findViewById<ImageView>(R.id.iv_notification_avatar)
        val tvTitle = view.findViewById<TextView>(R.id.tv_notification_title)
        val tvContent = view.findViewById<TextView>(R.id.tv_notification_content)
        val tvTime = view.findViewById<TextView>(R.id.tv_notification_time)
        val tvType = view.findViewById<TextView>(R.id.tv_notification_type)
        val ivUnread = view.findViewById<ImageView>(R.id.iv_unread_indicator)
        
        // 设置通知信息
        ImageLoader.loadAvatar(context, ivAvatar, notification.senderAvatar)
        tvTitle.text = notification.senderName
        tvContent.text = notification.getDisplayContent()
        tvTime.text = notification.getFormattedTime()
        tvType.text = notification.getTypeText()
        
        // 设置未读状态
        ivUnread.visibility = if (notification.isRead) View.GONE else View.VISIBLE
        
        // 设置背景色（未读通知高亮）
        view.setBackgroundResource(
            if (notification.isRead) android.R.color.transparent 
            else R.color.notification_unread_background
        )
        
        // 设置点击事件
        view.setOnClickListener {
            onNotificationClick(notification)
        }
        
        // 设置长按事件
        view.setOnLongClickListener {
            onNotificationLongClick(notification)
            true
        }
        
        return view
    }
    
    fun getUnreadCount(): Int { // 获取未读数量
        return notificationList.count { !it.isRead }
    }
    
    fun markAsRead(notificationId: String) { // 标记为已读
        notificationList.find { it.id == notificationId }?.let { notification ->
            // 这里应该更新数据源，由于是模拟数据，暂时不实现
        }
        notifyDataSetChanged()
    }
    
    fun markAllAsRead() { // 标记全部为已读
        // 这里应该更新数据源，由于是模拟数据，暂时不实现
        notifyDataSetChanged()
    }

    fun updateNotificationList(newNotificationList: List<Notification>) { // 更新通知列表
        notifyDataSetChanged()
    }
}
