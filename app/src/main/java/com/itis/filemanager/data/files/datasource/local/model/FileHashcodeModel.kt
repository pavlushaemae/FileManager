package com.itis.filemanager.data.files.datasource.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "file_hashcode")
data class FileHashcodeModel (
    @PrimaryKey
    val path: String,
    val hash: String
)


