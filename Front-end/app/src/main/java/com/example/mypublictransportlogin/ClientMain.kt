package com.example.mypublictransportlogin

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class ClientMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.client_main)
        val relativeLayout = findViewById<RelativeLayout>(R.id.main)
        val animationDrawable = relativeLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(3500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()

        findViewById<ImageButton>(R.id.Route).setOnClickListener {
            val intent1 = Intent(this, BussLines::class.java)
            startActivity(intent1)
        }

        findViewById<Button>(R.id.BuyPassButton).setOnClickListener {
            val intent11 = Intent(this, SelectBox::class.java)
            startActivity(intent11)
        }

        findViewById<Button>(R.id.BuyTicketButton).setOnClickListener {
            val intent11 = Intent(this, TicketsActivity::class.java)
            startActivity(intent11)
        }
    }
}