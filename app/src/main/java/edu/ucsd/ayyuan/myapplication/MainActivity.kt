package edu.ucsd.ayyuan.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.util.ArrayList
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {

    private val INPUT_REQUEST_CODE = 1
    private val activities = mutableListOf<UserActivity>()
    private var totalTime: Long = 0L
    private lateinit var adapter: ActivityAdapter

    private lateinit var btCreate: Button
    private lateinit var btStart: Button
    private lateinit var rvList: RecyclerView
    private lateinit var timerListAdapter: ItemTouchHelperAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Activity Timer"

        rvList = findViewById(R.id.rvList)
        btCreate = findViewById(R.id.btCreate)
        btStart = findViewById(R.id.btStart)
        adapter = ActivityAdapter(activities, this::duplicateActivity, this::deleteActivity)
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = adapter
        
        btStart.setOnClickListener{
            if (activities.isEmpty()) {
                Snackbar.make(it, "Add an event first!", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            totalTime = calculateTotalTime()
            val intent = Intent(this, CountdownActivity::class.java)
            intent.putExtra("TOTAL_TIME", totalTime)
            intent.putParcelableArrayListExtra("ACTIVITY_LIST", ArrayList(activities))
            startActivity(intent)
        }

        btCreate.setOnClickListener {
            startActivityForResult(Intent(this@MainActivity, CreateActivity::class.java), INPUT_REQUEST_CODE)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_save -> {
                showSaveTemplateDialog()
                true
            }
            R.id.mi_load -> {
                showLoadTemplateDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoadTemplateDialog() {
        TODO("Not yet implemented")
    }

    private fun showSaveTemplateDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Save Template")

        // Set up the input
        val input = EditText(this)
        input.hint = "Template Name"
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("Save") { dialog, which ->
            val templateName = input.text.toString()
            // Save the template with the given name
            saveTemplate(templateName)
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun saveTemplate(templateName: String) {
        val newTemplate = Template(name = templateName, timers = getTimersFromList())
    }

    // Assuming you have a method to get the current timers from your list
    private fun getTimersFromList(): List<UserActivity> {
        // Get the list of timers from your RecyclerView adapter or other data source
        return listOf()
    }

    private fun calculateTotalTime(): Long {
        return activities.sumOf { (it.minutes * 60000) + (it.seconds * 1000)}.toLong()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INPUT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val name = it.getStringExtra("activity_name").toString()
                val minutes = it.getIntExtra("activity_minutes", 0)
                val seconds = it.getIntExtra("activity_seconds", 0+0)
                activities.add(UserActivity(name, minutes, seconds))
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun deleteActivity(position: Int) {
        activities.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    private fun duplicateActivity(position: Int) {
        val activity = activities[position]
        activities.add(UserActivity(activity.name, activity.minutes, activity.seconds))
        adapter.notifyDataSetChanged()
    }


}