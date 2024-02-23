package com.example.pawandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityIncomingRequestDetailBinding
import com.example.pawandroid.model.Adopt
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IncomingRequestDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIncomingRequestDetailBinding
    private var id : String? = null
    private var userid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomingRequestDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("key")
        userid = intent.getStringExtra("userid")
        id?.let { getIncoming(it.toInt()) }

        binding.btnAcceptt.setOnClickListener {
            id?.let { it1 -> accept(it1.toInt()) }
        }
        binding.btnRejectt.setOnClickListener {
            id?.let { it1 -> reject(it1.toInt()) }
        }
        binding.btnViewProfilee.setOnClickListener {
            val intent = Intent(this, ViewProfileActivity::class.java)
            intent.putExtra("key", userid)
            startActivity(intent)
        }

    }
    private fun accept(id:Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.acceptIncoming(id)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Toast.makeText(applicationContext, "Accepted Successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun reject(id:Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.rejectIncoming(id)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Toast.makeText(applicationContext, "Rejected Successfully!", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }

        })
    }
    private fun getIncoming(id:Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getIncomingRequest(id)
        call.enqueue(object : Callback<Adopt> {
            override fun onResponse(call: Call<Adopt>, response: Response<Adopt>) {
                if (response.isSuccessful){
                    val pet = response.body()?.pet
                    val adopt = response.body()
                    val imageUrl = "http://192.168.43.156/${pet?.img}"
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
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }
        })

    }
}
