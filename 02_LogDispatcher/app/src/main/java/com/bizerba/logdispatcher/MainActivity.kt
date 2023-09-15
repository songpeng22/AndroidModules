package com.bizerba.logdispatcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.bizerba.modules.Log;

class MainActivity : AppCompatActivity() {
    //debug
    private var bDebug:Boolean = false
    private var tag:String = "MainActivity"
    private var strLog:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.initFileLog("xlog",MyApplication.getInstance().getCacheFolder())

        com.bizerba.modules.Log.v(tag,"MainActivity::onCreate()")
        Log.d(tag,"MainActivity::onCreate()")
        Log.i(tag,"MainActivity::onCreate()")
        Log.w(tag,"MainActivity::onCreate()")
        Log.e(tag,"MainActivity::onCreate()").f()

        com.bizerba.modules.Log.v(tag,"MainActivity::onCreate()")
        Log.d(tag,"MainActivity::onCreate()")
        Log.i(tag,"MainActivity::onCreate()")
        Log.w(tag,"MainActivity::onCreate()").f()
        Log.e(tag,"MainActivity::onCreate()")

    }
}