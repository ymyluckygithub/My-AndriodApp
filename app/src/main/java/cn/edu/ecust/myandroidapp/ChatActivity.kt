package cn.edu.ecust.myandroidapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.edu.ecust.myandroidapp.adapter.MessageAdapter
import cn.edu.ecust.myandroidapp.model.Friend
import cn.edu.ecust.myandroidapp.model.Message
import cn.edu.ecust.myandroidapp.utils.Constants
import cn.edu.ecust.myandroidapp.utils.PreferenceManager
import cn.edu.ecust.myandroidapp.widget.CustomTitleBar

class ChatActivity : AppCompatActivity() {
    
    private lateinit var titleBar: CustomTitleBar
    private lateinit var lvMessages: ListView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: Button
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var preferenceManager: PreferenceManager
    
    private var chatFriend: Friend? = null
    private var currentUserId: String = ""
    private val messageList = mutableListOf<Message>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        
        preferenceManager = PreferenceManager.getInstance(this)
        currentUserId = preferenceManager.getUser().username
        
        initViews()
        setupTitleBar()
        setupMessageList()
        setupSendMessage()
        loadChatData()
        loadMessageHistory()
    }
    
    private fun initViews() { // 初始化视图
        titleBar = findViewById(R.id.title_bar)
        lvMessages = findViewById(R.id.lv_messages)
        etMessage = findViewById(R.id.et_message)
        btnSend = findViewById(R.id.btn_send)
    }
    
    private fun setupTitleBar() { // 设置标题栏
        chatFriend = if (android.os.Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(Constants.EXTRA_FRIEND, Friend::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Constants.EXTRA_FRIEND)
        }
        val chatTitle = intent.getStringExtra(Constants.EXTRA_CHAT_TITLE) ?: "聊天"
        
        titleBar.setTitle(chatTitle)
        titleBar.setBackButtonVisible(true)
        titleBar.setMenuButtonVisible(false)
        titleBar.setOnBackClickListener {
            finish()
        }
    }
    
    private fun setupMessageList() { // 设置消息列表
        messageAdapter = MessageAdapter(this, messageList, currentUserId)
        lvMessages.adapter = messageAdapter
    }
    
    private fun setupSendMessage() { // 设置发送消息
        btnSend.setOnClickListener {
            sendMessage()
        }
        
        etMessage.setOnEditorActionListener { _, _, _ ->
            sendMessage()
            true
        }
    }
    
    private fun loadChatData() { // 加载聊天数据
        chatFriend?.let { friend ->
            // 这里可以加载与该好友的聊天记录
        }
    }
    
    private fun loadMessageHistory() { // 加载消息历史（模拟数据）
        messageList.clear()
        
        chatFriend?.let { friend ->
            messageList.addAll(listOf(
                Message(
                    id = "1",
                    content = "你好！",
                    senderId = friend.id,
                    senderName = friend.getDisplayName(),
                    receiverId = currentUserId,
                    timestamp = System.currentTimeMillis() - 3600000
                ),
                Message(
                    id = "2",
                    content = "你好，很高兴认识你！",
                    senderId = currentUserId,
                    senderName = "我",
                    receiverId = friend.id,
                    timestamp = System.currentTimeMillis() - 3500000
                ),
                Message(
                    id = "3",
                    content = "今天天气真不错呢",
                    senderId = friend.id,
                    senderName = friend.getDisplayName(),
                    receiverId = currentUserId,
                    timestamp = System.currentTimeMillis() - 3000000
                ),
                Message(
                    id = "4",
                    content = "是的，适合出去走走",
                    senderId = currentUserId,
                    senderName = "我",
                    receiverId = friend.id,
                    timestamp = System.currentTimeMillis() - 2500000
                )
            ))
        }
        
        messageAdapter.notifyDataSetChanged()
        scrollToBottom()
    }
    
    private fun sendMessage() { // 发送消息
        val messageContent = etMessage.text.toString().trim()
        
        if (messageContent.isEmpty()) {
            Toast.makeText(this, "请输入消息内容", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (messageContent.length > Constants.MAX_MESSAGE_LENGTH) {
            Toast.makeText(this, "消息内容过长", Toast.LENGTH_SHORT).show()
            return
        }
        
        chatFriend?.let { friend ->
            val newMessage = Message(
                id = System.currentTimeMillis().toString(),
                content = messageContent,
                senderId = currentUserId,
                senderName = "我",
                receiverId = friend.id,
                timestamp = System.currentTimeMillis()
            )
            
            messageList.add(newMessage)
            messageAdapter.notifyDataSetChanged()
            scrollToBottom()
            
            etMessage.setText("")
            
            // 模拟接收回复消息
            simulateReply(friend)
        }
    }
    
    private fun simulateReply(friend: Friend) { // 模拟回复消息
        val replies = arrayOf(
            "收到！",
            "好的，明白了",
            "哈哈，有趣",
            "确实如此",
            "我也这么想",
            "不错不错"
        )
        
        // 延迟1-3秒回复
        lvMessages.postDelayed({
            val replyMessage = Message(
                id = System.currentTimeMillis().toString(),
                content = replies.random(),
                senderId = friend.id,
                senderName = friend.getDisplayName(),
                receiverId = currentUserId,
                timestamp = System.currentTimeMillis()
            )
            
            messageList.add(replyMessage)
            messageAdapter.notifyDataSetChanged()
            scrollToBottom()
        }, (1000..3000).random().toLong())
    }
    
    private fun scrollToBottom() { // 滚动到底部
        if (messageList.isNotEmpty()) {
            lvMessages.setSelection(messageList.size - 1)
        }
    }
}
