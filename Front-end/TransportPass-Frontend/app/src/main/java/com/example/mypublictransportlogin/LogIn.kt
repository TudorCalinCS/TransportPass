package com.example.mypublictransportlogin

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import ConnectToServerViewModel
import android.app.Dialog
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class LogIn : AppCompatActivity() {
    private lateinit var passwordEditText: EditText
    private lateinit var keyIconImageView: ImageView
    private var isPasswordVisible = false
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find views
        passwordEditText = findViewById(R.id.passwordEditText)
        keyIconImageView = findViewById(R.id.keyIconImageView)

        // Initially hide the password
        passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        keyIconImageView.setImageResource(R.drawable.baseline_key_off_24)

        // Set OnClickListener for keyIconImageView to toggle password visibility
        keyIconImageView.setOnClickListener {
            togglePasswordVisibility()
        }

        // Set OnClickListener for createAccount TextView
        findViewById<TextView>(R.id.createAccount).setOnClickListener {
            Log.d("SERVER","CREATE ACCOUNT DIN LOGIN")
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.forgotPasswordTextView).setOnClickListener {
            val intent4 = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent4)
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.LOGIN).setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditText).text.toString()
            if (email == "" || password == "") {
                EMPTY_FIELDS()
            } else {
                val connectToServerViewModel = ConnectToServerViewModel.getInstance()
                lifecycleScope.launch {
                    connectToServerViewModel.connectToServer()
                    connectToServerViewModel.login(email, password)
                    val tip_client = connectToServerViewModel.getTipClient()
                    Log.d("SERVER", "TIP CLIENT : $tip_client")
                    when (tip_client) {
                        "CLIENT" -> {
                            Log.d("SERVER", "ESTE CLIENT DESCHIDEM MAIN")
                            val intent5 = Intent(this@LogIn, ClientMain::class.java)
                            startActivity(intent5)
                        }

                        "STUDENT" -> {
                            Log.d("SERVER", "ESTE STUDENT DESCHIDEM MAIN")
                            val intent5 = Intent(this@LogIn, ClientMain::class.java)
                            startActivity(intent5)
                        }

                        "CONTROLOR" -> {
                            Log.d("SERVER", "ESTE CONTROLOR DESCHIDEM QR")
                            val intent5 = Intent(this@LogIn, QRCodeReader::class.java)
                            startActivity(intent5)
                        }

                        else -> {
                            showDisclaimerDialogACCOUNT_NOT_FOUND()
                            connectToServerViewModel.setTipClient()
                        }
                    }
                }
            }

        }

        // Apply window insets to handle system UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Start animation
        val relativeLayout = findViewById<RelativeLayout>(R.id.main)
        val animationDrawable = relativeLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(3500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()
    }

    private fun EMPTY_FIELDS() {
        // Create a dialog instance
        dialog = Dialog(this)

        // Set the content view to the disclaimer layout
        dialog.setContentView(R.layout.fill_all_fields_disclaimer)

        // Find the OK button in the dialog layout
        val okButton = dialog.findViewById<Button>(R.id.okButton)

        val close = dialog.findViewById<ImageButton>(R.id.closeButtonPass)

        // Set a click listener for the OK button to dismiss the dialog
        okButton.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog when OK is clicked
        }

        close.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog when OK is clicked
        }


        // Show the dialog
        dialog.show()
    }

    private fun showDisclaimerDialogACCOUNT_NOT_FOUND() {
        // Create a dialog instance
        dialog = Dialog(this)

        // Set the content view to the disclaimer layout
        dialog.setContentView(R.layout.acces_denied)

        // Find the OK button in the dialog layout
        val okButton = dialog.findViewById<Button>(R.id.buttonGotIt1)

        val register = dialog.findViewById<Button>(R.id.buttonregister1)

        val close = dialog.findViewById<ImageButton>(R.id.closeButtonPasswordUpdate)

        close.setOnClickListener{
            dialog.dismiss()
        }

        // Set a click listener for the OK button to dismiss the dialog
        okButton.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog when OK is clicked
        }

        register.setOnClickListener {
            val intent5 = Intent(this@LogIn, SignUpActivity::class.java)
            startActivity(intent5)
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            keyIconImageView.setImageResource(R.drawable.baseline_key_off_24)
        } else {
            passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            keyIconImageView.setImageResource(R.drawable.key_icon)
        }
        isPasswordVisible = !isPasswordVisible
        passwordEditText.setSelection(passwordEditText.text.length)
    }
}
