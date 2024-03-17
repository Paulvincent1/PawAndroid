package com.example.pawandroid.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawandroid.IncomingRequestDetailActivity
import com.example.pawandroid.PetsEditActivity
import com.example.pawandroid.databinding.IncomingRequestListBinding
import com.example.pawandroid.databinding.MyProfileListBinding
import com.example.pawandroid.model.Adopt
import com.example.pawandroid.model.Pets

class IncomingRequestAdapter  (var adoptlist: MutableList<Adopt>): RecyclerView.Adapter<IncomingRequestAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: IncomingRequestListBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncomingRequestAdapter.ViewHolder {
        val view = IncomingRequestListBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return ViewHolder(view)
    }
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
            tcContact.text = currentItem.contact_number

            rvList.setOnClickListener {
                val intent = Intent(holder.itemView.context, IncomingRequestDetailActivity::class.java)
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