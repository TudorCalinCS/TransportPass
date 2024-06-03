package com.example.mypublictransportlogin


import Orar
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ShowRoute : AppCompatActivity() {
    private lateinit var orar :Orar
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_route)
        val imageview = findViewById<PhotoView>(R.id.RouteView)
        val backButton = findViewById<ImageButton>(R.id.backButtonSelectedRoute)

        backButton.setOnClickListener {
            val intent = Intent(this, BussLines::class.java)
            startActivity(intent)
            finish()
        }


        GlobalScope.launch(Dispatchers.Main) {
            orar = ConnectToServerViewModel.getInstance().getOrar()
            Log.d("SERVER", "ORARUL ESTE : $orar")

            val qrBitmap = orar.orar?.let { byteArrayToBitmap(it) }

            // Set the Bitmap as the source of the ImageView
            qrBitmap?.let { imageview.setImageBitmap(it) }
        }


    }
    private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return try {
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        } catch (e: Exception) {
            Log.e("BusPassQRCode", "Error converting byte array to bitmap: ${e.message}")
            null
        }
    }
}