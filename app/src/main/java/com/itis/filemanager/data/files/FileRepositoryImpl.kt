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
    private val list = arrayListOf<FileInfo>()

    override fun getListOfFiles(sortBy: String, asc: Boolean): Single<List<FileInfo>> {
//        list.clear()
//        if (currentDirectory.parent != null) {
//            list.add(FileInfo(name = "..", path = "..", dateOfCreate = null, listFiles = null))
//        }
//        currentDirectory.listFiles()?.let {
//            list.addAll(it.toListOfFileInfo())
//        }
        return Single.fromCallable {
            var list = getAllFiles(currentDirectory).toListOfFileInfo()
            when (sortBy) {
                "Name" -> {
                    list = if (asc) {
                        list.sortedBy { it.name }
                    } else {
                        list.sortedByDescending { it.name }
                    }
                }
                "Extension" -> {
                    list = if (asc) {
                        list.sortedBy { it.extension }
                    } else {
                        list.sortedByDescending { it.extension }
                    }
                }
                "Size" -> {
                    list = if (asc) {
                        list.sortedBy { it.size }
                    } else {
                        list.sortedByDescending { it.size }
                    }
                }
                "Date" -> {
                    list = if (asc) {
                        list.sortedBy { it.dateOfCreate }
                    } else {
                        list.sortedByDescending { it.dateOfCreate }
                    }
                }
            }

            return@fromCallable list
        }.subscribeOn(Schedulers.io())
    }

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
//        val digest = MessageDigest.getInstance("SHA-256")
//        file.inputStream().use { input ->
//            val buffer = ByteArray(8192)
//            var read = input.read(buffer, 0, buffer.size)
//            while (read > 0) {
//                digest.update(buffer, 0, read)
//                read = input.read(buffer, 0, buffer.size)
//            }
//        }
//        return digest.digest()
        val s = StringBuilder()
        s.append(file.name.length)
        s.append(file.length())
        s.append(file.path.length)
        s.append(file.absolutePath.length)
        return s.toString()
    }
}
