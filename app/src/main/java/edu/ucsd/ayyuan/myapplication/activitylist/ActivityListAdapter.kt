package edu.ucsd.ayyuan.myapplication.activitylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.ucsd.ayyuan.myapplication.R

open class ActivityListAdapter (
    var listener: Listener
) : ListAdapter<ActivityListItem, RecyclerView.ViewHolder>(TaskDiffCallBack()) {

    interface Listener {

        fun onDuplicateButtonPressed(position: Int)

        fun onDeleteButtonPressed(position: Int)
    }

    class TaskDiffCallBack : DiffUtil.ItemCallback<ActivityListItem>() {
        override fun areItemsTheSame(oldItem: ActivityListItem, newItem: ActivityListItem): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ActivityListItem, newItem: ActivityListItem): Boolean {
            return oldItem.time == newItem.time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_list_item, parent, false)
        return ActivityItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ActivityItemViewHolder).bind(position = position)
    }

    inner class ActivityItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.activity_name)
        private val tvTime: TextView = itemView.findViewById(R.id.activity_duration)
        private val btDuplicate: Button = itemView.findViewById(R.id.btDuplicate)

        private val btDelete: Button = itemView.findViewById(R.id.btDelete)
        fun bind(position: Int) {
            val activity = getItem(position)
            println("SPAGHETTI" + activity.time)
            tvName.text = "${activity.name}:"
            tvTime.text = formatTime(activity.time)

            btDuplicate.setOnClickListener {
                listener.onDuplicateButtonPressed(position = position)
            }

            btDelete.setOnClickListener {
                listener.onDeleteButtonPressed(position = position)
            }
        }
    }

    fun formatTime(totalTimeMs: Long): String {
        // Convert milliseconds to seconds
        val totalSeconds = (totalTimeMs / 1000).toInt()

        // Calculate minutes and seconds
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        // Format the time string
        return "$minutes minute${if (minutes != 1) "s" else ""} and $seconds second${if (seconds != 1) "s" else ""}"
    }
}

