package com.example.mypublictransportlogin

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class ShowRoute : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_route)
        val relativeLayout = findViewById<RelativeLayout>(R.id.mainRoute)
        val animationDrawable = relativeLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(3500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()
    }
}