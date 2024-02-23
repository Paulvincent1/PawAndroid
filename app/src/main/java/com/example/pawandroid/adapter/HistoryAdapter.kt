package com.example.pawandroid.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawandroid.IncomingRequestDetailActivity
import com.example.pawandroid.databinding.HistoryListBinding
import com.example.pawandroid.databinding.IncomingRequestListBinding
import com.example.pawandroid.model.Adopt
import com.example.pawandroid.model.History

data class HistoryAdapter  (var historylist: MutableList<History>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: HistoryListBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val view = HistoryListBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = historylist[position]

        holder.binding.apply {

            // Bind other views here if needed


            val imageUrl = "http://192.168.43.156/${currentItem.petimg}"
//                          http://192.168.100.192/ , paul = http://192.168.0.13/
            tvNamePet.text = currentItem.petname
            Glide.with(holder.itemView.context) // Use holder.itemView.context
                .load(imageUrl)
                .into(imageView)

            tvstatus.text = currentItem.status

            rvList.setOnClickListener {
                // Replace "urlToOpen" with the URL you want to open


                // Create an Intent with the ACTION_VIEW action
                val intent = Intent(android.content.Intent.ACTION_VIEW)

                intent.data = Uri.parse(currentItem.details)
                holder.itemView.context.startActivity(intent)
            }

        }

    }


    override fun getItemCount(): Int {
        return historylist.size
    }

}
