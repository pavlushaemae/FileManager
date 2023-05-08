package com.itis.filemanager.domain.files

import com.itis.filemanager.domain.files.model.FileInfo

class SetCurrentDirectoryUseCase(
    private val fileRepository: FileRepository
) {
    operator fun invoke(fileInfo: FileInfo) {
        return fileRepository.setCurrentDirectory(fileInfo)
    }
}
