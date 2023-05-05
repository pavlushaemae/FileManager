package com.itis.filemanager.di

import com.itis.filemanager.data.files.FileRepositoryImpl
import com.itis.filemanager.domain.files.*
import org.koin.dsl.module

val filesModule = module {
    single { provideFileRepository() }
    factory { GetListOfFilesUseCase(get()) }
    factory { GetCurrentDirectoryUseCase(get()) }
    factory { UpCurrentDirectoryUseCase(get()) }
    factory { SetCurrentDirectoryUseCase(get()) }
}

private fun provideFileRepository(): FileRepository {
    return FileRepositoryImpl()
}
