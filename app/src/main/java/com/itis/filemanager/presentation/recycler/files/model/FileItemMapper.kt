package com.itis.filemanager.presentation.recycler.files.model

import com.itis.filemanager.R
import com.itis.filemanager.domain.files.model.FileInfo
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun FileInfo.toFileItem(): FileItem = FileItem(
    name = name,
    size = size / KILOBYTES,
    path = path,
    dateOfCreate = dateOfCreate?.format("hh:mm dd.MM.YYYY").orEmpty(),
    isDirectory = isDirectory,
    absolutePath = absolutePath,
    drawable = getDrawable()
)

fun FileItem.toFileInfo(): FileInfo = FileInfo(
    name = name,
    size = size * KILOBYTES,
    path = path,
    dateOfCreate = dateOfCreate.parseToDate(),
    isDirectory = isDirectory,
    absolutePath = absolutePath,
    listFiles = null,
)

private const val KILOBYTES = 1024

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

private fun String.parseToDate(): Date? {
    return SimpleDateFormat("hh:mm dd.MM.yyyy", Locale.getDefault()).parse(this)
}

private fun Date.format(format: String): String {
    val dateFormat: DateFormat = SimpleDateFormat(format, Locale.getDefault())
    return dateFormat.format(this)
}
