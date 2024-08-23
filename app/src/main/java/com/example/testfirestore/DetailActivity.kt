package com.example.testfirestore

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.testfirestore.AddDataActivity.Companion.EXTRA_NOTE
import com.example.testfirestore.Main.MainActivity
import com.example.testfirestore.databinding.ActivityDetailBinding
import com.google.firebase.firestore.FirebaseFirestore

class DetailActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val documentId = intent.getStringExtra("DOCUMENT_ID")
        println("Haruse sih ada ini : $documentId")

        if (documentId != null) {
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
                        binding.showName.text = author
                        binding.showHP.text = noHp
                        binding.showJK.text = JK
                        binding.showTTL.text = ttl
                        binding.showQuote.text = quote

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

        binding.buttoEdit.setOnClickListener {
            val intent = Intent(this, AddDataActivity::class.java)
            intent.putExtra(EXTRA_NOTE, documentId)
            startActivity(intent)
        }
        binding.buttonHapus.setOnClickListener { deleteData() }
    }

    private fun deleteData() {
        val documentId = intent.getStringExtra("DOCUMENT_ID")
        if (documentId != null) {
            // Membuat dialog konfirmasi
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi Hapus")
            builder.setMessage("Apakah kamu yakin ingin menghapus dokumen ini?")
            // Jika pengguna mengkonfirmasi penghapusan
            builder.setPositiveButton("Ya") { dialog, _ ->
                db.collection("quote").document(documentId)
                    .delete()
                    .addOnSuccessListener {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Berhasil Menghapus", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Gagal menghapus dokumen: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                dialog.dismiss()
            }

            // Jika pengguna membatalkan penghapusan
            builder.setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            // Menampilkan dialog
            val dialog = builder.create()
            dialog.show()
        } else {
            println("Document ID tidak ditemukan di Intent.")
        }
    }

}

