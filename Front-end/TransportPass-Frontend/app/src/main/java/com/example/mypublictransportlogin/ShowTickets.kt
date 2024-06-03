package com.example.mypublictransportlogin

import Ticket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mypublictransportlogin.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowTickets : AppCompatActivity() {

    private lateinit var ticketContainer: LinearLayout
    private val generalTicketList: MutableList<View> = mutableListOf()
    private lateinit var ticketList: MutableList<Ticket>

    override fun onCreate(savedInstanceState: Bundle?) {
        val connectToServerViewModel = ConnectToServerViewModel.getInstance()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.mytickets)

        ticketContainer = findViewById(R.id.ticketContainer)

        lifecycleScope.launch(Dispatchers.Main) {
            connectToServerViewModel.TicketRequest()
            ticketList = connectToServerViewModel.getTicketList()

            //connectToServerViewModel.setTicketList()
            updateTickets(ticketList)
        }

        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Set OnClickListener for the back button to navigate to the MainActivity
        backButton.setOnClickListener {
            //connectToServerViewModel.resetTicketSize()
            val intent = Intent(this, ClientMain::class.java)
            startActivity(intent)
            finish() // Optional: finish SignUpActivity to remove it from the back stack
        }

    }

    private fun updateTickets(ticketList: List<Ticket>) {
        for (ticket in ticketList) {
            // Inflate GeneralTicket.xml
            val generalTicketView = layoutInflater.inflate(R.layout.general_ticket, null)

            // Check if the view is properly inflated
            if (generalTicketView == null) {
                Log.e("SERVER", "Failed to inflate general_ticket view")
                continue
            }

            // Configure and display ticket data in the inflated XML
            val titleTextView = generalTicketView.findViewById<TextView>(R.id.TYPE)
            titleTextView.text = ticket.tip

            val priceTextView = generalTicketView.findViewById<TextView>(R.id.PRICE)
            priceTextView.text = "Price: ${ticket.pret}0 RON"

            val validityTextView = generalTicketView.findViewById<TextView>(R.id.VALIDITY)
            validityTextView.text = "Validity: ${ticket.dataExpirare}"

            // Add the inflated XML to the container with layout parameters
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 0, 0, resources.getDimensionPixelSize(R.dimen.ticket_margin))

            generalTicketView.layoutParams = layoutParams

            generalTicketList.add(generalTicketView)
            ticketContainer.addView(generalTicketView)

            Log.d("SERVER", "Ticket view added for: ${ticket.tip}")
        }
        setListenersOnTickets()
    }


    private fun setListenersOnTickets() {
        Log.d("SERVER","COUNT ESTE : ${ticketContainer.childCount}")
        for (i in 0 until ticketContainer.childCount) {
            val child = ticketContainer.getChildAt(i)
            Log.d("SERVER","CHILD ESTE :  $child")
            val ticket = ticketList.getOrNull(i) // Get the corresponding ticket if available
            if (ticket != null) {
               // Log.d("SERVER","TICKET ESTE : $ticket")
                child.setOnClickListener {
                    Log.d("SERVER", "Setting click listener for ticket: ${ticket.tip}")
                    startBusTicketQRCodeActivity(ticket)
                    Log.d("SERVER", "Ticket clicked: ${ticket.tip}")
                }
            }
            else{
                Log.d("SERVER","NU EXISTA TICKET")
            }
        }
    }


    private fun startBusTicketQRCodeActivity(ticket: Ticket) {
        Log.d("SERVER", "IN startBusTicketQRCodeActivity")
        val intent = Intent(this, BusTicketQRCode::class.java)
        intent.putExtra("ticket", ticket)
        startActivity(intent)
    }
}
