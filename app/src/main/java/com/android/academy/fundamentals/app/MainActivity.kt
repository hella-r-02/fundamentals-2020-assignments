package com.android.academy.fundamentals.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.academy.fundamentals.app.workshop01.WS01Fragment
import com.android.academy.fundamentals.app.workshop02.Ws02Fragment
import com.android.academy.fundamentals.app.workshop03.WS03Fragment
import com.android.academy.fundamentals.app.workshop03.solution.WS03FragmentSolution

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WS03FragmentSolution.create())
                .commit()
        }
    }
}
