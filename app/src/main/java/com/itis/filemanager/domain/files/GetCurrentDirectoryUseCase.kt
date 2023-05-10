package com.itis.filemanager.domain.files

import com.itis.filemanager.domain.files.model.FileInfo

class GetCurrentDirectoryUseCaseImpl(
    private val fileRepository: FileRepository
) : GetCurrentDirectoryUseCase {
    override operator fun invoke(): FileInfo {
        return fileRepository.getCurrentDirectory()
    }
}

interface GetCurrentDirectoryUseCase {
    operator fun invoke(): FileInfo
}
