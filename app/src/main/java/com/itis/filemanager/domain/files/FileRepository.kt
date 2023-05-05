package com.itis.filemanager.domain.files

import com.itis.filemanager.domain.files.model.FileInfo
import com.itis.filemanager.presentation.recycler.files.model.FileItem

interface FileRepository {
    fun getListOfFiles(): List<FileInfo>?
    fun getCurrentDirectory(): FileInfo
    fun upCurrentDirectory(): FileInfo
    fun setCurrentDirectory(fileInfo: FileInfo)
}
