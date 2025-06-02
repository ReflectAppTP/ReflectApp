package com.example.reflect.presentation.screens.friends.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflect.R
import com.example.reflect.databinding.FragmentFriendsListBinding
import com.example.reflect.presentation.screens.friends.GetFriendsState
import com.example.reflect.presentation.screens.friends.adapter.FriendsListAdapter
import com.example.reflect.presentation.screens.friends.viewmodel.ViewModelFriends
import com.example.reflect.presentation.screens.profile.GetUserByIdState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FriendsListFragment : Fragment() {

    private var _binding: FragmentFriendsListBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelFriends by activityViewModels()
    private lateinit var friendsListAdapter: FriendsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFriendsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()

        with (binding) {
            fragmentFriendsRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            friendsListAdapter = FriendsListAdapter {
                vm.updateId(it)
                vm.getUser(it)
            }
            friendsListAdapter.updateState(vm.friendsListState.value)
            fragmentFriendsRV.adapter = friendsListAdapter
        }

        lifecycleScope.launch {
            vm.friendsListState.collect {
                handleFriendsState(it)
            }
        }

        lifecycleScope.launch {
            vm.getUserByIdState.collect {
                handleGetUserState(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleFriendsState(state: GetFriendsState) {
        friendsListAdapter.updateState(state)
    }

    private fun handleGetUserState(state: GetUserByIdState) {
        when (state) {
            is GetUserByIdState.Success -> {
                if (requireParentFragment().findNavController().currentDestination?.id == R.id.friendsListFragment) {
                    val args = Bundle().apply {
                        putParcelable("userModel", state.user)
                    }
                    requireParentFragment().requireParentFragment().findNavController().navigate(R.id.action_mainFragment_to_profileFriendFragment, args)
                    vm.updateUserState()
                }
            }
            else -> Unit
        }
    }
}