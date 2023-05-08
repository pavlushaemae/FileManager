package com.itis.filemanager.presentation.recycler.files.model

data class FileItem(
    val name: String,
    val size: Long,
    val dateOfCreate: String,
    val path: String,
    val isDirectory: Boolean,
    val absolutePath: String,
    val drawable: Int
)
