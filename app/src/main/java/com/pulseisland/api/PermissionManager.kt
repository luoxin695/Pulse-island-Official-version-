package com.pulseisland.api

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
import androidx.core.app.NotificationManagerCompat
import com.pulseisland.R

/**
 * 权限管理器 - Pulse Island API 的一部分
 * 负责权限检查与引导
 */
object PermissionManager {

    // 核心权限列表（必须全部获取才能进入主界面）
    val REQUIRED_PERMISSIONS = listOf(
        Manifest.permission.SYSTEM_ALERT_WINDOW, // 悬浮窗
        Manifest.permission.READ_PHONE_STATE,    // 来电监听
        Manifest.permission.RECORD_AUDIO,        // 录音波形
        Manifest.permission.ACCESS_FINE_LOCATION, // 导航
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS,  // Android 13+
    )

    // 建议权限（用户可选择性开启）
    val SUGGESTED_PERMISSIONS = listOf(
        Manifest.permission.FOREGROUND_SERVICE,
    )

    // 系统级设置（需要用户手动去设置中开启）
    val SYSTEM_SETTINGS = listOf(
        "无障碍服务",
        "通知使用权",
        "自启动",
        "忽略电池优化",
        "后台弹出界面"
    )

    fun checkAllRequiredPermissions(context: Context): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun checkAllSuggestedPermissions(context: Context): Boolean {
        return SUGGESTED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun isNotificationEnabled(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    fun openSystemOverlaySettings(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    fun openAccessibilitySettings(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }

    fun openBatteryOptimizationSettings(context: Context) {
        val intent = Intent(ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
        context.startActivity(intent)
    }

    fun requestPermissions(activity: Activity, requestCode: Int) {
        activity.requestPermissions(
            REQUIRED_PERMISSIONS.toTypedArray(),
            requestCode
        )
    }

    fun getPermissionDisplayName(permission: String): String {
        return when (permission) {
            Manifest.permission.SYSTEM_ALERT_WINDOW -> "悬浮窗"
            Manifest.permission.READ_PHONE_STATE -> "电话状态"
            Manifest.permission.RECORD_AUDIO -> "录音"
            Manifest.permission.ACCESS_FINE_LOCATION -> "精确定位"
            Manifest.permission.ACCESS_COARSE_LOCATION -> "粗略定位"
            Manifest.permission.POST_NOTIFICATIONS -> "通知"
            Manifest.permission.FOREGROUND_SERVICE -> "前台服务"
            else -> permission
        }
    }
}
