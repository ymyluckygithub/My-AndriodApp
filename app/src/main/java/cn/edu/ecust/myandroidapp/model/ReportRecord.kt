package cn.edu.ecust.myandroidapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReportRecord(
    val id: String, // 举报记录ID
    val reporterId: String, // 举报人ID
    val reportedId: String, // 被举报人ID
    val reason: String, // 举报原因
    val description: String = "", // 详细描述
    val status: String = "pending", // 处理状态：pending, reviewed, resolved
    val createdAt: Long = System.currentTimeMillis() // 创建时间
) : Parcelable {
    
    companion object {
        const val REASON_INAPPROPRIATE = "inappropriate"
        const val REASON_SPAM = "spam"
        const val REASON_FAKE = "fake"
        const val REASON_HARASSMENT = "harassment"
        const val REASON_OTHER = "other"
        
        const val STATUS_PENDING = "pending"
        const val STATUS_REVIEWED = "reviewed"
        const val STATUS_RESOLVED = "resolved"
    }
}
