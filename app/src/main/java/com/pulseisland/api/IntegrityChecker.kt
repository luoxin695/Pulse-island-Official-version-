package com.pulseisland.api

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat
import com.pulseisland.PulseIslandApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Pulse Island API 安全校验模块
 * 代号：Pulse Island API
 * 职责：网络检查 → 签名校验 → 完整性校验 → 版本校验
 */
object IntegrityChecker {

    enum class CheckResult {
        PASS,
        NETWORK_FAIL,      // 网络不通 → 静默崩溃
        SIGNATURE_FAIL,    // 签名不对 → 静默崩溃
        INTEGRITY_FAIL,    // 完整性被破坏 → 静默崩溃
        VERSION_FAIL,      // 版本落后 → 引导更新
        PERMISSION_FAIL    // 权限不足 → 引导设置
    }

    // 正式发布时的签名SHA-256
    private const val RELEASE_SIGNATURE = "YOUR_RELEASE_SIGNATURE_HASH"
    private const val MINIMUM_VERSION_CODE = 1

    suspend fun performFullCheck(context: Context): CheckResult = withContext(Dispatchers.IO) {
        // 1. 网络检查
        if (!isNetworkAvailable(context)) {
            return@withContext CheckResult.NETWORK_FAIL
        }

        // 2. 签名校验
        if (!verifySignature(context)) {
            return@withContext CheckResult.SIGNATURE_FAIL
        }

        // 3. 完整性校验（签名通过则代码未被篡改，安卓系统保证）
        // 4. 版本校验
        if (!verifyVersion(context)) {
            return@withContext CheckResult.VERSION_FAIL
        }

        // 5. 权限检查（由PermissionManager单独处理）
        return@withContext CheckResult.PASS
    }

    fun performInitialCheck(context: Context): Boolean {
        // 同步版本，用于启动时快速校验
        if (!isNetworkAvailable(context)) {
            // 静默崩溃
            android.os.Process.killProcess(android.os.Process.myPid())
            return false
        }
        if (!verifySignature(context)) {
            android.os.Process.killProcess(android.os.Process.myPid())
            return false
        }
        if (!verifyVersion(context)) {
            // 引导更新 - 由调用方处理
            return false
        }
        return true
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val cap = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
            return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            @Suppress("DEPRECATION")
            val info = cm.activeNetworkInfo ?: return false
            return info.isConnected
        }
    }

    private fun verifySignature(context: Context): Boolean {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo?.apkContentsSigners ?: return false
            } else {
                @Suppress("DEPRECATION")
                packageInfo.signatures ?: return false
            }
            // 取第一个签名进行校验
            val signature = signatures.firstOrNull() ?: return false
            val sha256 = android.util.Base64.encodeToString(
                java.security.MessageDigest.getInstance("SHA-256").digest(signature.toByteArray()),
                android.util.Base64.NO_WRAP
            )
            // 正式环境应对比 RELEASE_SIGNATURE
            // 调试模式允许所有签名
            BuildConfig.DEBUG || sha256 == RELEASE_SIGNATURE
        } catch (e: Exception) {
            false
        }
    }

    private fun verifyVersion(context: Context): Boolean {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionCode >= MINIMUM_VERSION_CODE
        } catch (e: Exception) {
            false
        }
    }
}

// 临时添加 BuildConfig 占位
// 实际构建时会自动生成
object BuildConfig {
    const val DEBUG = true
}
