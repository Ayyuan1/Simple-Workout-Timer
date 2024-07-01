package edu.ucsd.ayyuan.myapplication

import androidx.recyclerview.widget.DiffUtil
class TimerDiffCallback : DiffUtil.ItemCallback<UserActivity>() {
    override fun areItemsTheSame(oldItem: UserActivity, newItem: UserActivity): Boolean {
        // Use name as the unique identifier for each Timer
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: UserActivity, newItem: UserActivity): Boolean {
        // Check if the contents of the items are the same
        return oldItem == newItem
    }
}