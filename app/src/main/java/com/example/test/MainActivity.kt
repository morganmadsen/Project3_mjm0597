package com.example.test

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.animation.doOnEnd

class MainActivity : ComponentActivity() {

    // List of image resource IDs
    private val images = listOf(
        R.drawable.grad2, // Replace with your images
        R.drawable.grad3,
        R.drawable.grad4,
        R.drawable.grad5,
        R.drawable.grad6,
        R.drawable.grad7
    )
    private var currentIndex = 0 // Tracks the current image index

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find views by their IDs
        val currentImageView = findViewById<ImageView>(R.id.currentImageView)
        val nextImageView = findViewById<ImageView>(R.id.nextImageView)
        val buttonPrevious = findViewById<ImageButton>(R.id.buttonPrevious)
        val buttonNext = findViewById<ImageButton>(R.id.buttonNext)
        val buttonReset = findViewById<ImageButton>(R.id.buttonReset)
        val imageCounter = findViewById<TextView>(R.id.imageCounter)

        // Initialize the first image
        currentImageView.setImageResource(images[currentIndex])
        imageCounter.text = "${currentIndex + 1}/${images.size}"

        // Function to handle the animation and image transitions
        fun updateImageWithAnimation(next: Boolean) {
            val nextIndex = if (next) {
                (currentIndex + 1) % images.size
            } else {
                if (currentIndex == 0) images.size - 1 else currentIndex - 1
            }

            // Set the next image resource
            nextImageView.setImageResource(images[nextIndex])
            nextImageView.visibility = View.VISIBLE

            // Calculate animation directions
            val startOffset = if (next) currentImageView.width.toFloat() else -currentImageView.width.toFloat()
            val endOffset = if (next) -currentImageView.width.toFloat() else currentImageView.width.toFloat()

            // Animate the current image out
            ObjectAnimator.ofFloat(currentImageView, "translationX", 0f, endOffset).apply {
                duration = 300
                start()
            }

            // Animate the next image in
            ObjectAnimator.ofFloat(nextImageView, "translationX", startOffset, 0f).apply {
                duration = 300
                start()
            }.doOnEnd {
                // Update the current image and reset positions
                currentImageView.setImageResource(images[nextIndex])
                currentImageView.translationX = 0f
                nextImageView.visibility = View.GONE
                nextImageView.translationX = 0f
                currentIndex = nextIndex

                // Update the counter text
                imageCounter.text = "${currentIndex + 1}/${images.size}"
            }
        }

        // Button click listeners
        buttonNext.setOnClickListener {
            updateImageWithAnimation(next = true)
        }

        buttonPrevious.setOnClickListener {
            updateImageWithAnimation(next = false)
        }

        buttonReset.setOnClickListener {
            currentIndex = 0
            currentImageView.setImageResource(images[currentIndex])
            currentImageView.translationX = 0f
            nextImageView.visibility = View.GONE
            imageCounter.text = "${currentIndex + 1}/${images.size}"
        }
    }
}
