package com.android.academy.fundamentals.workshop_1

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.academy.fundamentals.R

class WS01ThreadsTaskFragment : Fragment(R.layout.fragment_ws_01) {

    private var threadButton: Button? = null
    private var threadTextView: TextView? = null

    private val handler = MyHandler()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findViews(view)
        threadButton?.setOnClickListener {
            startThread()
            startRunnable()
        }
    }

    override fun onDestroyView() {
        threadButton = null
        threadTextView = null
        super.onDestroyView()
    }

    private fun findViews(view: View) {
        threadButton = view.findViewById(R.id.thread_button)
        threadTextView = view.findViewById(R.id.thread_text_view)
    }

    private fun startThread() {
        printMessage(getString(R.string.wait))
        val thread = MyThread()
        thread.start()
    }

    private fun startRunnable() {
        printMessage(getString(R.string.wait))
        val runnable = MyRunnable()
        runnable.run()
    }

    private fun printMessage(mes: String) {
        threadTextView?.text = mes
    }

    @SuppressLint("HandlerLeak")
    inner class MyHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            printMessage(msg.data.getString(MESSAGE_KEY, ""))
        }
    }

    inner class MyThread : Thread() {
        override fun run() {
            sleep(6000)
            val msg = Message()
            msg.data.putString(MESSAGE_KEY, getString(R.string.thread_worked))
            handler.sendMessage(msg)

        }
    }

    inner class MyRunnable : Runnable {
        override fun run() {
            Thread.sleep(4000)
            val msg = Message()
            msg.data.putString(MESSAGE_KEY, getString(R.string.runnable_worked))
            handler.sendMessage(msg)

        }

    }

    companion object {
        private const val MESSAGE_KEY = "key"
    }
}