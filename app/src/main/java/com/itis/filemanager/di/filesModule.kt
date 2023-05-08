package com.itis.filemanager.di

import com.itis.filemanager.data.files.FileRepositoryImpl
import com.itis.filemanager.data.files.datasource.local.dao.FileHashcodeDao
import com.itis.filemanager.domain.files.*
import org.koin.dsl.module

val filesModule = module {
    single { provideFileRepository(get()) }
    factory { GetListOfFilesUseCase(get()) }
    factory { GetCurrentDirectoryUseCase(get()) }
    factory { UpCurrentDirectoryUseCase(get()) }
    factory { SetCurrentDirectoryUseCase(get()) }
    factory { LoadFileHashcodesToDbUseCase(get()) }
}

private fun provideFileRepository(
    fileHashcodeDao: FileHashcodeDao
): FileRepository {
    return FileRepositoryImpl(fileHashcodeDao)
}
