package cn.edu.ecust.myandroidapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BlockedUser(
    val id: Long = 0, // 屏蔽记录ID
    val userId: String, // 屏蔽者ID
    val blockedUserId: String, // 被屏蔽者ID
    val createdAt: Long = System.currentTimeMillis() // 创建时间
) : Parcelable
