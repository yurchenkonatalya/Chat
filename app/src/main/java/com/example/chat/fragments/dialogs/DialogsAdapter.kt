package com.example.chat.fragments.dialogs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat.BASE_IMAGE_URL
import com.example.chat.DB.entity.DialogEntity
import com.example.chat.R
import com.example.chat.databinding.FrameItemDialogBinding

class DialogsAdapter(
    private val itemClickListener: ItemClickListener
) : PagingDataAdapter<DialogEntity, RecyclerView.ViewHolder>(DialogListComparator) {
    interface ItemClickListener {
        fun onClick(userId: Long)
    }

    class DialogListViewHolder(val binding: FrameItemDialogBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            if (holder is DialogListViewHolder) {
                with(holder.binding) {
                    Glide
                        .with(root.context)
                        .load(BASE_IMAGE_URL + item.opponent_photo)
                        .placeholder(R.drawable.people)
                        .error(R.drawable.people)
                        .fitCenter()
                        .into(imageCard)
                    val textName = item.opponent_name + " " + item.opponent_surname
                    textViewNameItem.text = textName
                    textViewBrandName.text = item.text
                    textViewPrice.text = item.unread_msg_count.toString()
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
    ): DialogListViewHolder {
        return DialogListViewHolder(
            FrameItemDialogBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    object DialogListComparator : DiffUtil.ItemCallback<DialogEntity>() {
        override fun areItemsTheSame(oldItem: DialogEntity, newItem: DialogEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DialogEntity, newItem: DialogEntity): Boolean {
            return oldItem == newItem
        }
    }
}
