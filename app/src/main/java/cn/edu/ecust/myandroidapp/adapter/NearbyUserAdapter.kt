package cn.edu.ecust.myandroidapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import cn.edu.ecust.myandroidapp.R
import cn.edu.ecust.myandroidapp.model.User
import cn.edu.ecust.myandroidapp.service.LocationService
import cn.edu.ecust.myandroidapp.utils.ImageLoader

class NearbyUserAdapter(
    private val context: Context,
    private val userList: List<User>,
    private val currentUserLat: Double,
    private val currentUserLon: Double,
    private val onUserClick: (User) -> Unit
) : BaseAdapter() {
    
    override fun getCount(): Int = userList.size
    
    override fun getItem(position: Int): User = userList[position]
    
    override fun getItemId(position: Int): Long = position.toLong()
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_nearby_user, parent, false)
        
        val user = userList[position]
        
        val ivAvatar = view.findViewById<ImageView>(R.id.iv_user_avatar)
        val tvName = view.findViewById<TextView>(R.id.tv_user_name)
        val tvAge = view.findViewById<TextView>(R.id.tv_user_age)
        val tvGender = view.findViewById<TextView>(R.id.tv_user_gender)
        val tvInterests = view.findViewById<TextView>(R.id.tv_user_interests)
        val tvDistance = view.findViewById<TextView>(R.id.tv_user_distance)
        
        // 设置用户信息
        ImageLoader.loadAvatar(context, ivAvatar, user.getDisplayAvatar())
        tvName.text = user.username
        tvAge.text = "${user.age}岁"
        tvGender.text = user.gender
        
        // 设置兴趣
        val interests = user.getInterestList()
        tvInterests.text = if (interests.isNotEmpty()) {
            interests.take(3).joinToString(", ") // 最多显示3个兴趣
        } else {
            "暂无兴趣信息"
        }
        
        // 计算并设置距离
        val distance = LocationService.calculateDistance(
            currentUserLat, currentUserLon,
            user.latitude, user.longitude
        )
        tvDistance.text = LocationService.formatDistance(distance)
        
        // 设置点击事件
        view.setOnClickListener {
            onUserClick(user)
        }
        
        return view
    }
    
    // 根据距离筛选用户
    fun filterByDistance(maxDistance: Double): List<User> {
        return userList.filter { user ->
            val distance = LocationService.calculateDistance(
                currentUserLat, currentUserLon,
                user.latitude, user.longitude
            )
            distance <= maxDistance
        }
    }
    
    // 获取用户距离
    fun getUserDistance(user: User): Double {
        return LocationService.calculateDistance(
            currentUserLat, currentUserLon,
            user.latitude, user.longitude
        )
    }
}
