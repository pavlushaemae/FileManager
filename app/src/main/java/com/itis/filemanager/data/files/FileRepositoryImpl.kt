package com.itis.filemanager.data.files

import android.os.Environment
import com.itis.filemanager.data.files.datasource.local.dao.FileHashcodeDao
import com.itis.filemanager.data.files.datasource.local.model.FileHashcodeModel
import com.itis.filemanager.data.files.mapper.toFileInfo
import com.itis.filemanager.data.files.mapper.toListOfFileInfo
import com.itis.filemanager.domain.files.FileRepository
import com.itis.filemanager.domain.files.model.FileInfo
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class FileRepositoryImpl(
    private val fileHashcodeDao: FileHashcodeDao
) : FileRepository {
    private var currentDirectory: File = Environment.getExternalStorageDirectory()
    private var list = arrayListOf<FileInfo>()

    private fun getAllFiles(directory: File): List<File> {
        val files = mutableListOf<File>()
        directory.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                files.addAll(getAllFiles(file))
            } else {
                files.add(file)
            }
        }
        return files
    }

    override fun getListOfFiles(sortBy: String, asc: Boolean): Single<List<FileInfo>> {
        return Single.fromCallable {
            list.clear()

            currentDirectory.listFiles()?.let {
                list.addAll(it.toListOfFileInfo())
            }

            when (sortBy) {
                "Name" -> {
                    if (asc) {
                        list.sortBy { it.name }
                    } else {
                        list.sortByDescending { it.name }
                    }
                }
                "Extension" -> {
                    if (asc) {
                        list.sortBy { it.extension }
                    } else {
                        list.sortByDescending { it.extension }
                    }
                }
                "Size" -> {
                    if (asc) {
                        list.sortBy { it.size }
                    } else {
                        list.sortByDescending { it.size }
                    }
                }
                "Date" -> {
                    if (asc) {
                        list.sortBy { it.dateOfCreate }
                    } else {
                        list.sortByDescending { it.dateOfCreate }
                    }
                }
            }
            currentDirectory.parent?.let {
                if (it != Environment.getExternalStorageDirectory().parent)
                    list.add(0,
                        FileInfo(
                            name = "..",
                            dateOfCreate = null,
                            listFiles = null
                        )
                    )
            }

            return@fromCallable list
        }
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

    override fun loadFileHashcodesToDb(): Single<List<FileInfo>> {
        return Single.fromCallable {
            val files = getAllFiles(Environment.getExternalStorageDirectory())
            val changedFiles = mutableListOf<File>()
            for (file in files) {
                val hash = calculateHash(file)
                val fileHashcodeModel = fileHashcodeDao.findByPath(file.absolutePath)
                if (fileHashcodeModel == null) {
                    fileHashcodeDao.insert(FileHashcodeModel(file.absolutePath, hash))
                } else if (hash != (fileHashcodeModel.hash)) {
                    changedFiles.add(file)
                    fileHashcodeDao.insert(FileHashcodeModel(file.absolutePath, hash))
                }
            }
            return@fromCallable changedFiles.toListOfFileInfo()
        }.subscribeOn(Schedulers.io())
    }

    private fun calculateHash(file: File): String {
        val s = StringBuilder()
        s.append(file.name.length)
        s.append(file.length())
        s.append(file.path.length)
        s.append(file.absolutePath.length)
        return s.toString()
    }
}
