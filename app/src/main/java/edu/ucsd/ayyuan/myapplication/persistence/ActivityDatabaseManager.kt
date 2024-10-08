package edu.ucsd.ayyuan.myapplication.persistence

import android.content.Context
import edu.ucsd.ayyuan.myapplication.models.activityObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityDatabaseManager (context: Context) {

    private val database: ActivityDatabase = ActivityDatabase.getDatabase(context = context)

    fun getAllActivities(onResult: (List<ActivityEntity>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val activityEntitiesList = database.ActivityListDao().getAllActivities()

            withContext(Dispatchers.Main) {
                onResult(activityEntitiesList)
            }
        }
    }

    private fun insertCourse(activityObjects: List<activityObject>) {
        CoroutineScope(Dispatchers.IO).launch {
            database.ActivityListDao().insertActivities(activityObjects)
        }
    }
}