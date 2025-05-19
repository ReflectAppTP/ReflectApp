package com.example.reflect.presentation.screens.friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reflect.databinding.CardFriendListBinding
import com.example.reflect.databinding.EmptySearchFriendsBinding
import com.example.reflect.databinding.LoadingLottieBinding
import com.example.reflect.domain.model.UserModel
import com.example.reflect.presentation.screens.friends.SearchFriendsState

class SearchListAdapter(
    private val onClick: (UserModel) -> Unit
) : ListAdapter<SearchFriendsState, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    class EmptySearchFriendsViewHolder(
        val binding: EmptySearchFriendsBinding
    ): RecyclerView.ViewHolder(binding.root)

    class LoadingViewHolder(
        val binding: LoadingLottieBinding
    ): RecyclerView.ViewHolder(binding.root)

    class FriendsListViewHolder(
        private val binding: CardFriendListBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: UserModel, onClick: (UserModel) -> Unit) {
            with (binding) {
                root.setOnClickListener {
                    onClick(model)
                }

                cardFriendIconMaterialCardText.text = model.username.substring(0,1)
                cardFriendLogin.text = model.username
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SearchFriendsState.EmptyContent.viewType -> {
                val binding = EmptySearchFriendsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptySearchFriendsViewHolder(binding)
            }
            SearchFriendsState.Loading.viewType -> {
                val binding = LoadingLottieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(binding)
            }
            SearchFriendsState.Error("").viewType -> {
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
            is SearchFriendsState.Success -> {
                val record = item.users.first()
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

    fun updateState(state: SearchFriendsState) {
        val states = when (state) {
            is SearchFriendsState.Success -> state.users.map {
                SearchFriendsState.Success(listOf(it))
            }
            else -> listOf(state)
        }
        submitList(states)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchFriendsState>() {
            override fun areItemsTheSame(
                oldItem: SearchFriendsState,
                newItem: SearchFriendsState,
            ): Boolean {
                return (oldItem is SearchFriendsState.Success && newItem is SearchFriendsState.Success
                        && oldItem.users == newItem.users) ||
                        (oldItem is SearchFriendsState.Error && newItem is SearchFriendsState.Error
                                && oldItem.message == newItem.message)
            }

            override fun areContentsTheSame(
                oldItem: SearchFriendsState,
                newItem: SearchFriendsState,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}