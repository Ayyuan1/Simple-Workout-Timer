package edu.ucsd.ayyuan.myapplication

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar

class CountdownActivity : AppCompatActivity() {

    private lateinit var btPause: Button
    private lateinit var btCancel: Button
    private lateinit var tvCountDown: TextView
    private lateinit var activityList: ArrayList<UserActivity>
    private lateinit var progressBar: ProgressBar
    private lateinit var tvActivityName: TextView
    private lateinit var tvNextActivity: TextView

    private var currentActivityIndex = 0
    private var countDownTimer: CountDownTimer? = null
    private var totalTimeInMillis: Long = 0L
    private var timerRunning = false
    private var timeElapsed: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_countdown)

        supportActionBar?.title = "Timer"

        btPause = findViewById(R.id.btPause)
        btCancel = findViewById(R.id.btCancel)
        tvCountDown = findViewById(R.id.tvCountdown)
        progressBar = findViewById(R.id.progressBar)
        tvActivityName = findViewById(R.id.tvActivityName)
        tvNextActivity = findViewById(R.id.tvNextActivity)

        totalTimeInMillis = intent.getLongExtra("TOTAL_TIME", 0L)
        activityList = intent.getParcelableArrayListExtra<UserActivity>("ACTIVITY_LIST") ?: arrayListOf()

        startCountdownTimer()

        btCancel.setOnClickListener{
            countDownTimer?.cancel()
            finish()
        }

        val timeLeft = 0;
        btPause.setOnClickListener(View.OnClickListener {
            if (timerRunning) {
                // If timer is currently paused, resume it
                startCountdownTimer()
                btPause.setText("Pause")
            } else {
                // If timer is currently running, pause it
                pauseCountDownTimer()
                btPause.setText("Resume")
            }
            // Toggle the flag to indicate the current state of the timer
            timerRunning = !timerRunning
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun pauseCountDownTimer() {
        countDownTimer?.cancel()
    }

    private fun startCountdownTimer() {

        if (currentActivityIndex < activityList.size) {
            val activity = activityList[currentActivityIndex]
            tvActivityName.text = "Current: " + activity.name
            if (currentActivityIndex + 1 < activityList.size) {
                tvNextActivity.text = "Next: " + activityList[currentActivityIndex + 1].name
            } else {
                tvNextActivity.text = "Next: None"
            }

            val timeInMillis = computeTimeInMillis(activity.minutes, activity.seconds)
            progressBar.max = timeInMillis.toInt()

            countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = (millisUntilFinished / 1000) % 60
                    val minutes = (millisUntilFinished / 1000) / 60
                    tvCountDown.text = String.format("%02d:%02d", minutes, seconds)

                    timeElapsed += 1000
                    val progress = (timeInMillis - timeElapsed).toInt()
                    progressBar.progress = progress

                    if (millisUntilFinished <= 5000) {
                        playSound(R.raw.beep)
                    }
                }

                override fun onFinish() {
                    currentActivityIndex++
                    playSound(R.raw.ding)
                    startCountdownTimer()
                }
            }.start()
        } else {
            tvCountDown.text = "Done"
            progressBar.progress = progressBar.max
            showFinishedSnackBar()
        }
    }

    private fun playSound(soundResId: Int) {
        val mediaPlayer = MediaPlayer.create(this, soundResId)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { it.release() }
    }

    private fun showFinishedSnackBar() {
        val rootView: View = findViewById(android.R.id.content)
        Snackbar.make(rootView, "All activities completed!", Snackbar.LENGTH_LONG).show()
    }

    private fun computeTimeInMillis(minutes: Int, seconds: Int):Long {
        return ((minutes * 60000) + (seconds * 1000)).toLong()
    }
}