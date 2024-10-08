package edu.ucsd.ayyuan.myapplication.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class activityObject (
    var name: String = "",
    var time: Long = 0L
) : Parcelable