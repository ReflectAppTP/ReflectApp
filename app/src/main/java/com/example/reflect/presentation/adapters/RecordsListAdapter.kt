package com.example.reflect.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reflect.R
import com.example.reflect.databinding.CardStateBinding
import com.example.reflect.domain.model.RecordModel
import java.util.Calendar
import java.util.Locale

class RecordsListAdapter(
    private val records: List<RecordModel>,
    private val calendar: Calendar,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    class RecordViewHolder(
        private val binding: CardStateBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: RecordModel, calendar: Calendar, context: Context, onDelete: (Int) -> Unit, onEdit: (Int) -> Unit) {
            with (binding) {
                val today = Calendar.getInstance()
                calendar.time = model.creationDate

                // TODO: ГОВНОКОД!!! 
                if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
                    cardStateCreationDate.text = "Сегодня в ${String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d", calendar.get(Calendar.MINUTE))}"
                } else if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    calendar.get(Calendar.DAY_OF_MONTH) - today.get(Calendar.DAY_OF_MONTH) == -1) {
                    cardStateCreationDate.text = "Вчера в ${String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d", calendar.get(Calendar.MINUTE))}"
                } else {
                    if (today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
                        cardStateCreationDate.text = "${calendar.get(Calendar.DAY_OF_MONTH)} " +
                                "${
                                    calendar.getDisplayName(
                                        Calendar.MONTH,
                                        Calendar.LONG_FORMAT,
                                        Locale("ru")
                                    )
                                } в " +
                                "${String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d", calendar.get(Calendar.MINUTE))}"
                    } else {
                        cardStateCreationDate.text =
                            "${calendar.get(Calendar.YEAR)}-${calendar.get(Calendar.MONTH)+1}-${
                                calendar.get(Calendar.DAY_OF_MONTH)
                            } в " +
                                    "${String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY))}:${String.format("%02d", calendar.get(Calendar.MINUTE))}"
                    }
                }

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

                cardStateChangeDots.setOnClickListener {
                    val popupMenu = PopupMenu(context, cardStateChangeDots)
                    popupMenu.inflate(R.menu.card_state_menu)
                    popupMenu.setOnMenuItemClickListener {
                        when(it.itemId) {
                            R.id.menuEdit -> {
                                onEdit(model.id)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cardStateBinding = CardStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordViewHolder(cardStateBinding)
    }

    override fun getItemCount(): Int = records.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RecordViewHolder) {
            holder.bind(records[position], calendar, holder.itemView.context, onDelete, onEdit)
        }
    }
}