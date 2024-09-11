package edu.ucsd.ayyuan.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateActivity : AppCompatActivity() {

    companion object {
        private const val INPUT_REQUEST_CODE = 1
        private const val MILLISPERMINUTE = 60000
        private const val MILLISPERSECOND = 1000
    }
    private lateinit var btConfirm: Button
    private lateinit var npMinutes: NumberPicker
    private lateinit var npSeconds: NumberPicker
    private lateinit var tvSelectedMinutes: TextView
    private lateinit var tvSelectedSeconds: TextView
    private lateinit var etName: EditText

    private var name: String = ""
    private var timeInMillis: Long = 0L
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.title = "New Activity"

        btConfirm = findViewById(R.id.btConfirm)
        npMinutes = findViewById(R.id.npMinutes)
        npSeconds = findViewById(R.id.npSeconds)
        etName = findViewById(R.id.etName)
        tvSelectedMinutes = findViewById(R.id.tvSelectedMinutes)
        tvSelectedSeconds = findViewById(R.id.tvSelectedSeconds)
        npMinutes.maxValue = 60
        npSeconds.maxValue = 59
        btConfirm.isEnabled = false

        bindPickerButtons()
    }

    private fun bindPickerButtons() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                name = s.toString()
                btConfirm.isEnabled = timeInMillis != 0L && name.isNotBlank()
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        npMinutes.setOnValueChangedListener { _, oldVal, newVal ->
            timeInMillis -= oldVal * MILLISPERMINUTE
            timeInMillis += newVal * MILLISPERMINUTE
            btConfirm.isEnabled = timeInMillis != 0L && name.isNotBlank()
            tvSelectedMinutes.setText("Minutes: " + newVal)
        }

        npSeconds.setOnValueChangedListener { picker, oldVal, newVal ->
            timeInMillis -= oldVal * MILLISPERSECOND
            timeInMillis += newVal * MILLISPERSECOND
            btConfirm.isEnabled = timeInMillis != 0L && name.isNotBlank()
            tvSelectedSeconds.setText("Seconds: " + newVal)
        }


        btConfirm.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putExtra("activity_name", name)
            resultIntent.putExtra("activity_total_time", timeInMillis)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}