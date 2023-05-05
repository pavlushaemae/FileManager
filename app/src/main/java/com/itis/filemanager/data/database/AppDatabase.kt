package com.itis.filemanager.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.itis.filemanager.data.files.datasource.local.dao.FileHashcodeDao
import com.itis.filemanager.data.files.datasource.local.model.FileHashcodeModel

@Database(
    entities = [FileHashcodeModel::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getFileHashcodeDao(): FileHashcodeDao

    companion object {
        const val DATABASE_NAME = "com.itis.filemanager"
    }
}
