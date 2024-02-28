package com.example.pawandroid.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawandroid.IncomingRequestDetailActivity
import com.example.pawandroid.YourAcceptedRequestDetailActivity
import com.example.pawandroid.databinding.IncomingRequestListBinding
import com.example.pawandroid.model.Adopt

class YourAcceptedRequestAdapter(var adoptlist: MutableList<Adopt>): RecyclerView.Adapter<YourAcceptedRequestAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: IncomingRequestListBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourAcceptedRequestAdapter.ViewHolder {
        val view = IncomingRequestListBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = adoptlist[position]

        holder.binding.apply {

            // Bind other views here if needed
            val pet = currentItem.pet

            val imageUrl = "http://192.168.100.192/${pet.img}"
//                          http://192.168.100.192/ , paul = http://192.168.0.13/
            tvNamePet.text = pet.name
            Glide.with(holder.itemView.context) // Use holder.itemView.context
                .load(imageUrl)
                .into(imageView)

            tvRequesterName.text = currentItem.name
            tcContact.text = currentItem.contact_number

            rvList.setOnClickListener {
                val intent = Intent(holder.itemView.context, YourAcceptedRequestDetailActivity::class.java)
                intent.putExtra("key", currentItem.id.toString())
                intent.putExtra("userid", currentItem.userid.toString())
                holder.itemView.context.startActivity(intent)
            }

        }

    }


    override fun getItemCount(): Int {
        return adoptlist.size
    }

}