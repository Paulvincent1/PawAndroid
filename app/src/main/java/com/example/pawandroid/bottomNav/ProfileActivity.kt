package com.example.pawandroid.bottomNav

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.pawandroid.AdoptionStatusActivity
import com.example.pawandroid.IncomingRequestActivity
import com.example.pawandroid.LoginActivity
import com.example.pawandroid.MyProfileActivity
import com.example.pawandroid.MyRequestActivity
import com.example.pawandroid.PostAdoptionActivity
import com.example.pawandroid.R
import com.example.pawandroid.YourAcceptedRequestActivity
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityMainBinding
import com.example.pawandroid.databinding.ActivityProfileBinding
import com.example.pawandroid.model.User
import com.example.pawandroid.model.ViewProfile
import com.example.pawandroid.service.PawService
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var floatingadd: String? = null
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imageUri: Uri
    private var userId: String? = null
    private lateinit var imagePart: MultipartBody.Part
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCurrentUser()

        binding.imageView.setOnClickListener {
            openImagePicker()

        }
        binding.tvProfile.setOnClickListener {
            val intent =Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }
        binding.logoutBtn.setOnClickListener {
            val intent =Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set the selected item in the BottomNavigationView
        bottomNavigation.selectedItemId =
            R.id.nav_profile// Assuming "Search" is the current activity

        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Start MainActivity and clear the back stack
                    val intent = Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish() // Finish PetsActivity to prevent returning to it when pressing back
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.nav_pet_profile -> {
                    val intent = Intent(this, PetsActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish() // Finish PetsActivity to prevent returning to it when pressing back
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_fav -> {
                    // Start MainActivity and clear the back stack
                    val intent = Intent(this, PetFlexActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish() // Finish PetsActivity to prevent returning to it when pressing back
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.nav_history -> {
                    // Start MainActivity and clear the back stack
                    val intent = Intent(this, HistoryActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish() // Finish PetsActivity to prevent returning to it when pressing back
                    overridePendingTransition(0, 0)
                    true
                }
               

                R.id.nav_profile -> {
                    true
                }

                else -> false
            }
        }


        val spinner: Spinner = findViewById(R.id.editText_giveUp)
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.giveUp_pet,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                var selectedItem = parent.getItemAtPosition(position).toString()
                // Check if the selected item matches the item you want to trigger navigation for
                if (selectedItem == "Post for Adoption") {
                    // Navigate to the other activity
                    val intent = Intent(this@ProfileActivity,PostAdoptionActivity::class.java)
                    startActivity(intent)
                    parent.setSelection(0)
                }
                if (selectedItem == "Incoming Request") {
                    // Navigate to the other activity
                    val intent = Intent(this@ProfileActivity,IncomingRequestActivity::class.java)
                    startActivity(intent)
                    parent.setSelection(0)
                }
                if (selectedItem == "Your Accepted Request") {
                    // Navigate to the other activity
                    val intent = Intent(this@ProfileActivity, YourAcceptedRequestActivity::class.java)
                    startActivity(intent)
                    parent.setSelection(0)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing if nothing is selected
            }
        }

        val outerSpinner: Spinner = findViewById(R.id.editText_AdoptR)
        val spinnerAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.AdoptPet,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        outerSpinner.adapter = spinnerAdapter


        outerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(spinner: AdapterView<*>, view: View?, position: Int, id: Long) {
                var selectedItemValue = spinner.getItemAtPosition(position).toString()
                // Check if the selected item matches the item you want to trigger navigation for
                if (selectedItemValue == "My Request"){
                    // Navigate to the other activity
                    val intent = Intent(this@ProfileActivity, MyRequestActivity::class.java)
                    startActivity(intent)
                    // Reset the selection to "Choose..."
                    outerSpinner.setSelection(0)
                }
                if (selectedItemValue == "Adoption Status"){
                    // Navigate to the other activity
                    val intent = Intent(this@ProfileActivity, AdoptionStatusActivity::class.java)
                    startActivity(intent)
                    // Reset the selection to "Choose..."
                    outerSpinner.setSelection(0)
                }
            }

            override fun onNothingSelected(spinner: AdapterView<*>?) {
                // Do nothing if nothing is selected
            }
        }


    }
    private fun getCurrentUser(){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getCurrentUser()
        binding.progressBar7.visibility = View.VISIBLE
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                binding.progressBar7.visibility = View.GONE
                if(response.isSuccessful){
                    val response = response?.body()

                    binding.textView.text = response?.name

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
                binding.progressBar7.visibility = View.GONE
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun updateUserProfile(id:Int, imagePart: MultipartBody.Part){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        binding.progressBar7.visibility = View.VISIBLE
        val method = "PUT"
        val call = retrofit.updateUserProfile(id,method,imagePart)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                binding.progressBar7.visibility = View.GONE
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, "Finished", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                binding.progressBar7.visibility = View.GONE
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }

        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            binding.imageView.setImageURI(imageUri)
            val imageFile = File(imageUri?.let { getRealPathFromUri(it) }) // Convert URI to File
            val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())

            imagePart = MultipartBody.Part.createFormData("img", imageFile.name, requestBody)

            userId?.let { updateUserProfile(it.toInt(), imagePart) }
        }
    }

    private fun getRealPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val path = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return path ?: ""
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onBackPressed() {
        showExitConfirmationDialog()
    }
    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit Confirmation")
        builder.setMessage("Are you sure you want to exit the app?")
        builder.setPositiveButton("Yes") { _, _ ->
            // User confirmed exit, finish the current activity to exit the app
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            // User canceled exit, dismiss the dialog
            dialog.dismiss()
            // Perform any additional actions if needed
        }
        builder.setOnCancelListener {
            // Handle the case when the user cancels the dialog with the back button
            // Perform any additional actions if needed
        }

        val dialog = builder.create()
        dialog.show()
    }
}