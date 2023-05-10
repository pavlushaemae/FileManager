package com.itis.filemanager.domain.files

import com.itis.filemanager.domain.files.model.FileInfo

class UpCurrentDirectoryUseCaseImpl(
    private val fileRepository: FileRepository
) : UpCurrentDirectoryUseCase {
    override operator fun invoke(): FileInfo {
        return fileRepository.upCurrentDirectory()
    }
}

interface UpCurrentDirectoryUseCase {
    operator fun invoke(): FileInfo
}
