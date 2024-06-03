package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Payment : AppCompatActivity() {

    private lateinit var expiryDateEditText: EditText
    private lateinit var cardNumberEditText: EditText
    private lateinit var cvvEditText: EditText
    private lateinit var NameOnCard: EditText
    private lateinit var payButton: Button
    private var isNameOnCardValid = false
    private var isCardNumberValid = false
    private var isExpiryDateValid = false
    private var isCvvValid = false
    private lateinit var confirmationTextView: TextView
    private lateinit var confirmationNumberTextView: TextView
    private lateinit var confirmationDateTextView: TextView
    private lateinit var confirmationCvvTextView: TextView
    private lateinit var passType: String
    private var price: Double = -1.0
    private var x :Int =0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_activity)
        val connectToServerViewModel = ConnectToServerViewModel.getInstance()
        passType = intent.getStringExtra(SelectBox.EXTRA_PASS_TYPE_PASS) ?: ""
        price = intent.getDoubleExtra(SelectBox.EXTRA_PRICE_PASS, 0.0)
        Log.d("SERVER","PASS TYPE : $passType")
        Log.d("SERVER","PRICE: $price")
        if (passType.isEmpty() || price == -1.0) {
            passType = intent.getStringExtra(TicketsActivity.EXTRA_PASS_TYPE_TICKET) ?: ""
            price = intent.getDoubleExtra(TicketsActivity.EXTRA_PRICE_TICKET, 0.0)
            x=1
        }
        if(passType.isEmpty() || price==-1.0){
            passType=intent.getStringExtra(BusPassQRCode.EXTRA_UPDATE_TYPE) ?: ""
            price=intent.getDoubleExtra(BusPassQRCode.EXTRA_UPDATE_PRICE, 0.0)
            x=2
        }

        val closeButton: ImageButton = findViewById(R.id.closeButtonPass)
        closeButton.setOnClickListener {
            finish()
        }

        var totalPayText= findViewById<TextView>(R.id.TotalPayText)
        totalPayText.text = getString(R.string.total_to_pay, price)

        cardNumberEditText = findViewById(R.id.NumberOnCard)
        expiryDateEditText = findViewById(R.id.DateOnCard)
        cvvEditText = findViewById(R.id.CvvOnCard)
        NameOnCard = findViewById(R.id.NameOnCard) // Initialize NameOnCard EditText
        payButton = findViewById(R.id.PayButtonPayment)

        setupNameOnCardInput() // Call setup method for NameOnCard EditText
        setupExpiryDateInput()
        setupCvvInput()
        setupCardNumberInput()

        payButton.setOnClickListener {
            if (validateForm()) {
                when (x) {
                    0 -> {
                        connectToServerViewModel.buyPass(passType, price)
                        showConfirmationDialog()
                    }
                    1 -> {
                        connectToServerViewModel.buyTicket(passType, price)
                        connectToServerViewModel.resetTicketSize()
                        showConfirmationDialog()
                    }
                    else -> {
                        connectToServerViewModel.updatePass()
                        connectToServerViewModel.setAbonament()
                        showConfirmationDialog()
                    }
                }
            } else {
                Toast.makeText(this, "Please complete all the inputs!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupNameOnCardInput() {
        // Find the TextView by its id
        confirmationTextView = findViewById(R.id.confirmationNameTextView)

        NameOnCard.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                val nameOnCard = s.toString().trim()
                if (nameOnCard.isEmpty()) {
                    // Clear any existing confirmation message
                    confirmationTextView.visibility = View.GONE
                    // Set error message
                    NameOnCard.error = "Name on Card cannot be empty"
                    isNameOnCardValid = false
                } else {
                    // Clear error message
                    NameOnCard.error = null
                    // Set confirmation message
                    confirmationTextView.text = "Correct Format"
                    confirmationTextView.visibility = View.VISIBLE
                    isNameOnCardValid = true
                }
            }
        })
    }



    private fun setupCardNumberInput() {
        confirmationNumberTextView = findViewById(R.id.confirmationNumberTextView)
        cardNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val filtered = it.toString().filter { char -> char.isDigit() }
                    if (filtered != it.toString()) {
                        it.replace(0, it.length, filtered)
                    }

                    val cardNumber = filtered.trim()
                    if (cardNumber.length < 16) {
                        confirmationNumberTextView.visibility = View.GONE
                        cardNumberEditText.error = "Card number must be 16 digits!"
                        isCardNumberValid = false
                    } else if (cardNumber.length > 16) {
                        // Remove extra characters
                        cardNumberEditText.setText(cardNumber.substring(0, 16))
                        cardNumberEditText.setSelection(16) // Move cursor to the end
                    } else {
                        // Clear error message
                        cardNumberEditText.error = null
                        confirmationNumberTextView.text = "Correct Format"
                        confirmationNumberTextView.visibility = View.VISIBLE
                        // Set confirmation message
                        isCardNumberValid = true
                    }
                }
            }
        })
    }




    private fun setupExpiryDateInput() {
        confirmationDateTextView = findViewById(R.id.confirmationDateTextView)
        expiryDateEditText.addTextChangedListener(object : TextWatcher {
            private var current = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    var expiryDate = it.toString().trim().replace("/", "") // Remove any existing "/"
                    if (expiryDate.length > 7) {
                        expiryDate = expiryDate.substring(0, 7) // Truncate to 7 characters if longer
                    }

                    if (expiryDate.length >= 2 && expiryDate[1] != '/') {
                        // Automatically add "/" after the second character
                        expiryDate = expiryDate.substring(0, 2) + "/" + expiryDate.substring(2)
                    }

                    val firstTwoDigits = if (expiryDate.length >= 2) expiryDate.substring(0, 2) else ""
                    val lastFourDigits = expiryDate.takeLast(4)

                    if (firstTwoDigits.isNotEmpty() && lastFourDigits.isNotEmpty()) {
                        val isMonthValid = firstTwoDigits.toIntOrNull() in 1..12
                        val isYearValid = lastFourDigits.toIntOrNull() ?: 0 >= 2024

                        if (!isMonthValid) {
                            confirmationDateTextView.visibility=View.GONE
                            expiryDateEditText.error = "Invalid Month"
                            isExpiryDateValid=false
                        } else if (!isYearValid) {
                            confirmationDateTextView.visibility=View.GONE
                            expiryDateEditText.error = "Invalid Year"
                            isExpiryDateValid=false
                        } else {
                            confirmationDateTextView.text="Correct Format"
                            confirmationDateTextView.visibility = View.VISIBLE
                            isExpiryDateValid=true
                        }
                    }

                    current = expiryDate
                    expiryDateEditText.removeTextChangedListener(this)
                    expiryDateEditText.setText(current)
                    expiryDateEditText.setSelection(current.length)
                    expiryDateEditText.addTextChangedListener(this)
                }
            }

        })
    }


    private fun setupCvvInput() {
        confirmationCvvTextView = findViewById(R.id.confirmationCvvTextView)
        cvvEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val filtered = it.toString().filter { char -> char.isDigit() }
                    if (filtered != it.toString()) {
                        it.replace(0, it.length, filtered)
                    }

                    val cvv = filtered.trim()
                    if (cvv.length < 3) {
                        confirmationCvvTextView.visibility = View.GONE
                        cvvEditText.error = "CVV must be 3 digits"
                        isCvvValid = false
                    } else if (cvv.length > 3) {
                        // Remove extra characters
                        cvvEditText.setText(cvv.substring(0, 3))
                        cvvEditText.setSelection(3) // Move cursor to the end
                    } else {
                        // Clear any existing error message
                        cvvEditText.error = null
                        confirmationCvvTextView.text = "Correct format"
                        confirmationCvvTextView.visibility = View.VISIBLE
                        isCvvValid = true
                    }
                }
            }
        })
    }

    private fun validateForm(): Boolean {
        // Check if all input fields are valid
        return isNameOnCardValid && isCardNumberValid && isExpiryDateValid && isCvvValid
    }

    // Other methods...

    private fun showConfirmationDialog() {
        // Show confirmation dialog
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Payment Confirmation")
        dialogBuilder.setMessage("Payment was successful!")
        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            val intent = Intent(this, ClientMain::class.java)
            startActivity(intent)
            finish()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }


}
