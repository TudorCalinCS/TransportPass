package com.example.mypublictransportlogin

import Ticket
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BusTicketQRCode : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.busticket_qrcode_display)
        val ticket = intent.getParcelableExtra<Ticket>("ticket") as Ticket
        if (ticket != null) {
            Log.d("SERVER", "Received ticket: ${ticket.tip}, ${ticket.pret}, ${ticket.dataExpirare}")
            // Use the ticket details to generate the QR code or any other operations
        } else {
            Log.e("SERVER", "No ticket data received!")
        }

        val textTipTextView = findViewById<TextView>(R.id.Type)
        val textDataIncepere = findViewById<TextView>(R.id.PURCHASEDATE)
        val textDataExpirare = findViewById<TextView>(R.id.EXPIRYDATE)
        val imageview = findViewById<ImageView>(R.id.qrCodeImageViewTICKET)


        val passTypeString = getString(R.string.pass_type, ticket.tip)
        textTipTextView.text = passTypeString

        val validSinceString = getString(R.string.valid_since, ticket.dataIncepere)
        textDataIncepere.text = validSinceString

        val validUntilString = getString(R.string.valid_until, ticket.dataExpirare)
        textDataExpirare.text = validUntilString
        val qrBitmap = ticket.qr?.let { byteArrayToBitmap(it) }

        // Set the Bitmap as the source of the ImageView
        qrBitmap?.let { imageview.setImageBitmap(it) }

        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Set OnClickListener for the back button to navigate to the MainActivity
        backButton.setOnClickListener {
            val intent = Intent(this, ClientMain::class.java)
            startActivity(intent)
            finish() // Optional: finish SignUpActivity to remove it from the back stack
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