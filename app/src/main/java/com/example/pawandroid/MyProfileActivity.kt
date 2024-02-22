package com.example.pawandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawandroid.adapter.HomeAdapter
import com.example.pawandroid.adapter.MyProfileAdapter
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityMyProfileBinding
import com.example.pawandroid.model.Pets
import com.example.pawandroid.model.User
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyProfileBinding
    private lateinit var petList: MutableList<Pets>
    private lateinit var myProfileAdapter: MyProfileAdapter
    private lateinit var recyclerView: RecyclerView
    private var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        getCurrentUser()

        binding.tvViewProfile.text = userId



    }

    override fun onResume() {
        super.onResume()
        getCurrentUser()
    }
    private fun init() {
        recyclerView = binding.rv
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(applicationContext,2)
        petList = mutableListOf()
        myProfileAdapter = MyProfileAdapter(petList)
        recyclerView.adapter = myProfileAdapter
    }
    private fun getUserPets(id:Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getUserPets(id)
        binding.progressBar9.visibility = View.VISIBLE
        call.enqueue(object : Callback<List<Pets>> {
            override fun onResponse(call: Call<List<Pets>>, response: Response<List<Pets>>) {
                binding.progressBar9.visibility = View.GONE
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    petList.clear()
                    petList.addAll(responseBody)
                    myProfileAdapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext, "good", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Pets>>, t: Throwable) {
                binding.progressBar9.visibility = View.GONE
                // Add logging to indicate failure
                Toast.makeText(applicationContext, "bad", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun getCurrentUser(){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getCurrentUser()
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    val response = response?.body()

                    userId = response?.id.toString()
                    binding.tvViewProfile.text = response?.name

                    getUserPets(userId!!.toInt())

                    val imgUrl = if (!response?.img.isNullOrEmpty()) {
                        "http://192.168.0.13/${response?.img}"
                        // paul =  http://192.168.0.13/
                        //  nath =  http://192.168.100.192/
                    } else {
                        // Replace "default_image_url" with the resource ID of your default image
                        "https://static-00.iconduck.com/assets.00/profile-circle-icon-2048x2048-cqe5466q.png"
                    }
                    Glide.with(applicationContext)
                        .load(imgUrl
                        )
                        .into(binding.imageView2)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }

        })
    }
}