package com.itis.filemanager.presentation.fragments.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.itis.filemanager.domain.files.*
import com.itis.filemanager.domain.files.model.FileInfo
import com.itis.filemanager.presentation.recycler.files.model.FileItem
import com.itis.filemanager.presentation.recycler.files.model.toFileInfo
import com.itis.filemanager.presentation.recycler.files.model.toFileItemList
import com.itis.filemanager.presentation.utils.Sort
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
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

    private val _openFile: MutableLiveData<File> = MutableLiveData()
    val openFile: LiveData<File>
        get() = _openFile

    private val _shareFile: MutableLiveData<File> = MutableLiveData()
    val shareFile: LiveData<File>
        get() = _shareFile

    private val _currentPath: MutableLiveData<String> = MutableLiveData()
    val currentPath: LiveData<String>
        get() = _currentPath

    private val _listOfFiles: MutableLiveData<List<FileItem>> = MutableLiveData(emptyList())
    val listOfFiles: LiveData<List<FileItem>>
        get() = _listOfFiles

    private val sortBy: MutableLiveData<Sort> = MutableLiveData(Sort.SORT_NAME)

    private val sortByAsc: MutableLiveData<Boolean> = MutableLiveData(true)

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private fun openFile(aDirectory: FileInfo) {
        val file = File(aDirectory.absolutePath)
        _openFile.value = file
    }

    fun onItemClick(fileItem: FileItem) {
        fileItem.apply {
            if (name == "..") {
                upOneLevel()
            } else {
                browseTo(toFileInfo())
            }
        }
    }

    fun onShareClick(fileItem: FileItem) {
        _shareFile.value = File(fileItem.absolutePath)
    }

    fun sortBy(sort: Sort) {
        sortBy.value = sort
        fill()
    }

    fun sortByAsc(asc: Boolean) {
        sortByAsc.value = asc
        fill()
    }


    fun loadFilesToDb() {
        fileDisposable += loadFileHashcodesToDbUseCase()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                changedFiles = it.toFileItemList()
            }, onError = {
                Log.e("error", it.toString())
            })
    }

    fun getChangedFiles() {
        if (_listOfFiles.value == changedFiles) {
            browseToRootDirectory()
        } else {
            _listOfFiles.value = changedFiles
            _currentPath.value = "Changed files"
        }
    }

    fun browseToRootDirectory() {
        browseTo(getCurrentDirectoryUseCase())
    }

    private fun browseTo(directory: FileInfo) {
        setCurrentDirectoryUseCase(directory)
        if (directory.isDirectory) {
            fill()
            _currentPath.value = directory.absolutePath
        } else {
            openFile(directory)
        }
    }

    private fun fill() {
        sortBy.value?.let { sortBy ->
            sortByAsc.value?.let { sortByAsc ->
                fileDisposable +=
                    getListOfFilesUseCase(sortBy, sortByAsc)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe { _loading.value = true }
                        .doAfterTerminate { _loading.value = false }
                        .subscribeBy {
                            _listOfFiles.value = it.toFileItemList()
                        }
            }
        }

    }

    private fun upOneLevel() {
        browseTo(upCurrentDirectoryUseCase())
    }

    override fun onCleared() {
        super.onCleared()
        fileDisposable.clear()
    }
}
