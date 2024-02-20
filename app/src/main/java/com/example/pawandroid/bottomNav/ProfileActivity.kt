package com.example.pawandroid.bottomNav

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.pawandroid.AdoptionStatusActivity
import com.example.pawandroid.IncomingRequestActivity
import com.example.pawandroid.MyRequestActivity
import com.example.pawandroid.PostAdoptionActivity
import com.example.pawandroid.R
import com.example.pawandroid.YourAcceptedRequestActivity
import com.example.pawandroid.databinding.ActivityMainBinding
import com.example.pawandroid.databinding.ActivityProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private var floatingadd: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set the selected item in the BottomNavigationView
        bottomNavigation.selectedItemId =
            R.id.nav_profile// Assuming "Search" is the current activity

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
                    // Start MainActivity and clear the back stack
                    val intent = Intent(this, HistoryActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish() // Finish PetsActivity to prevent returning to it when pressing back
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.nav_profile -> {
                    true
                }

                else -> false
            }
        }


        val spinner: Spinner = findViewById(R.id.editText_giveUp)
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.giveUp_pet,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                var selectedItem = parent.getItemAtPosition(position).toString()
                // Check if the selected item matches the item you want to trigger navigation for
                if (selectedItem == "Post for Adoption") {
                    // Navigate to the other activity
                    val intent = Intent(this@ProfileActivity,PostAdoptionActivity::class.java)
                    startActivity(intent)
                    parent.setSelection(0)
                }
                if (selectedItem == "Incoming Request") {
                    // Navigate to the other activity
                    val intent = Intent(this@ProfileActivity,IncomingRequestActivity::class.java)
                    startActivity(intent)
                    parent.setSelection(0)
                }
                if (selectedItem == "Your Accepted Request") {
                    // Navigate to the other activity
                    val intent = Intent(this@ProfileActivity, YourAcceptedRequestActivity::class.java)
                    startActivity(intent)
                    parent.setSelection(0)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing if nothing is selected
            }
        }

        val outerSpinner: Spinner = findViewById(R.id.editText_AdoptR)
        val spinnerAdapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.AdoptPet,
            android.R.layout.simple_spinner_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        outerSpinner.adapter = spinnerAdapter


        outerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(spinner: AdapterView<*>, view: View?, position: Int, id: Long) {
                var selectedItemValue = spinner.getItemAtPosition(position).toString()
                // Check if the selected item matches the item you want to trigger navigation for
                if (selectedItemValue == "My Request"){
                    // Navigate to the other activity
                    val intent = Intent(this@ProfileActivity, MyRequestActivity::class.java)
                    startActivity(intent)
                    // Reset the selection to "Choose..."
                    outerSpinner.setSelection(0)
                }
                if (selectedItemValue == "Adoption Status"){
                    // Navigate to the other activity
                    val intent = Intent(this@ProfileActivity, AdoptionStatusActivity::class.java)
                    startActivity(intent)
                    // Reset the selection to "Choose..."
                    outerSpinner.setSelection(0)
                }
            }

            override fun onNothingSelected(spinner: AdapterView<*>?) {
                // Do nothing if nothing is selected
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