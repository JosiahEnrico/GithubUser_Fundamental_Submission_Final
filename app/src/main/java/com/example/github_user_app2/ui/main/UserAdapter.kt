package com.example.github_user_app2.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.github_user_app2.data.main.User
import com.example.github_user_app2.databinding.ItemUserRowBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val list = ArrayList<User>()

    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClickCallback (onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }

    fun setList(users: ArrayList<User>){
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(val binding: ItemUserRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: User) {
            binding.root.setOnClickListener {
                onItemClickCallBack?.onItemClicked(user)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .circleCrop()
                    .into(ivUser)
                tvUsername.text = user.login


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemUserRowBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return UserViewHolder((view))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickCallBack{
        fun onItemClicked(data: User)

    }
}