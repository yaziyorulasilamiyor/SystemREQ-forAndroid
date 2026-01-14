package com.example.systemreq

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class KatalogDetayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog_detail)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)

        val tvDevice = findViewById<TextView>(R.id.tvDevice)
        val tvReq = findViewById<TextView>(R.id.tvReq)

        val tvResult = findViewById<TextView>(R.id.tvResult)
        val tvMissing = findViewById<TextView>(R.id.tvMissing)

        val btnBack = findViewById<Button>(R.id.btnBack)

        // --- Intent ile gelen veriler ---
        val name = intent.getStringExtra("name") ?: "Bilinmeyen"
        val minSdk = intent.getIntExtra("minSdk", 0)
        val minRam = intent.getIntExtra("minRamMb", 0)
        val minStorage = intent.getIntExtra("minStorageMb", 0)

        val sdkInt = intent.getIntExtra("sdkInt", 0)
        val ramMb = intent.getLongExtra("ramMb", 0L)
        val freeMb = intent.getLongExtra("freeMb", 0L)

        tvTitle.text = name

        // cihaz bilgisi
        tvDevice.text = "Cihaz:\nSDK: $sdkInt\nRAM: ${ramMb} MB\nBoş Alan: ${freeMb} MB"

        // gereksinim
        tvReq.text = "Gereksinim:\nMin SDK: $minSdk\nMin RAM: ${minRam} MB\nMin Boş Alan: ${minStorage} MB"

        // --- Uygunluk hesabı ---
        val missing = mutableListOf<String>()

        if (sdkInt < minSdk) missing.add("Android SDK düşük")
        if (ramMb < minRam) missing.add("RAM yetersiz")
        if (freeMb < minStorage) missing.add("Depolama yetersiz")

        if (missing.isEmpty()) {
            tvResult.text = "SONUÇ: UYGUN ✅"
            tvMissing.text = "Eksik yok."
        } else {
            tvResult.text = "SONUÇ: UYGUN DEĞİL ❌"
            tvMissing.text = "Eksikler:\n- " + missing.joinToString("\n- ")
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
