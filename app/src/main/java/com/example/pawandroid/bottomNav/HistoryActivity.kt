package com.example.pawandroid.bottomNav

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawandroid.PostAdoptionActivity
import com.example.pawandroid.R
import com.example.pawandroid.adapter.HistoryAdapter
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityHistoryBinding
import com.example.pawandroid.databinding.ActivityMainBinding
import com.example.pawandroid.model.History
import com.example.pawandroid.service.PawService
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : AppCompatActivity() {
    private var floatingadd: String? = null
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyList: MutableList<History>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.posttoAdopt.setOnClickListener {
            val intent = Intent(this, PostAdoptionActivity::class.java)
            intent.putExtra("key", floatingadd)
            startActivity(intent)

        }
        init()
        getHistoryList()



        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set the selected item in the BottomNavigationView
        bottomNavigation.selectedItemId =
            R.id.nav_history// Assuming "Search" is the current activity

        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Start MainActivity and clear the back stack
                    val intent = Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish() // Finish PetsActivity to prevent returning to it when pressing back
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_pet_profile -> {
                    val intent = Intent(this, PetsActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish() // Finish PetsActivity to prevent returning to it when pressing back
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_history -> {
                    true
                }
                R.id.nav_profile -> {
                    // Start MainActivity and clear the back stack
                    val intent = Intent(this, ProfileActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish() // Finish PetsActivity to prevent returning to it when pressing back
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }
    private fun init(){
        recyclerView = binding.rvHistory
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        historyList = mutableListOf()
        historyAdapter = HistoryAdapter(historyList)
        recyclerView.adapter = historyAdapter
    }
    private fun getHistoryList(){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.history()
        call.enqueue(object : Callback<List<History>> {
            override fun onResponse(call: Call<List<History>>, response: Response<List<History>>) {
                if(response.isSuccessful){
                    historyList.clear()
                    response.body()?.let { historyList.addAll(it) }
                    historyAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<List<History>>, t: Throwable) {
                Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
            }

        })
    }
    override fun onBackPressed() {
        showExitConfirmationDialog()
    }
    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit Confirmation")
        builder.setMessage("Are you sure you want to exit the app?")
        builder.setPositiveButton("Yes") { _, _ ->
            // User confirmed exit, finish the current activity to exit the app
            finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            // User canceled exit, dismiss the dialog
            dialog.dismiss()
            // Perform any additional actions if needed
        }
        builder.setOnCancelListener {
            // Handle the case when the user cancels the dialog with the back button
            // Perform any additional actions if needed
        }

        val dialog = builder.create()
        dialog.show()
    }
}