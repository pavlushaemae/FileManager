package com.itis.filemanager.domain.files

import com.itis.filemanager.domain.files.model.FileInfo
import io.reactivex.rxjava3.core.Single

class LoadFileHashcodesToDbUseCaseImpl(
    private val fileRepository: FileRepository
) : LoadFileHashcodesToDbUseCase {
    override operator fun invoke(): Single<List<FileInfo>> {
        return fileRepository.loadFileHashcodesToDb()
    }
}

interface LoadFileHashcodesToDbUseCase {
    operator fun invoke(): Single<List<FileInfo>>
}
