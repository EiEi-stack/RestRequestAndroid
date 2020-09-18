package com.example.restrequestandroid

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var TAG = "HomeActivity"
    lateinit var tableLayout: TableLayout
    lateinit var calendarObj: Calendar
    lateinit var simpleDateFormat: SimpleDateFormat
    lateinit var dayFormat: SimpleDateFormat
    private var maxDay = 0
    private var workDayOfMonth = 0
    private var totalActualWorkDay = 0
    private var totalActualWorkColumnTwo = 0
    private var totalWorkingHours = 0
    private var totalWorkingMinutes = 0
    lateinit var setTimeEachCalendar: Calendar
    var dayId = ArrayList<Int>()
    var workOffStartHour = ArrayList<Int>()
    var workOffEndHour = ArrayList<Int>()
    var workOffBreakHour = ArrayList<Int>()
    var workOffTotalHour = ArrayList<Int>()
    var startHourColumn = ArrayList<Int>()
    var initStartTime = 0
    var initEndTime = 0
    var initBreakTime = 0
    var isClickable = false
    lateinit var mProgressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProgressBar = ProgressDialog(this)
        setContentView(R.layout.activity_home)
        simpleDateFormat = SimpleDateFormat("dd")
        var showCurrentDate: SimpleDateFormat = SimpleDateFormat("yyyy年MM月", Locale.JAPAN)
        dayFormat = SimpleDateFormat("EEE", Locale.JAPAN)
        calendarObj = Calendar.getInstance()
        setTimeEachCalendar = Calendar.getInstance()
        maxDay = calendarObj.getActualMaximum(Calendar.DAY_OF_MONTH)
        tableLayout = findViewById<TableLayout>(R.id.tbl_layout)
        val tvShowDate = findViewById<TextView>(R.id.txt_showDate)
        val btnSet = findViewById<TextView>(R.id.btn_print)
        tvShowDate.text = showCurrentDate.format(calendarObj.time)

        //前画面からデータを習得する
        val intent = getIntent()
        val bundle = intent.getBundleExtra("myInitTime")
        initStartTime = bundle.getInt("init_start")
        initEndTime = bundle.getInt("init_end")
        initBreakTime = bundle.getInt("init_break")


        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendarObj.set(Calendar.YEAR, year)
                calendarObj.set(Calendar.MONTH, month)
                calendarObj.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                maxDay = calendarObj.getActualMaximum(Calendar.DAY_OF_MONTH)
                updateDateInView()
                createTableRow()
                reset()
            }
        tvShowDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                DatePickerDialog(
                    this@HomeActivity, dateSetListener,
                    calendarObj.get(Calendar.YEAR),
                    calendarObj.get(Calendar.MONTH),
                    calendarObj.get(Calendar.DAY_OF_MONTH)
                ).show()


            }
        })
        btnSet.setOnClickListener(object : View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onClick(v: View?) {
//                layoutToImage()
                excelPrint()
            }

        })
        startLoadData()
        for (i in 0 until 31) {
            val columnTwoId = i.toString() + 2
            startHourColumn.add(columnTwoId.toInt())
        }

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

    }

    private fun excelPrint() {

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
//    private fun layoutToImage() {
//        val view = findViewById<LinearLayout>(R.id.container)
//        val scrollView = findViewById<ScrollView>(R.id.scrollView)
//        val totalHeight = view.getChildAt(0).height
//        val totalWidth = view.getChildAt(0).width
//        val bitmap = getBitmapFromView(view, totalHeight, totalWidth)
//
//        val path = File(applicationContext.filesDir, "LayoutImage" + File.separator + "image.jpeg")
//        try {
//            val fos = FileOutputStream(path)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
//            fos.flush()
//            fos.close()
//            MediaStore.Images.Media.insertImage(
//                applicationContext.contentResolver,
//                bitmap,
//                "Screen",
//                "screen"
//            )
//        } catch (e: FileNotFoundException) {
//            Log.e(TAG, "Error at Layout to image is :" + e)
//        } catch (e: Exception) {
//            Log.e(TAG, "Error at Layout to image is :" + e)
//        }
//        imageToPDF()
//
//    }

    private fun getBitmapFromView(view: View, totalHeight: Int, totalWidth: Int): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }
//    @RequiresApi(Build.VERSION_CODES.KITKAT)
//    private fun layoutToImage() {
//
//        val layout = findViewById<LinearLayout>(R.id.container)
//        //Viewを画像に変更
//        layout.isDrawingCacheEnabled = true
//        layout.buildDrawingCache()
//        val bm = layout.getDrawingCache()
////        val bm = Bitmap.createBitmap(container.getChildAt(0).width,container.getChildAt(0).height,Bitmap.Config.ARGB_8888)
//
//        val share = Intent(Intent.ACTION_SEND)
//        share.setType("image/jpeg")
//        try {
//            val path = File(applicationContext.filesDir, "LayoutImage" + File.separator)
//            if (!path.exists()) {
//                path.mkdir()
//            }
//            Log.d("--filePath", path.toString());
//            val outFile = File(path, "image" + ".jpeg")
//            val outputStrem = FileOutputStream(outFile)
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStrem)
//            imageToPDF()
//        } catch (e: FileNotFoundException) {
//            Log.e(TAG, "Saving received message failed with ", e)
//        } catch (e: IOException) {
//            Log.e(TAG, "Saving received message failed with ", e)
//        }
//
//
//    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun imageToPDF() {
        val dirPath = File(applicationContext.filesDir, "LayoutImage" + File.separator).toString()
        val file = dirPath + File.separator + "image.jpeg"
        val bitmap = BitmapFactory.decodeFile(file)
        val pdfDocument = PdfDocument()
        val myPageInfo = PdfDocument.PageInfo.Builder(500, 500, 1).create()
        val page = pdfDocument.startPage(myPageInfo)

        val rectangle = Rect(0, 0, 100, 100)
        page.canvas.drawBitmap(bitmap, null, rectangle, null)
        pdfDocument.finishPage(page)

        val pdfFile = dirPath + File.separator + "myPDF.pdf"
        val myPdfFile = File(pdfFile)
        try {
            pdfDocument.writeTo(FileOutputStream(myPdfFile))
        } catch (e: IOException) {
            Log.e(TAG, "Error at :$e")
        }
        pdfDocument.close()
        val intent = Intent(this@HomeActivity, PDFViewActivity::class.java)
        startActivity(intent)
    }

    private fun startLoadData() {
        mProgressBar.setCancelable(false)
        mProgressBar.setMessage(resources.getString(R.string.fetch_day))
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressBar.show()
        LoadDataTask().execute()

    }

    private fun reset() {
        workDayOfMonth = 0
        totalActualWorkDay = 0
        totalActualWorkColumnTwo = 0
        totalWorkingHours = 0
        totalWorkingMinutes = 0
        dayId.clear()
        startHourColumn.clear()
        workOffStartHour.clear()
        workOffEndHour.clear()
        workOffBreakHour.clear()
        workOffTotalHour.clear()


    }

    private fun addHeader() {
        val tblRow = TableRow(applicationContext)
        tblRow.setBackgroundColor(Color.BLACK)
        val layoutParams =
            TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            )
        layoutParams.setMargins(1, 1, 1, 1)
        for (j in 0 until 8) {
            val tvHeader = TextView(applicationContext)
            tvHeader.setPadding(0, 10, 0, 10)
            tvHeader.textSize = 14F
            tvHeader.setTypeface(null, Typeface.BOLD)
            tvHeader.setBackgroundColor(Color.LTGRAY)
            if (j == 0) {
                tvHeader.text = "日付"
            }
            if (j == 1) {
                tvHeader.text = "曜日"
            }
            if (j == 2) {
                tvHeader.text = "始業時間"
            }
            if (j == 3) {
                tvHeader.text = "終業時間"
            }
            if (j == 4) {
                tvHeader.text = "休憩時間"
            }
            if (j == 5) {
                tvHeader.text = "実働時間"
            }
            if (j == 6) {
                tvHeader.text = "備考"
            }
            if (j == 7) {
                tvHeader.text = "機能"
            }
            tblRow.addView(tvHeader)
        }
        tableLayout.addView(tblRow)
    }

    private fun addFooter() {
        val tblFooter = TableRow(applicationContext)
        tblFooter.setBackgroundColor(Color.LTGRAY)
        val layoutParams =
            TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            )
        layoutParams.setMargins(1, 1, 1, 1)
        tblFooter.layoutParams = layoutParams

        val params = tblFooter.getLayoutParams() as TableRow.LayoutParams
        params.span = 2

        for (j in 0 until 6) {

            val tvFooter = TextView(applicationContext)
            tvFooter.setPadding(20, 10, 0, 10)
            tvFooter.textSize = 14F
            tvFooter.setTypeface(null, Typeface.BOLD)
            tvFooter.setBackgroundColor(Color.LTGRAY)
            if (j == 0) {
                tvFooter.setText("当月合計")
                tvFooter.layoutParams = params
            }
            if (j == 1) {
                ftCountTotalWorkDay(tvFooter)
            }
            if (j == 2) {
                ftCountTotalActualWorkDay(tvFooter)
            }
            if (j == 3) {
                ftCountTotalRemainingHours(tvFooter)
            }
            if (j == 4) {
                ftCountTotalTime(tvFooter)
            }
            tblFooter.addView(tvFooter)
        }
        tableLayout.addView(tblFooter)
    }

    private fun ftCountTotalTime(tvOne: TextView) {
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
        tvOne.text = totaltime
    }

    private fun ftCountTotalRemainingHours(tvOne: TextView) {
        val differentHour = workDayOfMonth - totalActualWorkDay
        var remainHour = ""
        remainHour = differentHour.toString() + "日"
        tvOne.text = remainHour
    }

    private fun ftCountTotalWorkDay(tvOne: TextView) {
        var setTotalWorkDay = ""
        setTotalWorkDay = workDayOfMonth.toString() + "日"
        tvOne.text = setTotalWorkDay
    }

    private fun ftCountTotalActualWorkDay(tvOne: TextView) {
        var setTotalActualWorkDay = ""
        totalActualWorkDay = totalActualWorkColumnTwo
        setTotalActualWorkDay = totalActualWorkDay.toString() + "日"
        tvOne.text = setTotalActualWorkDay
    }

    private fun updateDateInView() {
        val sdf = SimpleDateFormat("y年 M月", Locale.JAPAN)
        txt_showDate.text = ""
        txt_showDate.text = sdf.format(calendarObj.time)
    }

    fun createTableRow() {
        tableLayout.removeAllViews()
        supportActionBar?.title = txt_showDate.text
        addHeader()
        for (i in 0 until maxDay) {
            workDayOfMonth += 1
            var mStartHour = 0
            var mStartMinutes = 0
            var mEndHour = 0
            var mEndMinutes = 0
            var mBreakHour = 0
            var mBreakMinutes = 0
            val showCalendarDate = Calendar.getInstance()
            showCalendarDate.set(Calendar.MONTH, calendarObj.get(Calendar.MONTH))
            showCalendarDate.set(Calendar.DAY_OF_MONTH, i + 1)
            val tblRow = TableRow(applicationContext)
            tblRow.setBackgroundColor(Color.GRAY)
            val layoutParams =
                TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
            layoutParams.setMargins(1, 1, 1, 1)
            for (j in 0 until 8) {

                val tvField = TextView(applicationContext)
                tvField.setPadding(0, 10, 0, 10)
                tvField.gravity = Gravity.CENTER
                tvField.textSize = 12F
                val id_i = i.toString()
                val id_j = j.toString()
                val id = id_i + id_j
                tvField.id = id.toInt()
                if (j == 0) {
                    isClickable = false
                    tvField.setText(simpleDateFormat.format(showCalendarDate.time))

                } else if (j == 1) {
                    isClickable = false
                    tvField.setText(dayFormat.format(showCalendarDate.time))


                } else if (j == 2) {
                    isClickable = true
                    if (initStartTime != 0) {
                        val hour = initStartTime / 60
                        val minutes = initStartTime % 60
                        val hrMin = "$hour:$minutes"
                        tvField.text = hrMin

                    } else {
                        tvField.text = "09:00"
                    }
                    if (tvField.text.isNotEmpty() && tvField.text != "") {

                    }
                    if (tvField.text.isNotEmpty() || tvField.text != "" || tvField.text != "ー") {
                        val value = tvField.text.toString()
                        val arr = value.split(":")
                        mStartHour += arr[0].toInt()
                        mStartMinutes += arr[1].toInt()
                    }
                } else if (j == 3) {
                    isClickable = true
                    if (initEndTime != 0) {
                        val hour = initEndTime / 60
                        val minutes = initEndTime % 60
                        val hrMin = "$hour:$minutes"
                        tvField.text = hrMin
                    } else {
                        tvField.text = "18:00"

                    }
                    if (tvField.text.isNotEmpty() || tvField.text != "" || tvField.text != "ー") {
                        val value = tvField.text.toString()
                        val arr = value.split(":")
                        mEndHour += arr[0].toInt()
                        mEndMinutes += arr[1].toInt()
                    }
                } else if (j == 4) {
                    isClickable = true
                    if (initBreakTime != 0) {
                        val hour = initBreakTime / 60
                        val minutes = initBreakTime % 60
                        val hrMin = "$hour:$minutes"
                        tvField.text = hrMin
                    } else {
                        tvField.text = "01:00"
                    }
                    if (tvField.text.isNotEmpty() || tvField.text != "" || tvField.text != "ー") {
                        val value = tvField.text.toString()
                        val arr = value.split(":")
                        mBreakHour += arr[0].toInt()
                        mBreakMinutes += arr[1].toInt()
                    }
                } else if (j == 5) {
                    isClickable = false
                    val arrayWorkingHourInfo = ArrayList<Int>()
                    arrayWorkingHourInfo.add(mStartHour)
                    arrayWorkingHourInfo.add(mStartMinutes)
                    arrayWorkingHourInfo.add(mEndHour)
                    arrayWorkingHourInfo.add(mEndMinutes)
                    arrayWorkingHourInfo.add(mBreakHour)
                    arrayWorkingHourInfo.add(mBreakMinutes)
                    funTodayWorkingHour(tvField, arrayWorkingHourInfo)
                } else if (j == 7) {
                    isClickable = false
                    tvField.text = "入力"

                } else {
                    isClickable = false
                    tvField.text = " "

                }
                tvField.setBackgroundColor(Color.WHITE)

                tblRow.addView(tvField)

                if (tvField.text == "土") {
                    tvField.setTextColor(Color.BLUE)
                    workDayOfMonth -= 1
                } else if (tvField.text == "日") {
                    tvField.setTextColor(Color.RED)
                    workDayOfMonth -= 1
                }
                if (tvField.text == "土" || tvField.text == "日") {
                    dayId.add(tvField.id)
                }


                for (day in dayId) {
                    if (tvField.id == day) {
                        if (tvField.text == "土" || tvField.text == "日") {
                            workOffStartHour.add(day + 1)
                            workOffEndHour.add(day + 2)
                            workOffBreakHour.add(day + 3)
                            workOffTotalHour.add(day + 4)

                        }
                    }
                }

                for (dayOff in workOffStartHour) {
                    if (tvField.id == dayOff) {
                        tvField.text = ""
                    }
                }

                for (dayOffEnd in workOffEndHour) {
                    if (tvField.id == dayOffEnd) {
                        tvField.text = ""
                    }
                }

                for (dayOffBreak in workOffBreakHour) {
                    if (tvField.id == dayOffBreak) {
                        tvField.text = ""
                    }
                }

                for (dayOffTotal in workOffTotalHour) {
                    if (tvField.id == dayOffTotal) {
                        tvField.text = ""
                    }
                }
                for (columnTwo in startHourColumn) {
                    if (tvField.id == columnTwo && tvField.text != "") {
                        totalActualWorkColumnTwo += 1
                    }
                }

                val timeDataSetListener =
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        setTimeEachCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        setTimeEachCalendar.set(Calendar.MINUTE, minute)
                        val sdf = SimpleDateFormat("hh:mm", Locale.JAPAN)
                        tvField.text = ""
                        tvField.text = sdf.format(setTimeEachCalendar.time)
                    }
                if (isClickable) {
                    tvField.setOnClickListener {
                        TimePickerDialog(
                            this@HomeActivity, 2, timeDataSetListener,
                            setTimeEachCalendar.get(Calendar.HOUR_OF_DAY),
                            setTimeEachCalendar.get(Calendar.MINUTE), true
                        ).show()

                    }
                }
            }
            tableLayout.addView(tblRow)

        }

        addFooter()
    }

    @SuppressLint("SetTextI18n")
    private fun funTodayWorkingHour(
        tvOne: TextView,
        arrayWorkingHourInfo: ArrayList<Int>
    ) {
        var mStartMinutes = 0
        var mEndMinutes = 0
        var mBreakMinutes = 0
        if (arrayWorkingHourInfo[0] != 0 || arrayWorkingHourInfo[1] != 0) {
            mStartMinutes = arrayWorkingHourInfo[0] * 60 + arrayWorkingHourInfo[1]
//                        tv_one.setText(tdyWorkingTime.toString())
        }
        if (arrayWorkingHourInfo[2] != 0 || arrayWorkingHourInfo[3] != 0) {
            mEndMinutes = arrayWorkingHourInfo[2] * 60 + arrayWorkingHourInfo[3]

        }
        if (arrayWorkingHourInfo[4] != 0 || arrayWorkingHourInfo[5] != 0) {
            mBreakMinutes = arrayWorkingHourInfo[4] * 60 + arrayWorkingHourInfo[5]

        }
        var remainingMinutes = 0
        remainingMinutes =
            mEndMinutes - (mStartMinutes + mBreakMinutes)
        var todayWorkHour = 0
        var todayWorkMinutes = 0
        todayWorkHour = remainingMinutes / 60
        todayWorkMinutes = remainingMinutes % 60
        if (todayWorkHour != null || todayWorkMinutes != null) {
            tvOne.text = "$todayWorkHour:$todayWorkMinutes"
        }
        if (tvOne.text != null && tvOne.text.isNotEmpty()) {

            val ab = tvOne.text
            val arr = ab.split(":")
            totalWorkingHours += arr[0].toInt()
            totalWorkingMinutes += arr[1].toInt()
        }
    }

    inner class LoadDataTask : AsyncTask<Integer, Integer, String>() {
        override fun doInBackground(vararg params: Integer?): String {
            try {
                Thread.sleep(6000)

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return "Task Completed"
        }

        override fun onPostExecute(result: String?) {
            mProgressBar.hide()
            createTableRow()

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_saveCurrentMonthWorkingTime) {

        } else if (id == R.id.menu_Excel) {

        } else if (id == R.id.menu_Print) {

        } else if (id == R.id.send_mail) {

        } else if (id == R.id.company_profile) {
            startActivity(Intent(this@HomeActivity, CompanyProfile::class.java))
        }
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}



