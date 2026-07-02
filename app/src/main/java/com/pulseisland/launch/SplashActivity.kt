<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.BIND_ACCESSIBILITY_SERVICE" />
    <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

    <application
        android:name=".PulseIslandApp"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.AppCompat.NoActionBar"
        android:usesCleartextTraffic="true"
        tools:targetApi="31">

        <activity
            android:name=".launch.SplashActivity"
            android:exported="true"
            android:theme="@style/Theme.AppCompat.NoActionBar">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity
            android:name=".launch.PermissionGuideActivity"
            android:exported="false"
            android:theme="@style/Theme.AppCompat.NoActionBar" />

        <activity
            android:name=".launch.ShizukuGuideActivity"
            android:exported="false"
            android:theme="@style/Theme.AppCompat.NoActionBar" />

        <activity
            android:name=".launch.LanguageSelectActivity"
            android:exported="false"
            android:theme="@style/Theme.AppCompat.NoActionBar" />

        <activity
            android:name=".mainui.MainActivity"
            android:exported="false"
            android:theme="@style/Theme.AppCompat.NoActionBar" />

        <activity
            android:name=".mainui.CustomizeIslandActivity"
            android:exported="false"
            android:theme="@style/Theme.AppCompat.NoActionBar" />

        <activity
            android:name=".mainui.EffectPreviewActivity"
            android:exported="false"
            android:theme="@style/Theme.AppCompat.NoActionBar" />

        <service
            android:name=".island.IslandService"
            android:exported="false"
            android:foregroundServiceType="mediaProjection"
            android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE" />

        <service
            android:name=".island.ScreenContentGrabber"
            android:exported="true"
            android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE">
            <intent-filter>
                <action android:name="android.accessibilityservice.AccessibilityService" />
            </intent-filter>
            <meta-data
                android:name="android.accessibilityservice"
                android:resource="@xml/accessibility_service_config" />
        </service>

        <receiver
            android:name=".island.BootReceiver"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <action android:name="android.intent.action.QUICKBOOT_POWERON" />
            </intent-filter>
        </receiver>

    </application>

</manifest>
