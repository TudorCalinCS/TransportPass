package com.example.mypublictransportlogin

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity

class BussLines: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bus_lines)


        val connectToServerViewModel = ConnectToServerViewModel.getInstance()

        findViewById<Button>(R.id.linia1).setOnClickListener {
            connectToServerViewModel.get_orar("1")
            val intent1 = Intent(this, ShowRoute::class.java)
            startActivity(intent1)
        }

        findViewById<Button>(R.id.linia2).setOnClickListener {
            connectToServerViewModel.get_orar("2")
            val intent1 = Intent(this, ShowRoute::class.java)
            startActivity(intent1)
        }

        findViewById<Button>(R.id.linia3).setOnClickListener {
            connectToServerViewModel.get_orar("3")
            val intent1 = Intent(this, ShowRoute::class.java)
            startActivity(intent1)
        }

        findViewById<Button>(R.id.linia4).setOnClickListener {
            connectToServerViewModel.get_orar("4")
            val intent1 = Intent(this, ShowRoute::class.java)
            startActivity(intent1)
        }


        findViewById<Button>(R.id.linia5).setOnClickListener {
            connectToServerViewModel.get_orar("5")
            val intent1 = Intent(this, ShowRoute::class.java)
            startActivity(intent1)
        }

        findViewById<Button>(R.id.linia6).setOnClickListener {
            connectToServerViewModel.get_orar("6")
            val intent1 = Intent(this, ShowRoute::class.java)
            startActivity(intent1)
        }

        findViewById<Button>(R.id.linia7).setOnClickListener {
            connectToServerViewModel.get_orar("7")
            val intent1 = Intent(this, ShowRoute::class.java)
            startActivity(intent1)
        }

        findViewById<Button>(R.id.linia8).setOnClickListener {
            connectToServerViewModel.get_orar("8")
            val intent1 = Intent(this, ShowRoute::class.java)
            startActivity(intent1)
        }

        findViewById<Button>(R.id.linia8l).setOnClickListener {
            connectToServerViewModel.get_orar("8L")
            val intent1 = Intent(this, ShowRoute::class.java)
            startActivity(intent1)
        }


        findViewById<Button>(R.id.linia9).setOnClickListener {
            connectToServerViewModel.get_orar("9")
            val intent1 = Intent(this, ShowRoute::class.java)
            startActivity(intent1)
        }


        findViewById<Button>(R.id.linia10).setOnClickListener {
            connectToServerViewModel.get_orar("10")
            val intent1 = Intent(this, ShowRoute::class.java)
            startActivity(intent1)
        }


        findViewById<Button>(R.id.linia14).setOnClickListener {
            connectToServerViewModel.get_orar("14")
            val intent1 = Intent(this, ShowRoute::class.java)
            startActivity(intent1)
        }

        findViewById<Button>(R.id.linia18).setOnClickListener {
            connectToServerViewModel.get_orar("18")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }



        findViewById<Button>(R.id.linia19).setOnClickListener {
            connectToServerViewModel.get_orar("19")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }


        findViewById<Button>(R.id.linia20).setOnClickListener {
            connectToServerViewModel.get_orar("20")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }



        findViewById<Button>(R.id.linia21).setOnClickListener {
            connectToServerViewModel.get_orar("21")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }


        findViewById<Button>(R.id.linia22).setOnClickListener {
            connectToServerViewModel.get_orar("22")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia23).setOnClickListener {
            connectToServerViewModel.get_orar("23")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia24).setOnClickListener {
            connectToServerViewModel.get_orar("24")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia24b).setOnClickListener {
            connectToServerViewModel.get_orar("24B")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }


        findViewById<Button>(R.id.linia25).setOnClickListener {
            connectToServerViewModel.get_orar("25")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia26).setOnClickListener {
            connectToServerViewModel.get_orar("26")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia26l).setOnClickListener {
            connectToServerViewModel.get_orar("26L")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia27).setOnClickListener {
            connectToServerViewModel.get_orar("27")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia28).setOnClickListener {
            connectToServerViewModel.get_orar("28")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia28b).setOnClickListener {
            connectToServerViewModel.get_orar("28B")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia29).setOnClickListener {
            connectToServerViewModel.get_orar("29")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia29s).setOnClickListener {
            connectToServerViewModel.get_orar("29S")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia30).setOnClickListener {
            connectToServerViewModel.get_orar("30")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia31).setOnClickListener {
            connectToServerViewModel.get_orar("31")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia32).setOnClickListener {
            connectToServerViewModel.get_orar("32")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia32b).setOnClickListener {
            connectToServerViewModel.get_orar("32B")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }


        findViewById<Button>(R.id.linia33).setOnClickListener {
            connectToServerViewModel.get_orar("33")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia34).setOnClickListener {
            connectToServerViewModel.get_orar("34")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia35).setOnClickListener {
            connectToServerViewModel.get_orar("35")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia36b).setOnClickListener {
            connectToServerViewModel.get_orar("36B")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia36l).setOnClickListener {
            connectToServerViewModel.get_orar("36L")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia37).setOnClickListener {
            connectToServerViewModel.get_orar("37")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia38).setOnClickListener {
            connectToServerViewModel.get_orar("38")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia39).setOnClickListener {
            connectToServerViewModel.get_orar("39")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia39L).setOnClickListener {
            connectToServerViewModel.get_orar("39L")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia40).setOnClickListener {
            connectToServerViewModel.get_orar("40")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia41).setOnClickListener {
            connectToServerViewModel.get_orar("41")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia42).setOnClickListener {
            connectToServerViewModel.get_orar("42")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia43).setOnClickListener {
            connectToServerViewModel.get_orar("43")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia43b).setOnClickListener {
            connectToServerViewModel.get_orar("43B")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia43p).setOnClickListener {
            connectToServerViewModel.get_orar("43P")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia44).setOnClickListener {
            connectToServerViewModel.get_orar("44")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia45).setOnClickListener {
            connectToServerViewModel.get_orar("45")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia46).setOnClickListener {
            connectToServerViewModel.get_orar("46")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia46b).setOnClickListener {
            connectToServerViewModel.get_orar("46B")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia46l).setOnClickListener {
            connectToServerViewModel.get_orar("46L")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia47).setOnClickListener {
            connectToServerViewModel.get_orar("47")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia48).setOnClickListener {
            connectToServerViewModel.get_orar("48")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia48l).setOnClickListener {
            connectToServerViewModel.get_orar("48L")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia50).setOnClickListener {
            connectToServerViewModel.get_orar("50")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia50d).setOnClickListener {
            connectToServerViewModel.get_orar("50D")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia50l).setOnClickListener {
            connectToServerViewModel.get_orar("50L")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia52).setOnClickListener {
            connectToServerViewModel.get_orar("52")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia52l).setOnClickListener {
            connectToServerViewModel.get_orar("52L")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia53).setOnClickListener {
            connectToServerViewModel.get_orar("53")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia100).setOnClickListener {
            connectToServerViewModel.get_orar("100")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia100l).setOnClickListener {
            connectToServerViewModel.get_orar("100L")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia101).setOnClickListener {
            connectToServerViewModel.get_orar("101")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia102).setOnClickListener {
            connectToServerViewModel.get_orar("102")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia102l).setOnClickListener {
            connectToServerViewModel.get_orar("102L")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam11).setOnClickListener {
            connectToServerViewModel.get_orar("M11")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam12).setOnClickListener {
            connectToServerViewModel.get_orar("M12")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam13).setOnClickListener {
            connectToServerViewModel.get_orar("M13")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam16).setOnClickListener {
            connectToServerViewModel.get_orar("M16")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam18).setOnClickListener {
            connectToServerViewModel.get_orar("M18")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam17).setOnClickListener {
            connectToServerViewModel.get_orar("M17")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam21).setOnClickListener {
            connectToServerViewModel.get_orar("M21")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam22).setOnClickListener {
            connectToServerViewModel.get_orar("M22")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam23).setOnClickListener {
            connectToServerViewModel.get_orar("M23")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam24).setOnClickListener {
            connectToServerViewModel.get_orar("M24")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam25).setOnClickListener {
            connectToServerViewModel.get_orar("M25")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam26).setOnClickListener {
            connectToServerViewModel.get_orar("M26")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam31).setOnClickListener {
            connectToServerViewModel.get_orar("M31")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam32).setOnClickListener {
            connectToServerViewModel.get_orar("M32")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam33).setOnClickListener {
            connectToServerViewModel.get_orar("M33")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam34).setOnClickListener {
            connectToServerViewModel.get_orar("M34")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia34b).setOnClickListener {
            connectToServerViewModel.get_orar("M34B")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam35).setOnClickListener {
            connectToServerViewModel.get_orar("M35")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }


        findViewById<Button>(R.id.liniam37).setOnClickListener {
            connectToServerViewModel.get_orar("M37")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam38).setOnClickListener {
            connectToServerViewModel.get_orar("M38")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam39).setOnClickListener {
            connectToServerViewModel.get_orar("M39")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.linia39l).setOnClickListener {
            connectToServerViewModel.get_orar("M39L")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }


        findViewById<Button>(R.id.liniam41).setOnClickListener {
            connectToServerViewModel.get_orar("M41")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam41l).setOnClickListener {
            connectToServerViewModel.get_orar("M41L")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam42).setOnClickListener {
            connectToServerViewModel.get_orar("M42")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam42l).setOnClickListener {
            connectToServerViewModel.get_orar("M42L")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam43).setOnClickListener {
            connectToServerViewModel.get_orar("M43")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam44).setOnClickListener {
            connectToServerViewModel.get_orar("M44")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam45).setOnClickListener {
            connectToServerViewModel.get_orar("M45")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam51).setOnClickListener {
            connectToServerViewModel.get_orar("M51")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }


        findViewById<Button>(R.id.liniam52).setOnClickListener {
            connectToServerViewModel.get_orar("M52")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam61).setOnClickListener {
            connectToServerViewModel.get_orar("M61")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam71).setOnClickListener {
            connectToServerViewModel.get_orar("M71")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.liniam81).setOnClickListener {
            connectToServerViewModel.get_orar("M81")
            val intent = Intent(this, ShowRoute::class.java)
            startActivity(intent)
        }


        // Find the back button
        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Set OnClickListener for the back button to navigate to the MainActivity
        backButton.setOnClickListener {
            val intent = Intent(this, ClientMain::class.java)
            startActivity(intent)
            finish() // Optional: finish SignUpActivity to remove it from the back stack
        }
    }
}