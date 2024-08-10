package com.applabs.batteryoptimizationpoc

import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {


    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Acquire a wake lock to keep the CPU running even when the screen is off
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag")
        wakeLock?.acquire()

        // Keep the screen on with full brightness
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val layoutParams = window.attributes
        layoutParams.screenBrightness = 1.0f // Set brightness to maximum
        window.attributes = layoutParams

        // Set up and start multiple Lottie animations
        val lottieAnimationView1 = findViewById<LottieAnimationView>(R.id.lottieAnimationView1)
        lottieAnimationView1.setAnimation(R.raw.confetti) // Complex animation
        lottieAnimationView1.playAnimation()

        val lottieAnimationView2 = findViewById<LottieAnimationView>(R.id.lottieAnimationView2)
        lottieAnimationView2.setAnimation(R.raw.confetti) // Another complex animation
        lottieAnimationView2.playAnimation()

        val lottieAnimationView3 = findViewById<LottieAnimationView>(R.id.lottieAnimationView3)
        lottieAnimationView3.setAnimation(R.raw.confetti) // Another complex animation
        lottieAnimationView3.playAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the wake lock when the activity is destroyed to save battery
        wakeLock?.release()
    }
}
