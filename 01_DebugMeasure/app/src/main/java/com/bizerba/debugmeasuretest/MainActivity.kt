package com.bizerba.debugmeasuretest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.widget.Toast
import com.bizerba.modules.DebugElement
import com.bizerba.modules.DebugMeasure
import com.bizerba.modules.DebugMeasure.getMap

class MainActivity : AppCompatActivity() {
    //debug
    private var bDebug:Boolean = false
    private var tag:String = "MainActivity"
    private var strLog:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val thread = HandlerThread("HandlerThread->Unnamed")
        thread.start()
        val handler = Handler(thread.looper)
        (DebugMeasure.getMap(tag,DebugMeasure.FLAG_MANUAL) as DebugElement).updateParam(3000)
        val r: Runnable = object : Runnable {
            var index:Int  = 0
            var buf:String = ""
            override fun run() {
                if(DebugMeasure.getMap(tag,DebugMeasure.FLAG_ONCE).isDebug()){
                    Log.v(tag,"reach only once")
                }

                if(DebugMeasure.getMap(tag,DebugMeasure.FLAG_1S).isDebug()){
                    Log.v(tag,"1s reached")
                }

                if(DebugMeasure.getMap(tag,DebugMeasure.FLAG_MANUAL).isDebug()){
                    Log.v(tag,"3s reached")
                }

                if(DebugMeasure.getMap(tag,DebugMeasure.FLAG_5S).isDebug()){
                    Log.v(tag,"5s reached")
                }

                if(DebugMeasure.getMap(tag,DebugMeasure.FLAG_NEVER).isDebug()){
                    Log.v(tag,"never reach")
                }

                DebugMeasure.getMap(tag,DebugMeasure.FLAG_1S).restartTimerIfNeed()
                DebugMeasure.getMap(tag,DebugMeasure.FLAG_5S).restartTimerIfNeed()
                DebugMeasure.getMap(tag,DebugMeasure.FLAG_MANUAL).restartTimerIfNeed()

                handler.postDelayed(this, 100)
            }
        }
        handler.postDelayed(r, 100)
    }
}