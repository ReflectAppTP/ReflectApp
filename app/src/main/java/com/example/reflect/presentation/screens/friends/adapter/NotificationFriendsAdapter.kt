package com.example.reflect.presentation.screens.friends.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reflect.databinding.CardFriendNotificationBinding
import com.example.reflect.databinding.EmptyFriendsNotificationsBinding
import com.example.reflect.databinding.LoadingLottieBinding
import com.example.reflect.domain.model.NotificationFriendshipModel
import com.example.reflect.presentation.screens.friends.GetFriendsNotificationsState
import com.example.reflect.presentation.screens.friends.GetFriendsState

class NotificationFriendsAdapter(
    private val onClick: (NotificationFriendshipModel) -> Unit
) : ListAdapter<GetFriendsNotificationsState, RecyclerView.ViewHolder>(DIFF_CALLBACK){

    class EmptyFriendsNotificationsViewHolder(
        val binding: EmptyFriendsNotificationsBinding
    ): RecyclerView.ViewHolder(binding.root)

    class LoadingViewHolder(
        val binding: LoadingLottieBinding
    ): RecyclerView.ViewHolder(binding.root)

    class FriendsNotificationListViewHolder(
        private val binding: CardFriendNotificationBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: NotificationFriendshipModel, onClick: (NotificationFriendshipModel) -> Unit) {
            with (binding) {
                root.setOnClickListener {
                    onClick(model)
                }

                cardFriendIconMaterialCardText.text = model.fromUser.username.substring(0,1)
                cardFriendLogin.text = model.fromUser.username
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GetFriendsState.EmptyContent.viewType -> {
                val binding = EmptyFriendsNotificationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyFriendsNotificationsViewHolder(binding)
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
                val binding = CardFriendNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FriendsNotificationListViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is GetFriendsNotificationsState.Success -> {
                val record = item.users.first()
                (holder as FriendsNotificationListViewHolder).bind(
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
    fun updateState(state: GetFriendsNotificationsState) {
        val states = when (state) {
            is GetFriendsNotificationsState.Success -> state.users.map {
                GetFriendsNotificationsState.Success(listOf(it))
            }
            else -> listOf(state)
        }
        submitList(states)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GetFriendsNotificationsState>() {
            override fun areItemsTheSame(
                oldItem: GetFriendsNotificationsState,
                newItem: GetFriendsNotificationsState,
            ): Boolean {
                return (oldItem is GetFriendsNotificationsState.Success && newItem is GetFriendsNotificationsState.Success
                        && oldItem.users == newItem.users) ||
                        (oldItem is GetFriendsNotificationsState.Error && newItem is GetFriendsNotificationsState.Error
                                && oldItem.message == newItem.message)
            }

            override fun areContentsTheSame(
                oldItem: GetFriendsNotificationsState,
                newItem: GetFriendsNotificationsState,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}