package edu.ucsd.ayyuan.myapplication.persistence

import android.content.Context
import edu.ucsd.ayyuan.myapplication.models.activityObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

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

    private fun insertActivities(activities: List<ActivityEntity>) {
        CoroutineScope(Dispatchers.IO).launch {
            database.ActivityListDao().insertActivities(activities)
        }
    }

    suspend fun insertWorkout(activities: List<activityObject>) {
        try {
            withContext(Dispatchers.IO) {
                val workoutId = UUID.randomUUID().toString()
                val activityEntities = mutableListOf<ActivityEntity>()

                activities.forEach {
                    val entity = ActivityEntity(
                        id = UUID.randomUUID().toString(),
                        workoutId = workoutId,
                        name = it.name,
                        timeInMs = it.time
                    )
                    activityEntities.add(entity)
                }

                insertActivities(activityEntities)
            }
        } catch (exception: Exception){
            exception.printStackTrace()
        }
    }
}