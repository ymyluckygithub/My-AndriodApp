package cn.edu.ecust.myandroidapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import cn.edu.ecust.myandroidapp.R
import cn.edu.ecust.myandroidapp.model.Friend
import cn.edu.ecust.myandroidapp.utils.ImageLoader

class FriendAdapter(
    private val context: Context,
    private val originalFriendList: List<Friend>,
    private val onFriendClick: (Friend) -> Unit
) : BaseAdapter(), Filterable {
    
    private var filteredFriendList: List<Friend> = originalFriendList
    
    override fun getCount(): Int = filteredFriendList.size
    
    override fun getItem(position: Int): Friend = filteredFriendList[position]
    
    override fun getItemId(position: Int): Long = position.toLong()
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false)
        
        val friend = filteredFriendList[position]
        
        val ivAvatar = view.findViewById<ImageView>(R.id.iv_friend_avatar)
        val tvName = view.findViewById<TextView>(R.id.tv_friend_name)
        val tvStatus = view.findViewById<TextView>(R.id.tv_friend_status)
        val tvSignature = view.findViewById<TextView>(R.id.tv_friend_signature)
        val ivOnlineStatus = view.findViewById<ImageView>(R.id.iv_online_status)
        
        // 设置好友信息
        ImageLoader.loadAvatar(context, ivAvatar, friend.getDisplayAvatar())
        tvName.text = friend.getDisplayName()
        tvStatus.text = friend.getStatusText()
        tvSignature.text = if (friend.signature.isNotEmpty()) friend.signature else "这个人很懒，什么都没留下"
        
        // 设置在线状态指示器
        ivOnlineStatus.setImageResource(
            if (friend.isOnline) R.drawable.ic_online else R.drawable.ic_offline
        )
        
        // 设置点击事件
        view.setOnClickListener {
            onFriendClick(friend)
        }
        
        return view
    }
    
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase() ?: ""
                
                val filtered = if (query.isEmpty()) {
                    originalFriendList
                } else {
                    originalFriendList.filter { friend ->
                        friend.username.lowercase().contains(query) ||
                        friend.nickname.lowercase().contains(query)
                    }
                }
                
                return FilterResults().apply {
                    values = filtered
                    count = filtered.size
                }
            }
            
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredFriendList = results?.values as? List<Friend> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
    
    fun updateFriendList(newFriendList: List<Friend>) { // 更新好友列表
        filteredFriendList = newFriendList
        notifyDataSetChanged()
    }
}
