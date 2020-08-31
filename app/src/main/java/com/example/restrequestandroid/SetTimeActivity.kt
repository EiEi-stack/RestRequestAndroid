package com.example.restrequestandroid

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_set_time.*
import java.text.SimpleDateFormat
import java.util.*

class SetTimeActivity : AppCompatActivity() {
    lateinit var startCalendar: Calendar
    lateinit var endCalendar: Calendar
    lateinit var breakCalendar: Calendar
    var startTimeMinutes = 0
    var endTimeMinutes = 0
    var breakTimeMinutes = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_time)

        startCalendar = Calendar.getInstance()
        endCalendar = Calendar.getInstance()
        breakCalendar = Calendar.getInstance()
        val setTime = findViewById<Button>(R.id.btn_set_init_time)
        val startTime = findViewById<TextView>(R.id.tv_init_start_time)
        val endTime = findViewById<TextView>(R.id.tv_init_end_time)
        val breakTime = findViewById<TextView>(R.id.tv_init_break_time)

        //選択した開始時間を設定する
        val startdataSetListerer = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                startCalendar.set(Calendar.MINUTE, minute)
                updateView()
            }

        }
        //選択した開始時間を設定する
        val enddataSetListerer = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                endCalendar.set(Calendar.MINUTE, minute)
                updateEndView()
            }

        }
        //選択した開休憩間を設定する
        val breakdataSetListerer = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                breakCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                breakCalendar.set(Calendar.MINUTE, minute)
                updateBreakView()
            }

        }
        //開始時間を選択する時
        startTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                TimePickerDialog(
                    this@SetTimeActivity, 2, startdataSetListerer,
                    startCalendar.get(Calendar.HOUR_OF_DAY),
                    startCalendar.get(Calendar.MINUTE), true
                ).show()
            }

        })
        //終了時間を選択する時
        endTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                TimePickerDialog(
                    this@SetTimeActivity, 2, enddataSetListerer,
                    endCalendar.get(Calendar.HOUR_OF_DAY),
                    endCalendar.get(Calendar.MINUTE), true
                ).show()
            }

        })
        //休憩時間を選択する時
        breakTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                TimePickerDialog(
                    this@SetTimeActivity, 2, breakdataSetListerer,
                    breakCalendar.get(Calendar.HOUR_OF_DAY),
                    breakCalendar.get(Calendar.MINUTE), true
                ).show()
            }

        })


        //つぎのActivityへ行く
        setTime.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@SetTimeActivity, HomeActivity::class.java)
                val bundle = Bundle()
                bundle.putInt("init_start", startTimeMinutes)
                bundle.putInt("init_end", endTimeMinutes)
                bundle.putInt("init_break", breakTimeMinutes)
                intent.putExtra("myInitTime",bundle)
                startActivity(intent)

            }
        })
    }

    //テキストビューに開始時間を設定する
    private fun updateView() {
        val sdf = SimpleDateFormat("hh:mm", Locale.JAPAN)
        tv_init_start_time.text = ""
        tv_init_start_time.text = sdf.format(startCalendar.time)
        val startTime = tv_init_start_time.text
        val arr = startTime.split(":")
        var hour = 0
        var minutes = 0
        hour = arr[0].toInt()
        minutes = arr[1].toInt()
        startTimeMinutes = hour * 60 + minutes
    }

    //テキストビューに終了時間を設定する
    private fun updateEndView() {
        val sdf = SimpleDateFormat("hh:mm", Locale.JAPAN)
        tv_init_end_time.text = ""
        tv_init_end_time.text = sdf.format(endCalendar.time)
        val startTime = tv_init_end_time.text
        val arr = startTime.split(":")
        var hour = 0
        var minutes = 0
        hour = arr[0].toInt()
        minutes = arr[1].toInt()
        endTimeMinutes = hour * 60 + minutes
    }

    //テキストビュー休憩時間を設定する
    private fun updateBreakView() {
        val sdf = SimpleDateFormat("hh:mm", Locale.JAPAN)
        tv_init_break_time.text = ""
        tv_init_break_time.text = sdf.format(breakCalendar.time)
        val startTime = tv_init_break_time.text
        val arr = startTime.split(":")
        var hour = 0
        var minutes = 0
        hour = arr[0].toInt()
        minutes = arr[1].toInt()
        breakTimeMinutes = hour * 60 + minutes
    }
}
