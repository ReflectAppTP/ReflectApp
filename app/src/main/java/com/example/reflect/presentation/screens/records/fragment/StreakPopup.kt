package com.example.reflect.presentation.screens.records.fragment

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.example.reflect.R
import com.example.reflect.databinding.PopupStreakBinding

class StreakPopup(private val context: Context) {
    private val binding by lazy {
        PopupStreakBinding.inflate(LayoutInflater.from(context), null,false)
    }
    private val popupWindow = PopupWindow(
        binding.root,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        true
    ).apply {
        elevation = 10f
    }

    fun show(anchorView: View) {
        popupWindow.showAsDropDown(
            anchorView,
            -anchorView.width / 2,
            20,
            Gravity.CENTER_HORIZONTAL
        )
    }

    fun updateData(streak: Int) {
        with (binding) {
            popupStreakImage.setOnClickListener {
                val scaleDownY = ObjectAnimator.ofFloat(it, "scaleY", 0.85f).apply {
                    duration = 70
                }
                val scaleUpY = ObjectAnimator.ofFloat(it, "scaleY", 1f).apply {
                    duration = 70
                }
                AnimatorSet().apply {
                    play(scaleDownY).before(scaleUpY)
                    start()
                }
            }
            popupStreakCount.text = streak.toString()
            when (streak) {
                0 -> popupStreakImage.setImageResource(R.drawable.image_cat_streak_0)
                in 1..6 -> popupStreakImage.setImageResource(R.drawable.image_cat_streak_1)
                else -> popupStreakImage.setImageResource(R.drawable.image_cat_streak_2)
            }
        }
    }

    fun dismiss() {
        popupWindow.dismiss()
    }
}
