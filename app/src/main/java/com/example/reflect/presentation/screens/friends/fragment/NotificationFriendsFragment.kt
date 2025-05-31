package com.example.reflect.presentation.screens.friends.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflect.databinding.FragmentNotificationFriendsBinding
import com.example.reflect.presentation.dialog.AcceptFriendshipDialog
import com.example.reflect.presentation.screens.friends.adapter.NotificationFriendsAdapter
import com.example.reflect.presentation.screens.friends.viewmodel.ViewModelFriends
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFriendsFragment : Fragment() {

    private var _binding: FragmentNotificationFriendsBinding? = null
    private val binding get() = _binding!!
    
    private val vm: ViewModelFriends by activityViewModels()
    private lateinit var notificationFriendsAdapter: NotificationFriendsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotificationFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        vm.fetchNotifications()
        with (binding) {
            fragmentNotificationRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            notificationFriendsAdapter = NotificationFriendsAdapter {
                val dialog = AcceptFriendshipDialog(it)
                dialog.show(parentFragmentManager, "Accept friendship dialog")
            }
            notificationFriendsAdapter.updateState(vm.friendsNotificationListState.value)
            fragmentNotificationRV.adapter = notificationFriendsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}