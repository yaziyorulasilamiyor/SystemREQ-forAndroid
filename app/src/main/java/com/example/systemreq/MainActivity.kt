package com.example.systemreq

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnGo = findViewById<Button>(R.id.btnGo)

        // cihaz bilgileri al
        val sdk = SistemBilgisi.getAndroidSdk()
        val ram = SistemBilgisi.getTotalRamMb(this)
        val free = SistemBilgisi.getFreeStorageMb()

        btnGo.setOnClickListener {
            val intent = Intent(this, KatalogActivity::class.java)
            intent.putExtra("sdkInt", sdk)
            intent.putExtra("ramMb", ram)
            intent.putExtra("freeMb", free)
            startActivity(intent)
        }
    }
}
