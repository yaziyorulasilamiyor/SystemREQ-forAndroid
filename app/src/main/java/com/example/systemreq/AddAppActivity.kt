package com.example.systemreq
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.systemreq.AppDbHelper
import com.example.systemreq.R

class AddAppActivity : AppCompatActivity() {

    private lateinit var db: AppDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_app)

        db = AppDbHelper(this)

        val etName = findViewById<EditText>(R.id.etName)
        val etMinSdk = findViewById<EditText>(R.id.etMinSdk)
        val etMinRam = findViewById<EditText>(R.id.etMinRam)
        val etMinStorage = findViewById<EditText>(R.id.etMinStorage)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val minSdk = etMinSdk.text.toString().toIntOrNull()
            val minRam = etMinRam.text.toString().toIntOrNull()
            val minStorage = etMinStorage.text.toString().toIntOrNull()

            if (name.isEmpty() || minSdk == null || minRam == null || minStorage == null) {
                Toast.makeText(this, "Tüm alanları doğru doldur.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.insertApp(name, minSdk, minRam, minStorage)
            Toast.makeText(this, "Kaydedildi.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
