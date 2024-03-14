package com.example.pawandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityFlexProfileBinding
import com.example.pawandroid.model.User
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FlexProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFlexProfileBinding
    private var floatingadd: String? = null
    private var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlexProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCurrentUser()


        binding.posttoAdopt.setOnClickListener {
            val intent = Intent(this, PostFlexActivity::class.java)
            intent.putExtra("key", floatingadd)
            startActivity(intent)
        }
    }


    private fun getCurrentUser(){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getCurrentUser()
        binding.progressBar10.visibility = View.VISIBLE
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                binding.progressBar10.visibility = View.GONE
                if(response.isSuccessful){
                    val response = response?.body()

                    binding.textView.text = response?.name

                    userId = response?.id.toString()
                    val imgUrl = if (!response?.img.isNullOrEmpty()) {
                        "http://192.168.1.107/${response?.img}"
                        // paul =  http://192.168.0.13/
                        //  nath =  http://192.168.100.192/
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
                binding.progressBar10.visibility = View.GONE
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }

        })
    }


}