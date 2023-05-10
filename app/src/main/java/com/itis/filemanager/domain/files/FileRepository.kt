package com.itis.filemanager.domain.files

import com.itis.filemanager.domain.files.model.FileInfo
import com.itis.filemanager.presentation.utils.Sort
import io.reactivex.rxjava3.core.Single

interface FileRepository {
    fun getListOfFiles(sortBy: Sort, asc: Boolean): Single<List<FileInfo>>
    fun getCurrentDirectory(): FileInfo
    fun upCurrentDirectory(): FileInfo
    fun setCurrentDirectory(fileInfo: FileInfo)
    fun loadFileHashcodesToDb(): Single<List<FileInfo>>
}
