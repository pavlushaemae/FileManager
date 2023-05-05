package com.itis.filemanager.data.files

import android.os.Environment
import com.itis.filemanager.data.files.mapper.toFileInfo
import com.itis.filemanager.data.files.mapper.toListOfFileInfo
import com.itis.filemanager.domain.files.FileRepository
import com.itis.filemanager.domain.files.model.FileInfo
import com.itis.filemanager.presentation.recycler.files.model.FileItem
import java.io.File

class FileRepositoryImpl : FileRepository {
    private var currentDirectory: File = Environment.getExternalStorageDirectory()
    private val list = arrayListOf<FileInfo>()

    override fun getListOfFiles(): List<FileInfo>? {
        list.clear()
        if (currentDirectory.parent != null) {
            list.add(FileInfo(name = "..", path = "..", dateOfCreate = null, listFiles = null))
        }
        currentDirectory.listFiles()?.let {
            list.addAll(it.toListOfFileInfo())
        }

        return list
    }

    override fun getCurrentDirectory(): FileInfo {
        return currentDirectory.toFileInfo()
    }

    override fun upCurrentDirectory(): FileInfo {
        currentDirectory.parentFile?.let {
            currentDirectory = it
        }
        return currentDirectory.toFileInfo()
    }

    override fun setCurrentDirectory(fileInfo: FileInfo) {
        currentDirectory = File(fileInfo.absolutePath)
    }
}
