package com.example.pawandroid

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityPetsEditBinding
import com.example.pawandroid.model.PetResponse
import com.example.pawandroid.model.Pets
import com.example.pawandroid.service.PawService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PetsEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetsEditBinding
    private var btnCancel : String? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imagePart: MultipartBody.Part
    private var id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetsEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("key")
        id?.let { getPet(it.toInt()) }


        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.imgPetInfo.setOnClickListener {
            openImagePicker()
        }
        binding.btnDelete.setOnClickListener {
            id?.let { it1 -> deletePet(it1.toInt()) }
        }
        binding.btnUpdate.setOnClickListener {
            binding.apply {
                val name = editTextPetName.text.toString().trim()
                val age = editTextAge.text.toString().trim()
                val species = binding.editTextSpecies.selectedItem.toString() // Use selectedItem property to get the selected species
                val breed = editTextBreed.text.toString().trim()
                val gender = binding.editTextGender.selectedItem.toString() // Use selectedItem property to get the selected gender
                val region = binding.editTextProvinces.selectedItem.toString() // Use selectedItem property to get the selected region
                val description = editTextDesc.text.toString().trim()

                // Check if imagePart is initialized and not null
                if (::imagePart.isInitialized) {
                    id?.let { it1 -> updatePet(it1.toInt(),name, age.toInt(),species,breed,gender,region,description,imagePart) }

                } else {
                    Toast.makeText(applicationContext, "img not initialized", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun updatePet(id: Int, name: String, age: Int, species: String, breed: String, gender: String, region: String, description: String, imagePart: MultipartBody.Part) {
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val method = "PUT" // Set the method parameter to "PUT"
        val call = retrofit.updatePet(
            id,
            method,
            name.toRequestBody(),
            age,
            species.toRequestBody(),
            breed.toRequestBody(),
            gender.toRequestBody(),
            region.toRequestBody(),
            description.toRequestBody(),
            imagePart
        )
        call.enqueue(object : Callback<Pets> {
            override fun onResponse(call: Call<Pets>, response: Response<Pets>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Pets>, t: Throwable) {
                Log.e("potektalaga", "Failed to post pet", t)
                Toast.makeText(applicationContext, "Failed to post pet", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun deletePet(id:Int){
        var ret = RetrofitBuilder.buildService(PawService::class.java)
        val call = ret.deletePet(id)
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
    private fun getPet(id:Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getPet(id)
        call.enqueue(object : Callback<PetResponse> {
            override fun onResponse(call: Call<PetResponse>, response: Response<PetResponse>) {
                if (response.isSuccessful){
                    val pet = response.body()?.pet
                    binding.apply {
                        editTextPetName.setText(pet?.name)
                        editTextAge.setText(pet?.age.toString())
                        editTextBreed.setText(pet?.breed)
                        editTextDesc.setText(pet?.description)
                        val imageUrl = "http://192.168.1.107/${pet?.img}"
 //                          http://192.168.100.192/ , paul = http://192.168.0.13/
                        Glide.with(applicationContext)
                            .load(imageUrl)
                            .into(imgPetInfo)

                        val genderAdapter = ArrayAdapter.createFromResource(
                            applicationContext,
                            R.array.gender,
                            android.R.layout.simple_spinner_item
                        )
                        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.editTextGender.adapter = genderAdapter
                        val genderPosition = genderAdapter.getPosition(pet?.gender)
                        binding.editTextGender.setSelection(genderPosition)


                        val speciesAdapter = ArrayAdapter.createFromResource(
                            applicationContext,  // Use activity context instead of applicationContext
                            R.array.SpinnerSpecies,  // Change to your array resource for species options
                            android.R.layout.simple_spinner_item
                        )
                        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.editTextSpecies.adapter = speciesAdapter


                        val speciesPosition = speciesAdapter.getPosition(pet?.species)
                        binding.editTextSpecies.setSelection(speciesPosition)



                        val provinceAdapter = ArrayAdapter.createFromResource(
                            applicationContext,  // Use activity context instead of applicationContext
                            R.array.provinces_array,  // Change to your array resource for province options
                            android.R.layout.simple_spinner_item
                        )
                        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.editTextProvinces.adapter = provinceAdapter

                        val provincePosition = provinceAdapter.getPosition(pet?.region)
                        binding.editTextProvinces.setSelection(provincePosition)


                    }
                }
            }

            override fun onFailure(call: Call<PetResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            binding.imgPetInfo.setImageURI(imageUri)
            imageUri?.let { uri ->
                try {
                    val imageFile = File(getRealPathFromUri(uri)) // Convert URI to File
                    if (imageFile.exists()) {
                        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                        imagePart = MultipartBody.Part.createFormData("img", imageFile.name, requestBody)
                    } else {
                        Log.e("PostAdoptionActivity", "File does not exist")
                        Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("PostAdoptionActivity", "Failed to get file from URI", e)
                    Toast.makeText(this, "Failed to get file from URI", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun getRealPathFromUri(uri: Uri): String {
        var filePath = ""
        try {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    filePath = cursor.getString(columnIndex)
                    Log.d("shit", "File path: $filePath")
                } else {
                    Log.e("PostAdoptionActivity", "Cursor moveToFirst failed")
                }
            } ?: Log.e("PostAdoptionActivity", "Cursor is null")
        } catch (e: Exception) {
            Log.e("PostAdoptionActivity", "Failed to get file path from URI", e)
        }
        return filePath
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
}