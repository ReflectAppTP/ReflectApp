package com.example.reflect.presentation.screens.friends.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflect.databinding.FragmentFriendsListBinding
import com.example.reflect.presentation.screens.friends.adapter.FriendsListAdapter
import com.example.reflect.presentation.screens.friends.viewmodel.ViewModelFriends
import dagger.hilt.android.AndroidEntryPoint

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
                vm.getUser(it)
            }
            friendsListAdapter.updateState(vm.friendsListState.value)
            fragmentFriendsRV.adapter = friendsListAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}