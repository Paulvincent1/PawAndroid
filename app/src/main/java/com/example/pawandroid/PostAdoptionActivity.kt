package com.example.pawandroid

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.pawandroid.bottomNav.MainActivity
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityPostAdoptionBinding
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

class PostAdoptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostAdoptionBinding
    private var btnBack : String? = null
    private var btnCancel : String? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imagePart: MultipartBody.Part // Added to hold the imagePart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAdoptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("key", btnBack)
            startActivity(intent)

        }
        binding.btnCancel.setOnClickListener {
           finish()
        }
        binding.imgPetInfo.setOnClickListener {
            openImagePicker()
        }
        binding.btnPost.setOnClickListener {
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
                    Toast.makeText(applicationContext, "hi", Toast.LENGTH_SHORT).show()
                    postPet(name, age.toInt(),species,breed,gender,region,description,imagePart)

                } else {
                    Toast.makeText(applicationContext, "img not initialized", Toast.LENGTH_SHORT).show()
                }
            }
        }





        val speciesSpinner = findViewById<Spinner>(R.id.editText_species)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val speciesAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.SpinnerSpecies,
            android.R.layout.simple_spinner_item
        )

        // Specify the layout to use when the list of choices appears
        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the spinner
        speciesSpinner.adapter = speciesAdapter

        // Set a prompt without adding it to the item list
        speciesSpinner.prompt = "Select Species"
    }

    private fun postPet(name: String, age: Int, species: String, breed: String, gender: String, region: String, description: String, imagePart: MultipartBody.Part){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.postForAdoption(
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
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, "Posted Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Pets>, t: Throwable) {
                Log.e("potekTalaga", "Failed to post pet", t)
                Toast.makeText(applicationContext, "Failed to post pet", Toast.LENGTH_SHORT).show()
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
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
}
