package com.example.reflect.presentation.screens.friends.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflect.databinding.FragmentNotificationFriendsBinding
import com.example.reflect.presentation.dialog.AcceptFriendshipDialog
import com.example.reflect.presentation.screens.friends.GetFriendsNotificationsState
import com.example.reflect.presentation.screens.friends.adapter.NotificationFriendsAdapter
import com.example.reflect.presentation.screens.friends.viewmodel.ViewModelNotificationFriendship
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationFriendsFragment : Fragment() {

    private var _binding: FragmentNotificationFriendsBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelNotificationFriendship by activityViewModels()
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

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.notifications.collect {
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        with (binding) {
            fragmentNotificationRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            notificationFriendsAdapter = NotificationFriendsAdapter {
                val dialog = AcceptFriendshipDialog(it)
                dialog.show(parentFragmentManager, "Accept friendship dialog")
            }
            notificationFriendsAdapter.updateState(vm.notificationState.value)
            fragmentNotificationRV.adapter = notificationFriendsAdapter
        }

        lifecycleScope.launch {
            vm.notificationState.collect { newState ->
                handleNotificationsState(newState)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleNotificationsState(state: GetFriendsNotificationsState) {
        notificationFriendsAdapter.updateState(state)
    }
}