package com.example.reflect.presentation.screens.ai.fragment

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reflect.databinding.FragmentAiBinding
import com.example.reflect.presentation.screens.ai.adapter.AIHelperTextAdapter
import com.example.reflect.presentation.screens.ai.adapter.AiMessageAdapter
import com.example.reflect.presentation.screens.ai.viewmodel.ViewModelAI
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AiFragment : Fragment() {

    private var _binding: FragmentAiBinding? = null
    private val binding get() = _binding!!

    private val vm: ViewModelAI by activityViewModels()

    private lateinit var helperTextAdapter: AIHelperTextAdapter
    private lateinit var messageAdapter: AiMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            aiEditTextField.setText(vm.inputTextValue.value)
            aiEditTextField.doAfterTextChanged { value ->
                vm.updateText(value.toString())
            }

            aiToolbarBackArrow.setOnClickListener {
                findNavController().popBackStack()
            }

            aiHelperTextsRV.post {
                val height = aiHelperTextsRV.height.toFloat()

                aiHelperTextsRV.translationY = height
                aiIconButtonSend.translationY = height
                aiIconButtonExpandMenu.translationY = height
                aiEditText.translationY = height
            }

            aiIconButtonExpandMenu.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    showRecyclerView()
                } else {
                    hideRecyclerView()
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showRecyclerView() {
        with (binding) {
            val animators = mutableListOf<Animator>()
            animators.add(ObjectAnimator.ofFloat(aiHelperTextsRV, "translationY", 0f))
            animators.add(ObjectAnimator.ofFloat(aiIconButtonSend, "translationY", 0f))
            animators.add(ObjectAnimator.ofFloat(aiIconButtonExpandMenu, "translationY", 0f))
            animators.add(ObjectAnimator.ofFloat(aiEditText, "translationY", 0f))

            AnimatorSet().apply {
                playTogether(animators)
                duration = 300
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }

//            ObjectAnimator.ofFloat(aiHelperTextsRV, "translationY", aiHelperTextsRV.height.toFloat(), 0f).apply {
//                duration = 300
//                start()
//            }
        }
    }

    private fun hideRecyclerView() {
        with (binding) {
            val height = aiHelperTextsRV.height.toFloat()
            val animators = mutableListOf<Animator>()
            animators.add(ObjectAnimator.ofFloat(aiHelperTextsRV, "translationY", height))
            animators.add(ObjectAnimator.ofFloat(aiIconButtonSend, "translationY", height))
            animators.add(ObjectAnimator.ofFloat(aiIconButtonExpandMenu, "translationY", height))
            animators.add(ObjectAnimator.ofFloat(aiEditText, "translationY", height))

            AnimatorSet().apply {
                playTogether(animators)
                duration = 300
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
        }
    }
}