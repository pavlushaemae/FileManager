package com.itis.filemanager.domain.files.model

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
) {
    companion object {
        const val BACK_NAME = ".."
    }
}
