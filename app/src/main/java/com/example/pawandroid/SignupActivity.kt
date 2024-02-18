package com.example.pawandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivitySignupBinding
import com.example.pawandroid.model.User
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnSignup.setOnClickListener {
                val name = tilUsername.text.toString()
                val email = tilEmail.text.toString()
                val pass = tilPassword.text.toString()
                val pass_confrim = tilPassword2.text.toString()

                register(name,email,pass,pass_confrim)
            }

        }
    }


    private fun register(name: String, email: String, password: String, password_confirmation: String){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.register(name, email, password, password_confirmation)
        binding.btnSignup.isEnabled = false
        binding.progressBar2.visibility = View.VISIBLE
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                binding.progressBar2.visibility = View.GONE
                binding.btnSignup.isEnabled = true
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, "Register Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                binding.progressBar2.visibility = View.GONE
                binding.btnSignup.isEnabled = true
                Toast.makeText(applicationContext, "cant connect", Toast.LENGTH_SHORT).show()
            }
        })
    }
}