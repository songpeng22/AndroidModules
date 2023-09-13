package com.bizerba.modules

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import java.util.BitSet

/*
 * DebugMeasure - judge if to output debug message in a loop
 * target - output some message, indicating loop is alive
 *
 * @param debugJudgeMillis - each debugJudgeMillis is met, isDebug() returns true
 * @remark example:
 * ((DebugElement)DebugMeasure.INSTANCE.getMap(TAG,DebugMeasure.FLAG_MANUAL)).updateParam(3000)
 * while(isActive){
 *   if(DebugMeasure.getMap(tag,DebugMeasure.FLAG_ONCE).isDebug()){
 *     Log.v(tag,"reached only once")
 *   }
 *   if(DebugMeasure.getMap(tag,DebugMeasure.FLAG_1S).isDebug()){
 *     Log.v(tag,"1s reached")
 *   }
 *   if(DebugMeasure.getMap(tag,DebugMeasure.FLAG_5S).isDebug()){
 *     Log.v(tag,"5s reached")
 *   }
 *   if(DebugMeasure.INSTANCE.getMap(tag,DebugMeasure.FLAG_MANUAL).isDebug()){
 *     Log.v(tag,"3s reached")
 *   }
 *   if(DebugMeasure.getMap(tag,DebugMeasure.FLAG_NEVER).isDebug()){
 *     Log.v(tag,"never reached")
 *   }
 *   ...
 *   DebugMeasure.getMap(tag,DebugMeasure.FLAG_1S).restartTimerIfNeed()
 *   DebugMeasure.getMap(tag,DebugMeasure.FLAG_5S).restartTimerIfNeed()
 *   DebugMeasure.INSTANCE.getMap(tag,DebugMeasure.FLAG_MANUAL).restartTimerIfNeed();
 * }
 * */
open class DebugElementOfBase{
    //debug
    var bDebug:Boolean = false
    protected open var who:String = "DebugElementOfBase"
    open var tag:String = "DebugElementOfBase"
    protected var logBuf:String = ""
    //
    protected var bNano:Boolean = true
    protected var bDebugJudge:Boolean = false
    //
    protected open var timeStart:Long = 0
    protected open var timeEnd:Long = 0
    protected open var timeElapsed:Long = 0

    open fun isDebug():Boolean{
        return false
    }

    protected fun currentTime():Long{
        if(bNano)
            return System.nanoTime()
        else
            return System.currentTimeMillis()
    }

    protected fun currentTimeInMillis():Long{
        if(bNano)
            return System.nanoTime() / 1000000
        else
            return System.currentTimeMillis()
    }

    public fun restartTimerIfNeed() {
        if(bDebugJudge){
            timeStart = currentTimeInMillis()
            bDebugJudge = false
        }
    }

    //for purpose of debug
    public fun whoIAm():String{
        return who
    }
}

data class DebugElement(var debugJudgeMillis:Long = 1000) : DebugElementOfBase() {
    //debug
    protected override var who:String = "DebugElement"
    //first
    private var bFirst:Boolean = true

    //return true when timer millis is reached
    override fun isDebug():Boolean{
        if(bFirst){
            bFirst = false
            timeStart = currentTimeInMillis()
        }

        timeEnd = currentTimeInMillis()
        timeElapsed = timeEnd - timeStart

        if(timeElapsed > debugJudgeMillis){
            bDebugJudge = true
        }
        else{
            bDebugJudge = false
        }

        if(bDebug){
            logBuf = String.format("%s:%s:time elapsed:%dms,judgeMills:%dms.",who,tag,timeElapsed,debugJudgeMillis)
            Log.v(tag,logBuf)
        }

        return bDebugJudge
    }

    fun updateParam(debugJudgeMillisIn:Long):DebugElement {
        debugJudgeMillis = debugJudgeMillisIn
        return this
    }
}

class DebugElementOfOnce: DebugElementOfBase(){
    //debug
    protected override var who:String = "DebugElementOfOnce"
    //first
    private var bFirst:Boolean = true
    override fun isDebug():Boolean {
        if(bFirst){
            bFirst = false
            return true
        }
        return false
    }
}

class DebugElementOfNever: DebugElementOfBase() {
    //debug
    protected override var who:String = "DebugElementOfNever"

    override fun isDebug():Boolean {
        return false
    }
}

object DebugMeasure {
    private var map = mutableMapOf<String,DebugElementOfBase>()

    const val FLAG_ONCE:Int = 1.shl(1)
    const val FLAG_NEVER:Int = 1.shl(2)
    const val FLAG_MANUAL:Int = 1.shl(3)
    const val FLAG_1S:Int = 1.shl(4)
    const val FLAG_5S:Int = 1.shl(5)

    var debugElementOfBase:DebugElementOfBase = DebugElementOfNever()

    fun getMap(keyIn:String,flag:Int = FLAG_NEVER):DebugElementOfBase{
        var key:String = String.format("%s_%d",keyIn,flag)
        if(map.containsKey(key))
            return map[key]!!

        //DebugElement factory
        if(flag.hasFlag(FLAG_ONCE)){
            debugElementOfBase = DebugElementOfOnce()
        }
        else if(flag.hasFlag(FLAG_NEVER)){
            debugElementOfBase = DebugElementOfNever()
        }
        else if(flag.hasFlag(FLAG_MANUAL)){
            debugElementOfBase = DebugElement(1000)
        }
        else if(flag.hasFlag(FLAG_1S)){
            debugElementOfBase = DebugElement(1000)
        }
        else if(flag.hasFlag(FLAG_5S)){
            debugElementOfBase = DebugElement(5000)
        }
        else{
            debugElementOfBase = DebugElementOfNever()

        }
        debugElementOfBase.tag = keyIn
        //end of DebugElement factory

        //map operation
        map.put(key, debugElementOfBase)
        return map[key]!!
    }

    fun remove(key:String){
        map.remove(key)
    }
    fun clear(){
        map.clear()
    }
    fun iterate(){
        println("iterate:")
        map.forEach { entry ->
            println("mapped: ${entry.key} : ${entry.value}")
        }
    }
    //local extensions
    fun Int.hasFlag(flag: Int): Boolean {
        return (this and flag) == flag
    }
}

