package com.itis.filemanager.domain.files

import com.itis.filemanager.domain.files.model.FileInfo
import com.itis.filemanager.presentation.utils.Sort
import io.reactivex.rxjava3.core.Single

class GetListOfFilesUseCaseImpl(
    private val fileRepository: FileRepository
) : GetListOfFilesUseCase {
    override operator fun invoke(
        sortBy: Sort,
        asc: Boolean
    ): Single<List<FileInfo>> {
        return fileRepository.getListOfFiles(sortBy, asc)
    }
}

interface GetListOfFilesUseCase {
    operator fun invoke(sortBy: Sort, asc: Boolean): Single<List<FileInfo>>
}
