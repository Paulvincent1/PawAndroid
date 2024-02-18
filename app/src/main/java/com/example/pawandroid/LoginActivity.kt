package com.example.pawandroid

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.pawandroid.R
import com.example.pawandroid.bottomNav.MainActivity
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityLoginBinding
import com.example.pawandroid.model.User
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener {
                val email = tilUsername.text
                val pass = tilPassword.text
                login(email.toString(),pass.toString())
            }
        }

    }
    override fun onBackPressed() {
        showExitConfirmationDialog()
    }




    private fun login(email: String, password: String) {
        val user = RetrofitBuilder.buildService(PawService::class.java)
        val call = user.login(email, password)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "login", Toast.LENGTH_SHORT).show()
                    val token = response.body()?.token
                    binding.textView2.text = token
                    token?.let { RetrofitBuilder.setAuthToken(it) }
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Finish the LoginActivity to prevent going back when pressing back button
                } else {
                    Toast.makeText(applicationContext, "failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext, "cant connect", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit Confirmation")
        builder.setMessage("Are you sure you want to exit the app?")
        builder.setPositiveButton("Yes") { _, _ ->
            // User confirmed exit, finish the current activity to exit the app
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            // User canceled exit, dismiss the dialog
            dialog.dismiss()
            // Perform any additional actions if needed
        }
        builder.setOnCancelListener {
            // Handle the case when the user cancels the dialog with the back button
            // Perform any additional actions if needed
        }

        val dialog = builder.create()
        dialog.show()
    }

}