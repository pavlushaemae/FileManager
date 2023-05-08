package com.itis.filemanager.presentation.recycler.files.model

import java.util.*

data class FileItem(
    val name: String = "",
    val size: Long = 0,
    val dateOfCreate: Date? = null,
    val path: String = "",
    val isDirectory: Boolean = false,
    val absolutePath: String = "",
    val drawable: Int
)
