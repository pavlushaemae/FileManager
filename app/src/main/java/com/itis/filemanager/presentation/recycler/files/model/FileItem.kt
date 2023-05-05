package com.itis.filemanager.presentation.recycler.files.model

import java.util.*
import kotlin.random.Random

data class FileItem(
    val name: String = "",
    val size: Long = 0,
    val dateOfCreate: Date? = null,
    val path: String = "",
    val isDirectory: Boolean = false,
    val absolutePath: String = ""
)
