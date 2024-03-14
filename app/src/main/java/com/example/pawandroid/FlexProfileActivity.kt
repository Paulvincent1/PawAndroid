package com.example.pawandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.pawandroid.adapter.PetFlexAdapter
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityFlexProfileBinding
import com.example.pawandroid.model.PetSocial
import com.example.pawandroid.model.User
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FlexProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFlexProfileBinding
    private lateinit var recyclerView : RecyclerView
    private lateinit var petFlexList :MutableList<PetSocial>
    private lateinit var petFlexAdapter: PetFlexAdapter
    private var floatingadd: String? = null
    private var userId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlexProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCurrentUser()
        init()
        getPetSocial()

        binding.posttoAdopt.setOnClickListener {
            val intent = Intent(this, PostFlexActivity::class.java)
            intent.putExtra("key", floatingadd)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getPetSocial()
    }

    private fun getCurrentUser() {
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getCurrentUser()
        binding.progressBar10.visibility = View.VISIBLE
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                binding.progressBar10.visibility = View.GONE
                if (response.isSuccessful) {
                    val response = response?.body()

                    binding.textView.text = response?.name

                    userId = response?.id.toString()
                    val imgUrl = if (!response?.img.isNullOrEmpty()) {
                        "http://192.168.0.13/${response?.img}"
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
    private fun init(){
        recyclerView = binding.rvFlex
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        petFlexList = mutableListOf()
        petFlexAdapter = PetFlexAdapter(petFlexList, applicationContext)
        recyclerView.adapter = petFlexAdapter
    }
    private fun getPetSocial(){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.ownPosts()
        call.enqueue(object : Callback<List<PetSocial>> {
            override fun onResponse(
                call: Call<List<PetSocial>>,
                response: Response<List<PetSocial>>
            ) {
                if (response.isSuccessful){
                    val petsocial =  response.body()!!
                    petFlexList.clear()
                    petFlexList.addAll(petsocial)
                    petFlexAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<PetSocial>>, t: Throwable) {
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }

        })
    }

}