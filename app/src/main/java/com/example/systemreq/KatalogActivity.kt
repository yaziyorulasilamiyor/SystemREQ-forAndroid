package com.example.systemreq

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class KatalogActivity : AppCompatActivity() {

    private lateinit var db: AppDbHelper
    private lateinit var listView: ListView

    private lateinit var tvSummary: TextView
    private lateinit var tvCount: TextView
    private lateinit var etSearch: EditText

    private var allApps: List<ReqApp> = emptyList()

    // kontrol
    private var sdkInt: Int = 0
    private var ramMb: Long = 0
    private var freeMb: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        tvSummary = findViewById(R.id.tvSummary)
        tvCount = findViewById(R.id.tvCount)
        etSearch = findViewById(R.id.etSearch)

        db = AppDbHelper(this)
        listView = findViewById(R.id.listApps)

        sdkInt = intent.getIntExtra("sdkInt", 0)
        ramMb = intent.getLongExtra("ramMb", 0L)
        freeMb = intent.getLongExtra("freeMb", 0L)

        // yedek kontrol
        if (sdkInt == 0 || ramMb == 0L || freeMb == 0L) {
            sdkInt = SistemBilgisi.getAndroidSdk()
            ramMb = SistemBilgisi.getTotalRamMb(this)
            freeMb = SistemBilgisi.getFreeStorageMb()
        }

        tvSummary.text = "SDK: $sdkInt • RAM: ${ramMb}MB • Boş: ${freeMb}MB"

        // boşsa örnek veri
        db.seedDemoData()

        // listeyi yükle
        loadList()

        // 🔎 arama: canlı filtre
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val q = s.toString().trim().lowercase()
                if (q.isEmpty()) showList(allApps)
                else showList(allApps.filter { it.name.lowercase().contains(q) })
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadList()
    }

    private fun loadList() {
        allApps = db.getAllApps()
        showList(allApps)
    }

    private fun showList(apps: List<ReqApp>) {
        tvCount.text = "Kayıt sayısı: ${apps.size}"

        val adapter = object : ArrayAdapter<ReqApp>(this, R.layout.row_app, apps) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v = convertView ?: layoutInflater.inflate(R.layout.row_app, parent, false)
                val app = apps[position]

                val tvName = v.findViewById<TextView>(R.id.tvName)
                val tvStatus = v.findViewById<TextView>(R.id.tvStatus)
                val tvBadge = v.findViewById<TextView>(R.id.tvBadge)

                val missing = missingList(app)

                tvName.text = app.name
                tvStatus.text = if (missing.isEmpty()) "Uygun" else "Eksik: " + missing.joinToString(", ")

                tvBadge.text = when {
                    missing.isEmpty() -> "✅"
                    missing.size == 1 -> "⚠️"
                    else -> "❌"
                }

                return v
            }
        }

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val app = apps[position]

            val i = Intent(this, KatalogDetayActivity::class.java)
            i.putExtra("name", app.name)
            i.putExtra("minSdk", app.minSdk)
            i.putExtra("minRamMb", app.minRamMb)
            i.putExtra("minStorageMb", app.minStorageMb)

            i.putExtra("sdkInt", sdkInt)
            i.putExtra("ramMb", ramMb)
            i.putExtra("freeMb", freeMb)

            startActivity(i)
        }
    }

    private fun missingList(app: ReqApp): List<String> {
        val missing = mutableListOf<String>()
        if (sdkInt < app.minSdk) missing.add("SDK")
        if (ramMb < app.minRamMb) missing.add("RAM")
        if (freeMb < app.minStorageMb) missing.add("Depolama")
        return missing
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.menu_apps, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                startActivity(Intent(this, UygulamaEkleActivity::class.java))
                true
            }
            R.id.action_seed -> {
                db.seedDemoData()
                loadList()
                Toast.makeText(this, "Örnek veri eklendi.", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_about -> {
                Toast.makeText(this, "Sistem Gereksinimleri Uygunluk Kontrolü Uygulaması", Toast.LENGTH_SHORT).show()
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
