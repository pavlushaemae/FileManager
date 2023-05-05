package com.itis.filemanager.domain.files

import com.itis.filemanager.domain.files.model.FileInfo

class UpCurrentDirectoryUseCase(
    private val fileRepository: FileRepository
) {
    operator fun invoke(): FileInfo{
        return fileRepository.upCurrentDirectory()
    }
}
