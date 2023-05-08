package com.itis.filemanager.di

import com.itis.filemanager.data.files.FileRepositoryImpl
import com.itis.filemanager.data.files.datasource.local.dao.FileHashcodeDao
import com.itis.filemanager.domain.files.*
import org.koin.dsl.module

val filesModule = module {
    single { provideFileRepository(get()) }
    factory { provideGetListOfFilesUseCase(get()) }
    factory { provideGetCurrentDirectoryUseCase(get()) }
    factory { provideUpCurrentDirectoryUseCase(get()) }
    factory { provideSetCurrentDirectoryUseCase(get()) }
    factory { provideLoadFileHashcodesToDbUseCase(get()) }
}

private fun provideFileRepository(
    fileHashcodeDao: FileHashcodeDao
): FileRepository {
    return FileRepositoryImpl(fileHashcodeDao)
}

private fun provideGetCurrentDirectoryUseCase(
    fileRepository: FileRepository
): GetCurrentDirectoryUseCase = GetCurrentDirectoryUseCaseImpl(fileRepository)

private fun provideGetListOfFilesUseCase(
    fileRepository: FileRepository
): GetListOfFilesUseCase = GetListOfFilesUseCaseImpl(fileRepository)

private fun provideUpCurrentDirectoryUseCase(
    fileRepository: FileRepository
): UpCurrentDirectoryUseCase = UpCurrentDirectoryUseCaseImpl(fileRepository)

private fun provideSetCurrentDirectoryUseCase(
    fileRepository: FileRepository
): SetCurrentDirectoryUseCase = SetCurrentDirectoryUseCaseImpl(fileRepository)

private fun provideLoadFileHashcodesToDbUseCase(
    fileRepository: FileRepository
): LoadFileHashcodesToDbUseCase = LoadFileHashcodesToDbUseCaseImpl(fileRepository)
