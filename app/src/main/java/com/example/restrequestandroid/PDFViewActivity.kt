package com.example.restrequestandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import java.io.File

class PDFViewActivity : AppCompatActivity(), OnPageChangeListener, OnLoadCompleteListener {
    lateinit var pdfView: PDFView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p_d_f_view)
        pdfView = findViewById<PDFView>(R.id.mypdfview)
        this.openPdf()
    }

    fun openPdf() {
        val root = File(applicationContext.filesDir, "LayoutImage" + File.separator).toString()
        val file = File(root, "myPDF.pdf")
//        pdfView.fromFile(file).defaultPage(0).enableSwipe(true).swipeHorizontal(false)
//            .onPageChange(this).enableAnnotationRendering(true).onLoad(this)
//            .scrollHandle(DefaultScrollHandle(this))
//            .load()
        if (file.exists()) {
            pdfView.fromFile(file)
                .load()
        }
    }

    override fun onPageChanged(page: Int, pageCount: Int) {

    }

    override fun loadComplete(nbPages: Int) {

    }
}
