package cn.edu.ecust.myandroidapp

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import cn.edu.ecust.myandroidapp.adapter.ContactAdapter
import cn.edu.ecust.myandroidapp.model.Friend
import cn.edu.ecust.myandroidapp.widget.CustomTitleBar

class ContactActivity : AppCompatActivity() {
    
    private lateinit var titleBar: CustomTitleBar
    private lateinit var searchView: SearchView
    private lateinit var lvContacts: ListView
    private lateinit var contactAdapter: ContactAdapter
    private val contactList = mutableListOf<Friend>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        
        initViews()
        setupTitleBar()
        setupContactList()
        loadContactData()
    }
    
    private fun initViews() { // 初始化视图
        titleBar = findViewById(R.id.title_bar)
        searchView = findViewById(R.id.search_view)
        lvContacts = findViewById(R.id.lv_contacts)
    }
    
    private fun setupTitleBar() { // 设置标题栏
        titleBar.setTitle("通讯录管理")
        titleBar.setBackButtonVisible(true)
        titleBar.setMenuButtonVisible(true)
        titleBar.setOnBackClickListener {
            finish()
        }
        titleBar.setOnMenuClickListener {
            openOptionsMenu()
        }
    }
    
    private fun setupContactList() { // 设置联系人列表
        contactAdapter = ContactAdapter(this, contactList) { friend ->
            showContactOptions(friend)
        }
        lvContacts.adapter = contactAdapter
        
        // 设置搜索功能
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                contactAdapter.filter.filter(query)
                return true
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                contactAdapter.filter.filter(newText)
                return true
            }
        })
    }
    
    private fun loadContactData() { // 加载联系人数据
        contactList.clear()
        contactList.addAll(listOf(
            Friend("1", "张三", "小张", R.drawable.avatar1, "", true, System.currentTimeMillis(), "今天天气真好！"),
            Friend("2", "李四", "小李", R.drawable.avatar2, "", false, System.currentTimeMillis() - 3600000, "忙碌的一天"),
            Friend("3", "王五", "", R.drawable.avatar3, "", true, System.currentTimeMillis(), "学习使我快乐"),
            Friend("4", "赵六", "小赵", R.drawable.avatar4, "", false, System.currentTimeMillis() - 7200000, ""),
            Friend("5", "钱七", "", R.drawable.avatar5, "", true, System.currentTimeMillis(), "代码改变世界"),
            Friend("6", "孙八", "小孙", R.drawable.avatar6, "", true, System.currentTimeMillis(), "热爱生活"),
            Friend("7", "周九", "", R.drawable.avatar7, "", false, System.currentTimeMillis() - 1800000, "努力工作"),
            Friend("8", "吴十", "小吴", R.drawable.avatar8, "", true, System.currentTimeMillis(), "保持微笑")
        ))
        contactAdapter.updateContactList(contactList)
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // 创建菜单
        menu?.add(0, 1, 0, "添加联系人")?.setIcon(R.drawable.ic_add)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean { // 菜单项点击
        return when (item.itemId) {
            1 -> {
                showAddContactDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun showAddContactDialog() { // 显示添加联系人对话框
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null)
        val etUsername = dialogView.findViewById<EditText>(R.id.et_username)
        val etNickname = dialogView.findViewById<EditText>(R.id.et_nickname)
        val etSignature = dialogView.findViewById<EditText>(R.id.et_signature)
        
        AlertDialog.Builder(this)
            .setTitle("添加联系人")
            .setView(dialogView)
            .setPositiveButton("添加") { _, _ ->
                val username = etUsername.text.toString().trim()
                val nickname = etNickname.text.toString().trim()
                val signature = etSignature.text.toString().trim()
                
                if (username.isNotEmpty()) {
                    addContact(username, nickname, signature)
                } else {
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun addContact(username: String, nickname: String, signature: String) { // 添加联系人
        val newContact = Friend(
            id = System.currentTimeMillis().toString(),
            username = username,
            nickname = nickname,
            avatarResId = R.drawable.ic_default_avatar,
            isOnline = false,
            lastSeenTime = System.currentTimeMillis(),
            signature = signature
        )
        
        contactList.add(newContact)
        contactAdapter.updateContactList(contactList)
        Toast.makeText(this, "联系人添加成功", Toast.LENGTH_SHORT).show()
    }
    
    private fun showContactOptions(friend: Friend) { // 显示联系人操作选项
        val options = arrayOf("发送消息", "删除联系人")
        
        AlertDialog.Builder(this)
            .setTitle(friend.getDisplayName())
            .setItems(options) { _, which ->
                when (which) {
                    0 -> sendMessage(friend)
                    1 -> confirmDeleteContact(friend)
                }
            }
            .show()
    }
    
    private fun sendMessage(friend: Friend) { // 发送消息
        Toast.makeText(this, "打开与${friend.getDisplayName()}的聊天", Toast.LENGTH_SHORT).show()
    }
    
    private fun confirmDeleteContact(friend: Friend) { // 确认删除联系人
        AlertDialog.Builder(this)
            .setTitle("删除联系人")
            .setMessage("确定要删除联系人 ${friend.getDisplayName()} 吗？")
            .setPositiveButton("删除") { _, _ ->
                deleteContact(friend)
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    private fun deleteContact(friend: Friend) { // 删除联系人
        contactList.remove(friend)
        contactAdapter.updateContactList(contactList)
        Toast.makeText(this, "联系人已删除", Toast.LENGTH_SHORT).show()
    }
}
