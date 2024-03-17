package com.example.pawandroid.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawandroid.IncomingRequestDetailActivity
import com.example.pawandroid.R
import com.example.pawandroid.databinding.AdoptionStatusListBinding
import com.example.pawandroid.databinding.IncomingRequestListBinding
import com.example.pawandroid.model.Adopt

class AdoptionStatusAdapter  (var adoptlist: MutableList<Adopt>): RecyclerView.Adapter<AdoptionStatusAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: AdoptionStatusListBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdoptionStatusAdapter.ViewHolder {
        val view = AdoptionStatusListBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ViewHolder(view)
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = adoptlist[position]

        holder.binding.apply {

            // Bind other views here if needed
            val pet = currentItem.pet

            val imageUrl = "https://pawadoptpaw.online/${pet.img}"
//                           , paul = https://pawadoptpaw.online/
            tvNamePet.text = pet.name
            Glide.with(holder.itemView.context) // Use holder.itemView.context
                .load(imageUrl)
                .into(imageView)

            tvRequesterName.text = currentItem.name
            when(currentItem.status){
                "accepted" ->{
                    tvStatus.text = "Accepted"
                    tvStatus.setTextColor(R.color.green)
                }
                "rejected"-> {
                    tvStatus.text = "Rejected"
                    tvStatus.setTextColor(R.color.red)
                }

            }
            tvStatus.text = currentItem.status


        }

    }


    override fun getItemCount(): Int {
        return adoptlist.size
    }

}