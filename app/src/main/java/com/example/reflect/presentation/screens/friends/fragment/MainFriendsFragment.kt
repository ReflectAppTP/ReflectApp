package com.example.reflect.presentation.screens.friends.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.reflect.R
import com.example.reflect.databinding.FragmentFriendsBinding
import com.example.reflect.presentation.screens.friends.GetFriendsNotificationsState
import com.example.reflect.presentation.screens.friends.viewmodel.ViewModelFriends
import com.example.reflect.presentation.screens.friends.viewmodel.ViewModelNotificationFriendship
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelFriends by activityViewModels()
    private val notificationsVM: ViewModelNotificationFriendship by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        vm.fetchFriends()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            fragmentFriendsToolbarSearchIcon.setOnClickListener {
                moveToScreen(FriendsScreen.Search)
            }

            fragmentFriendsToolbarNotificationIconDeluxe.setOnClickListener {
                moveToScreen(FriendsScreen.Notifications)
            }

            fragmentFriendsToolbarBackIcon.setOnClickListener {
                moveToScreen(FriendsScreen.FriendsList)
            }
        }

        lifecycleScope.launch {
            notificationsVM.notificationState.collect {
                handleNotifications(it)
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        moveToScreen(vm.currentScreen.value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun moveToScreen(friendsScreen: FriendsScreen) {
        vm.moveToScreen(friendsScreen)
        with (binding) {
            when (friendsScreen) {
                FriendsScreen.Search -> {
                    if (fragmentFriendsContainer.findNavController().currentDestination?.id != R.id.searchFriendsFragment) {
                        fragmentFriendsContainer.findNavController().navigate(R.id.action_friendsListFragment_to_searchFriendsFragment)
                    }
                    fragmentFriendsToolbarTitle.text = resources.getString(R.string.toolbarSearchFriendsTitle)
                    fragmentFriendsToolbarNotificationIcon.visibility = View.GONE
                    fragmentFriendsToolbarBackIcon.visibility = View.VISIBLE
                }
                FriendsScreen.Notifications -> {
                    if (fragmentFriendsContainer.findNavController().currentDestination?.id != R.id.notificationFriendsFragment) {
                        fragmentFriendsContainer.findNavController().navigate(R.id.action_friendsListFragment_to_notificationFriendsFragment)
                    }
                    fragmentFriendsToolbarTitle.text = resources.getString(R.string.toolbarNotificationFriendsTitle)
                    fragmentFriendsToolbarNotificationIcon.visibility = View.GONE
                    fragmentFriendsToolbarSearchIcon.visibility = View.GONE
                    fragmentFriendsToolbarBackIcon.visibility = View.VISIBLE
                }
                FriendsScreen.FriendsList -> {
                    if (fragmentFriendsContainer.findNavController().currentDestination?.id != R.id.friendsListFragment){
                        fragmentFriendsContainer.findNavController().popBackStack()
                    } else {
                        fragmentFriendsContainer.findNavController().navigate(
                            R.id.friendsListFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.friendsListFragment, true)
                                .build()
                        )
                    }
                    fragmentFriendsToolbarTitle.text = resources.getString(R.string.toolbarFriendsTitle)
                    fragmentFriendsToolbarBackIcon.visibility = View.GONE
                    fragmentFriendsToolbarSearchIcon.visibility = View.VISIBLE
                    fragmentFriendsToolbarNotificationIcon.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun handleNotifications(state: GetFriendsNotificationsState) {
        if (state is GetFriendsNotificationsState.Success) {
            binding.fragmentFriendsToolbarNotificationIconBadge.isVisible = state.users.size != 0
        } else if (state is GetFriendsNotificationsState.EmptyContent) {
            binding.fragmentFriendsToolbarNotificationIconBadge.isVisible = false
        }
    }
}

enum class FriendsScreen {
    FriendsList, Search, Notifications
}