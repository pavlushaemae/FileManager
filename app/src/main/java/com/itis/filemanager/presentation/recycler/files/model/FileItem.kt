package com.itis.filemanager.presentation.recycler.files.model

data class FileItem(
    val name: String = "",
    val size: Long = 0,
    val dateOfCreate: String = "",
    val path: String = "",
    val isDirectory: Boolean = false,
    val absolutePath: String = "",
    val drawable: Int
)
