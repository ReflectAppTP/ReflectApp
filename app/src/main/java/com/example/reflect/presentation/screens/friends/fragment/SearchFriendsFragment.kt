package com.example.reflect.presentation.screens.friends.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.reflect.databinding.FragmentSearchFriendsBinding
import com.example.reflect.presentation.screens.friends.viewmodel.ViewModelFriends
import com.example.reflect.presentation.screens.friends.viewmodel.ViewModelSearchFriends
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFriendsFragment : Fragment() {

    private var _binding: FragmentSearchFriendsBinding? = null
    private val binding get() = _binding!!

    private val mainVM: ViewModelFriends by activityViewModels()
    private val searchVM: ViewModelSearchFriends by viewModels()

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
                    fragmentSearchFriendsEditText.clearFocus()
                }
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onChangeQueryListener() = object: OnQueryTextListener {
        var job: Job? = null
        override fun onQueryTextSubmit(query: String?): Boolean {
            job?.cancel()

            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            job?.cancel()

            return false
        }

    }
}