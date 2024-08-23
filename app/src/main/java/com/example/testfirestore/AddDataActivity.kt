package com.example.testfirestore

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirestore.Main.MainActivity
import com.example.testfirestore.databinding.ActivityAddDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar


class AddDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDataBinding
    private lateinit var documentId: String
    private var isEdit = false

    val firebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()


    companion object{
        const val EXTRA_NOTE = "extraNote"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.dateInput.setEndIconOnClickListener(View.OnClickListener { showDatePicker() })
        binding.ttl.setOnClickListener{
            showDatePicker()
        }

        val documentId = intent.getStringExtra(EXTRA_NOTE)?: ""
        println("is edit doc : $documentId")

        val userId = firebaseAuth.uid ?: ""
//        Toast.makeText(this,userId, Toast.LENGTH_LONG).show()

        if (documentId.isNotEmpty()) {
            db.collection("quote").document(documentId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val author = document.getString("nama")
                        val noHp = document.getString("noHp")
                        val JK = document.getString("jenisK")
                        val ttl = document.getString("tanggal")
                        val quote = document.getString("quote")

                        // Menampilkan data di UI
                        binding.inputNama.editText?.setText(author)
                        binding.noHp.editText?.setText(noHp)
                        if (JK == "Laki-Laki") {
                            binding.radioGroup.check(binding.radioButton1.id)
                        } else if (JK == "Perempuan") {
                            binding.radioGroup.check(binding.radioButton2.id)
                        } else {
                            binding.radioGroup.check(binding.radioButton3.id)
                        }

                        binding.ttl.setText(ttl)
                        binding.inpuQuote.editText?.setText(quote)

                        println("ini quote : $quote")
                        println("ini author : $author")
                    } else {
                        println("Dokumen tidak ditemukan")
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getting document: $exception")
                }
        } else {
            println("Document ID tidak ditemukan di Intent.")
        }


        val actionBarTitle : String
        val btnTitle : String

        if(documentId.isEmpty()){
            isEdit = false
        }else{
            isEdit = true
        }

        println("is edit = $isEdit")

        if(isEdit==true){
            actionBarTitle ="Edit Data"
            btnTitle = "Edit"

        }else{
            actionBarTitle ="Tambah Data"
            btnTitle = "Simpan"
        }

        supportActionBar?.title = actionBarTitle
        binding.submitBtn.text = btnTitle


        println("ini lho : " + firebaseAuth.uid)
        binding.submitBtn.setOnClickListener {
            if (documentId.isNotEmpty()){
                editDb()
            }else{
                pushDb()
            }
        }

        println("ini lho : " + System.currentTimeMillis())

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, R.style.ThemeOverlay_AppCompat_Dialog,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val monthStr = String.format("%02d", month + 1)
            val dayStr = String.format("%02d", dayOfMonth)
            val date = "$dayStr/$monthStr/$year"
            binding.ttl .setText(date);
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun pushDb() {
        val userId = firebaseAuth.uid ?: "" // Pastikan userId tidak null
        val nama = binding.inputNama.editText?.text.toString()
        val noHp = binding.noHp.editText?.text.toString()

        fun getSelectedRadioButtonText(): String? {
            val selectedId = binding.radioGroup.checkedRadioButtonId
            return if (selectedId != -1) {
                val radioButton = binding.root.findViewById<RadioButton>(selectedId)
                radioButton.text.toString()
            } else {
                null
            }
        }

        val jenisK = getSelectedRadioButtonText() ?: "" // Menyediakan default value jika null
        val tanggal = binding.dateInput.editText?.text.toString()
        val quote = binding.inpuQuote.editText?.text.toString()
        val time = System.currentTimeMillis()

        // Membuat data class Quote
        val quoteData = Quote(
            userId = userId,
            nama = nama,
            noHp = noHp,
            jenisK = jenisK,
            tanggal = tanggal,
            quote = quote,
            timestamp = time
        )

        // Menambahkan data ke koleksi "quote"
        db.collection("quote")
            .add(quoteData) // Menggunakan metode `add` untuk membuat dokumen dengan ID otomatis
            .addOnSuccessListener { documentReference ->
                // Data berhasil ditambahkan
                Toast.makeText(this, "Data Berhasil di Tambahkan", Toast.LENGTH_LONG).show()
                val intent = Intent (this, MainActivity::class.java)
                    startActivity(intent)
            }
            .addOnFailureListener { e ->
                // Terjadi kesalahan
//                Log.w("Firestore", "Gagal menambahkan data", e)
            }
    }

    private fun editDb() {
        val cekDocument = intent.getStringExtra(EXTRA_NOTE)?: ""
        val userId = firebaseAuth.uid ?: "" // Pastikan userId tidak null
        val nama = binding.inputNama.editText?.text.toString()
        val noHp = binding.noHp.editText?.text.toString()

        // Mendapatkan teks dari RadioButton yang dipilih
        fun getSelectedRadioButtonText(): String? {
            val selectedId = binding.radioGroup.checkedRadioButtonId
            return if (selectedId != -1) {
                val radioButton = findViewById<RadioButton>(selectedId)
                radioButton.text.toString()
            } else {
                null
            }
        }

        val jenisK = getSelectedRadioButtonText() ?: ""
        val tanggal = binding.dateInput.editText?.text.toString()
        val quote = binding.inpuQuote.editText?.text.toString()
        val time = System.currentTimeMillis()

        // Data yang akan diperbarui
        val quoteData = mapOf(
            "userId" to userId,
            "nama" to nama,
            "noHp" to noHp,
            "jenisK" to jenisK,
            "tanggal" to tanggal,
            "quote" to quote,
            "timestamp" to time
        )

        // Memperbarui dokumen di Firestore
        db.collection("quote").document(cekDocument)
            .update(quoteData)
            .addOnSuccessListener {
                Log.d(TAG, "Document successfully updated!")
                Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent (this, MainActivity::class.java)
                startActivity(intent)
                finish() // Navigasi kembali ke aktivitas sebelumnya atau aktivitas lain
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating document", e)
                Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show()
            }
    }


}