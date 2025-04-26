package com.example.reflect.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reflect.R
import com.example.reflect.databinding.CardStateBinding
import com.example.reflect.domain.model.RecordModel

class RecordsListAdapter(
    private val records: List<RecordModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class RecordViewHolder(
        private val binding: CardStateBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: RecordModel, context: Context) {
            with (binding) {
                when(model.value) {
                    in 0..1 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_1)
                        cardStateChangeMoodTV.text = "Ужасное"
                    }
                    in 2..3 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_2)
                        cardStateChangeMoodTV.text = "Плохое"
                    }
                    in 4..6 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_3)
                        cardStateChangeMoodTV.text = "Нормальное"
                    }
                    in 7..8 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_4)
                        cardStateChangeMoodTV.text = "Хорошее"
                    }
                    in 9..10 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_5)
                        cardStateChangeMoodTV.text = "Потрясающее"
                    }
                    else -> throw IllegalStateException("Как так вообще получилось, что значение от 0 до 10 больше 10?!")
                }

                if (model.description.isNullOrEmpty()) {
                    cardStateDescriptionTV.visibility = View.GONE
                } else {
                    cardStateDescriptionTV.text = model.description
                }

                if (model.firstTagList.isNullOrEmpty()) {
                    cardStateFirstRV.visibility = View.GONE
                } else {
                    cardStateFirstRV.layoutManager = GridLayoutManager(context,2)
                    cardStateFirstRV.adapter = RecordsTagListAdapter(model.firstTagList)
                }

                if (model.secondTagList.isNullOrEmpty()) {
                    cardStateSecondRV.visibility = View.GONE
                } else {
                    cardStateSecondRV.layoutManager = GridLayoutManager(context,2)
                    cardStateSecondRV.adapter = RecordsTagListAdapter(model.secondTagList)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cardStateBinding = CardStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordViewHolder(cardStateBinding)
    }

    override fun getItemCount(): Int = records.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecordViewHolder) {
            holder.bind(records[position], holder.itemView.context)
        }
    }

}