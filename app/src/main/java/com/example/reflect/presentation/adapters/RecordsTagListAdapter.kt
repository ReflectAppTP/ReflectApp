package com.example.reflect.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.reflect.databinding.CardStateTagBinding
import com.example.reflect.domain.model.TagModel

class RecordsTagListAdapter(
    private var tags: List<TagModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class TagViewHolder(
        private val binding: CardStateTagBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: TagModel) {
            with (binding) {
                tagCardEmoji.text = model.emoji ?: ""
                tagCardEmoji.visibility = if (tagCardEmoji.text.isNullOrEmpty()) View.GONE else View.VISIBLE
                tagCardText.text = model.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val recordTagsViewBinding = CardStateTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(recordTagsViewBinding)
    }

    override fun getItemCount(): Int = tags.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TagViewHolder) {
            holder.bind(tags[position])
        }
    }

}