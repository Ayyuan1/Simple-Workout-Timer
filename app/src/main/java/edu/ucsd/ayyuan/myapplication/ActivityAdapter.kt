package edu.ucsd.ayyuan.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.os.Parcel
import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import java.util.Collections

data class UserActivity(val name: String, val minutes: Int, val seconds: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(minutes)
        parcel.writeInt(seconds)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserActivity> {
        override fun createFromParcel(parcel: Parcel): UserActivity {
            return UserActivity(parcel)
        }

        override fun newArray(size: Int): Array<UserActivity?> {
            return arrayOfNulls(size)
        }
    }
}


class ActivityAdapter (
    private val activities: MutableList<UserActivity>,
    private val onDuplicate: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder>(), ItemTouchHelperAdapter {

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

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(activities, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(activities, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }
}

