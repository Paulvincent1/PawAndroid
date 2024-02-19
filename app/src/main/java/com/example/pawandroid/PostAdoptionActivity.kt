package com.example.pawandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.pawandroid.adapter.SpeciesAdapter
import com.example.pawandroid.bottomNav.MainActivity
import com.example.pawandroid.databinding.ActivityPetsActivityBinding
import com.example.pawandroid.databinding.ActivityPostAdoptionBinding

class PostAdoptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostAdoptionBinding
    private var btnBack : String? = null
    private var btnCancel : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostAdoptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("key", btnBack)
            startActivity(intent)

        }
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("key", btnCancel)
            startActivity(intent)

        }


        val speciesSpinner = findViewById<Spinner>(R.id.editText_species)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val speciesAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.SpinnerSpecies,
            android.R.layout.simple_spinner_item
        )

        // Specify the layout to use when the list of choices appears
        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the spinner
        speciesSpinner.adapter = speciesAdapter

        // Set a prompt without adding it to the item list
        speciesSpinner.prompt = "Select Species"
    }

}
