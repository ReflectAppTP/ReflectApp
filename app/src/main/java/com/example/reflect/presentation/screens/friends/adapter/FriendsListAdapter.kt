package com.example.reflect.presentation.screens.friends.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reflect.databinding.CardFriendListBinding
import com.example.reflect.databinding.EmptyFriendsListBinding
import com.example.reflect.databinding.LoadingLottieBinding
import com.example.reflect.domain.model.GetFriendModel
import com.example.reflect.presentation.screens.friends.GetFriendsState

class FriendsListAdapter(
    private val onClick: (Int) -> Unit
) : ListAdapter<GetFriendsState, RecyclerView.ViewHolder>(DIFF_CALLBACK){

    class EmptyFriendsViewHolder(
        val binding: EmptyFriendsListBinding
    ): RecyclerView.ViewHolder(binding.root)

    class LoadingViewHolder(
        val binding: LoadingLottieBinding
    ): RecyclerView.ViewHolder(binding.root)

    class FriendsListViewHolder(
        private val binding: CardFriendListBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: GetFriendModel, onClick: (Int) -> Unit) {
            with (binding) {
                root.setOnClickListener {
                    onClick(model.id)
                }

                cardFriendIconMaterialCardText.text = model.login.substring(0,1)
                cardFriendLogin.text = model.login
                cardFriendPremiumIcon.visibility = if (model.isPremium) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GetFriendsState.EmptyContent.viewType -> {
                val binding = EmptyFriendsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyFriendsViewHolder(binding)
            }
            GetFriendsState.Loading.viewType -> {
                val binding = LoadingLottieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(binding)
            }
            GetFriendsState.Error("").viewType -> {
                val binding = LoadingLottieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(binding)
            }
            else -> {
                val binding = CardFriendListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FriendsListViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is GetFriendsState.Success -> {
                val record = item.friends.first()
                (holder as FriendsListViewHolder).bind(
                    record,
                    onClick
                )
            }
            else -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    // TODO: Ужасный и противный говнокод
    fun updateState(state: GetFriendsState) {
        val states = when (state) {
            is GetFriendsState.Success -> state.friends.map {
                GetFriendsState.Success(listOf(it))
            }
            else -> listOf(state)
        }
        submitList(states)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GetFriendsState>() {
            override fun areItemsTheSame(
                oldItem: GetFriendsState,
                newItem: GetFriendsState,
            ): Boolean {
                return (oldItem is GetFriendsState.Success && newItem is GetFriendsState.Success
                        && oldItem.friends == newItem.friends) ||
                        (oldItem is GetFriendsState.Error && newItem is GetFriendsState.Error
                                && oldItem.message == newItem.message)
            }

            override fun areContentsTheSame(
                oldItem: GetFriendsState,
                newItem: GetFriendsState,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}