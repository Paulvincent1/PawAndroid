package com.example.pawandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pawandroid.bottomNav.MainActivity
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityPetInfoBinding
import com.example.pawandroid.model.PetResponse
import com.example.pawandroid.model.Pets
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetInfoBinding
    private var id: String? = null
    private var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("key")
        id?.let { getPet(it.toInt()) }

        binding.btnAdopt.setOnClickListener {
            val intent = Intent(this, AdoptActivity::class.java)
            intent.putExtra("key", id)
            startActivity(intent)

        }

        binding.btnBack.setOnClickListener {
            finish()

        }
        binding.btnViewProfile.setOnClickListener {
            val intent = Intent(this, ViewProfileActivity::class.java)
            intent.putExtra("key", userId)
            startActivity(intent)
        }

    }

    private fun getPet(id: Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getPet(id)
        call.enqueue(object : Callback<PetResponse> {
            override fun onResponse(call: Call<PetResponse>, response: Response<PetResponse>) {
               if(response.isSuccessful){
                   val petResponse = response.body()
                   if (petResponse != null) {
                       val pet = petResponse.pet
                       val user = pet.user
                       userId = user.id.toString()
                       // Now you can access the pet object and its properties
                       binding.tvName.text = pet.name
                       binding.tvAge.text = pet.age
                       binding.tvSpecies.text = pet.species
                       binding.tvBreed.text = pet.breed
                       binding.tvDescription.text = pet.description
                       val imageUrl = "http://192.168.0.13/${pet.img}"
                       Glide.with(applicationContext)
                           .load(imageUrl)
                           .into(binding.imgPetInfo)
                       // Use the pet data as needed
                   }
               }
            }

            override fun onFailure(call: Call<PetResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "failed to load", Toast.LENGTH_SHORT).show()
            }
        })

    }
}