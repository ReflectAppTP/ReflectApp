package com.example.reflect.presentation.screens.friends.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.reflect.R
import com.example.reflect.databinding.FragmentFriendsBinding
import com.example.reflect.presentation.screens.friends.viewmodel.ViewModelFriends
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFriendsFragment : Fragment() {

    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelFriends by activityViewModels()

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
                if (fragmentFriendsContainer.findNavController().currentDestination?.label.toString() != resources.getResourceEntryName(R.layout.fragment_search_friends)) {
                    fragmentFriendsContainer.findNavController().navigate(R.id.action_friendsListFragment_to_searchFriendsFragment)
                    fragmentFriendsToolbarNotificationIcon.visibility = View.GONE
                    fragmentFriendsToolbarBackIcon.visibility = View.VISIBLE
                }
            }

            fragmentFriendsToolbarNotificationIcon.setOnClickListener {
                if (fragmentFriendsContainer.findNavController().currentDestination?.label.toString() != resources.getResourceEntryName(R.layout.fragment_notification_friends)) {
                    fragmentFriendsContainer.findNavController().navigate(R.id.action_friendsListFragment_to_notificationFriendsFragment)
                    fragmentFriendsToolbarNotificationIcon.visibility = View.GONE
                    fragmentFriendsToolbarSearchIcon.visibility = View.GONE
                    fragmentFriendsToolbarBackIcon.visibility = View.VISIBLE
                }
            }

            fragmentFriendsToolbarBackIcon.setOnClickListener {
                fragmentFriendsContainer.findNavController().popBackStack()
                fragmentFriendsToolbarBackIcon.visibility = View.GONE
                fragmentFriendsToolbarSearchIcon.visibility = View.VISIBLE
                fragmentFriendsToolbarNotificationIcon.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}