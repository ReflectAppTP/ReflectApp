package com.example.reflect.presentation.screens.ai.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflect.R
import com.example.reflect.databinding.FragmentAiBinding
import com.example.reflect.presentation.common.ToastUtils
import com.example.reflect.presentation.screens.ai.AiIntent
import com.example.reflect.presentation.screens.ai.GetAIMessageState
import com.example.reflect.presentation.screens.ai.ResetAIContextState
import com.example.reflect.presentation.screens.ai.SendAIMessageState
import com.example.reflect.presentation.screens.ai.adapter.AIHelperTextAdapter
import com.example.reflect.presentation.screens.ai.adapter.AiMessageAdapter
import com.example.reflect.presentation.screens.ai.viewmodel.ViewModelAI
import com.google.android.material.animation.AnimatorSetCompat.playTogether
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.NonCancellable.start
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AiFragment : Fragment() {

    private var _binding: FragmentAiBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelAI by activityViewModels()

    private lateinit var helperTextAdapter: AIHelperTextAdapter
    private lateinit var messageAdapter: AiMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 300
            isElevationShadowEnabled = true
        }

        sharedElementReturnTransition = MaterialContainerTransform().apply {
            duration = 500
            isElevationShadowEnabled = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            if (vm.messagesList.value.isEmpty()) {
                aiMessagesRV.visibility = View.GONE
                aiEmptyTitle.visibility = View.VISIBLE
            }

            aiEditTextField.setText(vm.inputTextValue.value)
            aiEditTextField.doAfterTextChanged { value ->
                vm.updateText(value.toString())
            }

            aiToolbarBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            aiIconButtonCleanContext.setOnClickListener {
                lifecycleScope.launch {
                    vm.userIntent.send(AiIntent.ResetAiContext)
                }
            }

            aiHelperTextsRV.post {
                val height = aiHelperTextsRV.height.toFloat()

                aiHelperTextsRV.translationY = height
                aiIconButtonSend.translationY = height
                aiIconButtonExpandMenuCard.translationY = height
                aiIconButtonCleanContext.translationY = height
                aiEditText.translationY = height
                aiLoadingGetAIMessage.translationY = height

                animateMessageRV(true)
            }

            aiMessagesRV.layoutParams.height = aiMessagesRV.height + 3 * aiHelperTextsRV.height
            aiIconButtonExpandMenu.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    showRecyclerView()
                } else {
                    hideRecyclerView()
                }
            }

            aiIconButtonSend.setOnClickListener {
                AppMetrica.reportEvent("Нажатие на кнопку Отправка запроса к неиросети")
                if (aiEditTextField.text?.isNotEmpty() == true) {
                    lifecycleScope.launch {
                        vm.userIntent.send(AiIntent.SendAiMessage)
                    }
                    vm.addMessage()
                    vm.updateText("")
                    aiEditTextField.setText(vm.inputTextValue.value)
                    hideKeyboard()
                    aiIconButtonSend.isEnabled = false
                }
            }

            aiHelperTextsRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            helperTextAdapter = AIHelperTextAdapter(
                onClick = {
                    vm.updateTextWithHelper(it)
                    aiEditTextField.append("$it ")
                }
            )
            helperTextAdapter.submitList(vm.helperTextList.value)
            aiHelperTextsRV.adapter = helperTextAdapter

            aiMessagesRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false).apply {
                stackFromEnd = true
            }
            messageAdapter = AiMessageAdapter()
            messageAdapter.submitList(vm.messagesList.value)
            aiMessagesRV.adapter = messageAdapter
        }

        lifecycleScope.launch {
            vm.resetContextState.collect { state ->
                handleResetContextState(state)
            }
        }

        lifecycleScope.launch {
            vm.getAIMessageState.collect { state ->
                handleGetMessageState(state)
            }
        }

        lifecycleScope.launch {
            vm.sendMessageState.collect { state ->
                handleSendMessageState(state)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        vm.cleanStates()
        _binding = null
    }

    private fun showRecyclerView() {
        with (binding) {
            val animators = mutableListOf<Animator>()

            animators.add(ObjectAnimator.ofFloat(aiHelperTextsRV, "translationY", 0f))
            animators.add(ObjectAnimator.ofFloat(aiIconButtonSend, "translationY", 0f))
            animators.add(ObjectAnimator.ofFloat(aiIconButtonExpandMenuCard, "translationY", 0f))
            animators.add(ObjectAnimator.ofFloat(aiIconButtonCleanContext, "translationY", 0f))
            animators.add(ObjectAnimator.ofFloat(aiEditText, "translationY", 0f))
            animators.add(ObjectAnimator.ofFloat(aiLoadingGetAIMessage, "translationY", 0f))

            animateMessageRV(false)

            AnimatorSet().apply {
                playTogether(animators)
                duration = 300
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
        }
    }

    private fun hideRecyclerView() {
        with (binding) {
            val height = aiHelperTextsRV.height.toFloat()
            val animators = mutableListOf<Animator>()

            animators.add(ObjectAnimator.ofFloat(aiHelperTextsRV, "translationY", height))
            animators.add(ObjectAnimator.ofFloat(aiIconButtonSend, "translationY", height))
            animators.add(ObjectAnimator.ofFloat(aiIconButtonExpandMenuCard, "translationY", height))
            animators.add(ObjectAnimator.ofFloat(aiIconButtonCleanContext, "translationY", height))
            animators.add(ObjectAnimator.ofFloat(aiEditText, "translationY", height))
            animators.add(ObjectAnimator.ofFloat(aiLoadingGetAIMessage, "translationY", height))

            animateMessageRV(true)

            AnimatorSet().apply {
                playTogether(animators)
                duration = 300
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
        }
    }

    private fun animateMessageRV(closeHelperRV: Boolean) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.connect(
            R.id.aiMessagesRV,
            ConstraintSet.BOTTOM,
            R.id.aiEditText,
            if (!closeHelperRV) ConstraintSet.TOP else ConstraintSet.BOTTOM,
            if (!closeHelperRV) 0 else 16
        )
        val transition = ChangeBounds()
        transition.interpolator = AccelerateDecelerateInterpolator()
        transition.duration = 300
        TransitionManager.beginDelayedTransition(binding.root, transition)
        constraintSet.applyTo(binding.root)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.aiEditTextField.clearFocus()
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    private fun handleResetContextState(state: ResetAIContextState) {
        with (binding) {
            when (state) {
                is ResetAIContextState.Loading -> {
                    aiMessagesRV.visibility = View.GONE
                    aiEmptyTitle.visibility = View.GONE
                    aiLoadingMessages.visibility = View.VISIBLE
                }
                is ResetAIContextState.Success -> {
                    aiLoadingMessages.visibility = View.GONE
                    aiEmptyTitle.visibility = View.VISIBLE
                    aiMessagesRV.visibility = View.GONE
                    messageAdapter.submitList(emptyList())
                    ToastUtils.showResetContextAI(requireContext())
                }
                is ResetAIContextState.Error -> {
                    aiEmptyTitle.visibility = View.GONE
                    aiLoadingMessages.visibility = View.GONE
                    aiMessagesRV.visibility = View.VISIBLE
                    ToastUtils.showErrorToast(requireContext())
                }
                is ResetAIContextState.Idle -> Unit
            }
        }
    }

    private fun handleGetMessageState(state: GetAIMessageState) {
        with (binding) {
            when (state) {
                is GetAIMessageState.Loading -> {
                    aiIconButtonSend.isEnabled = false
                    aiIconButtonSend.visibility = View.INVISIBLE
                    aiLoadingGetAIMessage.visibility = View.VISIBLE
                    ToastUtils.showLoadingToast(requireContext())
                }
                is GetAIMessageState.Success -> {
                    aiIconButtonSend.isEnabled = true
                    aiIconButtonSend.visibility = View.VISIBLE
                    aiLoadingGetAIMessage.visibility = View.GONE
                    aiEmptyTitle.visibility = View.GONE
                    aiMessagesRV.visibility = View.VISIBLE
                    messageAdapter.submitList(vm.messagesList.value)
                    messageAdapter.notifyDataSetChanged()
                    aiMessagesRV.scrollToPosition(vm.messagesList.value.lastIndex)
                }
                is GetAIMessageState.Error -> {
                    aiIconButtonSend.isEnabled = true
                    aiIconButtonSend.visibility = View.VISIBLE
                    aiLoadingGetAIMessage.visibility = View.GONE
                    ToastUtils.showErrorToast(requireContext())
                }
                is GetAIMessageState.Idle -> Unit
            }
        }
    }

    private fun handleSendMessageState(state: SendAIMessageState) {
        with (binding) {
            when (state) {
                is SendAIMessageState.Loading -> {
                    aiMessagesRV.visibility = View.GONE
                    aiEmptyTitle.visibility = View.GONE
                    aiLoadingMessages.visibility = View.VISIBLE
                }
                is SendAIMessageState.Success -> {
                    aiMessagesRV.visibility = View.VISIBLE
                    aiEmptyTitle.visibility = View.GONE
                    aiLoadingMessages.visibility = View.GONE
                    messageAdapter.submitList(vm.messagesList.value)
                    aiMessagesRV.scrollToPosition(vm.messagesList.value.lastIndex)
                }
                is SendAIMessageState.Error -> {
                    aiMessagesRV.visibility = View.VISIBLE
                    aiEmptyTitle.visibility = View.GONE
                    aiLoadingMessages.visibility = View.GONE
                    messageAdapter.submitList(vm.messagesList.value)
                    ToastUtils.showErrorToast(requireContext())
                }
                is SendAIMessageState.Idle -> Unit
            }
        }
    }
}