package com.eva.reminders.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eva.reminders.data.local.adapters.ColorEnumAdapter
import com.eva.reminders.data.local.adapters.DateTimeAdapter
import com.eva.reminders.data.local.dao.LabelsDao
import com.eva.reminders.data.local.dao.TaskDao
import com.eva.reminders.data.local.entity.LabelEntity
import com.eva.reminders.data.local.entity.TaskEntity
import com.eva.reminders.data.local.entity.TaskLabelRel

@Database(
    entities = [TaskEntity::class, LabelEntity::class, TaskLabelRel::class],
    version = 1, exportSchema = false
)
@TypeConverters(ColorEnumAdapter::class, DateTimeAdapter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract val taskLabelDao: LabelsDao
    abstract val taskDao: TaskDao

    companion object {
        private const val DATABASE_NAME = "REMINDERS_DATABASE"

        fun buildDataBase(context: Context): AppDataBase {
            return Room
                .databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
                .addTypeConverter(DateTimeAdapter.instance)
                .addTypeConverter(ColorEnumAdapter.instance)
                .build()
        }
    }
}