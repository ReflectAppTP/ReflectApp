package com.example.reflect.presentation.screens.friends.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflect.databinding.FragmentSearchFriendsBinding
import com.example.reflect.presentation.screens.friends.SearchFriendsState
import com.example.reflect.presentation.screens.friends.adapter.SearchListAdapter
import com.example.reflect.presentation.screens.friends.viewmodel.ViewModelFriends
import com.example.reflect.presentation.screens.friends.viewmodel.ViewModelSearchFriends
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFriendsFragment : Fragment() {

    private var _binding: FragmentSearchFriendsBinding? = null
    private val binding get() = _binding!!

    private val mainVM: ViewModelFriends by activityViewModels()
    private val searchVM: ViewModelSearchFriends by activityViewModels()

    private lateinit var searchListAdapter: SearchListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        var job: Job? = null

        with (binding) {
            fragmentSearchFriendsEditTextField.doAfterTextChanged {
                job?.cancel()
                if (it?.isNotEmpty() == true) job = CoroutineScope(Dispatchers.Main).launch {
                    delay(2000)
                    searchVM.searchUsers(it.toString())
                }
            }
            fragmentSearchFriendsEditTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    job?.cancel()
                    searchVM.searchUsers(fragmentSearchFriendsEditTextField.text.toString())
                    hideKeyboard()
                }
                true
            }

            fragmentSearchFriendsRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            searchListAdapter = SearchListAdapter {
                mainVM.getUser(it)
            }
            fragmentSearchFriendsRV.adapter = searchListAdapter
        }

        lifecycleScope.launch {
            searchVM.searchUsersState.collect {
                handleSearchState(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.fragmentSearchFriendsEditTextField.clearFocus()
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun handleSearchState(state: SearchFriendsState) {
        searchListAdapter.updateState(state)
    }
}