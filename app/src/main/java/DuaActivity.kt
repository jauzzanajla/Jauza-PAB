package com.example.pab_2301

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DuaActivity : AppCompatActivity() {

    private lateinit var tvHasil: TextView
    private lateinit var pbLoading: ProgressBar
    private lateinit var btnKembali: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dua)

        tvHasil = findViewById(R.id.tvHasil)
        pbLoading = findViewById(R.id.pbLoading)
        btnKembali = findViewById(R.id.btnKembali)

        // Tangkap data yang dikirim dari MainActivity
        // Perhatikan key "DATA_HASIL" harus sama persis dengan putExtra di MainActivity
        val data = intent.getStringExtra("DATA_HASIL")

        if (data != null) {
            tvHasil.text = data
        }

        pbLoading.isIndeterminate = false
        pbLoading.progress = 100

        // Tombol kembali menutup activity ini dan kembali ke form utama
        btnKembali.setOnClickListener {
            finish()
        }
    }
}