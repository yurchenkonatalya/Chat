package com.example.chat.fragments.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.chat.Constants.BASE_IMAGE_URL
import com.example.chat.DB.entity.MessageEntity
import com.example.chat.InfoHelper
import com.example.chat.R
import com.example.chat.databinding.FrameMsgDestinationBinding
import com.example.chat.databinding.FrameMsgSourceBinding

class DialogAdapter(
    private val userId: Long,
    private val userPhoto: String,
    private val myPhoto: String
) : PagingDataAdapter<MessageEntity, RecyclerView.ViewHolder>(MessageListComparator) {
    class MessageSourceViewHolder(val binding: FrameMsgSourceBinding) :
        RecyclerView.ViewHolder(binding.root)

    class MessageDestinationViewHolder(val binding: FrameMsgDestinationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)?.id_user_source == userId) 0 else 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            if (holder is MessageSourceViewHolder) {
                with(holder.binding) {
                    tvMsg.text = item.text
                    tvTime.text = InfoHelper.systemDateTimeToTime(item.date)
                    if (position + 1 == itemCount || getItem(position + 1)?.id_user_source != item.id_user_source
                        || InfoHelper.getTimeOlder2(
                            getItem(position)?.date!!,
                            getItem(position + 1)?.date!!
                        )
                    ) {
                        imageCard.isVisible = true
                        Glide
                            .with(root.context)
                            .load(BASE_IMAGE_URL + userPhoto)
                            .placeholder(R.drawable.people)
                            .error(R.drawable.people)
                            .fitCenter()
                            .centerCrop()
                            .into(imageCard)
                    } else
                        imageCard.isInvisible = true
                    if (item.info != null) {
                        item.info?.let {
                            msgInfo.isVisible = true
                            Glide
                                .with(root.context)
                                .load(BASE_IMAGE_URL + it)
                                .placeholder(R.drawable.people)
                                .error(R.drawable.people)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .into(msgInfo)
                        }
                        tvMsg.isVisible = false
                    } else {
                        msgInfo.isVisible = false
                        tvMsg.isVisible = true
                    }
                    if (position != itemCount - 1)
                        divider.isVisible = InfoHelper.getTimeOlder2(
                            getItem(position)?.date!!,
                            getItem(position + 1)?.date!!
                        )
                }
            } else {
                with((holder as MessageDestinationViewHolder).binding) {
                    tvMsg.text = item.text
                    tvTime.text = InfoHelper.systemDateTimeToTime(item.date)

                    if (position + 1 == itemCount || getItem(position + 1)?.id_user_source != item.id_user_source
                        || InfoHelper.getTimeOlder2(
                            getItem(position)?.date!!,
                            getItem(position + 1)?.date!!
                        )
                    ) {
                        imageCard.isVisible = true
                        Glide
                            .with(root.context)
                            .load(BASE_IMAGE_URL + myPhoto)
                            .placeholder(R.drawable.people)
                            .error(R.drawable.people)
                            .fitCenter()
                            .centerCrop()
                            .into(imageCard)
                    } else
                        imageCard.isInvisible = true
                    if (item.info != null) {
                        item.info?.let {
                            msgInfo.isVisible = true
                            Glide
                                .with(root.context)
                                .load(BASE_IMAGE_URL + it)
                                .placeholder(R.drawable.people)
                                .error(R.drawable.people)
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .into(msgInfo)
                        }
                        tvMsg.isVisible = false
                    } else {
                        msgInfo.isVisible = false
                        tvMsg.isVisible = true
                    }
                    if (position != itemCount - 1)
                        divider.isVisible = InfoHelper.getTimeOlder2(
                            getItem(position)?.date!!,
                            getItem(position + 1)?.date!!
                        )
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return if (viewType == 0)
            MessageSourceViewHolder(
                FrameMsgSourceBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        else
            MessageDestinationViewHolder(
                FrameMsgDestinationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }

    object MessageListComparator : DiffUtil.ItemCallback<MessageEntity>() {
        override fun areItemsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
            return oldItem == newItem
        }
    }
}