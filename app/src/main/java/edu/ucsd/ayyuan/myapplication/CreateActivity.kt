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
        private const val INPUT_REQUEST_CODE = 1;
    }
    private lateinit var btConfirm: Button
    private lateinit var npMinutes: NumberPicker
    private lateinit var npSeconds: NumberPicker
    private lateinit var tvSelectedMinutes: TextView
    private lateinit var tvSelectedSeconds: TextView
    private lateinit var etName: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create)

        supportActionBar?.title = "New Activity"

        btConfirm = findViewById(R.id.btConfirm)
        npMinutes = findViewById(R.id.npMinutes)
        npSeconds = findViewById(R.id.npSeconds)
        etName = findViewById(R.id.etName)
        tvSelectedMinutes = findViewById(R.id.tvSelectedMinutes)
        tvSelectedSeconds = findViewById(R.id.tvSelectedSeconds)

        npMinutes.maxValue = 60
        npSeconds.maxValue = 59

        var minutes = 0;
        var seconds = 0;
        var name = "Unnamed Activity"

        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                name = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        npMinutes.setOnValueChangedListener { picker, oldVal, newVal ->
            minutes = newVal
            tvSelectedMinutes.setText("Minutes: " + minutes)
        }

        npSeconds.setOnValueChangedListener { picker, oldVal, newVal ->
            seconds = newVal
            tvSelectedSeconds.setText("Seconds: " + seconds)
        }

        btConfirm.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putExtra("activity_name", name)
            resultIntent.putExtra("activity_minutes", minutes)
            resultIntent.putExtra("activity_seconds", seconds)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}