package com.example.mylogin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OTPActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)

        val verifyButton = findViewById<Button>(R.id.verifyButton)
        val codeEditText1 = findViewById<EditText>(R.id.codeEditText1)
        val codeEditText2 = findViewById<EditText>(R.id.codeEditText2)
        val codeEditText3 = findViewById<EditText>(R.id.codeEditText3)
        val codeEditText4 = findViewById<EditText>(R.id.codeEditText4)

        // Add text change listeners to move focus automatically
        codeEditText1.addTextChangedListener(CodeTextWatcher(codeEditText1, codeEditText2))
        codeEditText2.addTextChangedListener(CodeTextWatcher(codeEditText2, codeEditText3))
        codeEditText3.addTextChangedListener(CodeTextWatcher(codeEditText3, codeEditText4))

        verifyButton.setOnClickListener {
            val code1 = codeEditText1.text.toString()
            val code2 = codeEditText2.text.toString()
            val code3 = codeEditText3.text.toString()
            val code4 = codeEditText4.text.toString()

            val verificationCode = "$code1$code2$code3$code4"

            // Example: Check if the verification code is valid (you would replace this with your own logic)
            if (verificationCode == "1234") {
                // Verification successful
                Toast.makeText(this, "Verification successful!", Toast.LENGTH_SHORT).show()
            } else {
                // Invalid verification code
                Toast.makeText(this, "Invalid verification code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // TextWatcher to move focus to next EditText when a character is entered
    private class CodeTextWatcher(
        private val currentEditText: EditText,
        private val nextEditText: EditText?
    ) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s.toString().isNotEmpty() && nextEditText != null) {
                nextEditText.requestFocus()
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // No implementation needed
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // No implementation needed
        }
    }
}
