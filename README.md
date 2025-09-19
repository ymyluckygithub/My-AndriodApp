# Android聊天应用

一个基于传统Android View系统开发的简易聊天软件，支持用户登录、好友管理、消息通知和聊天功能。

## 🎯 项目特色

- **传统View架构**：完全使用传统Android View系统，兼容性强
- **ListView核心**：严格使用ListView控件显示所有列表数据
- **自定义控件复用**：CustomTitleBar在所有界面统一应用
- **中文友好**：完整的中文本地化支持
- **模块化设计**：清晰的代码结构和组件分离

## 📱 功能特性

### 🔐 用户认证
- 用户名密码登录
- 头像选择（8个预设头像）
- 登录状态保存
- 加载动画效果

### 👥 好友管理
- 好友列表显示（ListView）
- 搜索好友功能
- 在线状态显示
- 个性签名展示

### 📞 通讯录管理
- 添加联系人
- 删除联系人
- 搜索联系人
- 联系人信息管理

### 💬 聊天功能
- 消息发送接收
- 消息气泡显示
- 聊天记录（ListView）
- 自动回复模拟

### 🔔 消息通知
- 通知列表显示
- 未读数量统计
- 通知点击跳转
- 通知清除功能

### 📊 数据统计
- 好友性别分布饼图
- 好友年龄分布柱状图
- 聊天消息趋势折线图
- 匹配方式统计饼图
- 数据摘要展示

## 🏗️ 技术架构

### 核心组件
- **LoginActivity**：登录界面
- **MainActivity**：主界面
- **ContactActivity**：通讯录管理
- **ChatActivity**：聊天界面
- **StatisticsActivity**：数据统计界面
- **CustomTitleBar**：自定义标题栏控件

### 数据模型
- **User**：用户信息模型
- **Friend**：好友信息模型
- **Message**：消息模型
- **Notification**：通知模型

### 适配器组件
- **FriendAdapter**：好友列表适配器
- **ContactAdapter**：联系人列表适配器
- **MessageAdapter**：消息列表适配器
- **NotificationAdapter**：通知列表适配器
- **AvatarAdapter**：头像选择适配器

## 🎨 自定义用户头像

### 添加新头像步骤：
1. 在`app/src/main/res/drawable/`目录下添加新头像资源
2. 在`LoginActivity.kt`的`initAvatarGrid()`方法中添加新头像ID
3. 建议使用`avatar_9.xml`、`avatar_10.xml`等命名

### 代码示例：
```kotlin
private fun initAvatarGrid() {
    val avatarList = listOf(
        R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3,
        R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6,
        R.drawable.avatar7, R.drawable.avatar8,
        R.drawable.avatar9, // 新添加的头像
        R.drawable.avatar10 // 新添加的头像
    )
    // ...
}
```

## 👥 修改好友列表信息

### 修改现有好友信息：
在`MainActivity.kt`的`loadFriendData()`方法中编辑Friend对象：

```kotlin
private fun loadFriendData() {
    friendList.clear()
    friendList.addAll(listOf(
        Friend("1", "张三", "小张", R.drawable.avatar_1, "", true, System.currentTimeMillis(), "今天天气真好！"),
        Friend("2", "李四", "小李", R.drawable.avatar_2, "", false, System.currentTimeMillis() - 3600000, "忙碌的一天"),
        // 添加新好友
        Friend("6", "新朋友", "昵称", R.drawable.avatar_6, "", true, System.currentTimeMillis(), "个性签名"),
    ))
    friendAdapter.updateFriendList(friendList)
}
```

### Friend数据结构：
```kotlin
Friend(
    id: String,           // 好友ID
    username: String,     // 用户名
    nickname: String,     // 昵称
    avatarResId: Int,     // 头像资源ID
    avatarUrl: String,    // 头像URL（可选）
    isOnline: Boolean,    // 在线状态
    lastSeenTime: Long,   // 最后在线时间
    signature: String     // 个性签名
)
```

## 🚀 编译和运行

### 环境要求
- Android Studio Arctic Fox或更高版本
- Android SDK API 24+
- Kotlin 1.8+

### 编译步骤
```bash
# 清理项目
./gradlew clean

# 编译Debug版本
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug
```

### 运行测试
```bash
# 运行单元测试
./gradlew test

# 运行UI测试
./gradlew connectedAndroidTest
```

## 📂 项目结构

```
app/src/main/
├── java/cn/edu/ecust/myandroidapp/
│   ├── adapter/          # 适配器类
│   ├── model/           # 数据模型
│   ├── utils/           # 工具类
│   ├── widget/          # 自定义控件
│   ├── LoginActivity.kt # 登录界面
│   ├── MainActivity.kt  # 主界面
│   ├── ContactActivity.kt # 通讯录管理
│   └── ChatActivity.kt  # 聊天界面
├── res/
│   ├── drawable/        # 图标和背景资源
│   ├── layout/          # 布局文件
│   ├── menu/           # 菜单资源
│   ├── values/         # 字符串、颜色、主题
│   └── ...
└── AndroidManifest.xml # 应用配置
```

## 🎯 使用说明

1. **启动应用**：首次启动进入登录界面
2. **用户登录**：输入用户名密码，选择头像，点击登录
3. **主界面**：查看好友列表和消息通知
4. **通讯录管理**：点击通讯录按钮管理联系人
5. **开始聊天**：点击好友头像进入聊天界面
6. **消息通知**：查看和管理消息通知
7. **数据统计**：点击主界面右上角菜单按钮，选择"数据统计"查看各种图表

### 📊 数据统计功能说明

**访问方式**：主界面 → 右上角菜单按钮 → 数据统计

**图表类型**：
- **好友性别分布**：饼图显示男女比例
- **好友年龄分布**：柱状图显示不同年龄段分布
- **聊天消息趋势**：折线图显示最近7天消息数量变化
- **匹配方式统计**：饼图显示随机匹配和附近的人的比例
- **数据摘要**：显示总好友数、今日消息数、匹配成功数等关键指标

**技术实现**：
- 使用MPAndroidChart图表库
- 支持图表动画效果
- 数据来源于SQLite数据库
- 支持实时数据更新

## 📝 开发说明

- 所有代码遵循Android开发最佳实践
- 使用传统View系统，避免Compose依赖
- 严格使用ListView控件显示列表数据
- 自定义控件在所有Activity中统一应用
- 完整的中文本地化支持
- 模块化设计，便于维护和扩展

## 🔧 技术栈

- **语言**：Kotlin
- **UI框架**：Android View System
- **架构**：传统Activity架构
- **数据存储**：SharedPreferences + SQLite
- **列表控件**：ListView + BaseAdapter
- **图片加载**：自定义ImageLoader
- **图表库**：MPAndroidChart v3.1.0
- **主题**：Material Design Components

---

**开发完成时间**：2025年9月10日  
**版本**：v1.0  
**开发者**：AI Assistant
