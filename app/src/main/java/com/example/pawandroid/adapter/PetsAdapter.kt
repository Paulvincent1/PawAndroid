package com.example.pawandroid.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawandroid.PetInfoActivity
import com.example.pawandroid.R
import com.example.pawandroid.databinding.HomeListBinding
import com.example.pawandroid.databinding.PetProfileListBinding
import com.example.pawandroid.model.Pets

class PetsAdapter(var petlist: MutableList<Pets>): RecyclerView.Adapter<PetsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PetProfileListBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetsAdapter.ViewHolder {
        val view = PetProfileListBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = petlist[position]

        holder.binding.apply {

            // Bind other views here if needed

            val imageUrl = "https://pawadoptpaw.online/${currentItem.img}"
//                          http://192.168.100.192/ , paul = "http://192.168.0.13/
            tvNameRequest.text = currentItem.name
            tvage.text = currentItem.age.toString()
            tvbreed.text = currentItem.breed
            Glide.with(holder.itemView.context) // Use holder.itemView.context
                .load(imageUrl)
                .into(imgPetProfile)

            rvClick.setOnClickListener {
                val intent = Intent(holder.itemView.context, PetInfoActivity::class.java)
                intent.putExtra("key", currentItem.id.toString())
                holder.itemView.context.startActivity(intent)
            }

            when (currentItem.species) {
                "Dog" -> {
                    img1.setImageResource(R.drawable.hachico)
                }

                "Cat" -> {
                    img1.setImageResource(R.drawable.kitty)
                }
            }
        }

    }


    override fun getItemCount(): Int {
        return petlist.size
    }
    fun updateData(newDataset: List<Pets>) {
        petlist = newDataset as MutableList<Pets>
        notifyDataSetChanged() // Notify the adapter that the dataset has changed
    }
}