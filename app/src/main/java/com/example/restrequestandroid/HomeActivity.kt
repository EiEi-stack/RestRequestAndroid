package com.example.restrequestandroid

    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.widget.Button

    class HomeActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_home)
            val btn_show_map = findViewById<Button>(R.id.btn_show_map)
            btn_show_map.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
