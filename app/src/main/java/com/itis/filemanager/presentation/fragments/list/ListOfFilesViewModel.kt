package com.itis.filemanager.presentation.fragments.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itis.filemanager.domain.files.*
import com.itis.filemanager.domain.files.model.FileInfo
import com.itis.filemanager.presentation.recycler.files.FileAdapter
import com.itis.filemanager.presentation.recycler.files.model.FileItem
import com.itis.filemanager.presentation.recycler.files.model.toFileInfo
import com.itis.filemanager.presentation.recycler.files.model.toFileItemList
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
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
    private val setCurrentDirectoryUseCase: SetCurrentDirectoryUseCase,
    private val loadFileHashcodesToDbUseCase: LoadFileHashcodesToDbUseCase
) : ViewModel() {

    private var fileDisposable: CompositeDisposable = CompositeDisposable()

    private var changedFiles: List<FileItem> = listOf()

    private val _adapter: MutableStateFlow<FileAdapter?> = MutableStateFlow(null)
    val adapter: StateFlow<FileAdapter?>
        get() = _adapter

    private val _openFile: MutableSharedFlow<File?> = MutableSharedFlow()
    val openFile: SharedFlow<File?>
        get() = _openFile

    private val _shareFile: MutableSharedFlow<File?> = MutableSharedFlow()
    val shareFile: SharedFlow<File?>
        get() = _shareFile

    private val _currentPath: MutableSharedFlow<String> = MutableSharedFlow()
    val currentPath: SharedFlow<String>
        get() = _currentPath

    private val _listOfFiles: MutableStateFlow<List<FileItem>> = MutableStateFlow(listOf())
    val listOfFiles: StateFlow<List<FileItem>>
        get() = _listOfFiles

    private val sortBy: MutableStateFlow<String> = MutableStateFlow("Name")


    private val sortByAsc: MutableStateFlow<Boolean> = MutableStateFlow(true)


    fun browseToRootDirectory() {
        browseTo(getCurrentDirectoryUseCase())
    }


    private fun browseTo(aDirectory: FileInfo) {
//        setCurrentDirectoryUseCase(aDirectory)
        if (aDirectory.isDirectory) {
            fill()
//        viewModelScope.launch {
//            _currentPath.emit(aDirectory.absolutePath)
//        }
        } else {
            viewModelScope.launch {
                val file = File(aDirectory.absolutePath)
                _openFile.emit(file)
            }
        }
    }

    private fun fill() {
        fileDisposable +=
            getListOfFilesUseCase(sortBy.value, sortByAsc.value)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { Log.e("", "loading") }
                .doAfterTerminate { Log.e("", "loading finished") }
                .subscribeBy {
                    _listOfFiles.value = it.toFileItemList()
                }
    }

//    private fun upOneLevel() {
//        browseTo(upCurrentDirectoryUseCase())
//    }

    fun initAdapter() {
        _adapter.value = FileAdapter(
            onItemClick = {
//            if (it.path == "..") {
//                upOneLevel()
//            } else {
                browseTo(it.toFileInfo())
//            }
            }, onShareClick = {
                viewModelScope.launch {
                    _shareFile.emit(File(it.absolutePath))
                }
            })
    }

    fun sortBy(sort: String) {
        sortBy.value = sort
        fill()
    }

    fun sortByAsc(asc: String) {
        sortByAsc.value = asc == "Ascending"
        fill()
    }


    fun loadFilesToDb() {
        fileDisposable += loadFileHashcodesToDbUseCase()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                changedFiles = it.toFileItemList()
            }

    }

    fun getChangedFiles() {
        if (_listOfFiles.value == changedFiles) {
            fill()
        } else {
            _listOfFiles.value = changedFiles
        }

    }

    override fun onCleared() {
        super.onCleared()
        fileDisposable.clear()
    }
}
