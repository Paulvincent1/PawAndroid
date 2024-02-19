package com.example.pawandroid.bottomNav

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawandroid.PetInfoActivity
import com.example.pawandroid.PostAdoptionActivity
import com.example.pawandroid.R
import com.example.pawandroid.adapter.HomeAdapter
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityMainBinding
import com.example.pawandroid.model.Pets
import com.example.pawandroid.service.PawService
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var petList: MutableList<Pets>
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var recyclerView: RecyclerView
    private var floatingadd: String? = null
    private var imgProfile: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.posttoAdopt.setOnClickListener {
            val intent = Intent(this, PostAdoptionActivity::class.java)
            intent.putExtra("key", floatingadd)
            startActivity(intent)

        }

        binding.imgProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("key", imgProfile)
            startActivity(intent)

        }


        init()
        getPetList()


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // No need to start MainActivity again, just stay in the current activity
                    true
                }
                R.id.nav_pet_profile -> {
                    startActivity(Intent(this, PetsActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_history -> {
                    // Start MainActivity and clear the back stack
                    val intent = Intent(this, HistoryActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish() // Finish PetsActivity to prevent returning to it when pressing back
                    overridePendingTransition(0, 0)
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

        // Set the default selected item
        bottomNavigation.selectedItemId = R.id.nav_home
    }
    private fun getPetList() {
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getPetsList()
        binding.progressBar3.visibility = View.VISIBLE
        call.enqueue(object : Callback<List<Pets>> {
            override fun onResponse(call: Call<List<Pets>>, response: Response<List<Pets>>) {
                binding.progressBar3.visibility = View.GONE
                if (response.isSuccessful) {
                    val petsResponse = response.body()
                    petsResponse?.let { pets ->
                        // Assuming petList is a member variable of your class
                        petList.clear() // Clear existing list before adding new items
                        petList.addAll(pets)
                        homeAdapter.notifyDataSetChanged()
                    } ?: run {
                        // Handle null response body
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<List<Pets>>, t: Throwable) {
                binding.progressBar3.visibility = View.GONE
                Toast.makeText(applicationContext, "No pets available", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun init() {
        recyclerView = binding.rvHome
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(applicationContext,2)
        petList = mutableListOf()
        homeAdapter = HomeAdapter(petList)
        recyclerView.adapter = homeAdapter
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
