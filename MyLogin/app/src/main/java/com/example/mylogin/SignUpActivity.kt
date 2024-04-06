package com.example.mylogin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {
    private var passwordEditText: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private var passwordIconImageView: ImageView? = null
    private var confirmPasswordIconImageView: ImageView? = null
    private var isPasswordVisible = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

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

        // Set OnClickListener for loginTextView to navigate back to the login activity
        findViewById<TextView>(R.id.loginTextView).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val signUpButton = findViewById<Button>(R.id.signUpButton)

        signUpButton.setOnClickListener {
            val intent = Intent(this, OTPActivity::class.java)
            startActivity(intent)
        }
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
