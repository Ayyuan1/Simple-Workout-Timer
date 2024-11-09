package edu.ucsd.ayyuan.myapplication.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.ucsd.ayyuan.myapplication.models.activityObject

@Dao
interface ActivityListDao {
    @Query("SELECT * FROM activities")
    fun getAllActivities(): List<ActivityEntity>

    @Query("SELECT * FROM activities WHERE workoutId = :workoutId")
    fun getWorkoutById(workoutId: String): List<ActivityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(activities: List<ActivityEntity>)
}