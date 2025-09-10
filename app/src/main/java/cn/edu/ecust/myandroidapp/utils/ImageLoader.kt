package cn.edu.ecust.myandroidapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.core.content.ContextCompat
import cn.edu.ecust.myandroidapp.R
import java.io.File
import java.io.FileInputStream
import java.io.IOException

object ImageLoader {
    
    private const val DEFAULT_AVATAR_SIZE = 200 // 默认头像尺寸
    
    fun loadAvatar(context: Context, imageView: ImageView, avatar: Any?) { // 加载头像
        when (avatar) {
            is Int -> { // 资源ID
                if (avatar != 0) {
                    imageView.setImageResource(avatar)
                } else {
                    setDefaultAvatar(context, imageView)
                }
            }
            is String -> { // 文件路径
                if (avatar.isNotEmpty()) {
                    loadImageFromPath(context, imageView, avatar)
                } else {
                    setDefaultAvatar(context, imageView)
                }
            }
            else -> {
                setDefaultAvatar(context, imageView)
            }
        }
    }
    
    private fun loadImageFromPath(context: Context, imageView: ImageView, path: String) { // 从路径加载图片
        try {
            val file = File(path)
            if (file.exists()) {
                val bitmap = decodeSampledBitmapFromFile(path, DEFAULT_AVATAR_SIZE, DEFAULT_AVATAR_SIZE)
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                } else {
                    setDefaultAvatar(context, imageView)
                }
            } else {
                setDefaultAvatar(context, imageView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            setDefaultAvatar(context, imageView)
        }
    }
    
    private fun setDefaultAvatar(context: Context, imageView: ImageView) { // 设置默认头像
        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_default_avatar))
    }
    
    private fun decodeSampledBitmapFromFile(path: String, reqWidth: Int, reqHeight: Int): Bitmap? { // 压缩图片
        return try {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeFile(path, options)
            
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
            options.inJustDecodeBounds = false
            
            BitmapFactory.decodeFile(path, options)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    
    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int { // 计算压缩比例
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
