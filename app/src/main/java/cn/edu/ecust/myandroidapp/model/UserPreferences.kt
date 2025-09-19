package cn.edu.ecust.myandroidapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPreferences(
    val id: Long = 0, // 偏好设置ID
    val userId: String, // 用户ID
    val minAge: Int = 18, // 最小年龄
    val maxAge: Int = 50, // 最大年龄
    val preferredGender: String = "any", // 偏好性别：male, female, any
    val maxDistance: Int = 50, // 最大距离（公里）
    val interestsFilter: String = "", // 兴趣过滤（逗号分隔）
    val createdAt: Long = System.currentTimeMillis(), // 创建时间
    val updatedAt: Long = System.currentTimeMillis() // 更新时间
) : Parcelable {
    
    companion object {
        const val GENDER_MALE = "male"
        const val GENDER_FEMALE = "female"
        const val GENDER_ANY = "any"
    }
    
    // 获取兴趣过滤列表
    fun getInterestsFilterList(): List<String> {
        return if (interestsFilter.isNotEmpty()) interestsFilter.split(",") else emptyList()
    }
    
    // 设置兴趣过滤列表
    fun setInterestsFilterList(interests: List<String>): UserPreferences {
        return this.copy(interestsFilter = interests.joinToString(","))
    }
}
