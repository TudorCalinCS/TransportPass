package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {
    private var confirmPasswordEditText: EditText? = null
    private var confirmPasswordIconImageView: ImageView? = null
    private var isConfirmPasswordVisible = false // Initially set to invisible
    private lateinit var dialog: Dialog

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val connectToServerViewModel = ConnectToServerViewModel.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<MaterialButton>(R.id.UpdatePassword).setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = findViewById<EditText>(R.id.confirmPasswordEditText).text.toString()
            if(email=="" || password=="")
            {
                EMPTY_FIELDS()
            }
            else{
                connectToServerViewModel.connectToServer()
                connectToServerViewModel.updatePassword(email,password)
                lifecycleScope.launch {
                    val response = connectToServerViewModel.getUpdatePasswordResponse()
                    if(response == "OkResponse"){
                        DONE()
                    }
                    else{
                        FAILED()
                    }
                    connectToServerViewModel.set_UpdatePasswordResponse()
                }
            }
        }


        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        confirmPasswordIconImageView = findViewById(R.id.confirmPasswordIconImageView)

        // Set initial transformation method to PasswordTransformationMethod (invisible)
        confirmPasswordEditText?.transformationMethod = PasswordTransformationMethod.getInstance()
        confirmPasswordIconImageView?.setImageResource(R.drawable.baseline_key_off_24)

        confirmPasswordIconImageView?.setOnClickListener {
            toggleConfirmPasswordVisibility()
        }

        val relativeLayout = findViewById<RelativeLayout>(R.id.main)
        val animationDrawable = relativeLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(3500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            confirmPasswordEditText?.transformationMethod = PasswordTransformationMethod.getInstance()
            confirmPasswordIconImageView?.setImageResource(R.drawable.baseline_key_off_24)
        } else {
            confirmPasswordEditText?.transformationMethod = HideReturnsTransformationMethod.getInstance()
            confirmPasswordIconImageView?.setImageResource(R.drawable.key_icon)
        }
        isConfirmPasswordVisible = !isConfirmPasswordVisible
        confirmPasswordEditText?.setSelection(confirmPasswordEditText?.text?.length ?: 0)
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


    private fun DONE(){
        dialog = Dialog(this)
        // Set the content view to the disclaimer layout
        dialog.setContentView(R.layout.password_update_done)

        // Find the OK button in the dialog layout
        val okButton = dialog.findViewById<Button>(R.id.okUpdatePassword)

        val close = dialog.findViewById<ImageButton>(R.id.closeButtonPasswordUpdate)

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


    private fun FAILED(){
        dialog = Dialog(this)
        // Set the content view to the disclaimer layout
        dialog.setContentView(R.layout.password_update_failed)

        // Find the OK button in the dialog layout
        val okButton = dialog.findViewById<Button>(R.id.okFailedUpdatePassword)

        val close = dialog.findViewById<ImageButton>(R.id.closeButtonFailedPasswordUpdate)

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
}
