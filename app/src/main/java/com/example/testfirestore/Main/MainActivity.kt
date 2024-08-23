package com.example.testfirestore.Main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testfirestore.AdapterFirestore
import com.example.testfirestore.AddDataActivity
import com.example.testfirestore.LoginActivity
import com.example.testfirestore.Quote
import com.example.testfirestore.R
import com.example.testfirestore.databinding.ActivityMainBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


import com.google.firebase.auth.FirebaseAuth


import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private lateinit var viewModel: MainViewModel
    val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var adapter: AdapterFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.RvListQuote.layoutManager = LinearLayoutManager(this)



        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid

        val query = FirebaseFirestore.getInstance()
            .collection("quote").whereEqualTo("userId", userId)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)

        val options = FirestoreRecyclerOptions.Builder<Quote>()
            .setQuery(query, Quote::class.java)
            .build()

        adapter = AdapterFirestore(options)

        binding.RvListQuote.adapter = adapter

        binding.addData.setOnClickListener {
            val intent = Intent(this, AddDataActivity::class.java)
            startActivity(intent)
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action") { Toast.makeText(this@MainActivity, "Halo ini action dari snackbar", Toast.LENGTH_SHORT).show()}
//                .show()
        }


    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        // Panggil fungsi untuk memuat ulang data RecyclerView
        adapter.startListening()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {

        firebaseAuth.signOut()

        // Menghapus sesi Google Sign-In jika menggunakan Google
        val googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
        googleSignInClient.signOut().addOnCompleteListener {
            // Arahkan pengguna ke halaman login setelah logout
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish() // Tutup activity saat ini
        }
    }
}