package com.example.pawandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawandroid.adapter.IncomingRequestAdapter
import com.example.pawandroid.adapter.YourAcceptedRequestAdapter
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityYourAcceptedRequestBinding
import com.example.pawandroid.model.Adopt
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YourAcceptedRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityYourAcceptedRequestBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var yourAcceptedRequestAdapter: YourAcceptedRequestAdapter
    private lateinit var adoptList: MutableList<Adopt>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYourAcceptedRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        getOngoingList()
    }
    private fun init(){
        recyclerView = binding.rvYourAccepted
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        adoptList = mutableListOf()
        yourAcceptedRequestAdapter = YourAcceptedRequestAdapter(adoptList)
        recyclerView.adapter = yourAcceptedRequestAdapter
    }
    private fun getOngoingList(){
        val r = RetrofitBuilder.buildService(PawService::class.java)
        val c = r.onGoingRequestList()
        c.enqueue(object : Callback<List<Adopt>> {
            override fun onResponse(call: Call<List<Adopt>>, response: Response<List<Adopt>>) {
                if (response.isSuccessful){
                    adoptList.clear()
                    response.body()?.let { adoptList.addAll(it) }
                    yourAcceptedRequestAdapter.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<List<Adopt>>, t: Throwable) {
            }

        })
    }
}