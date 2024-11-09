package edu.ucsd.ayyuan.myapplication.activitylist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import edu.ucsd.ayyuan.myapplication.CountdownActivity
import edu.ucsd.ayyuan.myapplication.CreateActivity
import edu.ucsd.ayyuan.myapplication.R
import edu.ucsd.ayyuan.myapplication.databinding.ActivityListBinding
import edu.ucsd.ayyuan.myapplication.models.Workout
import edu.ucsd.ayyuan.myapplication.models.activityObject
import edu.ucsd.ayyuan.myapplication.persistence.ActivityDatabase
import edu.ucsd.ayyuan.myapplication.persistence.ActivityDatabaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ActivityListActivity : AppCompatActivity(), ActivityListAdapter.Listener {

    private var _binding: ActivityListBinding? = null
    private val binding get() = _binding!!
    private val INPUT_REQUEST_CODE = 1
    private var totalTime: Long = 0L

    private lateinit var activityListAdapter: ActivityListAdapter
    private lateinit var activityDatabaseManager: ActivityDatabaseManager
    private lateinit var btCreate: Button
    private lateinit var btStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.setOnApplyWindowInsetsListener { view, insets ->
            val systemInsets = insets.systemWindowInsetBottom
            view.setPadding(0, 0, 0, systemInsets)
            insets
        }

        supportActionBar?.title = getString(R.string.simple_workout_timer)

        btCreate = findViewById(R.id.btCreate)
        btStart = findViewById(R.id.btStart)
        activityListAdapter = ActivityListAdapter(listener = this)
        activityDatabaseManager = ActivityDatabaseManager(context = this)
        binding.activityList.adapter = activityListAdapter

        //checkForExercises()
        bindActionButtons()
    }

    private fun checkForExercises() {
        if (activityListAdapter.currentList.isNotEmpty()) {
            binding.noExercisesEmptyState.visibility =  View.GONE
        } else {
            binding.noExercisesEmptyState.visibility =  View.VISIBLE
        }
    }

    private fun bindActionButtons() {
        btStart.setOnClickListener{
            if (activityListAdapter.currentList.isEmpty()) {
                Snackbar.make(it, "Add an event first!", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            totalTime = calculateTotalTime()
            val intent = Intent(this, CountdownActivity::class.java)
            val activityList = arrayListOf<ActivityListItem>()
            for (activity in activityListAdapter.currentList) {
                activityList.add(activity)
            }
            intent.putParcelableArrayListExtra("activity_list", activityList)
            startActivity(intent)
        }

        btCreate.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }
    private fun calculateTotalTime(): Long {
        for (activity in activityListAdapter.currentList) {
            totalTime += activity.activity.time
        }
        return totalTime
    }

    override fun onDuplicateButtonPressed(position: Int) {
        val currentItem = activityListAdapter.currentList[position]
        val copiedItem = ActivityListItem(
            activity = activityObject(
                name = currentItem.activity.name,
                time = currentItem.activity.time
            )
        )
        val updatedList = activityListAdapter.currentList.toMutableList()
        updatedList.add(copiedItem)

        activityListAdapter.submitList(updatedList)
    }

    override fun onDeleteButtonPressed(position: Int) {
        val updatedList = activityListAdapter.currentList.toMutableList()

        // Ensure that the position is valid before removing
        if (position in updatedList.indices) {
            updatedList.removeAt(position)

            activityListAdapter.submitList(updatedList) {
                activityListAdapter.notifyItemRemoved(position)
                activityListAdapter.notifyItemRangeChanged(position, updatedList.size - position)
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INPUT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val name = it.getStringExtra("activity_name").toString()
                val totalTimeInMs = it.getLongExtra("activity_total_time", 0)
                val newActivity = ActivityListItem(
                    activity = activityObject(
                        name = name,
                        time = totalTimeInMs
                    )
                )
                val updatedList = activityListAdapter.currentList.toMutableList()
                updatedList.add(newActivity)
                activityListAdapter.submitList(updatedList)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_load -> {
                loadWorkouts()
                true
            }
            R.id.mi_save -> {
                saveWorkout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveWorkout() {
        if (activityListAdapter.currentList.isEmpty()) {
            Toast.makeText(this, "Add at least one exercise to save this workout.", Toast.LENGTH_LONG).show()
        }
        val activityItemList = activityListAdapter.currentList
        val activityList = mutableListOf<activityObject>()
        activityItemList.forEach {
            activityList.add(it.activity)
        }
        CoroutineScope(Dispatchers.IO).launch {
            activityDatabaseManager.insertWorkout(activityList)
        }
        Toast.makeText(this, "Workout Saved!", Toast.LENGTH_LONG).show()
    }

    private fun loadWorkouts() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}