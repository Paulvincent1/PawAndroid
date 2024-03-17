package com.example.pawandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.pawandroid.adapter.MyRequestAdapter
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityMyRequestBinding
import com.example.pawandroid.model.Adopt
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyRequestBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adoptList : MutableList<Adopt>
    private lateinit var myRequestAdapter: MyRequestAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnBack.setOnClickListener {
            finish()

        }

        init()
        getRequestList()
    }

    override fun onResume() {
        super.onResume()
        getRequestList()
    }


    private fun init(){
        recyclerView = binding.rvMyRequest
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        adoptList = mutableListOf()
        myRequestAdapter = MyRequestAdapter(adoptList)
        recyclerView.adapter = myRequestAdapter

    }
    private fun getRequestList(){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getMyRequestList()
        Toast.makeText(applicationContext, "Get Pet", Toast.LENGTH_SHORT).show()
        binding.progressBar5.visibility = View.VISIBLE
        call.enqueue(object : Callback<List<Adopt>> {
            override fun onResponse(call: Call<List<Adopt>>, response: Response<List<Adopt>>) {
                binding.progressBar5.visibility = View.GONE
                if(response.isSuccessful){
                    val response = response.body()!!
                    adoptList.clear()
                    adoptList.addAll(response)
                    myRequestAdapter.notifyDataSetChanged()
                    if (response.isEmpty()){
                        Toast.makeText(applicationContext, "No Request Available", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            override fun onFailure(call: Call<List<Adopt>>, t: Throwable) {
                binding.progressBar5.visibility = View.GONE
                Toast.makeText(applicationContext, "Failed to retrieve items", Toast.LENGTH_SHORT).show()
            }

        })
    }

}