package com.android.fundamentals.workshop04.task

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.fundamentals.R
import com.android.fundamentals.workshop04.SampleBottomSheet
import com.google.android.material.snackbar.Snackbar
import java.util.*

class WS04AssignmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ws04)

        findViewById<Button>(R.id.btn_show_alert_dialog)?.apply {
            setOnClickListener {

                AlertDialog.Builder(context)
                    .setTitle("Alert!!!")
                    .setPositiveButton("ok") { _, _ ->
                        Toast.makeText(
                            context,
                            "ok",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("cancel") { _, _ ->
                        Toast.makeText(
                            context,
                            "cancel",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNeutralButton("retry") { _, _ ->
                        Toast.makeText(
                            context,
                            "retry",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setOnCancelListener {
                        Toast.makeText(
                            context,
                            "thw window is closed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .show()
            }
        }

        findViewById<Button>(R.id.btn_show_dialog_fragment)?.apply {
            setOnClickListener {
                val dialog = WS04AssignmentDialogFragment()
                dialog.show(supportFragmentManager, "dialog fragment")
            }
        }

        findViewById<Button>(R.id.btn_show_time_picker)?.apply {
            setOnClickListener {
                val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                val minute = Calendar.getInstance().get(Calendar.MINUTE)
                val timePickerDialog = TimePickerDialog(
                    this@WS04AssignmentActivity,
                    { p1, p2, p3 ->
                        Snackbar
                            .make(rootView, "$p2:$p3", Snackbar.LENGTH_SHORT)
                            .show()
                        println("$p2:$p3")
                    },
                    hour,
                    minute,
                    true
                )

                timePickerDialog.show()
            }
        }

        findViewById<Button>(R.id.btn_show_date_picker)?.apply {
            setOnClickListener {
                val year = Calendar.getInstance().get(Calendar.YEAR)
                val month = Calendar.getInstance().get(Calendar.MONTH)
                val dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                val datePickerDialog = DatePickerDialog(
                    this@WS04AssignmentActivity,
                    { p0, p1, p2, p3 ->
                        Snackbar.make(
                            rootView,
                            "you chose $p3/$p2/$p1",
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    },
                    year,
                    month,
                    dayOfMonth
                )

                datePickerDialog.show()
            }
        }

        findViewById<Button>(R.id.btn_show_bottom_sheet_dialog)?.apply {
            setOnClickListener {
                SampleBottomSheet().show(supportFragmentManager, "bottom sheet dialog")
            }
        }
    }
}