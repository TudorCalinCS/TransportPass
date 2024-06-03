package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.button.MaterialButton
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ConnectToServerViewModel
import android.app.Dialog
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class SignUpActivity : AppCompatActivity() {
    private var passwordEditText: EditText? = null
    private var firstName: EditText? = null
    private var lastName: EditText? = null
    private var cnp: EditText? = null
    private var email: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private var passwordIconImageView: ImageView? = null
    private var confirmPasswordIconImageView: ImageView? = null
    private var isPasswordVisible = false
    private var isValidItemSelected = false
    private lateinit var statutAutoComplete: AutoCompleteTextView
    private lateinit var adapterItems: ArrayAdapter<String>
    private lateinit var dialog: Dialog
    private lateinit var chooseFilesButton: Button
    private lateinit var photo : ByteArray


    companion object {
        private const val REQUEST_CHOOSE_PHOTO = 2
    }

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("SERVER","SUNTEM IN SIGNUP")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //chooseFilesButton = findViewById(R.id.ChooseYourFiles)

        val backButton = findViewById<ImageButton>(R.id.backButtonsignup)
        backButton.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish()
        }

        val items = arrayOf("Pupil", "Student", "Pensioner", "-")
        statutAutoComplete = findViewById(R.id.auto_complete_txt)
        adapterItems = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        statutAutoComplete.setAdapter(adapterItems)


        // Set input type to null to prevent keyboard from showing
        statutAutoComplete.inputType = 0

        // Always show the dropdown when touching the AutoCompleteTextView
        statutAutoComplete.setOnTouchListener { _, _ ->
            statutAutoComplete.showDropDown()
            hideKeyboard(statutAutoComplete)
            false
        }

        // Always show the dropdown when the AutoCompleteTextView gains focus
        statutAutoComplete.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                statutAutoComplete.showDropDown()
                hideKeyboard(view)
            }
        }

        statutAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            statutAutoComplete.setText(selectedItem, false)
            Toast.makeText(this@SignUpActivity, "Item $selectedItem selected", Toast.LENGTH_SHORT).show()
            chooseFilesButton.isEnabled = selectedItem == "Student"
        }

        chooseFilesButton = findViewById(R.id.ChooseYourFiles)
        // Define the list of items

        // Initialize the AutoCompleteTextView
        statutAutoComplete = findViewById(R.id.auto_complete_txt)

        // Initialize the ArrayAdapter with the list of items
        adapterItems = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)

        // Set the adapter to the AutoCompleteTextView
        statutAutoComplete.setAdapter(adapterItems)

        val connectToServerViewModel = ConnectToServerViewModel.getInstance()

        firstName = findViewById(R.id.firstNameEditText)
        lastName = findViewById(R.id.lastNameEditText)
        email = findViewById(R.id.emailEditText)
        cnp = findViewById(R.id.ssnEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        passwordIconImageView = findViewById(R.id.passwordIconImageView)
        confirmPasswordIconImageView = findViewById(R.id.confirmPasswordIconImageView)

        findViewById<MaterialButton>(R.id.signUpButton).setOnClickListener {
            // Extract text safely, handling nulls correctly and trimming spaces
            val firstNameText =
                findViewById<TextInputEditText>(R.id.firstNameEditText)?.text?.toString()
                    ?.trim() ?: ""
            val lastNameText =
                findViewById<TextInputEditText>(R.id.lastNameEditText)?.text?.toString()?.trim()
                    ?: ""
            val emailText =
                findViewById<TextInputEditText>(R.id.emailEditText)?.text?.toString()?.trim()
                    ?: ""
            val cnpText =
                findViewById<TextInputEditText>(R.id.ssnEditText)?.text?.toString()?.trim()
                    ?: ""
            val passwordText =
                findViewById<TextInputEditText>(R.id.passwordEditText)?.text?.toString()?.trim()
                    ?: ""
            val statut =
                findViewById<AutoCompleteTextView>(R.id.auto_complete_txt)?.text?.toString()
                    ?.trim() ?: ""

            // Debug log for each field to check their actual content
            Log.d("SERVER", "First Name: '$firstNameText'")
            Log.d("SERVER", "Last Name: '$lastNameText'")
            Log.d("SERVER", "Email: '$emailText'")
            Log.d("SERVER", "CNP: '$cnpText'")
            Log.d("SERVER", "Password: '$passwordText'")
            Log.d("SERVER", "Statut: '$statut'")

            // Check if any field is empty and show dialog if true
            if (firstNameText.isEmpty() || lastNameText.isEmpty() || emailText.isEmpty() || cnpText.isEmpty() || passwordText.isEmpty() || statut.isEmpty())
            {
                EMPTY_FIELDS()
            }
            else if(statut=="Student" && (!::photo.isInitialized ||firstNameText.isEmpty() || lastNameText.isEmpty() || emailText.isEmpty() || cnpText.isEmpty() || passwordText.isEmpty() ))
            {
                    EMPTY_FIELDS()

            } else {
                if (statut == "Student") {
                    lifecycleScope.launch {
                        var nr=0
                        while(!::photo.isInitialized){
                            delay(1000)
                            Log.d("SERVER","SUNTEM IN PHOTO DELAY $nr")
                            nr++
                        }
                        connectToServerViewModel.signup(
                            firstNameText,
                            lastNameText,
                            emailText,
                            cnpText,
                            passwordText,
                            statut,
                            photo
                        )
                        lifecycleScope.launch {
                            var response = connectToServerViewModel.getSignupResponse()
                            Log.d("SERVER", "SIGNUP RESPONSE ESTE : $response")
                            when (response) {
                                "ErrorResponse" -> ALREADY_HAVE_AN_ACCOUNT()
                                "OkResponse" -> CREATED_ACCOUNT()
                                else -> NOT_STUDENT()
                            }
                            connectToServerViewModel.setSignupResponse()
                        }
                    }
                }
                else{
                    connectToServerViewModel.signup(
                        firstNameText,
                        lastNameText,
                        emailText,
                        cnpText,
                        passwordText,
                        statut,
                        byteArrayOf()
                    )
                    lifecycleScope.launch {
                        var response = connectToServerViewModel.getSignupResponse()
                        Log.d("SERVER", "SIGNUP RESPONSE ESTE : $response")
                        when (response) {
                            "ErrorResponse" -> ALREADY_HAVE_AN_ACCOUNT()
                            "OkResponse" -> CREATED_ACCOUNT()
                            else -> NOT_STUDENT()
                        }
                        connectToServerViewModel.setSignupResponse()
                    }
                }

            }
        }

        passwordIconImageView?.setOnClickListener {
            togglePasswordVisibility()
        }

        confirmPasswordIconImageView?.setOnClickListener {
            toggleConfirmPasswordVisibility()
        }

        findViewById<TextView>(R.id.loginTextView).setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
        }

        val relativeLayout = findViewById<RelativeLayout>(R.id.main)
        val animationDrawable = relativeLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(3500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()

        chooseFilesButton.setOnClickListener {
            openGallery()
        }
        chooseFilesButton.isEnabled = false // Initially disable the button
    }



    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

        private fun togglePasswordVisibility() {
            val transformationMethod = if (!isPasswordVisible) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            passwordEditText?.transformationMethod = transformationMethod
            passwordIconImageView?.setImageResource(if (!isPasswordVisible) R.drawable.key_icon else R.drawable.baseline_key_off_24)
            isPasswordVisible = !isPasswordVisible
            passwordEditText?.setSelection(passwordEditText?.text?.length ?: 0)
        }

        private fun toggleConfirmPasswordVisibility() {
            val transformationMethod = if (!isPasswordVisible) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            confirmPasswordEditText?.transformationMethod = transformationMethod
            confirmPasswordIconImageView?.setImageResource(if (!isPasswordVisible) R.drawable.key_icon else R.drawable.baseline_key_off_24)
            isPasswordVisible = !isPasswordVisible
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


        private fun ALREADY_HAVE_AN_ACCOUNT() {
            // Create a dialog instance
            dialog = Dialog(this)

            // Set the content view to the disclaimer layout
            dialog.setContentView(R.layout.already_have_an_account)

            // Find the OK button in the dialog layout
            val okButton = dialog.findViewById<Button>(R.id.okButton)

            val close = dialog.findViewById<ImageButton>(R.id.closeButton2)

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

        private fun CREATED_ACCOUNT(){
            dialog = Dialog(this)

            // Set the content view to the disclaimer layout
            dialog.setContentView(R.layout.created_account_disclaimer)

            // Find the OK button in the dialog layout
            val loginButtonAccountCreated = dialog.findViewById<Button>(R.id.loginButtonAccountCreated)

            val close = dialog.findViewById<ImageButton>(R.id.closeButtonAccountCreated)

            // Set a click listener for the OK button to dismiss the dialog
            loginButtonAccountCreated.setOnClickListener {
                val intent = Intent(this, LogIn::class.java)
                startActivity(intent)
            }

            close.setOnClickListener {
                dialog.dismiss() // Dismiss the dialog when OK is clicked
            }


            // Show the dialog
            dialog.show()
        }




    private fun NOT_STUDENT(){
        dialog = Dialog(this)

        // Set the content view to the disclaimer layout
        dialog.setContentView(R.layout.student_not_found)

        // Find the OK button in the dialog layout
        val loginButtonAccountCreated = dialog.findViewById<Button>(R.id.okReminder)

        val close = dialog.findViewById<ImageButton>(R.id.closeButtonDataFailed)

        // Set a click listener for the OK button to dismiss the dialog
        loginButtonAccountCreated.setOnClickListener {
            dialog.dismiss()
        }

        close.setOnClickListener {
            dialog.dismiss() // Dismiss the dialog when OK is clicked
        }


        // Show the dialog
        dialog.show()
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CHOOSE_PHOTO -> {
                    data?.data?.also { uri ->
                        // Handle the image selected from the gallery here
                        Toast.makeText(this, "Photo selected successfully", Toast.LENGTH_SHORT).show()
                        lifecycleScope.launch {
                            saveImageToAppStorage(uri)
                        }
                    }
                }
            }
        }
    }

    private suspend fun saveImageToAppStorage(imageUri: Uri) {
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            if (inputStream == null) {
                Toast.makeText(this, "Failed to open image stream", Toast.LENGTH_SHORT).show()
                return
            }
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val file = File(getExternalFilesDir(null), "IMG_${timeStamp}.jpg")

            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }  // Automatically closes both streams

            photo = getFileAsByteArray(file) // Now read the file
            Log.d("SERVER","PHOTO ESTE $photo")


            Toast.makeText(this, "Photo saved successfully: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("SERVER", "Failed to save photo", e)
            Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show()
        }
    }




    fun getFileAsByteArray(file: File): ByteArray {
        val byteArray: ByteArray
        FileInputStream(file).use { inputStream ->
            ByteArrayOutputStream().use { output ->
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } != -1) {
                    output.write(buffer, 0, length)
                }
                byteArray = output.toByteArray()
            }
        }
        return byteArray
    }

    fun byteArrayToString(byteArray: ByteArray): String {
        return byteArray.joinToString(",", prefix = "[", postfix = "]") { it.toString() }
    }
}


