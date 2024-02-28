package com.example.pawandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityIncomingRequestDetailBinding
import com.example.pawandroid.databinding.ActivityYourAcceptedRequestDetailBinding
import com.example.pawandroid.model.Adopt
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YourAcceptedRequestDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityYourAcceptedRequestDetailBinding
    private var id : String? = null
    private var userid : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYourAcceptedRequestDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("key")
        userid = intent.getStringExtra("userid")
        id?.let { onGoingRequestDetail(it.toInt()) }

        binding.btnDone.setOnClickListener {
            id?.let { it1 -> done(it1.toInt()) }
        }
        binding.btnViewProfilee.setOnClickListener {
            val intent = Intent(this, ViewProfileActivity::class.java)
            intent.putExtra("key", userid)
            startActivity(intent)
        }
    }
    private fun done(id: Int){
        val retrofit= RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.doneRequest(id)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, "Request Done Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun onGoingRequestDetail(id :Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.onGoingRequestDetail(id)
        call.enqueue(object : Callback<Adopt> {
            override fun onResponse(call: Call<Adopt>, response: Response<Adopt>) {
                if (response.isSuccessful){
                    Toast.makeText(applicationContext, "good", Toast.LENGTH_SHORT).show()

                    val pet = response.body()?.pet
                    val adopt = response.body()
                    val imageUrl = "http://192.168.100.192/${pet?.img}"
//                          http://192.168.100.192/ , paul = http://192.168.0.13/
                    Glide.with(applicationContext)
                        .load(imageUrl)
                        .into(binding.imageView3)
                    binding.tvPetnamee.text = pet?.name
                    binding.tvRequesterNamee.text = adopt?.name
                    binding.tvAddressss.text = adopt?.address
                    binding.TvContactNumber.text= adopt?.contact_number
                    binding.tvCity.text = adopt?.city
                    binding.tvAddtionalComment.text = adopt?.additional_comment
                }
            }

            override fun onFailure(call: Call<Adopt>, t: Throwable) {
                Toast.makeText(applicationContext, "bad", Toast.LENGTH_SHORT).show()
            }

        })
    }
}