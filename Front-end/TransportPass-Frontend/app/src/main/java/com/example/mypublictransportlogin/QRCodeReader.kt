package com.example.mypublictransportlogin

import QRDetails
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.mypublictransportlogin.databinding.QrCodeReaderBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.launch

class QRCodeReader : AppCompatActivity() {
    private lateinit var qrcodedetails : QRDetails
    val connectToServerViewModel = ConnectToServerViewModel.getInstance()
    private val requestPermissionLauncher=
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted: Boolean ->
            if(isGranted){
                showCamera()
            }
            else{

            }
        }
    private val scanLauncher=
        registerForActivityResult(ScanContract()) {
                result: ScanIntentResult ->
            run {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                } else {


                    val contentInt: Int? = result.contents.toIntOrNull()

                    if (contentInt != null) {
                        connectToServerViewModel.InfoQR(contentInt)
                    }
                    lifecycleScope.launch {
                        qrcodedetails = connectToServerViewModel.getQRInfo()
                        setResult()
                        connectToServerViewModel.resetQRInfo()
                    }

                }
            }
        }
    private lateinit var binding: QrCodeReaderBinding

    private fun setResult(){

        binding.textResult.text="VALID!"
        binding.textResult1.text=qrcodedetails.dataExpirare
        binding.textResult2.text=qrcodedetails.nume
        binding.textResult3.text=qrcodedetails.tip
    }

    private fun showCamera() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan QR CODE")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(false)

        scanLauncher.launch(options)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initViews()

        val backButton = findViewById<ImageButton>(R.id.backButtonqr_scanner)

        // Set OnClickListener for the back button to navigate to the MainActivity
        backButton.setOnClickListener {
            connectToServerViewModel.setTipClient()
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            finish() // Optional: finish SignUpActivity to remove it from the back stack
        }
    }

    private fun initViews() {
        binding.fab.setOnClickListener{
            checkPermissionCamera(this)
        }
    }

    private fun checkPermissionCamera(context:Context) {
        if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            showCamera()
        }
        else if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
            Toast.makeText(context,"CAMERA permission requested", Toast.LENGTH_SHORT).show()
        }
        else{
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun initBinding() {
        binding = QrCodeReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}