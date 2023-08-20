package com.cwnextgen.letternumberchase.utils

import android.view.View
import android.view.animation.Animation
import com.cwnextgen.letternumberchase.R
import android.view.animation.AnimationUtils
import android.widget.TextView

object Animations {

    //shake view
     fun playShakeAnimation(view: View) {
        // Load the shake animation
        val shakeAnimation = AnimationUtils.loadAnimation(view.context, R.anim.shake_animation)

        // Set an animation listener to hide the view when the animation ends
        shakeAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Not needed
            }

            override fun onAnimationEnd(animation: Animation?) {
              //  view.visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Not needed
            }
        })

        // Start the shake animation on the provided view
        view.startAnimation(shakeAnimation)
    }
}


//fade in/out
fun textChangeWithAnimation(textView: TextView, newText: String) {
    val fadeOut = AnimationUtils.loadAnimation(textView.context, R.anim.fade_out)
    val fadeIn = AnimationUtils.loadAnimation(textView.context, R.anim.fade_in)

    // Set an animation listener for the fade-out animation
    fadeOut.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            // Not needed
        }

        override fun onAnimationRepeat(animation: Animation?) {
            // Not needed
        }

        override fun onAnimationEnd(animation: Animation?) {
            // Change the text when the fade-out animation is complete
            textView.text = newText
            textView.startAnimation(fadeIn) // Start the fade-in animation
        }
    })

    textView.startAnimation(fadeOut) // Start the fade-out animation
}