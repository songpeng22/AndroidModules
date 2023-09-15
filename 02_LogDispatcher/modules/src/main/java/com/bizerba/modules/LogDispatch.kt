package com.bizerba.modules

import android.util.Log
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator

object Log {
    private var fileLogSel:String = ""
    fun initFileLog(select:String,dir:String){
        when(select){
            "xlog" -> {
                initXLog(dir)
                fileLogSel = "xlog"
            }
            "slf4j" -> {
                fileLogSel = "slf4j"
            }
            else -> {
                fileLogSel = "none"
            }
        }
    }

    private const val FLAG_VERBOSE:Int = 1.shl(1)
    private const val FLAG_DEBUG:Int = 1.shl(2)
    private const val FLAG_INFO:Int = 1.shl(3)
    private const val FLAG_WARNING:Int = 1.shl(4)
    private const val FLAG_ERROR:Int = 1.shl(5)
    private var level:Int  = FLAG_VERBOSE
    private var tagBuf:String = ""
    private var msgBuf:String = ""
    fun v(tag:String, msg:String):com.bizerba.modules.Log{
        android.util.Log.v(tag, msg)
        level = FLAG_VERBOSE
        tagBuf = tag
        msgBuf = msg
        return this@Log
    }

    fun d(tag:String, msg:String):com.bizerba.modules.Log{
        android.util.Log.d(tag, msg)
        level = FLAG_DEBUG
        tagBuf = tag
        msgBuf = msg
        return this@Log
    }

    fun i(tag:String, msg:String):com.bizerba.modules.Log{
        android.util.Log.i(tag, msg)
        level = FLAG_INFO
        tagBuf = tag
        msgBuf = msg
        return this@Log
    }

    fun w(tag:String, msg:String):com.bizerba.modules.Log{
        android.util.Log.w(tag, msg)
        level = FLAG_WARNING
        tagBuf = tag
        msgBuf = msg
        return this@Log
    }

    fun e(tag:String, msg:String):com.bizerba.modules.Log {
        android.util.Log.e(tag, msg)
        level = FLAG_ERROR
        tagBuf = tag
        msgBuf = msg
        return this@Log
    }

    fun f():com.bizerba.modules.Log{
        if(fileLogSel.equals("xlog")){
            if(level.hasFlag(FLAG_VERBOSE))
                XLog.tag(tagBuf).v(msgBuf)
            else if(level.hasFlag(FLAG_DEBUG))
                XLog.tag(tagBuf).d(msgBuf)
            else if(level.hasFlag(FLAG_INFO))
                XLog.tag(tagBuf).i(msgBuf)
            else if(level.hasFlag(FLAG_WARNING))
                XLog.tag(tagBuf).w(msgBuf)
            else if(level.hasFlag(FLAG_ERROR))
                XLog.tag(tagBuf).e(msgBuf)
            else{

            }
        }
        else if(fileLogSel.equals("slf4j")){

        }
        else{

        }
        return this@Log
    }

    private var xLogConfig: LogConfiguration? = null
    private fun initXLog(dir:String) {
        xLogConfig = LogConfiguration.Builder()
            .logLevel(LogLevel.ALL)         // Specify log level, logs below this level won't be printed, default: LogLevel.ALL
            .tag("LogDispatch")               // Specify TAG, default: "X-LOG"
            .t()                            // Enable thread info, disabled by default
            .st(1)                    // Enable stack trace info with depth 2, disabled by default
            .build()
        val androidPrinter: AndroidPrinter = AndroidPrinter()           // Printer that print the log using com.elvishew.xlog.XLog.Log
//        val consolePrinter = ConsolePrinter();               // Printer that print the log to console using System.out
        val filePrinter: FilePrinter = FilePrinter.Builder(dir)// Specify the path to save log file
            .fileNameGenerator(DateFileNameGenerator())             // Default: ChangelessFileNameGenerator("log")
            .backupStrategy(FileSizeBackupStrategy(1024 * 1024 * 50)) // Default: FileSizeBackupStrategy(1024 * 1024)
            .cleanStrategy(FileLastModifiedCleanStrategy(1000 * 60 * 60 * 24 * 7)) // Default: NeverCleanStrategy(), 7天
            .build()
        XLog.init( // Initialize XLog
            xLogConfig,  // Specify the log configuration, if not specified, will use new LogConfiguration.Builder().build()
//            androidPrinter,  // Specify printers, if no printer is specified, AndroidPrinter(for Android)/ConsolePrinter(for java) will be used.
//            consolePrinter,
            filePrinter
        )
    }
    //local extensions
    fun Int.hasFlag(flag: Int): Boolean {
        return (this and flag) == flag
    }
}