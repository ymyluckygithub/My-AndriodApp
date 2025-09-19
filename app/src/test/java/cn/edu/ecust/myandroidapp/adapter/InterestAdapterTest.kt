package cn.edu.ecust.myandroidapp.adapter

import android.content.Context
import cn.edu.ecust.myandroidapp.utils.Constants
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.mock

/**
 * InterestAdapter测试类
 * 验证兴趣选择功能
 */
class InterestAdapterTest {
    
    @Test
    fun testInterestSelection() {
        val mockContext = mock(Context::class.java)
        val adapter = InterestAdapter(mockContext, Constants.INTEREST_TAGS)
        
        // 测试初始状态
        assertTrue("初始状态应该没有选中的兴趣", adapter.getSelectedInterests().isEmpty())
        
        // 测试设置选中兴趣
        val selectedInterests = listOf("音乐", "电影", "运动")
        adapter.setSelectedInterests(selectedInterests)
        
        val result = adapter.getSelectedInterests()
        assertEquals("选中的兴趣数量应该正确", 3, result.size)
        assertTrue("应该包含音乐", result.contains("音乐"))
        assertTrue("应该包含电影", result.contains("电影"))
        assertTrue("应该包含运动", result.contains("运动"))
    }
    
    @Test
    fun testClearSelection() {
        val mockContext = mock(Context::class.java)
        val adapter = InterestAdapter(mockContext, Constants.INTEREST_TAGS)
        
        // 先设置一些选中项
        adapter.setSelectedInterests(listOf("音乐", "电影"))
        assertEquals("设置后应该有2个选中项", 2, adapter.getSelectedInterests().size)
        
        // 清空选择
        adapter.clearSelection()
        assertTrue("清空后应该没有选中项", adapter.getSelectedInterests().isEmpty())
    }
    
    @Test
    fun testAdapterBasicMethods() {
        val mockContext = mock(Context::class.java)
        val adapter = InterestAdapter(mockContext, Constants.INTEREST_TAGS)
        
        // 测试基本方法
        assertEquals("数量应该等于兴趣标签数量", Constants.INTEREST_TAGS.size, adapter.count)
        
        for (i in Constants.INTEREST_TAGS.indices) {
            assertEquals("获取的项目应该正确", Constants.INTEREST_TAGS[i], adapter.getItem(i))
            assertEquals("ID应该等于位置", i.toLong(), adapter.getItemId(i))
        }
    }
}
