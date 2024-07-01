package edu.ucsd.ayyuan.myapplication
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson

@Dao
interface TemplateDao {
    @Insert
    suspend fun insert(template: Template)

    @Query("SELECT * FROM template_table ORDER BY id ASC")
    suspend fun getAllTemplates(): List<Template>
}

@Dao
interface TimerDao {

}
@Entity(tableName = "template_table")
data class Template(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @TypeConverters(TimerListConverter::class) val timers: List<UserActivity>
)

// Converter to store list of timers
class TimerListConverter {
    @TypeConverter
    fun fromTimerList(timers: List<UserActivity>): String {
        // Convert list of timers to JSON string or other format
        return Gson().toJson(timers)
    }

    @TypeConverter
    fun toTimerList(data: String): List<UserActivity> {
        // Convert JSON string back to list of timers
        return Gson().fromJson(data, Array<UserActivity>::class.java).toList()
    }
}
