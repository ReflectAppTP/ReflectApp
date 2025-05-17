package com.example.reflect.presentation.screens.ai.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.reflect.domain.model.AIMessageModel
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reflect.databinding.CardAiMessageBinding
import com.example.reflect.databinding.CardAiMessageResponseBinding

class AiMessageAdapter: ListAdapter<AIMessageModel, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    class MessageViewHolder(
        val binding: CardAiMessageBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: AIMessageModel) {
            binding.aiCardMessageTV.text = model.message
        }
    }

    class ResponseViewHolder(
        val binding: CardAiMessageResponseBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: AIMessageModel) {
            binding.aiCardMessageTV.text = model.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].isResponse) VIEW_TYPE_AI else VIEW_TYPE_USER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val binding = CardAiMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MessageViewHolder(binding)
            }
            VIEW_TYPE_AI -> {
                val binding = CardAiMessageResponseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ResponseViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> holder.bind(currentList[position])
            is ResponseViewHolder -> holder.bind(currentList[position])
        }
    }


    override fun getItemCount(): Int = currentList.size

    companion object {
        private const val VIEW_TYPE_USER = 0
        private const val VIEW_TYPE_AI = 1

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AIMessageModel>() {
            override fun areItemsTheSame(
                oldItem: AIMessageModel,
                newItem: AIMessageModel,
            ): Boolean {
                return oldItem.message == newItem.message && oldItem.isResponse == newItem.isResponse
            }

            override fun areContentsTheSame(
                oldItem: AIMessageModel,
                newItem: AIMessageModel,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}