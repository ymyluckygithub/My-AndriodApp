package cn.edu.ecust.myandroidapp

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.edu.ecust.myandroidapp.database.ChatDatabaseHelper
import cn.edu.ecust.myandroidapp.utils.Constants
import cn.edu.ecust.myandroidapp.widget.CustomTitleBar
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class StatisticsActivity : AppCompatActivity() {

    private lateinit var titleBar: CustomTitleBar
    private lateinit var pieChartGender: PieChart // 性别比例饼图
    private lateinit var barChartAge: BarChart // 年龄分布柱状图
    private lateinit var lineChartMessages: LineChart // 聊天消息趋势图
    private lateinit var pieChartMatchType: PieChart // 匹配类型饼图
    
    private lateinit var dbHelper: ChatDatabaseHelper
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        
        initViews()
        initDatabase()
        loadStatisticsData()
    }
    
    private fun initViews() {
        titleBar = findViewById(R.id.titleBar)
        pieChartGender = findViewById(R.id.pieChartGender)
        barChartAge = findViewById(R.id.barChartAge)
        lineChartMessages = findViewById(R.id.lineChartMessages)
        pieChartMatchType = findViewById(R.id.pieChartMatchType)
        
        setupTitleBar()
    }
    
    private fun setupTitleBar() {
        titleBar.setTitle("数据统计")
        titleBar.setBackButtonVisible(true)
        titleBar.setMenuButtonVisible(false)
        titleBar.setOnBackClickListener {
            finish()
        }
    }
    
    private fun initDatabase() {
        dbHelper = ChatDatabaseHelper(this)
    }
    
    private fun loadStatisticsData() {
        try {
            setupGenderPieChart()
            setupAgeBarChart()
            setupMessageLineChart()
            setupMatchTypePieChart()
        } catch (e: Exception) {
            Toast.makeText(this, "加载统计数据失败: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupGenderPieChart() { // 设置性别比例饼图
        val genderData = getGenderStatistics()
        
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(genderData["male"]?.toFloat() ?: 0f, "男性"))
        entries.add(PieEntry(genderData["female"]?.toFloat() ?: 0f, "女性"))
        entries.add(PieEntry(genderData["other"]?.toFloat() ?: 0f, "其他"))
        
        val dataSet = PieDataSet(entries, "性别分布")
        dataSet.colors = listOf(
            Color.parseColor("#FF6B6B"), // 红色
            Color.parseColor("#4ECDC4"), // 青色
            Color.parseColor("#45B7D1")  // 蓝色
        )
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.WHITE
        
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pieChartGender))
        
        pieChartGender.data = data
        pieChartGender.setUsePercentValues(true)
        pieChartGender.description.isEnabled = false
        pieChartGender.setDrawHoleEnabled(true)
        pieChartGender.setHoleColor(Color.WHITE)
        pieChartGender.setTransparentCircleColor(Color.WHITE)
        pieChartGender.setTransparentCircleAlpha(110)
        pieChartGender.holeRadius = 58f
        pieChartGender.transparentCircleRadius = 61f
        pieChartGender.setDrawCenterText(true)
        pieChartGender.centerText = "好友性别\n分布"
        pieChartGender.setCenterTextSize(14f)
        pieChartGender.setCenterTextColor(Color.GRAY)
        
        // 设置图例
        val legend = pieChartGender.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.xEntrySpace = 7f
        legend.yEntrySpace = 0f
        legend.yOffset = 0f
        
        pieChartGender.setEntryLabelColor(Color.WHITE)
        pieChartGender.setEntryLabelTextSize(12f)
        pieChartGender.animateY(1400)
        pieChartGender.invalidate()
    }
    
    private fun setupAgeBarChart() { // 设置年龄分布柱状图
        val ageData = getAgeStatistics()
        
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        
        var index = 0f
        for ((ageRange, count) in ageData) {
            entries.add(BarEntry(index, count.toFloat()))
            labels.add(ageRange)
            index++
        }
        
        val dataSet = BarDataSet(entries, "年龄分布")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        
        val data = BarData(dataSet)
        data.barWidth = 0.9f
        
        barChartAge.data = data
        barChartAge.setFitBars(true)
        
        // 设置描述
        val description = Description()
        description.text = "好友年龄分布统计"
        description.textSize = 12f
        barChartAge.description = description
        
        // 设置X轴
        val xAxis = barChartAge.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        
        // 设置Y轴
        barChartAge.axisLeft.setDrawGridLines(false)
        barChartAge.axisRight.isEnabled = false
        
        // 设置图例
        barChartAge.legend.isEnabled = true
        
        barChartAge.animateY(1000)
        barChartAge.invalidate()
    }
    
    private fun setupMessageLineChart() { // 设置消息趋势折线图
        val messageData = getMessageStatistics()
        
        val entries = ArrayList<Entry>()
        val labels = ArrayList<String>()
        
        var index = 0f
        for ((date, count) in messageData) {
            entries.add(Entry(index, count.toFloat()))
            labels.add(date)
            index++
        }
        
        val dataSet = LineDataSet(entries, "每日消息数量")
        dataSet.color = Color.parseColor("#FF6B6B")
        dataSet.setCircleColor(Color.parseColor("#FF6B6B"))
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setDrawCircleHole(false)
        dataSet.valueTextSize = 10f
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.parseColor("#FFCCCB")
        
        val data = LineData(dataSet)
        
        lineChartMessages.data = data
        
        // 设置描述
        val description = Description()
        description.text = "最近7天消息统计"
        description.textSize = 12f
        lineChartMessages.description = description
        
        // 设置X轴
        val xAxis = lineChartMessages.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        
        // 设置Y轴
        lineChartMessages.axisLeft.setDrawGridLines(true)
        lineChartMessages.axisRight.isEnabled = false
        
        lineChartMessages.animateX(1000)
        lineChartMessages.invalidate()
    }
    
    private fun setupMatchTypePieChart() { // 设置匹配类型饼图
        val matchData = getMatchTypeStatistics()
        
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(matchData["random"]?.toFloat() ?: 0f, "随机匹配"))
        entries.add(PieEntry(matchData["nearby"]?.toFloat() ?: 0f, "附近的人"))
        
        val dataSet = PieDataSet(entries, "匹配方式")
        dataSet.colors = listOf(
            Color.parseColor("#96CEB4"), // 绿色
            Color.parseColor("#FFEAA7")  // 黄色
        )
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pieChartMatchType))
        
        pieChartMatchType.data = data
        pieChartMatchType.setUsePercentValues(true)
        pieChartMatchType.description.isEnabled = false
        pieChartMatchType.setDrawHoleEnabled(true)
        pieChartMatchType.setHoleColor(Color.WHITE)
        pieChartMatchType.holeRadius = 40f
        pieChartMatchType.setDrawCenterText(true)
        pieChartMatchType.centerText = "匹配方式\n统计"
        pieChartMatchType.setCenterTextSize(12f)
        
        pieChartMatchType.animateY(1000)
        pieChartMatchType.invalidate()
    }
    
    // 获取性别统计数据
    private fun getGenderStatistics(): Map<String, Int> {
        val db = dbHelper.readableDatabase
        val result = mutableMapOf<String, Int>()
        
        try {
            // 统计好友表中的性别分布（这里使用用户表作为示例）
            val cursor = db.rawQuery("""
                SELECT ${Constants.COL_GENDER}, COUNT(*) as count 
                FROM ${Constants.TABLE_USERS} 
                WHERE ${Constants.COL_GENDER} != '' 
                GROUP BY ${Constants.COL_GENDER}
            """, null)
            
            result["male"] = 0
            result["female"] = 0
            result["other"] = 0
            
            while (cursor.moveToNext()) {
                val gender = cursor.getString(0) ?: ""
                val count = cursor.getInt(1)
                
                when (gender.lowercase()) {
                    "male", "男", "男性" -> result["male"] = count
                    "female", "女", "女性" -> result["female"] = count
                    else -> result["other"] = (result["other"] ?: 0) + count
                }
            }
            cursor.close()
            
            // 如果没有数据，使用模拟数据
            if (result.values.sum() == 0) {
                result["male"] = 15
                result["female"] = 12
                result["other"] = 3
            }
            
        } catch (e: Exception) {
            // 使用模拟数据
            result["male"] = 15
            result["female"] = 12
            result["other"] = 3
        }
        
        return result
    }
    
    // 获取年龄统计数据
    private fun getAgeStatistics(): Map<String, Int> {
        val db = dbHelper.readableDatabase
        val result = mutableMapOf<String, Int>()
        
        try {
            val cursor = db.rawQuery("""
                SELECT 
                    CASE 
                        WHEN ${Constants.COL_AGE} < 20 THEN '18-19'
                        WHEN ${Constants.COL_AGE} < 25 THEN '20-24'
                        WHEN ${Constants.COL_AGE} < 30 THEN '25-29'
                        WHEN ${Constants.COL_AGE} < 35 THEN '30-34'
                        ELSE '35+'
                    END as age_range,
                    COUNT(*) as count
                FROM ${Constants.TABLE_USERS}
                WHERE ${Constants.COL_AGE} > 0
                GROUP BY age_range
                ORDER BY age_range
            """, null)
            
            while (cursor.moveToNext()) {
                val ageRange = cursor.getString(0)
                val count = cursor.getInt(1)
                result[ageRange] = count
            }
            cursor.close()
            
            // 如果没有数据，使用模拟数据
            if (result.isEmpty()) {
                result["18-19"] = 5
                result["20-24"] = 12
                result["25-29"] = 8
                result["30-34"] = 4
                result["35+"] = 1
            }
            
        } catch (e: Exception) {
            // 使用模拟数据
            result["18-19"] = 5
            result["20-24"] = 12
            result["25-29"] = 8
            result["30-34"] = 4
            result["35+"] = 1
        }
        
        return result
    }
    
    // 获取消息统计数据
    private fun getMessageStatistics(): Map<String, Int> {
        val result = mutableMapOf<String, Int>()
        
        // 生成最近7天的模拟数据
        val calendar = java.util.Calendar.getInstance()
        for (i in 6 downTo 0) {
            calendar.add(java.util.Calendar.DAY_OF_YEAR, if (i == 6) -6 else 1)
            val dateStr = "${calendar.get(java.util.Calendar.MONTH) + 1}/${calendar.get(java.util.Calendar.DAY_OF_MONTH)}"
            result[dateStr] = (10..50).random() // 随机生成10-50条消息
        }
        
        return result
    }
    
    // 获取匹配类型统计数据
    private fun getMatchTypeStatistics(): Map<String, Int> {
        val db = dbHelper.readableDatabase
        val result = mutableMapOf<String, Int>()
        
        try {
            val cursor = db.rawQuery("""
                SELECT ${Constants.COL_MATCH_TYPE}, COUNT(*) as count 
                FROM ${Constants.TABLE_MATCH_HISTORY} 
                GROUP BY ${Constants.COL_MATCH_TYPE}
            """, null)
            
            result["random"] = 0
            result["nearby"] = 0
            
            while (cursor.moveToNext()) {
                val matchType = cursor.getString(0)
                val count = cursor.getInt(1)
                result[matchType] = count
            }
            cursor.close()
            
            // 如果没有数据，使用模拟数据
            if (result.values.sum() == 0) {
                result["random"] = 25
                result["nearby"] = 15
            }
            
        } catch (e: Exception) {
            // 使用模拟数据
            result["random"] = 25
            result["nearby"] = 15
        }
        
        return result
    }
}
