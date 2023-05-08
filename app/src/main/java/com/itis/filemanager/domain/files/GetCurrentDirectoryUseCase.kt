package com.itis.filemanager.domain.files

import com.itis.filemanager.domain.files.model.FileInfo

class GetCurrentDirectoryUseCase(
    private val fileRepository: FileRepository
) {
    operator fun invoke(): FileInfo {
        return fileRepository.getCurrentDirectory()
    }
}

