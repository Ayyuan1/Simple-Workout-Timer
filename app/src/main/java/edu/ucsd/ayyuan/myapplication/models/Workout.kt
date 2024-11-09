package edu.ucsd.ayyuan.myapplication.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Workout (
    val id: String = "",
    val name: String = "",
    val exerciseList: List<activityObject> = listOf()
) : Parcelable