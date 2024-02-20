package com.example.pawandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityMyRequestEditBinding
import com.example.pawandroid.model.Adopt
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRequestEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyRequestEditBinding
    private var id : String? = null
    private var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyRequestEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("key")
        imageUrl= intent.getStringExtra("imageUrl")
        Glide.with(applicationContext)
            .load(imageUrl)
            .into(binding.imgPetInfo)
        id?.let { getMyRequest(it.toInt()) }

        binding.btnUpdate.setOnClickListener{
            binding.apply {

                val name = editTextFullNameEdit.text.toString().trim()
                val address = editTextAddressEdit.text.toString().trim()
                val city = editTextCityEdit.text.toString().trim()
                val contact = editTextContactNumberEdit.text.toString().trim()
                val veterinaryInformationChecked = checkBoxVeterinaryCareEdit.isChecked
                val adoptionAgreementChecked = textViewCommitmentEdit.isChecked // Corrected variable name
                val additionalComment = editTextWhyAdoptEdit.text.toString().trim()

                if (name.isEmpty()) {
                    editTextFullNameEdit.error = "Please enter your name"
                    Toast.makeText(applicationContext, "Please enter your name", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (address.isEmpty()) {
                    editTextAddressEdit.error = "Please enter your address"
                    Toast.makeText(applicationContext, "Please enter your address", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (city.isEmpty()) {
                    editTextCityEdit.error = "Please enter your city"
                    Toast.makeText(applicationContext, "Please enter your city", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (contact.isEmpty()) {
                    editTextContactNumberEdit.error = "Please enter your contact number"
                    Toast.makeText(applicationContext, "Please enter your contact number", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!veterinaryInformationChecked) {
                    checkBoxVeterinaryCareEdit.error = "Please confirm veterinary information"
                    Toast.makeText(applicationContext, "Please confirm veterinary information", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!adoptionAgreementChecked) {
                    textViewCommitmentEdit.error = "Please agree to adoption agreement"
                    Toast.makeText(applicationContext, "Please agree to adoption agreement", Toast.LENGTH_SHORT).show()// Corrected error message
                    return@setOnClickListener
                }

                if (additionalComment.isEmpty()) {
                    editTextWhyAdoptEdit.error = "Please provide additional comments"
                    Toast.makeText(applicationContext, "Please provide additional comments", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                id?.let { it1 -> updateRequest(it1.toInt(),name, address, city, contact, "true" ,"true", additionalComment) }

            }

        }

        binding.btnDelete.setOnClickListener {
            id?.let { it1 -> deleteRequest(it1.toInt()) }
        }
    }

    private fun getMyRequest(id: Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getMyRequest(id)
        binding.progressBar11.visibility = View.VISIBLE
        call.enqueue(object : Callback<Adopt> {
            override fun onResponse(call: Call<Adopt>, response: Response<Adopt>) {
                binding.progressBar11.visibility = View.GONE
                if (response.isSuccessful){
                    binding.apply {
                        val responseData = response.body()
                        if (responseData != null) {
                            editTextFullNameEdit.setText(responseData.name)
                            editTextAddressEdit.setText(responseData.address)
                            editTextCityEdit.setText(responseData.city)
                            editTextContactNumberEdit.setText(responseData.contact_number)
                            checkBoxVeterinaryCareEdit.isChecked = responseData.veterinary_information
                            textViewCommitmentEdit.isChecked = responseData.adoption_agreement
                            editTextWhyAdoptEdit.setText(responseData.additional_comment)
                        }
                    }

                }
            }

            override fun onFailure(call: Call<Adopt>, t: Throwable) {
                binding.progressBar11.visibility = View.GONE
                Toast.makeText(applicationContext, "Failed to retrieve", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateRequest( id: Int,
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
        val call = retrofit.updateAdoptionRequest(
            id,
            name,
            address,
            city,
            contact_number,
            veterinary_information,
            adoption_agreement,
            additional_comment
        )
        call.enqueue(object : Callback<Adopt> {
            override fun onResponse(call: Call<Adopt>, response: Response<Adopt>) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Adopt>, t: Throwable) {
                Toast.makeText(applicationContext, "Update Failed", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun deleteRequest(id: Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.deleteRequest(id)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }
}