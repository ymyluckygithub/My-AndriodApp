package cn.edu.ecust.myandroidapp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import cn.edu.ecust.myandroidapp.R

class CustomTitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val backButton: ImageButton
    private val titleText: TextView
    private val menuButton: ImageButton
    
    private var onBackClickListener: (() -> Unit)? = null
    private var onMenuClickListener: (() -> Unit)? = null

    init {
        try {
            LayoutInflater.from(context).inflate(R.layout.custom_title_bar, this, true)

            backButton = findViewById(R.id.btn_back)
            titleText = findViewById(R.id.tv_title)
            menuButton = findViewById(R.id.btn_menu)

            // 处理自定义属性
            attrs?.let {
                val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomTitleBar)

                try {
                    val title = typedArray.getString(R.styleable.CustomTitleBar_titleText)
                    title?.let { setTitle(it) }

                    val showBack = typedArray.getBoolean(R.styleable.CustomTitleBar_showBackButton, true)
                    setBackButtonVisible(showBack)

                    val showMenu = typedArray.getBoolean(R.styleable.CustomTitleBar_showMenuButton, false)
                    setMenuButtonVisible(showMenu)
                } finally {
                    typedArray.recycle()
                }
            }

            // 设置点击事件
            backButton.setOnClickListener { onBackClickListener?.invoke() }
            menuButton.setOnClickListener { onMenuClickListener?.invoke() }
        } catch (e: Exception) {
            android.util.Log.e("CustomTitleBar", "Error initializing CustomTitleBar", e)
            throw e
        }
    }

    fun setTitle(title: String) { // 设置标题文本
        titleText.text = title
    }

    fun setBackButtonVisible(visible: Boolean) { // 设置返回按钮可见性
        backButton.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setMenuButtonVisible(visible: Boolean) { // 设置菜单按钮可见性
        menuButton.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setOnBackClickListener(listener: () -> Unit) { // 设置返回按钮点击监听
        onBackClickListener = listener
    }

    fun setOnMenuClickListener(listener: () -> Unit) { // 设置菜单按钮点击监听
        onMenuClickListener = listener
    }
}
