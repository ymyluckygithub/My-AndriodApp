package cn.edu.ecust.myandroidapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import cn.edu.ecust.myandroidapp.R
import cn.edu.ecust.myandroidapp.model.Message

class MessageAdapter(
    private val context: Context,
    private val messageList: List<Message>,
    private val currentUserId: String
) : BaseAdapter() {
    
    companion object {
        private const val TYPE_SENT = 0
        private const val TYPE_RECEIVED = 1
    }
    
    override fun getCount(): Int = messageList.size
    
    override fun getItem(position: Int): Message = messageList[position]
    
    override fun getItemId(position: Int): Long = position.toLong()
    
    override fun getViewTypeCount(): Int = 2
    
    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (message.isSentByMe(currentUserId)) TYPE_SENT else TYPE_RECEIVED
    }
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val message = messageList[position]
        val viewType = getItemViewType(position)
        
        val view = when (viewType) {
            TYPE_SENT -> {
                convertView ?: LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false)
            }
            TYPE_RECEIVED -> {
                convertView ?: LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
        
        val tvMessage = view.findViewById<TextView>(R.id.tv_message)
        val tvTime = view.findViewById<TextView>(R.id.tv_time)
        val tvSender = view.findViewById<TextView>(R.id.tv_sender)
        
        // 设置消息内容
        tvMessage.text = message.content
        tvTime.text = message.getFormattedTime()
        
        // 设置发送者信息（仅接收消息显示）
        if (viewType == TYPE_RECEIVED) {
            tvSender?.text = message.senderName
        }
        
        return view
    }
}
