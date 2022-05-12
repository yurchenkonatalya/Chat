package com.example.chat.fragments.searchUsers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat.BASE_IMAGE_URL
import com.example.chat.DB.entity.UserEntity
import com.example.chat.R
import com.example.chat.databinding.FrameItemUserBinding
import com.example.chat.network.responses.AuthorizeUserResponse

class SearchUserAdapter(
    private val itemClickListener: ItemClickListener
) : PagingDataAdapter<UserEntity, RecyclerView.ViewHolder>(UserListComparator) {
    interface ItemClickListener {
        fun onClick(userId: Long)
    }
    class UserListViewHolder(val binding: FrameItemUserBinding) :
        RecyclerView.ViewHolder(binding.root)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            if (holder is UserListViewHolder) {
                with(holder.binding) {
                    Glide
                        .with(root.context)
                        .load(BASE_IMAGE_URL + item.user_photo)
                        .placeholder(R.drawable.people)
                        .error(R.drawable.people)
                        .fitCenter()
                        .into(imageCard)
                    tvName.text = item.user_name
                    tvSurname.text = item.user_surname
                    root.setOnClickListener {
                        itemClickListener.onClick(item.id)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserListViewHolder {
        return UserListViewHolder(
            FrameItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    object UserListComparator : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem == newItem
        }
    }
}
