package com.example.pawandroid.bottomNav

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawandroid.FlexProfileActivity
import com.example.pawandroid.PostAdoptionActivity
import com.example.pawandroid.R
import com.example.pawandroid.adapter.HistoryAdapter
import com.example.pawandroid.adapter.PetFlexAdapter
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.ActivityHistoryBinding
import com.example.pawandroid.databinding.ActivityPetFlexBinding
import com.example.pawandroid.model.History
import com.example.pawandroid.model.PetSocial
import com.example.pawandroid.service.PawService
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetFlexActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetFlexBinding
    private lateinit var recyclerView : RecyclerView
    private lateinit var petFlexList :MutableList<PetSocial>
    private lateinit var petFlexAdapter: PetFlexAdapter
    private var floatingadd: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetFlexBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.posttoAdopt.setOnClickListener {
            val intent = Intent(this, FlexProfileActivity::class.java)
            intent.putExtra("key", floatingadd)
            startActivity(intent)

        }

        init()
        getPetSocial()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set the selected item in the BottomNavigationView
        bottomNavigation.selectedItemId =
            R.id.nav_fav// Assuming "Search" is the current activity

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
                R.id.nav_fav -> {
                    // Do nothing, already in PetFlexActivity
                    true
                }
                R.id.nav_history -> {
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
    private fun init(){
        recyclerView = binding.rvPetFlex
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        petFlexList = mutableListOf()
        petFlexAdapter = PetFlexAdapter(petFlexList, applicationContext)
        recyclerView.adapter = petFlexAdapter
    }

    private fun getPetSocial(){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.petsocial()
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