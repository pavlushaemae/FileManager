package com.itis.filemanager.domain.files

import com.itis.filemanager.domain.files.model.FileInfo
import io.reactivex.rxjava3.core.Single

class LoadFileHashcodesToDbUseCase (
    private val fileRepository: FileRepository
) {
    operator fun invoke(): Single<List<FileInfo>> {
        return fileRepository.loadFileHashcodesToDb()
    }
}
