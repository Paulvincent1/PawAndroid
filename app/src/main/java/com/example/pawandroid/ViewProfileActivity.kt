package com.example.pawandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawandroid.adapter.HomeAdapter
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityViewProfileBinding
import com.example.pawandroid.model.Pets
import com.example.pawandroid.model.ViewProfile
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewProfileBinding
    private lateinit var petList: MutableList<Pets>
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("key")
        binding.emailText.text = userId

        init()
        userId?.let { getUserPets(it.toInt()) }
        userId?.let { getProfile(it.toInt()) }
        binding.backToBack.setOnClickListener {
            finish()
        }



    }
    private fun init() {
        recyclerView = binding.rvViewProfile
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(applicationContext,2)
        petList = mutableListOf()
        homeAdapter = HomeAdapter(petList)
        recyclerView.adapter = homeAdapter
    }

    private fun getUserPets(id:Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getUserPets(id)
        call.enqueue(object : Callback<List<Pets>> {
            override fun onResponse(call: Call<List<Pets>>, response: Response<List<Pets>>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    petList.clear()
                    petList.addAll(responseBody)
                    homeAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Pets>>, t: Throwable) {
                // Add logging to indicate failure
                Log.e("getUserPets", "Failed to get user pets", t)
                Toast.makeText(applicationContext, "bad", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun getProfile(id: Int) {
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getUser(id)
        call.enqueue(object : Callback<ViewProfile> {
            override fun onResponse(call: Call<ViewProfile>, response: Response<ViewProfile>) {
                if (response.isSuccessful) {
                    val viewProfile = response.body()
                    viewProfile?.let { profile ->
                        val user = profile.user
                        Toast.makeText(applicationContext, "good get profile", Toast.LENGTH_SHORT).show()
                        binding.tvNameProfile.text = user.name
                        binding.emailText.text = user.email
                        val imageUrl = "http://192.168.0.13/${user.img}"
                        Glide.with(applicationContext)
                            .load(imageUrl)
                            .into(binding.imageView)

                    }
                }
            }

            override fun onFailure(call: Call<ViewProfile>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }


}


