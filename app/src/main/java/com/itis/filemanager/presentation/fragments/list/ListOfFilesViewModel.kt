package com.itis.filemanager.presentation.fragments.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.filemanager.domain.files.GetCurrentDirectoryUseCase
import com.itis.filemanager.domain.files.GetListOfFilesUseCase
import com.itis.filemanager.domain.files.SetCurrentDirectoryUseCase
import com.itis.filemanager.domain.files.UpCurrentDirectoryUseCase
import com.itis.filemanager.domain.files.model.FileInfo
import com.itis.filemanager.presentation.recycler.files.FileAdapter
import com.itis.filemanager.presentation.recycler.files.model.FileItem
import com.itis.filemanager.presentation.recycler.files.model.toFileInfo
import com.itis.filemanager.presentation.recycler.files.model.toFileItemList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class ListOfFilesViewModel(
    private val getCurrentDirectoryUseCase: GetCurrentDirectoryUseCase,
    private val getListOfFilesUseCase: GetListOfFilesUseCase,
    private val upCurrentDirectoryUseCase: UpCurrentDirectoryUseCase,
    private val setCurrentDirectoryUseCase: SetCurrentDirectoryUseCase
) : ViewModel() {

    private val _adapter: MutableStateFlow<FileAdapter?> = MutableStateFlow(null)
    val adapter: StateFlow<FileAdapter?>
        get() = _adapter

    private val _openFile: MutableSharedFlow<File?> = MutableSharedFlow()
    val openFile: SharedFlow<File?>
        get() = _openFile

    private val _currentPath: MutableSharedFlow<String> = MutableSharedFlow()
    val currentPath: SharedFlow<String>
        get() = _currentPath

    private val _listOfFiles: MutableStateFlow<List<FileItem>> = MutableStateFlow(listOf())
    val listOfFiles: StateFlow<List<FileItem>>
        get() = _listOfFiles


    fun browseToRootDirectory() {
        browseTo(getCurrentDirectoryUseCase())
    }


    private fun browseTo(aDirectory: FileInfo) {
        setCurrentDirectoryUseCase(aDirectory)
        if (aDirectory.isDirectory) {
            fill()
        viewModelScope.launch {
            _currentPath.emit(aDirectory.absolutePath)
        }
        } else {
            viewModelScope.launch {
                val file = File(aDirectory.absolutePath)
                _openFile.emit(file)
            }
        }
    }

    private fun fill() {
        getListOfFilesUseCase()?.let {
            _listOfFiles.value = it.toFileItemList()
        }
    }

    private fun upOneLevel() {
        browseTo(upCurrentDirectoryUseCase())
    }

    fun initAdapter() {
        _adapter.value = FileAdapter {
            if (it.path == "..") {
                upOneLevel()
            } else {
                browseTo(it.toFileInfo())
            }
        }
    }
}
