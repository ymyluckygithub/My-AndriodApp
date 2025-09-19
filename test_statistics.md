# 数据统计功能测试指南

## 如何访问数据统计功能

### 方法1：通过主界面菜单
1. 打开MyAndroidApp应用
2. 在主界面右上角找到菜单按钮（三个点的图标）
3. 点击菜单按钮
4. 在弹出的菜单中选择"数据统计"选项

### 方法2：通过代码验证
如果菜单不显示，可以检查以下几点：

#### 检查点1：MainActivity中的菜单按钮
- 确保MainActivity.kt中有`btnMenu.setOnClickListener`
- 确保调用了`showPopupMenu(it)`

#### 检查点2：菜单资源文件
- 检查`res/menu/main_menu.xml`中是否有`menu_statistics`项
- 确保图标`ic_chart.xml`存在

#### 检查点3：StatisticsActivity注册
- 检查`AndroidManifest.xml`中是否注册了StatisticsActivity
- 确保有正确的parent activity设置

## 预期功能

### 数据统计界面应该显示：
1. **好友性别分布饼图** - 显示男女比例
2. **好友年龄分布柱状图** - 按年龄段统计
3. **聊天消息趋势折线图** - 最近7天消息数量
4. **匹配方式统计饼图** - 随机匹配vs附近的人
5. **数据摘要卡片** - 总好友数、今日消息数等

### 如果数据库为空
- 应用会显示模拟数据进行演示
- 所有图表都应该正常显示

## 故障排除

### 如果菜单不显示：
1. 检查主界面布局中是否有`btn_menu`按钮
2. 确认按钮的点击事件是否正确绑定
3. 查看logcat日志是否有错误信息

### 如果统计界面崩溃：
1. 检查MPAndroidChart依赖是否正确添加
2. 确认所有图表ID在布局文件中存在
3. 查看是否有数据库访问权限问题

## 技术实现细节

- **图表库**: MPAndroidChart v3.1.0
- **数据源**: SQLite数据库 + 模拟数据
- **界面**: 使用CardView和ScrollView的响应式布局
- **导航**: 从MainActivity通过Intent跳转
