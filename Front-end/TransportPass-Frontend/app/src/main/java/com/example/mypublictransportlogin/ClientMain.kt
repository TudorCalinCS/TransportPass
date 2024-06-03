package com.example.mypublictransportlogin

import AbonamentDetails
import Ticket
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar

class ClientMain : AppCompatActivity() {

    private lateinit var ticketList: MutableList<Ticket>
    private lateinit var dialog: Dialog
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val connectToServerViewModel = ConnectToServerViewModel.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.client_main)
        findViewById<androidx.cardview.widget.CardView>(R.id.Route).setOnClickListener {
            val intent1 = Intent(this, BussLines::class.java)
            startActivity(intent1)
        }

        findViewById<androidx.cardview.widget.CardView>(R.id.myPassCard).setOnClickListener {

            lifecycleScope.launch {
                connectToServerViewModel.show_pass()
                var ShowPassRespone = connectToServerViewModel.getShowPassResponse()
                connectToServerViewModel.setShowPassResponse()
                if(ShowPassRespone=="ErrorResponse"){
                    NoPassFound()
                }
                else{
                    val intent = Intent(this@ClientMain,BusPassQRCode::class.java)
                    startActivity(intent)
                }
            }

        }

        findViewById<androidx.cardview.widget.CardView>(R.id.BuyPassCard).setOnClickListener {
            connectToServerViewModel.Verify()
            lifecycleScope.launch{
                var response = connectToServerViewModel.getVerify()
                connectToServerViewModel.setPassExists()
                if(response=="INTENT"){
                    val intent11 = Intent(this@ClientMain, SelectBox::class.java)
                    startActivity(intent11)
                }
                else if(response =="DISCLAIMER") {
                    USER_ALREADY_HAS_PASS()
                }
            }

        }

        findViewById<androidx.cardview.widget.CardView>(R.id.BuyTicketButton).setOnClickListener {
            if(isTodayFriday()){
                GreenFriday()
            }
            else {
                val intent11 = Intent(this, TicketsActivity::class.java)
                startActivity(intent11)
            }
        }


        findViewById<androidx.cardview.widget.CardView>(R.id.ticketsCard).setOnClickListener {
            connectToServerViewModel.TicketRequest()
            lifecycleScope.launch(Dispatchers.Main) {
                ticketList = connectToServerViewModel.getTicketList()
                //connectToServerViewModel.resetTicketSize()
                if(ticketList.size==0){
                    NoTicketsFound()
                    connectToServerViewModel.resetTicketSize()
                }
                else{
                    val intent12 = Intent(this@ClientMain, ShowTickets::class.java)
                    startActivity(intent12)
                }
            }

        }

        findViewById<androidx.cardview.widget.CardView>(R.id.logoutCard).setOnClickListener {
            connectToServerViewModel.setTipClient()
            val intent12 = Intent(this, LogIn::class.java)
            startActivity(intent12)
        }

    }

    private fun NoTicketsFound(){
        dialog = Dialog(this)

        // Set the content view to the disclaimer layout
        dialog.setContentView(R.layout.noticketstoshow)

        // Find the OK button in the dialog layout
        val BuyTicketsDisclaimer = dialog.findViewById<Button>(R.id.BuyTicketsDisclaimer)

        val close = dialog.findViewById<ImageButton>(R.id.closeButtonTickets)

        // Set a click listener for the OK button to dismiss the dialog
        BuyTicketsDisclaimer.setOnClickListener {
            val intent5 = Intent(this@ClientMain, TicketsActivity::class.java)
            startActivity(intent5)
        }

        close.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog when OK is clicked
        }


        // Show the dialog
        dialog.show()
    }



    private fun NoPassFound(){
        dialog = Dialog(this)

        // Set the content view to the disclaimer layout
        dialog.setContentView(R.layout.nobuspasstoshow)

        // Find the OK button in the dialog layout
        val BuyTicketsDisclaimer = dialog.findViewById<Button>(R.id.buttonBuyPass)

        val close = dialog.findViewById<ImageButton>(R.id.closeButtonPass)

        // Set a click listener for the OK button to dismiss the dialog
        BuyTicketsDisclaimer.setOnClickListener {
            val intent5 = Intent(this@ClientMain, SelectBox::class.java)
            startActivity(intent5)
        }

        close.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog when OK is clicked
        }


        // Show the dialog
        dialog.show()
    }

    private fun isTodayFriday(): Boolean {
        // Get the instance of the Calendar
        val calendar = Calendar.getInstance()
        // Get the current day of the week
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        // Check if it is Friday
        return dayOfWeek == Calendar.FRIDAY
    }



    private fun GreenFriday(){
        dialog = Dialog(this)

        // Set the content view to the disclaimer layout
        dialog.setContentView(R.layout.green_friday)

        // Find the OK button in the dialog layout
        val gotIt = dialog.findViewById<Button>(R.id.gotitGreen)

        val close = dialog.findViewById<ImageButton>(R.id.closeButtonGreenFriday)

        // Set a click listener for the OK button to dismiss the dialog
        gotIt.setOnClickListener {
            dialog.dismiss()
        }

        close.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog when OK is clicked
        }


        // Show the dialog
        dialog.show()
    }


    private fun USER_ALREADY_HAS_PASS(){
        dialog = Dialog(this)

        // Set the content view to the disclaimer layout
        dialog.setContentView(R.layout.already_have_a_bus_pass)

        // Find the OK button in the dialog layout
        val gotIt = dialog.findViewById<Button>(R.id.okRed)

        val close = dialog.findViewById<ImageButton>(R.id.closeButtonfrobidden)

        // Set a click listener for the OK button to dismiss the dialog
        gotIt.setOnClickListener {
            dialog.dismiss()
        }

        close.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog when OK is clicked
        }


        // Show the dialog
        dialog.show()
    }

}