package com.itis.filemanager.di

import android.content.Context
import androidx.room.Room
import com.itis.filemanager.data.database.AppDatabase
import com.itis.filemanager.data.files.datasource.local.dao.FileHashcodeDao
import org.koin.dsl.module

val databaseModule = module {
    single { provideRoomDatabase(get()) }
    single { provideFileHashcodeDao(get()) }
}

fun provideRoomDatabase(
    context: Context,
): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
    .build()

fun provideFileHashcodeDao(
    roomDatabase: AppDatabase
): FileHashcodeDao = roomDatabase.getFileHashcodeDao()
