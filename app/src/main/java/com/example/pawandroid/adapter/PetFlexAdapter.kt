package com.example.pawandroid.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pawandroid.MyRequestEditActivity
import com.example.pawandroid.PetFlexEditActivity
import com.example.pawandroid.PetInfoActivity
import com.example.pawandroid.R
import com.example.pawandroid.builder.RetrofitBuilder
import com.example.pawandroid.databinding.PetFlexListBinding
import com.example.pawandroid.model.PetSocial
import com.example.pawandroid.service.PawService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetFlexAdapter(val petFlexList: MutableList<PetSocial>, val context: Context): RecyclerView.Adapter<PetFlexAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PetFlexListBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetFlexAdapter.ViewHolder {
        val view = PetFlexListBinding.inflate(LayoutInflater.from(parent.context), parent , false)
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


            val userImg = currentItem.user.img
            val userUrlImg = "https://pawadoptpaw.online/${userImg}"
//                          , paul =https://pawadoptpaw.online/
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
            edit.setOnClickListener {
                val intent = Intent(holder.itemView.context, PetFlexEditActivity::class.java)
                intent.putExtra("key", currentItem.id.toString())
                holder.itemView.context.startActivity(intent)
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
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

}