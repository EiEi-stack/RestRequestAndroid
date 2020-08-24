package com.example.restrequestandroid

import android.os.Bundle
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    lateinit var calendarView: CalendarView
    lateinit var format: SimpleDateFormat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")
        val dayFormat = SimpleDateFormat("EEE", Locale.JAPAN)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH,1)
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 0 until maxDay) {
            calendar.set(Calendar.DAY_OF_MONTH, i + 1)

            println(simpleDateFormat.format(calendar.time))
            println(dayFormat.format(calendar.time))
        }

    }
}
