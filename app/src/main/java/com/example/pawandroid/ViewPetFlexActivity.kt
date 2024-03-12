package com.example.pawandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.pawandroid.databinding.ActivityViewPetFlexBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ViewPetFlexActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewPetFlexBinding
    private var id: String? = null
    private var userId: String? = null
    private var imageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPetFlexBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val view: View = layoutInflater.inflate(R.layout.item_bottom_sheet_report_for_flex, null)
        val dialog = BottomSheetDialog(this)

        binding.btnReport.setOnClickListener {
            dialog.setContentView(view)
            dialog.show()

            // Access TextViews only after layout inflation
            val reason1 = view.findViewById<TextView>(R.id.tvReason1)
            val reason2 = view.findViewById<TextView>(R.id.tvReason2)
            val reason3 = view.findViewById<TextView>(R.id.tvReason3)


        }
    }
}
