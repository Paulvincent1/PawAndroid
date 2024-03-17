package com.example.pawandroid

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityPetFlexEditBinding
import com.example.pawandroid.model.PetSocial
import com.example.pawandroid.model.User
import com.example.pawandroid.service.PawService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PetFlexEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetFlexEditBinding
    private var id: String? = null
    private var userId: String? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imagePart: MultipartBody.Part // Added to hold the imagePart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetFlexEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("key")
        id?.let { editPost(it.toInt()) }
        getCurrentUser()

        binding.apply {
            postBtn.setOnClickListener {
                val caption = editTextDesc.text.toString().trim()
                // Check if imagePart is initialized and not null
                if (::imagePart.isInitialized) {
                    Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                    id?.let { it1 -> update(it1.toInt(),caption,imagePart) }

                } else {
                    Toast.makeText(applicationContext, "Put Image", Toast.LENGTH_SHORT).show()
                }
            }
            btnDelete.setOnClickListener {
                id?.let { it1 -> deletePost(it1.toInt()) }
            }
            putImage.setOnClickListener {
                openImagePicker()
            }
           imageView4.setOnClickListener {
                finish()
            }
        }

    }

    private fun editPost(id:Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.editPost(id)
        binding.progressBar12.visibility =View.VISIBLE
        call.enqueue(object : Callback<PetSocial> {
            override fun onResponse(call: Call<PetSocial>, response: Response<PetSocial>) {
                binding.progressBar12.visibility =View.GONE
                if(response.isSuccessful){
                    val post = response.body()
                    binding.editTextDesc.setText(post?.caption)
                    val imageUrl = "https://pawadoptpaw.online/${post?.img}"
                    //                           , paul = https://pawadoptpaw.online/
                    Glide.with(applicationContext)
                        .load(imageUrl)
                        .into(binding.imgPetInfo)
                }
            }

            override fun onFailure(call: Call<PetSocial>, t: Throwable) {
                binding.progressBar12.visibility =View.GONE
                Toast.makeText(applicationContext, "Failed to Load", Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun update(id:Int, caption: String, img: MultipartBody.Part){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val method = "PUT"
        val call = retrofit.updatePost(id,method,caption.toRequestBody(),img)
        binding.progressBar12.visibility =View.VISIBLE
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                binding.progressBar12.visibility =View.GONE
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                binding.progressBar12.visibility =View.GONE
                Toast.makeText(applicationContext, "Failed to Update", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun deletePost(id: Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.deletePost(id)
        binding.progressBar12.visibility = View.VISIBLE
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                binding.progressBar12.visibility =View.GONE
                if(response.isSuccessful){
                    Toast.makeText(applicationContext, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                binding.progressBar12.visibility =View.GONE
                Toast.makeText(applicationContext, "Failed to delete", Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun getCurrentUser(){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getCurrentUser()
        binding.progressBar12.visibility = View.VISIBLE
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                binding.progressBar12.visibility = View.GONE
                if(response.isSuccessful){
                    val response = response?.body()

                    binding.textView16.text = response?.name

                    userId = response?.id.toString()
                    val imgUrl = if (!response?.img.isNullOrEmpty()) {
                        "https://pawadoptpaw.online/${response?.img}"
                        // paul =  https://pawadoptpaw.online/
                        //  nath =  
                    } else {
                        // Replace "default_image_url" with the resource ID of your default image
                        "https://static-00.iconduck.com/assets.00/profile-circle-icon-2048x2048-cqe5466q.png"
                    }
                    Glide.with(applicationContext)
                        .load(imgUrl)
                        .transform(CircleCrop())
                        .into(binding.imageView)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                binding.progressBar12.visibility = View.GONE
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
                    Log.d("Initiate", "File path: $filePath")
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