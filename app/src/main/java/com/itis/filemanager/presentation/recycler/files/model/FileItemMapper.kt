package com.itis.filemanager.presentation.recycler.files.model

import com.itis.filemanager.domain.files.model.FileInfo

fun FileInfo.toFileItem(): FileItem = FileItem(
    name = name,
    size = size,
    path = path,
    dateOfCreate = dateOfCreate,
    isDirectory = isDirectory,
    absolutePath = absolutePath,
)

fun FileItem.toFileInfo(): FileInfo = FileInfo(
    name = name,
    size = size,
    path = path,
    dateOfCreate = dateOfCreate,
    isDirectory = isDirectory,
    absolutePath = absolutePath,
    listFiles = null
)

fun List<FileInfo>.toFileItemList() = map { it.toFileItem() }
