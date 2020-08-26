package com.example.restrequestandroid

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    lateinit var calendarView: CalendarView
    lateinit var format: SimpleDateFormat
    lateinit var tbl_layout: TableLayout
    lateinit var calendarobj: Calendar
    lateinit var simpleDateFormat: SimpleDateFormat
    lateinit var showcurrentDate: SimpleDateFormat
    lateinit var dayFormat: SimpleDateFormat
    private var maxDay = 0
    private var totalWorkDay = 0
    private var totalActualWorkDay = 0
    private var totalWorkingHours = 0
    private var totalWorkingMinutes = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        simpleDateFormat = SimpleDateFormat("dd")
        showcurrentDate = SimpleDateFormat("yyyy年MM月", Locale.JAPAN)
        dayFormat = SimpleDateFormat("EEE", Locale.JAPAN)
        calendarobj = Calendar.getInstance()
        maxDay = calendarobj.getActualMaximum(Calendar.DAY_OF_MONTH)
        tbl_layout = findViewById<TableLayout>(R.id.tbl_layout)
        val tv_showDate = findViewById<TextView>(R.id.txt_showDate)
        tv_showDate.setText(showcurrentDate.format(calendarobj.time))
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                calendarobj.set(Calendar.YEAR, year)
                calendarobj.set(Calendar.MONTH, month)
                calendarobj.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                maxDay = calendarobj.getActualMaximum(Calendar.DAY_OF_MONTH)
                updateDateInView()
                createTableRow()
                setValue()
            }
        }
        tv_showDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                DatePickerDialog(
                    this@HomeActivity, dateSetListener,
                    calendarobj.get(Calendar.YEAR),
                    calendarobj.get(Calendar.MONTH),
                    calendarobj.get(Calendar.DAY_OF_MONTH)
                ).show()


            }
        })
        createTableRow()
    }

    private fun setValue() {
        totalWorkDay = 0
        totalActualWorkDay = 0
        totalWorkingHours = 0
        totalWorkingMinutes = 0
    }

    private fun addHeader() {
        val tbl_row = TableRow(applicationContext)
        tbl_row.setBackgroundColor(Color.BLACK)
        val layoutParams =
            TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            )
        layoutParams.setMargins(1, 1, 1, 1)
        for (j in 0 until 8) {
            val tv_one = TextView(applicationContext)
            tv_one.setPadding(0, 10, 0, 10)
            tv_one.setTextSize(14F)
            tv_one.setTypeface(null, Typeface.BOLD)
            tv_one.setBackgroundColor(Color.LTGRAY)
            if (j == 0) {
                tv_one.setText("日付")
            }
            if (j == 1) {
                tv_one.setText("曜日")
            }
            if (j == 2) {
                tv_one.setText("始業時間")
            }
            if (j == 3) {
                tv_one.setText("終業時間")
            }
            if (j == 4) {
                tv_one.setText("休憩時間")
            }
            if (j == 5) {
                tv_one.setText("実働時間")
            }
            if (j == 6) {
                tv_one.setText("備考")
            }
            if (j == 7) {
                tv_one.setText("機能")
            }
            tbl_row.addView(tv_one)
        }
        tbl_layout.addView(tbl_row)
    }

    private fun addFooter() {
        val tbl_row_footer = TableRow(applicationContext)
        tbl_row_footer.setBackgroundColor(Color.LTGRAY)
        val layoutParams =
            TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            )
        layoutParams.setMargins(1, 1, 1, 1)
        tbl_row_footer.layoutParams = layoutParams

        val params = tbl_row_footer.getLayoutParams() as TableRow.LayoutParams
        params.span = 2

        for (j in 0 until 6) {
            val tv_one = TextView(applicationContext)
            tv_one.setPadding(0, 10, 0, 10)
            tv_one.setTextSize(14F)
            tv_one.setTypeface(null, Typeface.BOLD)
            tv_one.setBackgroundColor(Color.LTGRAY)
            if (j == 0) {
                tv_one.setText("当月合計")
                tv_one.layoutParams = params
            }
            if (j == 1) {
                var setTotalWorkDay = ""
                setTotalWorkDay = totalWorkDay.toString() + "日"
                tv_one.setText(setTotalWorkDay)
            }
            if (j == 2) {
                var setTotalActualWorkDay = ""
                setTotalActualWorkDay = totalActualWorkDay.toString() + "日"
                tv_one.setText(setTotalActualWorkDay)
            }
            if (j == 3) {
                tv_one.setText("1日")
            }
            if (j == 4) {
                var hour = 0
                var minutes = 0
                var totalhour = 0
                var totalminutes = 0
                var totaltime = ""
                hour = totalWorkingMinutes / 60
                minutes = totalWorkingMinutes % 60
                totalhour = totalWorkingHours + hour
                totalminutes = minutes
                totaltime = totalhour.toString() + ":" + totalminutes.toString()
                tv_one.setText(totaltime)
            }
            tbl_row_footer.addView(tv_one)
        }
        tbl_layout.addView(tbl_row_footer)
    }

    fun updateDateInView() {
        val sdf = SimpleDateFormat("y年 M月", Locale.JAPAN)
        txt_showDate.text = ""
        txt_showDate.text = sdf.format(calendarobj.time)
    }

    fun createTableRow() {
        tbl_layout.removeAllViews()
        addHeader()

        for (i in 0 until maxDay) {
            totalWorkDay += 1
            val showCalendarDate = Calendar.getInstance()
            showCalendarDate.set(Calendar.DAY_OF_MONTH, i + 1)
            val tbl_row = TableRow(applicationContext)
            tbl_row.setBackgroundColor(Color.GRAY)
            val layoutParams =
                TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
            layoutParams.setMargins(1, 1, 1, 1)
            for (j in 0 until 8) {

                val tv_one = TextView(applicationContext)
                tv_one.setPadding(0, 10, 0, 10)
                tv_one.gravity = Gravity.CENTER
                tv_one.setTextSize(12F)
                val id_i = i.toString()
                val id_j = j.toString()
                val id = id_i + id_j
                tv_one.id = id.toInt()
                if (j == 0) {
                    tv_one.setText(simpleDateFormat.format(showCalendarDate.time))


                } else if (j == 1) {
                    tv_one.setText(dayFormat.format(showCalendarDate.time))
                } else if (j == 2) {
                    totalActualWorkDay += 1
                    tv_one.setText("09:00")
                } else if (j == 3) {
                    tv_one.setText("18:00")
                } else if (j == 4) {
                    tv_one.setText("01:00")
                } else if (j == 5) {
                    tv_one.setText("8:30")
                    val ab = tv_one.text
                    val arr = ab.split(":")
                    totalWorkingHours += arr[0].toInt()
                    totalWorkingMinutes += arr[1].toInt()

                } else if (j == 7) {
                    tv_one.setText("入力")
                } else {
                    tv_one.setText(" ")
                }
                tv_one.setBackgroundColor(Color.WHITE)

                tbl_row.addView(tv_one)


                if (tv_one.text == "土") {
                    tv_one.setTextColor(Color.BLUE)
                    totalWorkDay -= 1
                } else if (tv_one.text == "日") {
                    tv_one.setTextColor(Color.RED)
                    totalWorkDay -= 1
                }
                tv_one.setOnClickListener {
                    Toast.makeText(
                        applicationContext,
                        "Hey Hello" + totalWorkingHours,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            tbl_layout.addView(tbl_row)

        }
        addFooter()
    }
}



