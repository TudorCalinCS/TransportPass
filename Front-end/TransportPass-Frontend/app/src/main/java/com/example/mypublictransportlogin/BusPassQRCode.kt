package com.example.mypublictransportlogin

import AbonamentDetails
import android.annotation.SuppressLint
import android.app.Dialog
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

class BusPassQRCode : AppCompatActivity() {
    private lateinit var abonament: AbonamentDetails
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private lateinit var dialog: Dialog

    companion object {
        const val EXTRA_UPDATE_TYPE = "com.example.mypublictransportlogin.UPDATE_TYPE"
        const val EXTRA_UPDATE_PRICE = "com.example.mypublictransportlogin.UPDATE_PRICE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buspass_qrcode_display)
        Log.d("SERVER", "IN BUSPASSQRCODE")

        val textTipTextView = findViewById<TextView>(R.id.TextTip)
        val textDataIncepere = findViewById<TextView>(R.id.DataIncepere)
        val textDataExpirare = findViewById<TextView>(R.id.DataExpirare)
        val imageview = findViewById<ImageView>(R.id.qrCodeImageView)

        GlobalScope.launch(Dispatchers.Main) {
            abonament = ConnectToServerViewModel.getInstance().getAbonamentDetails()
            Log.d("SERVER", "ABONAMENTUL ESTE : $abonament")

            val passTypeString = getString(R.string.pass_type, abonament.tip)
            textTipTextView.text = passTypeString

            val validSinceString = getString(R.string.valid_since, abonament.dataIncepere)
            textDataIncepere.text = validSinceString

            val validUntilString = getString(R.string.valid_until, abonament.dataExpirare)
            textDataExpirare.text = validUntilString
            val qrBitmap = abonament.qr?.let { byteArrayToBitmap(it) }

            // Set the Bitmap as the source of the ImageView
            qrBitmap?.let { imageview.setImageBitmap(it) }


            val currentDate = Calendar.getInstance().time
            val expirationDate = dateFormat.parse(abonament.dataExpirare)
            expirationDate?.let {
                val diff = it.time - currentDate.time
                val daysLeft = diff / (1000 * 60 * 60 * 24)
                if (daysLeft in 0..3) {
                    showReminderDialog()
                }
            }
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Set OnClickListener for the back button to navigate to the MainActivity
        backButton.setOnClickListener {
            val intent = Intent(this, ClientMain::class.java)
            startActivity(intent)
            finish() // Optional: finish SignUpActivity to remove it from the back stack
        }


    }

    private fun showReminderDialog() {
        dialog = Dialog(this)

        // Set the content view to the disclaimer layout
        dialog.setContentView(R.layout.three_days_left_reminder)

        // Find the OK button in the dialog layout
        val okButton = dialog.findViewById<Button>(R.id.okReminder)

        val renewButton = dialog.findViewById<Button>(R.id.buyFromReminder)

        val close = dialog.findViewById<ImageButton>(R.id.closeButtonReminder)

        // Set a click listener for the OK button to dismiss the dialog
        okButton.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog when OK is clicked
        }

        close.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog when OK is clicked
        }

        renewButton.setOnClickListener {
            val intent19 = Intent(this, Payment::class.java).apply {
                putExtra(EXTRA_UPDATE_TYPE, abonament.tip)
                putExtra(EXTRA_UPDATE_PRICE, abonament.pret)
            }
            startActivity(intent19)
        }


        // Show the dialog
        dialog.show()
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

