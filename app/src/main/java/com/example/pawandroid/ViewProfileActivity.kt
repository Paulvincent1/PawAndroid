package com.example.pawandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pawandroid.databinding.ActivityViewProfileBinding

class ViewProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewProfileBinding
    private var userId: String? = null
    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("key")
        binding.emailText.text = userId

        binding.backToBack.setOnClickListener {
            val intent = Intent(this,PetInfoActivity::class.java)
            intent.putExtra("key", id)
            startActivity(intent)

        }
    }
}