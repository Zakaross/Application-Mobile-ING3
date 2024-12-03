package com.example.helloing3


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        findViewById<View>(R.id.btnConsulter).setOnClickListener { v: View? ->
            val intent = Intent(
                this,
                ListGalleryActivity::class.java
            )
            startActivity(intent)
        }

        findViewById<View>(R.id.btnAjouter).setOnClickListener { v: View? ->
            val intent = Intent(
                this,
                AddGalleryActivity::class.java
            )
            startActivity(intent)
        }
    }
}
