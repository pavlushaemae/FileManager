package com.itis.filemanager.domain.files

import com.itis.filemanager.domain.files.model.FileInfo

class GetListOfFilesUseCase (
    private val fileRepository: FileRepository
) {
    operator fun invoke(): List<FileInfo>? {
        return fileRepository.getListOfFiles()
    }
}
