package com.example.pawandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawandroid.databinding.HomeListBinding
import com.example.pawandroid.model.Pets

class HomeAdapter(val petlist: MutableList<Pets>): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: HomeListBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolder {
        val view = HomeListBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = petlist[position]

        holder.binding.apply {

            // Bind other views here if needed

            val imageUrl = "http://192.168.0.13/${currentItem.img}"
            tvPetName.text = currentItem.name
            Glide.with(holder.itemView.context) // Use holder.itemView.context
                .load(imageUrl)
                .into(ivPlace)

            ivPlace.setOnClickListener {

            }
        }

    }


    override fun getItemCount(): Int {
        return petlist.size
    }
}