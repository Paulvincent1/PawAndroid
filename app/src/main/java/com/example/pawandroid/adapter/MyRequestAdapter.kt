package com.example.pawandroid.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawandroid.MyRequestEditActivity
import com.example.pawandroid.PetInfoActivity
import com.example.pawandroid.databinding.MyRequestListBinding
import com.example.pawandroid.model.Adopt

class MyRequestAdapter(val requestlist: MutableList<Adopt>): RecyclerView.Adapter<MyRequestAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: MyRequestListBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRequestAdapter.ViewHolder {
        val view = MyRequestListBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = requestlist[position]

        holder.binding.apply {

            // Bind other views here if needed
            val pet = currentItem.pet
            val img = pet.img
            val imageUrl = "https://pawadoptpaw.online/${img}"
//                          http://192.168.100.192/, paul =http://192.168.0.13/
            tvNameRequest.text = currentItem.name
            tvAddress.text = currentItem.address
            Glide.with(holder.itemView.context) // Use holder.itemView.context
                .load(imageUrl)
                .into(imgRequest)


            button3.setOnClickListener {
                val intent = Intent(holder.itemView.context, MyRequestEditActivity::class.java)
                intent.putExtra("key", currentItem.id.toString())
                intent.putExtra("imageUrl", imageUrl)
                holder.itemView.context.startActivity(intent)
            }

        }

    }


    override fun getItemCount(): Int {
        return requestlist.size
    }
}