package com.itis.filemanager.presentation.fragments.list

import org.koin.androidx.viewmodel.dsl.viewModel

import org.koin.dsl.module

val listOfFilesModule = module {
    viewModel { ListOfFilesViewModel(get(), get(), get(), get()) }
}
