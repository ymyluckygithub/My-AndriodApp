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

class ContactAdapter(
    private val context: Context,
    private val originalContactList: List<Friend>,
    private val onContactClick: (Friend) -> Unit
) : BaseAdapter(), Filterable {
    
    private var filteredContactList: List<Friend> = originalContactList
    
    override fun getCount(): Int = filteredContactList.size
    
    override fun getItem(position: Int): Friend = filteredContactList[position]
    
    override fun getItemId(position: Int): Long = position.toLong()
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
        
        val contact = filteredContactList[position]
        
        val ivAvatar = view.findViewById<ImageView>(R.id.iv_contact_avatar)
        val tvName = view.findViewById<TextView>(R.id.tv_contact_name)
        val tvUsername = view.findViewById<TextView>(R.id.tv_contact_username)
        val tvSignature = view.findViewById<TextView>(R.id.tv_contact_signature)
        
        // 设置联系人信息
        ImageLoader.loadAvatar(context, ivAvatar, contact.getDisplayAvatar())
        tvName.text = contact.getDisplayName()
        tvUsername.text = "用户名: ${contact.username}"
        tvSignature.text = if (contact.signature.isNotEmpty()) contact.signature else "这个人很懒，什么都没留下"
        
        // 设置点击事件
        view.setOnClickListener {
            onContactClick(contact)
        }
        
        return view
    }
    
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase() ?: ""
                
                val filtered = if (query.isEmpty()) {
                    originalContactList
                } else {
                    originalContactList.filter { contact ->
                        contact.username.lowercase().contains(query) ||
                        contact.nickname.lowercase().contains(query) ||
                        contact.signature.lowercase().contains(query)
                    }
                }
                
                return FilterResults().apply {
                    values = filtered
                    count = filtered.size
                }
            }
            
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredContactList = results?.values as? List<Friend> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
    
    fun updateContactList(newContactList: List<Friend>) { // 更新联系人列表
        filteredContactList = newContactList
        notifyDataSetChanged()
    }
}
