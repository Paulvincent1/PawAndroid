package com.example.pawandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pawandroid.bottomNav.MainActivity
import com.example.pawandroid.databinding.ActivityAdoptBinding

class AdoptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdoptBinding
    private var id: String? = null
    private var btnCancel: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdoptBinding.inflate(layoutInflater)
        setContentView(binding.root)





        binding.btnBack.setOnClickListener {
            val intent = Intent(this,PetInfoActivity::class.java)
            intent.putExtra("key", id)
            startActivity(intent)

        }
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("key", btnCancel)
            startActivity(intent)

        }
    }
}