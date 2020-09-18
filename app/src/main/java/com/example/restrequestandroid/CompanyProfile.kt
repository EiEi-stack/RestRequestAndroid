package com.example.restrequestandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CompanyProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_profile)
        val imgSmartPhone = findViewById<ImageView>(R.id.img_smartPhone)
        val imgLocalPhone = findViewById<ImageView>(R.id.img_localPhone)
        val tvSmartPhone = findViewById<TextView>(R.id.tv_smartPhone)
        val tvLocalPhone = findViewById<TextView>(R.id.tv_localPhone)

        imgLocalPhone.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tvLocalPhone.text.toString()))
            startActivity(intent)
        })

        imgSmartPhone.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tvSmartPhone.text.toString()))
            startActivity(intent)
        })
    }


}
