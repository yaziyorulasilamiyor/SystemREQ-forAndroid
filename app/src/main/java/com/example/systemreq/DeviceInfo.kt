package com.example.systemreq

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs

object DeviceInfo {

    fun getAndroidSdk(): Int {
        return Build.VERSION.SDK_INT
    }

    fun getTotalRamMb(ctx: Context): Long {
        val am = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = ActivityManager.MemoryInfo()
        am.getMemoryInfo(info)

        return info.totalMem / (1024 * 1024)
    }

    fun getFreeStorageMb(): Long {
        val path = Environment.getDataDirectory().path
        val stat = StatFs(path)


        return stat.availableBytes / (1024 * 1024)
    }
}
