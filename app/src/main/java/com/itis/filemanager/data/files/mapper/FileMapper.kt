package com.itis.filemanager.data.files.mapper

import com.itis.filemanager.domain.files.model.FileInfo
import java.io.File
import java.util.*

fun File.toFileInfo(): FileInfo = FileInfo(
    name = name,
    size = length(),
    path = path,
    dateOfCreate = Date(lastModified()),
    isDirectory = isDirectory,
    absolutePath = absolutePath,
    extension = extension,
    listFiles = listFiles()?.toListOfFileInfo()
)

fun Array<out File>.toListOfFileInfo() = map {
    it.toFileInfo()
}

