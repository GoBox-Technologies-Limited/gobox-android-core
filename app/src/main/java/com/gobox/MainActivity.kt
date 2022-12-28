package com.gobox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gobox.core.ui.GoButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        findViewById<GoButton>(R.id.btn_test).text = "協助: 91836254"
    }
}