package com.example.restrequestandroid

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    lateinit var calendarView: CalendarView
    lateinit var format: SimpleDateFormat
    val cal = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
        val dayFormat = SimpleDateFormat("EEE", Locale.JAPAN)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1)
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val tv_showDate = findViewById<TextView>(R.id.txt_showDate)

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }
        tv_showDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                DatePickerDialog(
                    this@HomeActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()

            }
        })
        for (i in 0 until maxDay) {
            calendar.set(Calendar.DAY_OF_MONTH, i + 1)

            println(simpleDateFormat.format(calendar.time))
            println(dayFormat.format(calendar.time))
        }

    }

    fun updateDateInView() {
        val sdf = SimpleDateFormat("y年 M月", Locale.JAPAN)
        txt_showDate.text = ""
        txt_showDate.text = sdf.format(cal.time)
    }
}
