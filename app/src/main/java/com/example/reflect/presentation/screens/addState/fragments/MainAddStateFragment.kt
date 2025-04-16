package com.example.reflect.presentation.screens.addState.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.reflect.R
import com.example.reflect.databinding.FragmentMainAddStateBinding

class MainAddStateFragment : Fragment() {

    private var _binding: FragmentMainAddStateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainAddStateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with (binding) {
            addStateSlider.addOnChangeListener { _, value, _ ->
                when (value.toInt()) {
                    in 0..1 -> addStateImage.setImageResource(R.drawable.ic_add_state_emotion_1)
                    in 2..3 -> addStateImage.setImageResource(R.drawable.ic_add_state_emotion_2)
                    in 4..6 -> addStateImage.setImageResource(R.drawable.ic_add_state_emotion_3)
                    in 7..8 -> addStateImage.setImageResource(R.drawable.ic_add_state_emotion_4)
                    in 9..10 -> addStateImage.setImageResource(R.drawable.ic_add_state_emotion_5)
                    else -> throw IllegalStateException("Как так вообще получилось, что значение от 0 до 10 больше 10?!")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}