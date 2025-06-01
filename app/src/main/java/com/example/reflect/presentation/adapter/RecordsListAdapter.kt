package com.example.reflect.presentation.adapter

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
import com.example.reflect.databinding.EmptyRecordsBinding
import com.example.reflect.databinding.LoadingLottieBinding
import com.example.reflect.domain.model.RecordModel
import com.example.reflect.presentation.common.DateUtils
import com.example.reflect.presentation.screens.records.GetRecordsState
import java.util.Calendar

class RecordsListAdapter(
    private val calendar: Calendar,
    private val onEdit: (Int, RecordModel) -> Unit,
    private val onDelete: (Int) -> Unit
) : ListAdapter<GetRecordsState, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    class EmptyRecordsViewHolder(
        val binding: EmptyRecordsBinding
    ): RecyclerView.ViewHolder(binding.root)

    class LoadingViewHolder(
        val binding: LoadingLottieBinding
    ): RecyclerView.ViewHolder(binding.root)

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
                        cardStateChangeMoodTV.text = context.resources.getString(R.string.cardStateMood, context.resources.getString(R.string.awfulState))
                    }
                    in 2..3 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_2)
                        cardStateChangeMoodTV.text = context.resources.getString(R.string.cardStateMood, context.resources.getString(R.string.badState))
                    }
                    in 4..6 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_3)
                        cardStateChangeMoodTV.text = context.resources.getString(R.string.cardStateMood, context.resources.getString(R.string.normalState))
                    }
                    in 7..8 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_4)
                        cardStateChangeMoodTV.text = context.resources.getString(R.string.cardStateMood, context.resources.getString(R.string.goodState))
                    }
                    in 9..10 -> {
                        cardStateImageView.setImageResource(R.drawable.ic_state_image_5)
                        cardStateChangeMoodTV.text = context.resources.getString(R.string.cardStateMood, context.resources.getString(R.string.excellentState))
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GetRecordsState.EmptyContent.viewType -> {
                val binding = EmptyRecordsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyRecordsViewHolder(binding)
            }
            GetRecordsState.Loading.viewType -> {
                val binding = LoadingLottieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(binding)
            }
            GetRecordsState.Error("").viewType -> {
                val binding = LoadingLottieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(binding)
            }
            else -> {
                val binding = CardStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RecordViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is GetRecordsState.Success -> {
                val record = item.records.first()
                (holder as RecordViewHolder).bind(
                    record,
                    calendar,
                    holder.itemView.context,
                    onDelete,
                    onEdit
                )
            }
            else -> {
                Unit
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    // TODO: Ужасный и противный говнокод 
    fun updateState(state: GetRecordsState) {
        val states = when (state) {
            is GetRecordsState.Success -> state.records.map { record ->
                GetRecordsState.Success(listOf(record))
            }
            else -> listOf(state)
        }
        submitList(states)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GetRecordsState>() {
            override fun areItemsTheSame(oldItem: GetRecordsState, newItem: GetRecordsState): Boolean {
                return (oldItem is GetRecordsState.Success && newItem is GetRecordsState.Success &&
                        oldItem.records == newItem.records) ||
                        (oldItem is GetRecordsState.Error && newItem is GetRecordsState.Error &&
                                oldItem.message == newItem.message)
            }

            override fun areContentsTheSame(oldItem: GetRecordsState, newItem: GetRecordsState): Boolean {
                return oldItem == newItem
            }
        }
    }
}