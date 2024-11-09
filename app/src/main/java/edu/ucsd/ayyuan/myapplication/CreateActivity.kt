package edu.ucsd.ayyuan.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_activity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "New Exercise"

        btConfirm = findViewById(R.id.btConfirm)
        npMinutes = findViewById(R.id.npMinutes)
        npSeconds = findViewById(R.id.npSeconds)
        etName = findViewById(R.id.etName)
        tvSelectedMinutes = findViewById(R.id.tvSelectedMinutes)
        tvSelectedSeconds = findViewById(R.id.tvSelectedSeconds)

        val minValue = 0
        val maxValue = 55
        val increment = 5
        val rangeSize = (maxValue - minValue) / increment + 1
        val displayedValues = arrayOfNulls<String>(rangeSize)
        for (i in 0 until rangeSize) {
            displayedValues[i] = (minValue + i * increment).toString()
        }

        npMinutes.maxValue = 10
        npSeconds.setMinValue(0)
        npSeconds.setMaxValue(displayedValues.size - 1)
        npSeconds.setDisplayedValues(displayedValues)
        npSeconds.setWrapSelectorWheel(true)

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
            timeInMillis -= oldVal * 5 * MILLISPERSECOND
            timeInMillis += newVal * 5 * MILLISPERSECOND
            btConfirm.isEnabled = timeInMillis != 0L && name.isNotBlank()
            tvSelectedSeconds.setText("Seconds: " + (newVal*5))
        }


        btConfirm.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putExtra("activity_name", name)
            resultIntent.putExtra("activity_total_time", timeInMillis)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_right_out, R.anim.slide_right_in)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}