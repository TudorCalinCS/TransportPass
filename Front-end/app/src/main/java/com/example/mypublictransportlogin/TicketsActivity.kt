package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TicketsActivity : AppCompatActivity() {

    private var selectedPassLayout: LinearLayout? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bus_ticket)

        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Set OnClickListener for the back button to navigate to the MainActivity
        backButton.setOnClickListener {
            val intent = Intent(this, ClientMain::class.java)
            startActivity(intent)
            finish() // Optional: finish SignUpActivity to remove it from the back stack
        }

        findViewById<LinearLayout>(R.id.firstpass)?.setOnClickListener {
            toggleBusPassSelection(findViewById(R.id.firstpass) as LinearLayout, "URBAN TICKET", "3.00 RON")
        }

        findViewById<LinearLayout>(R.id.thirdpass)?.setOnClickListener {
            toggleBusPassSelection(findViewById(R.id.thirdpass) as LinearLayout, "NIGHT TICKET", "5.00 RON")
        }

        findViewById<LinearLayout>(R.id.forthpass)?.setOnClickListener {
            toggleBusPassSelection(findViewById(R.id.forthpass) as LinearLayout, "ONE DAY TICKET", "20.00 RON")
        }

        findViewById<LinearLayout>(R.id.fifthpass)?.setOnClickListener {
            toggleBusPassSelection(findViewById(R.id.fifthpass) as LinearLayout, "ONE WEEK TICKET", "75.00 RON")
        }

        findViewById<LinearLayout>(R.id.secondpass)?.setOnClickListener {
            toggleBusPassSelection(findViewById(R.id.secondpass) as LinearLayout, "URBAN TICKET", "3.00 RON  30 minutes")
        }
    }

    private fun onBusPassSelected(layout: LinearLayout, passType: String, price: String) {
        // Change background color of the selected bus pass layout
        layout.setBackgroundResource(R.drawable.bus_pass_background_change) // Set to gold color

        // Optionally, you can show a Toast message indicating the selected pass
        Toast.makeText(this, "$passType selected", Toast.LENGTH_SHORT).show()
        selectedPassLayout = layout // Update selected layout
    }

    private fun deselectBusPass(layout: LinearLayout) {
        // Change background color of the selected bus pass layout
        layout.setBackgroundResource(R.drawable.white_background) // Set to original color

        // Optionally, you can show a Toast message indicating the deselected pass
        val passType = when (layout.id) {
            R.id.firstpass -> "URBAN PASS"
            R.id.secondpass -> "METROPOLITAN PASS"
            R.id.thirdpass -> "INTRA-COMMUNAL PASS"
            R.id.forthpass -> "INTER-COMMUNAL PASS"
            else -> "Unknown Pass"
        }
//        Toast.makeText(this, "$passType unselected", Toast.LENGTH_SHORT).show()
        selectedPassLayout = null // Clear selected layout
    }

    private fun toggleBusPassSelection(layout: LinearLayout, passType: String, price: String) {
        if (layout == selectedPassLayout) {
            // If the clicked layout is already selected, deselect it
            deselectBusPass(layout)
        } else {
            // Deselect the currently selected pass (if any)
            selectedPassLayout?.let { deselectBusPass(it) }

            // Select the new pass
            onBusPassSelected(layout, passType, price)
        }
    }
}
