package com.itis.filemanager.di

import com.itis.filemanager.presentation.fragments.list.listOfFilesModule
import org.koin.dsl.module

val featureModule = module {
    includes(
        listOfFilesModule
    )
}
