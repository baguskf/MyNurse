package com.example.testfirestore


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View

import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.example.testfirestore.Main.MainActivity


import com.example.testfirestore.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var googleSignInClient: GoogleSignInClient
    var firebaseAuth= FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging")
        progressDialog.setMessage("Silahkan tunggu....")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)


        if (firebaseAuth.currentUser != null) {
            // Jika pengguna sudah login, arahkan ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


        binding.signInButton.setOnClickListener {
            val siginIntent= googleSignInClient.signInIntent
            startActivityForResult(siginIntent, RC_SIGN_IN)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //jika berhasil
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                e.printStackTrace()
//                Log.e(TAG, "Received an invalid google id token response", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken : String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
            }
            .addOnFailureListener{ exception ->
                val rootView = findViewById<View>(android.R.id.content) // Mengambil root view dari activity
                Snackbar.make(rootView, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action") { Toast.makeText(this@LoginActivity, "Halo ini action dari snackbar", Toast.LENGTH_SHORT).show()}
                    .show()
            }
    }

    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 1001
    }
}
