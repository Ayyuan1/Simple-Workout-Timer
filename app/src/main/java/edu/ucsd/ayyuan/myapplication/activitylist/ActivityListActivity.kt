package edu.ucsd.ayyuan.myapplication.activitylist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import edu.ucsd.ayyuan.myapplication.CountdownActivity
import edu.ucsd.ayyuan.myapplication.CreateActivity
import edu.ucsd.ayyuan.myapplication.R
import edu.ucsd.ayyuan.myapplication.databinding.ActivityListBinding
import edu.ucsd.ayyuan.myapplication.models.activityObject


class ActivityListActivity : AppCompatActivity(), ActivityListAdapter.Listener {

    private val INPUT_REQUEST_CODE = 1
    private var totalTime: Long = 0L
    private lateinit var activityListAdapter: ActivityListAdapter
    private var _binding: ActivityListBinding? = null
    private val binding get() = _binding!!
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
        binding.activityList.adapter = activityListAdapter

        bindActionButtons()
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
        updatedList.removeAt(position)

        activityListAdapter.submitList(updatedList)
    }

    @Deprecated("Deprecated in Java")
    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}