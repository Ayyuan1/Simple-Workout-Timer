package edu.ucsd.ayyuan.myapplication.activitylist

import android.os.Parcelable
import edu.ucsd.ayyuan.myapplication.models.activityObject
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActivityListItem(
    var activity: activityObject = activityObject()
) : Parcelable
