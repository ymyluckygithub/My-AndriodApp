package cn.edu.ecust.myandroidapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import cn.edu.ecust.myandroidapp.R

class AvatarAdapter(
    private val context: Context,
    private val avatarList: List<Int>,
    private val onAvatarSelected: (Int) -> Unit
) : BaseAdapter() {
    
    private var selectedPosition = 0 // 默认选择第一个
    
    override fun getCount(): Int = avatarList.size
    
    override fun getItem(position: Int): Int = avatarList[position]
    
    override fun getItemId(position: Int): Long = position.toLong()
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_avatar, parent, false)
        
        val ivAvatar = view.findViewById<ImageView>(R.id.iv_avatar)
        val ivSelected = view.findViewById<ImageView>(R.id.iv_selected)
        
        // 设置头像
        ivAvatar.setImageResource(avatarList[position])
        
        // 设置选中状态
        ivSelected.visibility = if (position == selectedPosition) View.VISIBLE else View.GONE
        
        // 设置点击事件
        view.setOnClickListener {
            val oldPosition = selectedPosition
            selectedPosition = position
            
            // 通知数据变化
            notifyDataSetChanged()
            
            // 回调选择事件
            onAvatarSelected(avatarList[position])
        }
        
        return view
    }
    
    fun setSelectedPosition(position: Int) { // 设置选中位置
        if (position in 0 until avatarList.size) {
            selectedPosition = position
            notifyDataSetChanged()
        }
    }
    
    fun getSelectedAvatar(): Int { // 获取选中头像
        return avatarList[selectedPosition]
    }
}
