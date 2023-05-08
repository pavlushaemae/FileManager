package com.itis.filemanager.presentation.recycler.files.model

import com.itis.filemanager.R
import com.itis.filemanager.domain.files.model.FileInfo

fun FileInfo.toFileItem(): FileItem = FileItem(
    name = name,
    size = size,
    path = path,
    dateOfCreate = dateOfCreate,
    isDirectory = isDirectory,
    absolutePath = absolutePath,
    drawable = getDrawable()
)

fun FileItem.toFileInfo(): FileInfo = FileInfo(
    name = name,
    size = size,
    path = path,
    dateOfCreate = dateOfCreate,
    isDirectory = isDirectory,
    absolutePath = absolutePath,
    listFiles = null,
)

fun List<FileInfo>.toFileItemList() = map { it.toFileItem() }

fun FileInfo.getDrawable(): Int {
    return when (extension) {
        "png" -> R.drawable.png_file
        "txt" -> R.drawable.txt_file
        "pdf" -> R.drawable.pdf_file
        "mp4" -> R.drawable.mp4_file
        "mp3" -> R.drawable.mp3_file
        "jpg" -> R.drawable.jpg_file
        "jpeg" -> R.drawable.jpeg_file
        else -> R.drawable.ic_baseline_folder_24
    }
}
