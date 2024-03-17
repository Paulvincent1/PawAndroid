package com.example.pawandroid

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener {
                val email = tilUsername.text.toString()
                val pass = tilPassword.text.toString()
                login(email, pass)
            }

            tvSignup.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignupActivity::class.java)
                startActivity(intent)
            }

            // Underline the text in tvSignup
            val content = SpannableString(tvSignup.text)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            tvSignup.text = content

            // Create a TextWatcher for the password EditText
            tilPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(editable: Editable?) {
                    // You can perform any actions after the text has changed, e.g., validation
                }
            })

            showPassLogin.setOnCheckedChangeListener { _, isChecked ->
                // Update the variable to track the state of the "Show Password" checkbox
                isPasswordVisible = isChecked
                // Toggle the password visibility based on the checkbox state
                updatePasswordVisibility()
            }
        }
    }

    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    private fun updatePasswordVisibility() {
        if (isPasswordVisible) {
            // If checked, set the input type to visible text
            binding.tilPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            // If not checked, set the input type to password
            binding.tilPassword.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        // Move the cursor to the end of the text
        binding.tilPassword.setSelection(binding.tilPassword.text?.length ?: 0)
    }

    private fun login(email: String, password: String) {
        val user = RetrofitBuilder.buildService(PawService::class.java)
        val call = user.login(email, password)
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.isEnabled = false
        binding.tilUsername.isEnabled = false
        binding.tilPassword.isEnabled = false
        binding.tvSignup.isEnabled = false
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                binding.tilUsername.isEnabled = true
                binding.tilPassword.isEnabled = true
                binding.tvSignup.isEnabled = true

                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Login", Toast.LENGTH_SHORT).show()
                    val token = response.body()?.token
                    token?.let { RetrofitBuilder.setAuthToken(it) }
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Finish the LoginActivity to prevent going back when pressing back button
                } else {
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.isEnabled = true
                binding.tilUsername.isEnabled = true
                binding.tilPassword.isEnabled = true
                binding.tvSignup.isEnabled = true
                Toast.makeText(applicationContext, "Invalid Credentials", Toast.LENGTH_SHORT).show()
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