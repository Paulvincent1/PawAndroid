package com.example.pawandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pawandroid.databinding.ActivityViewProfileBinding

class ViewProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewProfileBinding
    private var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("key")
        binding.textView8.text = userId
    }
}