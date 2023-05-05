package com.itis.filemanager.domain.files.model

import java.security.cert.Extension
import java.util.*

data class FileInfo(
    val name: String = "",
    val size: Long = 0,
    val dateOfCreate: Date? = null,
    val path: String = "",
    val isDirectory: Boolean = false,
    val absolutePath: String = "",
    val extension: String = "",
    val listFiles: List<FileInfo>?
)
