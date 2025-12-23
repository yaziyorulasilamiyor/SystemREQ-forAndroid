package com.example.systemreq

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvSdk = findViewById<TextView>(R.id.tvSdk)
        val tvRam = findViewById<TextView>(R.id.tvRam)
        val tvStorage = findViewById<TextView>(R.id.tvStorage)
        val btnGo = findViewById<Button>(R.id.btnGo)

        val sdk = DeviceInfo.getAndroidSdk()
        val ram = DeviceInfo.getTotalRamMb(this)
        val free = DeviceInfo.getFreeStorageMb()

        tvSdk.text = "Android (SDK): $sdk"
        tvRam.text = "Toplam RAM: ${ram} MB"
        tvStorage.text = "Boş Depolama: ${free} MB"

        btnGo.setOnClickListener {
            val intent = Intent(this, AppListActivity::class.java)
            intent.putExtra("sdkInt", sdk)
            intent.putExtra("ramMb", ram)
            intent.putExtra("freeMb", free)
            startActivity(intent)
        }
    }
}
