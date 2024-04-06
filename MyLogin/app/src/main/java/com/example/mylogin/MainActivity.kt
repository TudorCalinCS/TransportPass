package com.example.mylogin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var passwordEditText: EditText? = null
    private var keyIconImageView: ImageView? = null
    private var isPasswordVisible = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        passwordEditText = findViewById(R.id.passwordEditText)
        keyIconImageView = findViewById(R.id.keyIconImageView)

        // Ensure keyIconImageView is not null before setting OnClickListener
        keyIconImageView?.setOnClickListener {
            togglePasswordVisibility()
        }

        // Set click listener for createAccountTextView
        findViewById<TextView>(R.id.createAccountTextView).setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun togglePasswordVisibility() {
        if (!isPasswordVisible) {
            // Show password
            passwordEditText!!.transformationMethod = HideReturnsTransformationMethod.getInstance()
            keyIconImageView!!.setImageResource(R.drawable.key_icon)
            isPasswordVisible = true
        } else {
            // Hide password
            passwordEditText!!.transformationMethod = PasswordTransformationMethod.getInstance()
            keyIconImageView!!.setImageResource(R.drawable.baseline_key_off_24)
            isPasswordVisible = false
        }

        // Move cursor to the end of the password
        passwordEditText!!.setSelection(passwordEditText!!.text.length)
    }
}
