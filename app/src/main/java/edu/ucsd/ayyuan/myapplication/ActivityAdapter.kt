package edu.ucsd.ayyuan.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class UserActivity(val name: String, val minutes: Int, val seconds: Int)

class ActivityAdapter (
    private val activities: MutableList<UserActivity>,
    private val onDuplicate: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>() {

    inner class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val btDuplicate: Button = itemView.findViewById(R.id.btDuplicate)
        val btDelete: Button = itemView.findViewById(R.id.btDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_activity,parent, false)
        return ActivityViewHolder(view)
    }

    override fun getItemCount() = activities.size

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val activity = activities[position]
        holder.tvName.text = "${activity.name}"
        holder.tvTime.text = "${activity.minutes}: ${activity.seconds} "
        holder.btDuplicate.setOnClickListener{ onDuplicate(position) }
        holder.btDelete.setOnClickListener{ onDelete(position) }
    }

}

