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
import com.example.pawandroid.PostAdoptionActivity
import com.example.pawandroid.R
import com.example.pawandroid.adapter.HomeAdapter
import com.example.pawandroid.adapter.PetsAdapter
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityPetsActivityBinding
import com.example.pawandroid.databinding.ActivityProfileBinding
import com.example.pawandroid.model.Pets
import com.example.pawandroid.service.PawService
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetsActivityBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var petProfileList : MutableList<Pets>
    private lateinit var petsAdapter: PetsAdapter
    private var floatingadd: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.posttoAdopt.setOnClickListener {
            val intent = Intent(this, PostAdoptionActivity::class.java)
            intent.putExtra("key", floatingadd)
            startActivity(intent)

        }

        init()
        getPetList()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set the selected item in the BottomNavigationView
        bottomNavigation.selectedItemId =
            R.id.nav_pet_profile// Assuming "Search" is the current activity

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
                    // Do nothing, already in PetsActivity
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
    }

    private fun getPetList() {
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.getPetsList()
        binding.progressBar6.visibility = View.VISIBLE
        call.enqueue(object : Callback<List<Pets>> {
            override fun onResponse(call: Call<List<Pets>>, response: Response<List<Pets>>) {
                binding.progressBar6.visibility = View.GONE
                if (response.isSuccessful) {
                    val petsResponse = response.body()
                    petsResponse?.let { pets ->
                        // Assuming petList is a member variable of your class
                        petProfileList.clear() // Clear existing list before adding new items
                        petProfileList.addAll(pets)
                        petsAdapter.notifyDataSetChanged()
                    } ?: run {
                        // Handle null response body
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<List<Pets>>, t: Throwable) {
                binding.progressBar6.visibility = View.GONE
                Toast.makeText(applicationContext, "No pets available", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun init() {
        recyclerView = binding.rvHome
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        petProfileList = mutableListOf()
        petsAdapter = PetsAdapter(petProfileList)
        recyclerView.adapter = petsAdapter
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