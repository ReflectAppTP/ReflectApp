package com.example.reflect.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.reflect.R
import com.example.reflect.databinding.DialogAcceptFriendshipBinding
import com.example.reflect.domain.model.NotificationFriendshipModel
import com.example.reflect.presentation.screens.friends.viewmodel.ViewModelNotificationFriendship

class AcceptFriendshipDialog(
    private val userModel: NotificationFriendshipModel
): DialogFragment() {

    private var _binding: DialogAcceptFriendshipBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelNotificationFriendship by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAcceptFriendshipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with (binding) {
            dialogAgreeButton.setOnClickListener {
                vm.acceptFriendship(userModel.fromUser.id)
                dismiss()
            }

            dialogDeclineButton.setOnClickListener {
                vm.rejectFriendship(userModel.fromUser.id)
                dismiss()
            }
        }
    }

    override fun getTheme(): Int = R.style.DialogTheme

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}