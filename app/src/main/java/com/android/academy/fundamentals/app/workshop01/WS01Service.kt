package com.android.academy.fundamentals.app.workshop01

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.widget.Toast
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

private const val KEY_TIK = "key"
private const val TIMER_DELAY_MS = 300L
private const val TIMER_PERIOD_MS = 3000L

//TODO 01 : extends from Service
class WS01Service : Service() {

    private val timer: Timer = Timer()

    //TODO 02 : override onStartCommand
    // create Toast with message Services starts
    // set timer
    // timer.scheduleAtFixedRate(MainTask(), TIMER_DELAY_MS, TIMER_PERIOD_MS)
    // return START_NOT_STICKY or START_STICKY
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // return super.onStartCommand(intent, flags, startId)
        Toast.makeText(applicationContext, "Service start", Toast.LENGTH_SHORT).show()
        timer.scheduleAtFixedRate(MainTask(), TIMER_DELAY_MS, TIMER_PERIOD_MS)
        return Service.START_NOT_STICKY
    }

    inner class MainTask : TimerTask() {

        override fun run() {
            val mes = Message.obtain()
            mes.data.putString(KEY_TIK, "Tik")

            toastHandler.sendMessage(mes)
        }

        override fun cancel(): Boolean {
            val mes = Message.obtain()
            mes.data.putString(KEY_TIK, "Time off")

            toastHandler.sendMessage(mes)

            return super.cancel()
        }
    }

    //TODO 03 : override onDestroy
    // turn off timer
    // timer.cancel()
    // send Toast with message Service stopped
    override fun onDestroy() {
        timer.cancel()
        Toast.makeText(applicationContext, "Service stopped", Toast.LENGTH_SHORT).show()
    }

    private val toastHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            Toast.makeText(applicationContext, msg.data.getString(KEY_TIK), Toast.LENGTH_SHORT)
                .show()
        }
    }


    //TODO 04 : override onBind
    // return null
    override fun onBind(p0: Intent?): IBinder? {
       return null
    }

    //TODO 05 : DON'T FORGET ABOUT MANIFEST
}
