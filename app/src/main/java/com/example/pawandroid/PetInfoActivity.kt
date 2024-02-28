package com.example.pawandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pawandroid.bottomNav.MainActivity
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityPetInfoBinding
import com.example.pawandroid.model.PetResponse
import com.example.pawandroid.model.Pets
import com.example.pawandroid.service.PawService
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetInfoBinding
    private var id: String? = null
    private var userId: String? = null
    private var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("key")
        id?.let { getPet(it.toInt()) }


        val view: View = layoutInflater.inflate(R.layout.item_bottom_sheet_report, null)
        val dialog = BottomSheetDialog(this)

        binding.btnReport.setOnClickListener {
            dialog.setContentView(view)
            dialog.show()

            // Access TextViews only after layout inflation
            val reason1 = view.findViewById<TextView>(R.id.tvReason1)
            val reason2 = view.findViewById<TextView>(R.id.tvReason2)
            val reason3 = view.findViewById<TextView>(R.id.tvReason3)

            // Set click listeners after TextViews are initialized
            reason1?.setOnClickListener {
                id?.let { it1 -> report(it1.toInt(),"Nudity or sexual activity") }
                dialog.dismiss()
            }
            reason2?.setOnClickListener {
                id?.let { it1 -> report(it1.toInt(),"Scam of fraud") }
                dialog.dismiss()
            }
            reason3?.setOnClickListener {
                id?.let { it1 -> report(it1.toInt(),"Others") }
                dialog.dismiss()
            }
        }

        binding.btnAdopt.setOnClickListener {
            val intent = Intent(this, AdoptActivity::class.java)
            intent.putExtra("key", id)
            intent.putExtra("imageUrl",imageUrl)
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
    private fun report(id: Int, reason: String) {
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.reportPost(id, reason)
        binding.progressBar4.visibility = View.VISIBLE
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                binding.progressBar4.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@PetInfoActivity, "Thanks for letting us know!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@PetInfoActivity, "Unsuccessful response", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                binding.progressBar4.visibility = View.GONE
                Toast.makeText(this@PetInfoActivity, "Error Occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("potektalaga", "Error occurred", t)
            }
        })
    }


    private fun getPet(id: Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getPet(id)
        binding.progressBar4.visibility = View.VISIBLE
        call.enqueue(object : Callback<PetResponse> {
            override fun onResponse(call: Call<PetResponse>, response: Response<PetResponse>) {
                binding.progressBar4.visibility = View.GONE
                binding.btnViewProfile.isEnabled = true
                binding.btnAdopt.isEnabled = true
               if(response.isSuccessful){
                   val petResponse = response.body()
                   if (petResponse != null) {
                       val pet = petResponse.pet
                       val user = pet.user
                       userId = user.id.toString()
                       // Now you can access the pet object and its properties
                       binding.tvNamePet.text = pet.name
                       binding.tvAge.text = pet.age.toString()
                       binding.tvSpecies.text = pet.species
                       binding.tvBreed.text = pet.breed
                       binding.tvDescription.text = pet.description
                       binding.tvLocation.text = pet.region
                       imageUrl = "http://192.168.100.192/${pet.img}"
//                                   paul =  http://192.168.0.13/
//                                   nath =  http://192.168.100.192/
                       Glide.with(applicationContext)
                           .load(imageUrl)
                           .into(binding.imgPetInfo)
                       // Use the pet data as needed
                   }
               }
            }

            override fun onFailure(call: Call<PetResponse>, t: Throwable) {
                binding.progressBar4.visibility = View.GONE
                Toast.makeText(applicationContext, "failed to load", Toast.LENGTH_SHORT).show()
            }
        })

    }
}