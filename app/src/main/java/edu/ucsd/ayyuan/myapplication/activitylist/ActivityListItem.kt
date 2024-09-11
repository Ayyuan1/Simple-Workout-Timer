package edu.ucsd.ayyuan.myapplication.activitylist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActivityListItem(
    var name: String = "",
    var time: Long = 0L
) : Parcelable
