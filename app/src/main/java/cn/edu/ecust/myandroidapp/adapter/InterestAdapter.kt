package cn.edu.ecust.myandroidapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import cn.edu.ecust.myandroidapp.R

class InterestAdapter(
    private val context: Context,
    private val interestList: Array<String>
) : BaseAdapter() {
    
    private val selectedInterests = mutableSetOf<String>()
    
    override fun getCount(): Int = interestList.size
    
    override fun getItem(position: Int): String = interestList[position]
    
    override fun getItemId(position: Int): Long = position.toLong()
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_interest, parent, false)
        
        val tvInterest = view.findViewById<TextView>(R.id.tv_interest)
        val cbInterest = view.findViewById<CheckBox>(R.id.cb_interest)
        
        val interest = interestList[position]
        
        // 设置兴趣文本
        tvInterest.text = interest
        
        // 设置选中状态
        cbInterest.isChecked = selectedInterests.contains(interest)
        
        // 设置点击事件
        view.setOnClickListener {
            if (selectedInterests.contains(interest)) {
                selectedInterests.remove(interest)
                cbInterest.isChecked = false
            } else {
                selectedInterests.add(interest)
                cbInterest.isChecked = true
            }
        }
        
        // CheckBox点击事件
        cbInterest.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedInterests.add(interest)
            } else {
                selectedInterests.remove(interest)
            }
        }
        
        return view
    }
    
    // 获取选中的兴趣列表
    fun getSelectedInterests(): List<String> {
        return selectedInterests.toList()
    }
    
    // 设置选中的兴趣
    fun setSelectedInterests(interests: List<String>) {
        selectedInterests.clear()
        selectedInterests.addAll(interests)
        notifyDataSetChanged()
    }
    
    // 清空选择
    fun clearSelection() {
        selectedInterests.clear()
        notifyDataSetChanged()
    }
}
