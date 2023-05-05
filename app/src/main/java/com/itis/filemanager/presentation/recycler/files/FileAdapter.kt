package com.itis.filemanager.presentation.recycler.files

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.itis.filemanager.presentation.recycler.files.utils.ItemCallback
import com.itis.filemanager.presentation.recycler.files.model.FileItem

class FileAdapter(
    private val onItemClick: (FileItem) -> Unit
): ListAdapter<FileItem, RecyclerView.ViewHolder>(ItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        FileHolder.create(parent, onItemClick)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as FileHolder).onBind(getItem(position))

}
