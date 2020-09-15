package com.example.restrequestandroid

import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CompanyProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_profile)
        val typeFace = Typeface.createFromAsset(assets,"font/Bonitto_Slant_DEMO.otf")
    }
}
