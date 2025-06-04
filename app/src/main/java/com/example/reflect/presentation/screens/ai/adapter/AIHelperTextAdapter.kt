package com.example.reflect.presentation.screens.ai.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.reflect.databinding.CardAiHelperTextBinding
import com.example.reflect.domain.model.AIHelperTextModel

class AIHelperTextAdapter(
    private val onClick: (String) -> Unit
): ListAdapter<AIHelperTextModel, AIHelperTextAdapter.HelperTextViewHolder>(DIFF_CALLBACK){

    class HelperTextViewHolder(
        val binding: CardAiHelperTextBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: AIHelperTextModel, onClick: (String) -> Unit) {
            with (binding) {
                root.text = model.string
                root.setOnClickListener {
                    onClick(model.string)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelperTextViewHolder {
        val binding = CardAiHelperTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HelperTextViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HelperTextViewHolder, position: Int) {
        holder.bind(currentList[position], onClick)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AIHelperTextModel>() {
            override fun areItemsTheSame(
                oldItem: AIHelperTextModel,
                newItem: AIHelperTextModel,
            ): Boolean {
                return oldItem.string == newItem.string
            }

            override fun areContentsTheSame(
                oldItem: AIHelperTextModel,
                newItem: AIHelperTextModel,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}