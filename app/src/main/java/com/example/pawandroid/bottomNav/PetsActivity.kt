package com.example.pawandroid.bottomNav

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pawandroid.PostAdoptionActivity
import com.example.pawandroid.R
import com.example.pawandroid.databinding.ActivityPetsActivityBinding
import com.example.pawandroid.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PetsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPetsActivityBinding
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