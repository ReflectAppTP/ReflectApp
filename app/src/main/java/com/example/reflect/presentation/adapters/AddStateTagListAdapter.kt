package com.example.reflect.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.reflect.R
import com.example.reflect.databinding.CardTagBinding
import com.example.reflect.domain.model.TagModel

class AddStateTagListAdapter(
    private var tags: List<TagModel>,
    private var selectedTags: List<Int>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class TagViewHolder(
        private val binding: CardTagBinding,
        private val selectedTags: List<Int>,
        private val onClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: TagModel, context: Context) {
            // TODO: переделать
            with (binding) {
                root.checkedIcon = null
                if (selectedTags.contains(model.id)) {
                    changeCardState(context, true)
                }
                root.setOnClickListener {
                    root.isChecked = !root.isChecked
                    changeCardState(context = context, isChecked = root.isChecked)
                    onClick(model.id)
                }
                tagCardEmoji.text = model.emoji ?: ""
                tagCardEmoji.visibility = if (tagCardEmoji.text.isNullOrEmpty()) View.GONE else View.VISIBLE
                tagCardText.text = model.name
            }
        }

        private fun changeCardState(context: Context, isChecked: Boolean) {
            with (binding) {
                root.setCardBackgroundColor(ContextCompat.getColorStateList(
                    context,
                    if (isChecked) R.color.secondaryContainer else R.color.surfaceContainerLow)!!
                )
                root.setStrokeColor(ContextCompat.getColorStateList(
                    context,
                    if (isChecked) R.color.secondaryContainer else R.color.surfaceContainerLow)!!
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val tagCardViewBinding = CardTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(tagCardViewBinding, selectedTags, onClick)
    }

    override fun getItemCount(): Int = tags.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TagViewHolder) {
            holder.bind(tags[position], holder.itemView.context)
        }
    }
}