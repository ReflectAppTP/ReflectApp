package com.example.reflect.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reflect.R
import com.example.reflect.databinding.CardStatisticTagBinding
import com.example.reflect.domain.model.StatisticTagModel

class StatisticTagListAdapter:
    ListAdapter<StatisticTagModel, StatisticTagListAdapter.StatisticViewHolder>(DIFF_CALLBACK) {

    class StatisticViewHolder(
        private val binding: CardStatisticTagBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: StatisticTagModel, context: Context) {
            with (binding) {
                cardStatisticTagTV.text = context.resources.getString(R.string.cardStatisticTagTV, model.freq)
                cardStatisticTagCard.tagCardEmoji.text = model.emoji
                cardStatisticTagCard.tagCardText.text = model.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        val cardStatisticTagBinding = CardStatisticTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatisticViewHolder(cardStatisticTagBinding)
    }

    override fun onBindViewHolder(holder: StatisticViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(currentList[position], holder.itemView.context)

        holder.itemView.apply {
            translationX = -context.resources.displayMetrics.widthPixels.toFloat()
            alpha = 0f

            viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewTreeObserver.removeOnPreDrawListener(this)

                    animate()
                        .translationX(0f)
                        .alpha(1f)
                        .setStartDelay(position * 100L)
                        .setDuration(300L)
                        .setInterpolator(DecelerateInterpolator())
                        .start()

                    return true
                }
            })
        }
    }

    override fun getItemCount(): Int = currentList.size

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StatisticTagModel>() {
            override fun areItemsTheSame(
                oldItem: StatisticTagModel,
                newItem: StatisticTagModel,
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: StatisticTagModel,
                newItem: StatisticTagModel,
            ): Boolean = oldItem == newItem
        }
    }
}