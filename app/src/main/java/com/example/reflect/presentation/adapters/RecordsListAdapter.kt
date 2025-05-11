package com.example.reflect.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reflect.R
import com.example.reflect.databinding.CardStateBinding
import com.example.reflect.domain.model.RecordModel
import com.example.reflect.presentation.common.DateUtils
import java.util.Calendar

class RecordsListAdapter(
    private val calendar: Calendar,
    private val onEdit: (Int, RecordModel) -> Unit,
    private val onDelete: (Int) -> Unit
) : ListAdapter<RecordModel, RecordsListAdapter.RecordViewHolder>(DIFF_CALLBACK){

    class RecordViewHolder(
        private val binding: CardStateBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: RecordModel, today: Calendar, context: Context, onDelete: (Int) -> Unit, onEdit: (Int, RecordModel) -> Unit) {
            with (binding) {
                val currentDate = Calendar.getInstance()
                currentDate.time = model.creationDate!!

                cardStateCreationDate.text = DateUtils.creationDateToString(today = today, currentDate = currentDate)

                when(model.value) {
                    in 0..1 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_1)
                        cardStateChangeMoodTV.text = context.resources.getString(R.string.cardStateMood, "Ужасное")
                    }
                    in 2..3 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_2)
                        cardStateChangeMoodTV.text = context.resources.getString(R.string.cardStateMood, "Плохое")
                    }
                    in 4..6 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_3)
                        cardStateChangeMoodTV.text = context.resources.getString(R.string.cardStateMood, "Нормально")
                    }
                    in 7..8 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_4)
                        cardStateChangeMoodTV.text = context.resources.getString(R.string.cardStateMood, "Хорошее")
                    }
                    in 9..10 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_5)
                        cardStateChangeMoodTV.text = context.resources.getString(R.string.cardStateMood, "Отличное")
                    }
                    else -> throw IllegalStateException("Как так вообще получилось, что значение от 0 до 10 больше 10?!")
                }

                if (model.description.isNullOrEmpty()) {
                    cardStateDescriptionTV.visibility = View.GONE
                } else {
                    cardStateDescriptionTV.visibility = View.VISIBLE
                    cardStateDescriptionTV.text = model.description
                }

                if (model.firstTagList.isNullOrEmpty()) {
                    cardStateFirstRV.visibility = View.GONE
                } else {
                    cardStateFirstRV.visibility = View.VISIBLE
                    cardStateFirstRV.layoutManager = GridLayoutManager(context,2)
                    cardStateFirstRV.adapter = RecordsTagListAdapter(model.firstTagList)
                }

                if (model.secondTagList.isNullOrEmpty()) {
                    cardStateSecondRV.visibility = View.GONE
                } else {
                    cardStateSecondRV.visibility = View.VISIBLE
                    cardStateSecondRV.layoutManager = GridLayoutManager(context,2)
                    cardStateSecondRV.adapter = RecordsTagListAdapter(model.secondTagList)
                }

                cardStateChangeDots.setOnClickListener {
                    val popupMenu = PopupMenu(context, cardStateChangeDots)
                    popupMenu.inflate(R.menu.card_state_menu)
                    popupMenu.setOnMenuItemClickListener {
                        when(it.itemId) {
                            R.id.menuEdit -> {
                                onEdit(model.id, model)
                                true
                            }
                            R.id.menuDelete -> {
                                onDelete(model.id)
                                true
                            }
                            else -> false
                        }
                    }
                    popupMenu.show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val cardStateBinding = CardStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordViewHolder(cardStateBinding)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bind(currentList[position], calendar, holder.itemView.context, onDelete, onEdit)
    }

    override fun getItemCount(): Int = currentList.size

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecordModel>() {
            override fun areItemsTheSame(oldItem: RecordModel, newItem: RecordModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: RecordModel, newItem: RecordModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}