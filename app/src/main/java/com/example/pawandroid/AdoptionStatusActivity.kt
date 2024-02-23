package com.example.pawandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawandroid.adapter.AdoptionStatusAdapter
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityAdoptionStatusBinding
import com.example.pawandroid.model.Adopt
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdoptionStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdoptionStatusBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var yourRequestList: MutableList<Adopt>
    private lateinit var adoptionStatusAdapter: AdoptionStatusAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdoptionStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        getYourRequestList()


    }

    private fun init(){
        recyclerView = binding.rv
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        yourRequestList = mutableListOf()
        adoptionStatusAdapter = AdoptionStatusAdapter(yourRequestList)
        recyclerView.adapter = adoptionStatusAdapter
    }

    private fun getYourRequestList(){
        val retrofit =RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getYourRequestList()
        call.enqueue(object : Callback<List<Adopt>> {
            override fun onResponse(call: Call<List<Adopt>>, response: Response<List<Adopt>>) {
                if(response.isSuccessful){
                   yourRequestList.clear()
                    response.body()?.let { yourRequestList.addAll(it) }
                    adoptionStatusAdapter.notifyDataSetChanged()

                }
            }

            override fun onFailure(call: Call<List<Adopt>>, t: Throwable) {
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }

        })
    }
}