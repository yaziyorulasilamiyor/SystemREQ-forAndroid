package com.example.systemreq

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs

object SistemBilgisi {

    //android sürümü al
    fun getAndroidSdk(): Int {
        return Build.VERSION.SDK_INT
    }

    //ram al
    fun getTotalRamMb(ctx: Context): Long {
        val am = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val info = ActivityManager.MemoryInfo()
        am.getMemoryInfo(info)

        return info.totalMem / (1024 * 1024)
    }
    // boş depolama alanını al
    fun getFreeStorageMb(): Long {
        val path = Environment.getDataDirectory().path
        val stat = StatFs(path)


        return stat.availableBytes / (1024 * 1024)
    }
}
