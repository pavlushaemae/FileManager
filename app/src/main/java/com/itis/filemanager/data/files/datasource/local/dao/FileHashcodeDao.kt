package com.itis.filemanager.data.files.datasource.local.dao

import androidx.room.*
import com.itis.filemanager.data.files.datasource.local.model.FileHashcodeModel

@Dao
interface FileHashcodeDao {

    @Query("SELECT * FROM file_hashcode")
    fun getAll(): List<FileHashcodeModel>

    @Query("SELECT * FROM file_hashcode WHERE path = :path")
    fun findByPath(path: String): FileHashcodeModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fileHashcodeModel: FileHashcodeModel)

    @Delete
    fun delete(fileHashcodeModel: FileHashcodeModel)
}
