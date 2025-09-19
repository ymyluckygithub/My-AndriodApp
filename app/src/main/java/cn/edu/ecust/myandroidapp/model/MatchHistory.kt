package cn.edu.ecust.myandroidapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchHistory(
    val id: String, // 匹配记录ID
    val userId1: String, // 用户1 ID
    val userId2: String, // 用户2 ID
    val matchType: String = "random", // 匹配类型：random, nearby
    val isMutual: Boolean = false, // 是否互相匹配
    val status: String = "pending", // 状态：pending, accepted, rejected
    val createdAt: Long = System.currentTimeMillis() // 创建时间
) : Parcelable {
    
    companion object {
        const val TYPE_RANDOM = "random"
        const val TYPE_NEARBY = "nearby"
        
        const val STATUS_PENDING = "pending"
        const val STATUS_ACCEPTED = "accepted"
        const val STATUS_REJECTED = "rejected"
    }
}
