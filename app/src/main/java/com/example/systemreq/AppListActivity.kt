package com.example.systemreq

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AppListActivity : AppCompatActivity() {

    private lateinit var db: AppDbHelper
    private lateinit var listView: ListView

    private var sdkInt: Int = 0
    private var ramMb: Long = 0
    private var freeMb: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_list)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        db = AppDbHelper(this)
        listView = findViewById(R.id.listApps)


        sdkInt = intent.getIntExtra("sdkInt", 0)
        ramMb = intent.getLongExtra("ramMb", 0L)
        freeMb = intent.getLongExtra("freeMb", 0L)


        if (sdkInt == 0 || ramMb == 0L || freeMb == 0L) {
            sdkInt = DeviceInfo.getAndroidSdk()
            ramMb = DeviceInfo.getTotalRamMb(this)
            freeMb = DeviceInfo.getFreeStorageMb()
        }


        db.seedDemoData()

        loadList()
    }

    override fun onResume() {
        super.onResume()
        loadList()
    }

    private fun loadList() {
        val apps = db.getAllApps()

        val lines = apps.map { app ->
            val status = calcStatus(app)
            "${app.name} — $status"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lines)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            // şimdilik tıklayınca sadece toast verelim (hoca “tıklama var mı” diye sorarsa var)
            Toast.makeText(this, "Seçildi: ${apps[position].name}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calcStatus(app: ReqApp): String {
        val okSdk = sdkInt >= app.minSdk
        val okRam = ramMb >= app.minRamMb
        val okStorage = freeMb >= app.minStorageMb

        val okCount = listOf(okSdk, okRam, okStorage).count { it }

        return when {
            okCount == 3 -> "Uygun ✅"
            okCount == 2 -> "Sınırda ⚠️"
            else -> "Uygun Değil ❌"
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.menu_apps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                startActivity(Intent(this, AddAppActivity::class.java))
                true
            }
            R.id.action_seed -> {
                db.seedDemoData()
                loadList()
                Toast.makeText(this, "Örnek veri eklendi.", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_about -> {
                Toast.makeText(this, "Sistem gereksinimi uygunluk kontrolü", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_clear -> {
                db.deleteAll()
                loadList()
                Toast.makeText(this, "Tüm kayıtlar silindi.", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
