package com.example.pawandroid.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawandroid.PetFlexEditActivity
import com.example.pawandroid.R
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.PetFlexIndexListBinding
import com.example.pawandroid.databinding.PetFlexListBinding
import com.example.pawandroid.model.PetSocial
import com.example.pawandroid.service.PawService
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetFlexIndexAdapter(val petFlexList: MutableList<PetSocial>, val context: Context): RecyclerView.Adapter<PetFlexIndexAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PetFlexIndexListBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetFlexIndexAdapter.ViewHolder {
        val view = PetFlexIndexListBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = petFlexList[position]

        holder.binding.apply {

            // Bind other views here if needed

            val postImg = currentItem.img
            val postUrlImg = "https://pawadoptpaw.online/${postImg}"
//                          , paul =https://pawadoptpaw.online/
            Glide.with(holder.itemView.context) // Use holder.itemView.context
                .load(postUrlImg)
                .into(imgPetProfile)
            contentFlex.text = currentItem.caption


            val userUrlImg = if (!currentItem.user.img.isNullOrEmpty()) {
                 "https://pawadoptpaw.online/${currentItem.user.img}"
                // paul =  https://pawadoptpaw.online/
                //  nath =
            } else {
                // Replace "default_image_url" with the resource ID of your default image
                "https://static-00.iconduck.com/assets.00/profile-circle-icon-2048x2048-cqe5466q.png"
            }

            tvNameFlex.text = currentItem.user.name
            Glide.with(holder.itemView.context) // Use holder.itemView.context
                .load(userUrlImg)
                .into(imgProfile)

            likecount.text = currentItem.like_count.toString()
            val id = currentItem.id
            when (currentItem.is_Liked) {
                0 -> {
                    img1.setImageResource(R.drawable.baseline_favorite_24)
                    img1.setOnClickListener {
                        img1.setImageResource(R.drawable.heart_fill)
                        currentItem.like_count += 1
                        likecount.text = currentItem.like_count.toString()
                        like(id)
                        currentItem.is_Liked = 1
                        notifyItemChanged(position)
                    }
                }
                1 -> {
                    img1.setImageResource(R.drawable.heart_fill)
                    img1.setOnClickListener {
                        img1.setImageResource(R.drawable.baseline_favorite_24)
                        currentItem.like_count -= 1
                        likecount.text = currentItem.like_count.toString()
                        unlike(id)
                        currentItem.is_Liked = 0
                        notifyItemChanged(position)
                    }
                }
            }
            // Access the layout inflater from the context
            val inflater = LayoutInflater.from(holder.itemView.context)
            val view: View = inflater.inflate(R.layout.item_bottom_sheet_report, null)
            val dialog = BottomSheetDialog(holder.itemView.context)

            report.setOnClickListener {
                dialog.setContentView(view)
                dialog.show()

                // Access TextViews only after layout inflation
                val reason1 = view.findViewById<TextView>(R.id.tvReason1)
                val reason2 = view.findViewById<TextView>(R.id.tvReason2)
                val reason3 = view.findViewById<TextView>(R.id.tvReason3)

                // Set click listeners after TextViews are initialized
                reason1?.setOnClickListener {
                    report(currentItem.id,"Nudity or sexual activity")
                    dialog.dismiss()
                }
                reason2?.setOnClickListener {
                    report(currentItem.id,"Scam of fraud")
                    dialog.dismiss()
                }
                reason3?.setOnClickListener {
                    report(currentItem.id,"Others")

                    dialog.dismiss()
                }

            }





        }

    }


    override fun getItemCount(): Int {
        return petFlexList.size
    }
    fun like(id: Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.like(id)
        val heartEmoticon = "\u2764"
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful){
                    Toast.makeText(context, "$heartEmoticon", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun unlike(id: Int){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.unlike(id)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful){
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun report(id:Int, reason: String){
        val retrofit = RetrofitBuilder.buildService(PawService::class.java)
        val call = retrofit.reportPostFlex(id, reason)
        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful){
                    Toast.makeText(context, "Thanks for letting us know!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(context, "failed to report", Toast.LENGTH_SHORT).show()
            }
        })

    }

}