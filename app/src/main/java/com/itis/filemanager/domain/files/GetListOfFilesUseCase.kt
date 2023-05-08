package com.itis.filemanager.domain.files

import com.itis.filemanager.domain.files.model.FileInfo
import io.reactivex.rxjava3.core.Single

class GetListOfFilesUseCase (
    private val fileRepository: FileRepository
) {
    operator fun invoke(
        sortBy: String,
        asc: Boolean
    ): Single<List<FileInfo>> {
        return fileRepository.getListOfFiles(sortBy, asc)
    }
}
