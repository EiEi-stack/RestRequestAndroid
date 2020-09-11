package com.example.restrequestandroid

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
import android.widget.TimePicker
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
    var TAG = "HomeActivity"
    lateinit var tbl_layout: TableLayout
    lateinit var calendarobj: Calendar
    lateinit var simpleDateFormat: SimpleDateFormat
    lateinit var showcurrentDate: SimpleDateFormat
    lateinit var dayFormat: SimpleDateFormat
    private var maxDay = 0
    private var totalWorkDay = 0
    private var allActualWorkDay = 0
    private var totalActualWorkDay = 0
    private var totalActualWorkColumnTwo = 0
    private var totalWorkingHours = 0
    private var totalWorkingMinutes = 0
    lateinit var dirpath: String
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
        showcurrentDate = SimpleDateFormat("yyyy年MM月", Locale.JAPAN)
        dayFormat = SimpleDateFormat("EEE", Locale.JAPAN)
        calendarobj = Calendar.getInstance()
        setTimeEachCalendar = Calendar.getInstance()
        maxDay = calendarobj.getActualMaximum(Calendar.DAY_OF_MONTH)
        tbl_layout = findViewById<TableLayout>(R.id.tbl_layout)
        val tv_showDate = findViewById<TextView>(R.id.txt_showDate)
        val btn_set = findViewById<TextView>(R.id.btn_print)
        tv_showDate.setText(showcurrentDate.format(calendarobj.time))

        //前画面からデータを習得する
        val intent = getIntent()
        val bundle = intent.getBundleExtra("myInitTime")
        initStartTime = bundle.getInt("init_start")
        initEndTime = bundle.getInt("init_end")
        initBreakTime = bundle.getInt("init_break")


        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendarobj.set(Calendar.YEAR, year)
                calendarobj.set(Calendar.MONTH, month)
                calendarobj.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                maxDay = calendarobj.getActualMaximum(Calendar.DAY_OF_MONTH)
                updateDateInView()
                createTableRow()
                reset()
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
        btn_set.setOnClickListener(object : View.OnClickListener {
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
        val toggle = ActionBarDrawerToggle(this,drawerLayout,0,0)
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
        dirpath = File(applicationContext.filesDir, "LayoutImage" + File.separator).toString()
        val file = dirpath + File.separator + "image.jpeg"
        val bitmap = BitmapFactory.decodeFile(file)
        val pdfDocument = PdfDocument()
        val myPageInfo = PdfDocument.PageInfo.Builder(500, 500, 1).create()
        val page = pdfDocument.startPage(myPageInfo)

        val rectangle = Rect(0, 0, 100, 100)
        page.canvas.drawBitmap(bitmap, null, rectangle, null)
        pdfDocument.finishPage(page)

        val pdfFile = dirpath + File.separator + "myPDF.pdf"
        val myPDFFile = File(pdfFile)
        try {
            pdfDocument.writeTo(FileOutputStream(myPDFFile))
        } catch (e: IOException) {
            Log.e(TAG, "Error at :" + e)
        }
        pdfDocument.close()
        val intent = Intent(this@HomeActivity, PDFViewActivity::class.java)
        startActivity(intent)
    }

    fun startLoadData() {
        mProgressBar.setCancelable(false)
        mProgressBar.setMessage(resources.getString(R.string.fetch_day))
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressBar.show()
        LoadDataTask().execute()

    }

    private fun reset() {
        totalWorkDay = 0
        allActualWorkDay = 0
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
            tv_one.setPadding(20, 10, 0, 10)
            tv_one.setTextSize(14F)
            tv_one.setTypeface(null, Typeface.BOLD)
            tv_one.setBackgroundColor(Color.LTGRAY)
            if (j == 0) {
                tv_one.setText("当月合計")
                tv_one.layoutParams = params
            }
            if (j == 1) {
                ftCountTotalWorkDay(tv_one)
            }
            if (j == 2) {
                ftCountTotalActualWorkDay(tv_one)
            }
            if (j == 3) {
                ftCountToalRemainingHours(tv_one)
            }
            if (j == 4) {
                ftCountTotalTime(tv_one)
            }
            tbl_row_footer.addView(tv_one)
        }
        tbl_layout.addView(tbl_row_footer)
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
        tvOne.setText(totaltime)
    }

    private fun ftCountToalRemainingHours(tvOne: TextView) {
        val differenthour = totalWorkDay - totalActualWorkDay
        var remainHour = ""
        remainHour = differenthour.toString() + "日"
        tvOne.setText(remainHour)
    }

    private fun ftCountTotalWorkDay(tvOne: TextView) {
        var setTotalWorkDay = ""
        setTotalWorkDay = totalWorkDay.toString() + "日"
        tvOne.setText(setTotalWorkDay)
    }

    private fun ftCountTotalActualWorkDay(tvOne: TextView) {
        var setTotalActualWorkDay = ""
        totalActualWorkDay =  totalActualWorkColumnTwo
//        totalActualWorkDay = allActualWorkDay
        setTotalActualWorkDay = totalActualWorkDay.toString() + "日"
        tvOne.setText(setTotalActualWorkDay)
    }

    fun updateDateInView() {
        val sdf = SimpleDateFormat("y年 M月", Locale.JAPAN)
        txt_showDate.text = ""
        txt_showDate.text = sdf.format(calendarobj.time)
    }

    fun createTableRow() {
        tbl_layout.removeAllViews()
        supportActionBar?.setTitle(txt_showDate.text)
        addHeader()
        for (i in 0 until maxDay) {
            totalWorkDay += 1
            var mStartHour = 0
            var mStartMinutes = 0
            var mEndHour = 0
            var mEndMinutes = 0
            var mBreakHour = 0
            var mBreakMinutes = 0
            val showCalendarDate = Calendar.getInstance()
            showCalendarDate.set(Calendar.MONTH, calendarobj.get(Calendar.MONTH))
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
                    isClickable = false
                    tv_one.setText(simpleDateFormat.format(showCalendarDate.time))

                } else if (j == 1) {
                    isClickable = false
                    tv_one.setText(dayFormat.format(showCalendarDate.time))


                } else if (j == 2) {
                    isClickable = true
                    if (initStartTime != 0) {
                        val hour = initStartTime / 60
                        val minutes = initStartTime % 60
                        val hrMin = hour.toString() + ":" + minutes.toString()
                        tv_one.setText(hrMin)

                    } else {
                        tv_one.setText("09:00")
                    }
                    if (tv_one.text.isNotEmpty() && tv_one.text != "") {
                        allActualWorkDay += 1

                    }
                    if (tv_one.text.isNotEmpty() || tv_one.text != "" || tv_one.text != "ー") {
                        val value = tv_one.text.toString()
                        val arr = value.split(":")
                        mStartHour += arr[0].toInt()
                        mStartMinutes += arr[1].toInt()
                    }
                } else if (j == 3) {
                    isClickable = true
                    if (initEndTime != 0) {
                        val hour = initEndTime / 60
                        val minutes = initEndTime % 60
                        val hrMin = hour.toString() + ":" + minutes.toString()
                        tv_one.setText(hrMin)
                    } else {
                        tv_one.setText("18:00")

                    }
                    if (tv_one.text.isNotEmpty() || tv_one.text != "" || tv_one.text != "ー") {
                        val value = tv_one.text.toString()
                        val arr = value.split(":")
                        mEndHour += arr[0].toInt()
                        mEndMinutes += arr[1].toInt()
                    }
                } else if (j == 4) {
                    isClickable = true
                    if (initBreakTime != 0) {
                        val hour = initBreakTime / 60
                        val minutes = initBreakTime % 60
                        val hrMin = hour.toString() + ":" + minutes.toString()
                        tv_one.setText(hrMin)
                    } else {
                        tv_one.setText("01:00")
                    }
                    if (tv_one.text.isNotEmpty() || tv_one.text != "" || tv_one.text != "ー") {
                        val value = tv_one.text.toString()
                        val arr = value.split(":")
                        mBreakHour += arr[0].toInt()
                        mBreakMinutes += arr[1].toInt()
                    }
                } else if (j == 5) {
                    isClickable = false
                    var total_start_minutes = 0
                    var total_end_minutes = 0
                    var total_break_minutes = 0
                    if (mStartHour != 0 || mStartMinutes != 0) {
                        total_start_minutes = mStartHour * 60 + mStartMinutes
//                        tv_one.setText(tdyWorkingTime.toString())
                    }
                    if (mEndHour != 0 || mEndMinutes != 0) {
                        total_end_minutes = mEndHour * 60 + mEndMinutes

                    }
                    if (mBreakHour != 0 || mBreakMinutes != 0) {
                        total_break_minutes = mBreakHour * 60 + mBreakMinutes

                    }
                    var remainingMinutes = 0
                    remainingMinutes =
                        total_end_minutes - (total_start_minutes + total_break_minutes)
                    var todayWorkHour = 0
                    var todayWorkMinutes = 0
                    todayWorkHour = remainingMinutes / 60
                    todayWorkMinutes = remainingMinutes % 60
                    if (todayWorkHour != null || todayWorkMinutes != null) {
                        tv_one.text = todayWorkHour.toString() + ":" + todayWorkMinutes.toString()
                    }
                    if (tv_one.text != null && tv_one.text.isNotEmpty()) {

                        val ab = tv_one.text
                        val arr = ab.split(":")
                        totalWorkingHours += arr[0].toInt()
                        totalWorkingMinutes += arr[1].toInt()
                    }


                } else if (j == 7) {
                    isClickable = false
                    tv_one.setText("入力")

                } else {
                    isClickable = false
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
                if (tv_one.text == "土" || tv_one.text == "日") {
                    dayId.add(tv_one.id)
                }


                for (day in dayId) {
                    if (tv_one.id == day) {
                        if (tv_one.text == "土" || tv_one.text == "日") {
                            workOffStartHour.add(day + 1)
                            workOffEndHour.add(day + 2)
                            workOffBreakHour.add(day + 3)
                            workOffTotalHour.add(day + 4)

                        }
                    }
                }

                for (dayoff in workOffStartHour) {
                    if (tv_one.id == dayoff) {
                        tv_one.text = ""
                    }
                }

                for (dayoffEnd in workOffEndHour) {
                    if (tv_one.id == dayoffEnd) {
                        tv_one.text = ""
                    }
                }

                for (dayoffBreak in workOffBreakHour) {
                    if (tv_one.id == dayoffBreak) {
                        tv_one.text = ""
                    }
                }

                for (dayoffTotal in workOffTotalHour) {
                    if (tv_one.id == dayoffTotal) {
                        tv_one.text = ""
                    }
                }
                for (columnTwo in startHourColumn) {
                    if (tv_one.id == columnTwo && tv_one.text != "") {
                        totalActualWorkColumnTwo += 1
                    }
                }

                val timeDataSetListener = object : TimePickerDialog.OnTimeSetListener {
                    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                        setTimeEachCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        setTimeEachCalendar.set(Calendar.MINUTE, minute)
                        val sdf = SimpleDateFormat("hh:mm", Locale.JAPAN)
                        tv_one.text = ""
                        tv_one.text = sdf.format(setTimeEachCalendar.time)
                    }

                }
                if (isClickable == true) {
                    tv_one.setOnClickListener {
                        TimePickerDialog(
                            this@HomeActivity, 2, timeDataSetListener,
                            setTimeEachCalendar.get(Calendar.HOUR_OF_DAY),
                            setTimeEachCalendar.get(Calendar.MINUTE), true
                        ).show()

                    }
                }
            }
            tbl_layout.addView(tbl_row)

        }

        addFooter()
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

        }else if (id == R.id.send_mail) {

        }
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}



