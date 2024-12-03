package com.example.helloing3

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.findViewTreeViewModelStoreOwner

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnGallery:Button = findViewById(R.id.button_galery)
        val btnSubject:Button = findViewById(R.id.button_subject)
        val tvSalutation:TextView = findViewById(R.id.welcom)
        btnSubject.setOnClickListener { view->
            tvSalutation.setText("Welcome to ING3 subject")
            val intent = Intent(this, SubjectsActivity::class.java)
            startActivity(intent)
        }
        btnGallery.setOnClickListener { view->
            tvSalutation.setText("Welcome to ING3 Gallery")
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }
        val btnSendEmail:Button = findViewById(R.id.button_email)
        btnSendEmail.setOnClickListener { view->
            sendEmail()
        }
    }

    fun sendEmail(){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtras(Intent.EXTRA_EMAIL, arrayOf("dogugua.honore@gmail.com", "zakariagamane40@gmail.com"))
        intent.putExtras(Intent.EXTRA_TEXT, "How are you?")
        intent.putExtras(Intent.EXTRA_SUBJECT, "Texte sur l'objet")

        if (intent.resolveActivity(packageManager) !=null){
            startActivity(intent)
        }

    }

}

private fun Intent.putExtras(extraEmail: String, com: Any) {

}
