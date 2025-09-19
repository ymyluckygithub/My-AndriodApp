package cn.edu.ecust.myandroidapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "", // 用户ID
    val username: String, // 用户名
    val password: String = "", // 密码（仅登录时使用）
    val avatarResId: Int = 0, // 头像资源ID
    val avatarPath: String = "", // 头像路径
    val isLoggedIn: Boolean = false, // 登录状态
    val lastLoginTime: Long = 0L, // 最后登录时间
    val age: Int = 18, // 年龄
    val gender: String = "", // 性别
    val latitude: Double = 0.0, // 纬度
    val longitude: Double = 0.0, // 经度
    val interests: String = "", // 兴趣标签（逗号分隔）
    val createdAt: Long = System.currentTimeMillis(), // 创建时间
    val updatedAt: Long = System.currentTimeMillis() // 更新时间
) : Parcelable {
    
    companion object {
        fun createEmpty() = User("", "", "", 0, "", false, 0L) // 创建空用户对象
    }

    // 获取兴趣标签列表
    fun getInterestList(): List<String> {
        return if (interests.isNotEmpty()) interests.split(",") else emptyList()
    }

    // 设置兴趣标签列表
    fun setInterestList(interestList: List<String>): User {
        return this.copy(interests = interestList.joinToString(","))
    }
    
    fun getDisplayAvatar(): Any { // 获取显示头像（资源ID或路径）
        return if (avatarResId != 0) avatarResId else avatarPath
    }
}
