package com.example.mypublictransportlogin

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity

class BussLines: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bus_lines)
        val relativeLayout = findViewById<ScrollView>(R.id.main1)
        val animationDrawable = relativeLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(3500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()

        findViewById<Button>(R.id.linia).setOnClickListener {
            val intent1 = Intent(this, ShowRoute::class.java)
            startActivity(intent1)
        }

        // Find the back button
        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Set OnClickListener for the back button to navigate to the MainActivity
        backButton.setOnClickListener {
            val intent = Intent(this, ClientMain::class.java)
            startActivity(intent)
            finish() // Optional: finish SignUpActivity to remove it from the back stack
        }
    }
}