package com.itis.filemanager.domain.files

import com.itis.filemanager.domain.files.model.FileInfo

class SetCurrentDirectoryUseCaseImpl(
    private val fileRepository: FileRepository
) : SetCurrentDirectoryUseCase {
    override operator fun invoke(fileInfo: FileInfo) {
        return fileRepository.setCurrentDirectory(fileInfo)
    }
}

interface SetCurrentDirectoryUseCase {
    operator fun invoke(fileInfo: FileInfo)
}
