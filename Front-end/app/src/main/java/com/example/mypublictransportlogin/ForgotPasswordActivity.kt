package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity: AppCompatActivity() {
    private var passwordEditText: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private var passwordIconImageView: ImageView? = null
    private var confirmPasswordIconImageView: ImageView? = null
    private var isPasswordVisible = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Set OnClickListener for the back button to navigate to the MainActivity
        backButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish() // Optional: finish SignUpActivity to remove it from the back stack
        }

        // Initialize views
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        passwordIconImageView = findViewById(R.id.passwordIconImageView)
        confirmPasswordIconImageView = findViewById(R.id.confirmPasswordIconImageView)

        // Set OnClickListener for passwordIconImageView to toggle password visibility
        passwordIconImageView?.setOnClickListener {
            togglePasswordVisibility()
        }

        // Set OnClickListener for confirmPasswordIconImageView to toggle confirm password visibility
        confirmPasswordIconImageView?.setOnClickListener {
            toggleConfirmPasswordVisibility()
        }


        val relativeLayout = findViewById<RelativeLayout>(R.id.main)
        val animationDrawable = relativeLayout.background as AnimationDrawable

        animationDrawable.setEnterFadeDuration(3500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()


    }

    private fun togglePasswordVisibility() {
        if (!isPasswordVisible) {
            // Show password
            passwordEditText!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
            passwordIconImageView!!.setImageResource(R.drawable.key_icon)
            isPasswordVisible = true
        } else {
            // Hide password
            passwordEditText!!.transformationMethod = PasswordTransformationMethod.getInstance()
            passwordIconImageView!!.setImageResource(R.drawable.baseline_key_off_24)
            isPasswordVisible = false
        }

        // Move cursor to the end of the password
        passwordEditText!!.setSelection(passwordEditText!!.text.length)
    }

    private fun toggleConfirmPasswordVisibility() {
        if (!isPasswordVisible) {
            // Show password
            confirmPasswordEditText!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
            confirmPasswordIconImageView!!.setImageResource(R.drawable.key_icon)
            isPasswordVisible = true
        } else {
            // Hide password
            confirmPasswordEditText!!.transformationMethod = PasswordTransformationMethod.getInstance()
            confirmPasswordIconImageView!!.setImageResource(R.drawable.baseline_key_off_24)
            isPasswordVisible = false
        }

        // Move cursor to the end of the password
        confirmPasswordEditText!!.setSelection(confirmPasswordEditText!!.text.length)
    }
}