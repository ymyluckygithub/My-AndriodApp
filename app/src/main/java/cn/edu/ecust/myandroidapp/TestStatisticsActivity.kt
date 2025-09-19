package cn.edu.ecust.myandroidapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * 测试Activity - 用于直接测试数据统计功能
 */
class TestStatisticsActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 创建简单的测试界面
        val button = Button(this).apply {
            text = "打开数据统计"
            textSize = 18f
            setPadding(50, 50, 50, 50)
            setOnClickListener {
                openStatistics()
            }
        }
        
        setContentView(button)
        
        // 显示提示信息
        Toast.makeText(this, "点击按钮测试数据统计功能", Toast.LENGTH_LONG).show()
    }
    
    private fun openStatistics() {
        try {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "正在打开数据统计...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "错误: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}
