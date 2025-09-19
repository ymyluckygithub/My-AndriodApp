package cn.edu.ecust.myandroidapp.database.dao

import cn.edu.ecust.myandroidapp.model.ReportRecord

interface ReportDao {
    
    // 插入举报记录
    fun insertReport(report: ReportRecord): Long
    
    // 根据ID获取举报记录
    fun getReportById(reportId: String): ReportRecord?
    
    // 获取用户的举报记录
    fun getReportsByReporter(reporterId: String): List<ReportRecord>
    
    // 获取被举报的记录
    fun getReportsByReported(reportedId: String): List<ReportRecord>
    
    // 更新举报状态
    fun updateReportStatus(reportId: String, status: String): Int
    
    // 删除举报记录
    fun deleteReport(reportId: String): Int
    
    // 获取待处理的举报
    fun getPendingReports(): List<ReportRecord>
    
    // 检查用户是否已举报过某人
    fun hasReported(reporterId: String, reportedId: String): Boolean
}
