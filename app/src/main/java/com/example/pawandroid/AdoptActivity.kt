package com.example.pawandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityAdoptBinding
import com.example.pawandroid.model.Adopt
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdoptActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdoptBinding
    private var id: String? = null
    private var btnCancel: String? = null
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdoptBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("key")
        imageUrl = intent.getStringExtra("imageUrl")
        Glide.with(applicationContext)
            .load(imageUrl)
            .into(binding.imgPetInfo)
        binding.apply {
            btnAdopt3.setOnClickListener {
                val name = editTextFullName.text.toString().trim()
                val address = editTextAddress.text.toString().trim()
                val city = editTextCity.text.toString().trim()
                val contact = editTextContactNumber.text.toString().trim()
                val veterinaryInformationChecked = checkBoxVeterinaryCare.isChecked
                val adoptionAgreementChecked = textViewCommitment.isChecked // Corrected variable name
                val additionalComment = editTextWhyAdopt.text.toString().trim()

                if (name.isEmpty()) {
                    editTextFullName.error = "Please Enter your name"
                    Toast.makeText(applicationContext, "Please enter your name", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (address.isEmpty()) {
                    editTextAddress.error = "Please enter your address"
                    Toast.makeText(applicationContext, "Please enter your address", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (city.isEmpty()) {
                    editTextCity.error = "Please enter your city"
                    Toast.makeText(applicationContext, "Please enter your city", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (contact.isEmpty()) {
                    editTextContactNumber.error = "Please enter your contact number"
                    Toast.makeText(applicationContext, "Please enter your contact number", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!veterinaryInformationChecked) {
                    checkBoxVeterinaryCare.error = "Please confirm veterinary information"
                    Toast.makeText(applicationContext, "Please confirm veterinary information", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!adoptionAgreementChecked) {
                    textViewCommitment.error = "Please agree to adoption agreement"
                    Toast.makeText(applicationContext, "Please agree to adoption agreement", Toast.LENGTH_SHORT).show()// Corrected error message
                    return@setOnClickListener
                }

                if (additionalComment.isEmpty()) {
                    editTextWhyAdopt.error = "Please provide additional comments"
                    Toast.makeText(applicationContext, "Please provide additional comments", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Validation successful, proceed with adoption request
                id?.let { it1 -> adoptPet(it1.toInt(), name, address, city, contact, "true" ,"true", additionalComment) }


            }
        }



        binding.btnBack.setOnClickListener {
            finish()

        }
        binding.btnCancel.setOnClickListener {
            finish()

        }

    }

    private fun adoptPet(
        id: Int,
        name: String,
        address: String,
        city: String,
        contact_number: String,
        veterinary_information: String,
        adoption_agreement: String,
        additional_comment: String
    ) {
        // Building the Retrofit service
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)

        // Making the API call to adopt the pet
        val call = retrofit.adoptPet(
            id,
            name,
            address,
            city,
            contact_number,
            veterinary_information,
            adoption_agreement,
            additional_comment
        )
        binding.btnAdopt3.isEnabled = false
        // Enqueuing the call to make it asynchronous
        call.enqueue(object : Callback<Adopt> {
            override fun onResponse(call: Call<Adopt>, response: Response<Adopt>) {
                // Handling successful response
                binding.btnAdopt3.isEnabled = true
                if(response.isSuccessful){
                    // Showing a toast message indicating success
                    Toast.makeText(applicationContext, "Pet Requested Successfully", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@AdoptActivity, MyRequestActivity::class.java)
                    startActivity(intent)
                    // Finishing the current activity or process
                    finish()
                }else{
                    Toast.makeText(applicationContext, "Already Requested", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Adopt>, t: Throwable) {
                // Handling failure response
                binding.btnAdopt3.isEnabled = true
                // Showing a toast message indicating failure
                Toast.makeText(applicationContext, "Connection error", Toast.LENGTH_SHORT).show()
            }
        })
    }


}

