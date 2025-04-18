package com.example.reflect.presentation.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.reflect.R
import com.example.reflect.databinding.CardTagBinding
import com.example.reflect.domain.model.TagModel

class AddStateTagListAdapter(
    private var tags: List<TagModel>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class TagViewHolder(
        private val binding: CardTagBinding,
        private val onClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: TagModel, context: Context) {
            // TODO: переделать
            with (binding) {
                root.checkedIcon = null
                root.setOnClickListener {
                    root.isChecked = !root.isChecked
                    root.setCardBackgroundColor(ContextCompat.getColorStateList(
                        context,
                        if (root.isChecked) R.color.secondaryContainer else R.color.surfaceContainerLow)!!
                    )
                    root.setStrokeColor(ContextCompat.getColorStateList(
                        context,
                        if (root.isChecked) R.color.secondaryContainer else R.color.surfaceContainerLow)!!
                    )
                    onClick(model.id)
                }
                tagCardEmoji.text = model.emoji ?: ""
                tagCardEmoji.visibility = if (tagCardEmoji.text.isNullOrEmpty()) View.GONE else View.VISIBLE
                tagCardText.text = model.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val tagCardViewBinding = CardTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagViewHolder(tagCardViewBinding, onClick)
    }

    override fun getItemCount(): Int = tags.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TagViewHolder) {
            holder.bind(tags[position], holder.itemView.context)
        }
    }

}