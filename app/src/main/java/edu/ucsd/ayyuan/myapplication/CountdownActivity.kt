package edu.ucsd.ayyuan.myapplication

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.min

class CountdownActivity : AppCompatActivity() {

    private lateinit var btPause: Button
    private lateinit var btCancel: Button
    private lateinit var tvCountDown: TextView
    private var countDownTimer: CountDownTimer? = null
    private var totalTimeInMillis: Long = 0L
    private var timerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_countdown)

        supportActionBar?.title = ""

        btPause = findViewById(R.id.btPause)
        btCancel = findViewById(R.id.btCancel)
        tvCountDown = findViewById(R.id.tvCountdown)

        totalTimeInMillis = intent.getLongExtra("TOTAL_TIME", 0L)
        startCountdownTimer(totalTimeInMillis)

        btCancel.setOnClickListener{
            countDownTimer?.cancel()
            finish()
        }

        btPause.setOnClickListener{
            timerRunning = false
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun startCountdownTimer(timeInMillis: Long) {
        countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = (millisUntilFinished / 1000) % 60
                val minutes = (millisUntilFinished / 1000) / 60
                tvCountDown.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                tvCountDown.text = "00:00"
                timerRunning = false
            }
        }.start()
        timerRunning = true

    }
}