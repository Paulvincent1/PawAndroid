package com.example.pawandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawandroid.adapter.IncomingRequestAdapter
import com.example.pawandroid.bottomNav.MainActivity
import com.example.pawandroid.bottomNav.ProfileActivity
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityIncomingRequestBinding
import com.example.pawandroid.model.Adopt
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IncomingRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIncomingRequestBinding
    private lateinit var recyclerView: RecyclerView
    private var btnBack : String? = null
    private lateinit var incomingRequestDetailAdapter: IncomingRequestAdapter
    private lateinit var adoptList: MutableList<Adopt>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomingRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        incomingRequestList()

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("key", btnBack)
            startActivity(intent)

        }
    }

    override fun onResume() {
        super.onResume()
        incomingRequestList()
    }
    private fun init(){
        recyclerView = binding.rvIncoming
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        adoptList = mutableListOf()
        incomingRequestDetailAdapter = IncomingRequestAdapter(adoptList)
        recyclerView.adapter = incomingRequestDetailAdapter
    }


    private fun incomingRequestList(){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getIncomingRequestList()
        call.enqueue(object : Callback<List<Adopt>> {
            override fun onResponse(call: Call<List<Adopt>>, response: Response<List<Adopt>>) {
                if(response.isSuccessful){
                    adoptList.clear()
                    response.body()?.let { adoptList.addAll(it) }
                    incomingRequestDetailAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<Adopt>>, t: Throwable) {
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }

        })
    }
}