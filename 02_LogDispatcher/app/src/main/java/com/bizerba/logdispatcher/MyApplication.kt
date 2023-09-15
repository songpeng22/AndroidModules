package com.bizerba.logdispatcher

import android.app.ActivityManager
import android.app.Application
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Process.myUid
import java.io.File


class MyApplication : Application() {
    //singleton
    companion object {
        private var instance: MyApplication? = null

        fun getInstance() : MyApplication {
            return instance as MyApplication
        }
    }
    //
    override fun onCreate() {
        super.onCreate()
        //fill in instance
        instance = this
    }

    fun getCacheFolder():String{
        var cacheDir:String = applicationInfo.dataDir + File.separator + "cache" + File.separator
        return cacheDir
    }

    fun getFilesFolder():String{
        var cacheDir:String = applicationInfo.dataDir + File.separator + "files" + File.separator
        return cacheDir
    }
}