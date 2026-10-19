package com.example.pab_2301

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi komponen form
        val edtNama = findViewById<EditText>(R.id.edtNama)
        val rgJk = findViewById<RadioGroup>(R.id.rgJk)
        val spKota = findViewById<Spinner>(R.id.spKota)
        val cbCoding = findViewById<CheckBox>(R.id.cbCoding)
        val cbDesign = findViewById<CheckBox>(R.id.cbDesign)
        val cbGaming = findViewById<CheckBox>(R.id.cbGaming)
        val swMenyetujui = findViewById<Switch>(R.id.swMenyetujui)
        val btnSimpan = findViewById<Button>(R.id.btn_submit)

        // Setup Data Spinner Kota
        val kota = arrayOf("Pilih Kota", "Jakarta", "Bandung", "Surabaya", "Padang")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kota)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spKota.adapter = adapter

        // Aksi Tombol Simpan
        btnSimpan.setOnClickListener {
            val nama = edtNama.text.toString().trim()

            // Validasi Nama
            if (nama.isEmpty()) {
                Toast.makeText(this, "Nama Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi Jenis Kelamin
            if (rgJk.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Jenis Kelamin Harus Dipilih", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi Kota Asal
            val kotaDipilih = spKota.selectedItem.toString()
            if (kotaDipilih == "Pilih Kota") {
                Toast.makeText(this, "Silakan pilih Kota Asal", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validasi S&K
            if (!swMenyetujui.isChecked) {
                Toast.makeText(this, "Anda Harus Menyetujui S&K", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ambil Nilai Jenis Kelamin
            val jk = when (rgJk.checkedRadioButtonId) {
                R.id.rbLaki -> "Laki-laki"
                R.id.rbPerempuan -> "Perempuan"
                else -> "-"
            }

            // Ambil Nilai Checkbox Hobi
            val listHobi = mutableListOf<String>()
            if (cbCoding.isChecked) listHobi.add("Coding")
            if (cbDesign.isChecked) listHobi.add("Desain UI/UX")
            if (cbGaming.isChecked) listHobi.add("Gaming")
            val hobiStr = if (listHobi.isNotEmpty()) listHobi.joinToString(", ") else "Tidak ada"

            // Susun Paket Data
            val dataLengkap = """
                Nama Lengkap : $nama
                
                Jenis Kelamin : $jk
                
                Kota Asal : $kotaDipilih
                
                Hobi : $hobiStr
                
                S&K : Disetujui
            """.trimIndent()

            // NAVIGASI MENGGUNAKAN INTENT KE DuaActivity
            val intent = Intent(this, DuaActivity::class.java).apply {
                putExtra("DATA_HASIL", dataLengkap)
            }
            startActivity(intent)
        }
    }
}