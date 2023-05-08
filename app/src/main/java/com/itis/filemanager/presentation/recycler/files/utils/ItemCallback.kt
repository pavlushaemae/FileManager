package com.itis.filemanager.presentation.recycler.files.utils

import androidx.recyclerview.widget.DiffUtil
import com.itis.filemanager.presentation.recycler.files.model.FileItem

object ItemCallback : DiffUtil.ItemCallback<FileItem>() {
    override fun areItemsTheSame(
        oldItem: FileItem,
        newItem: FileItem
    ): Boolean =
        oldItem.path == newItem.path

    override fun areContentsTheSame(
        oldItem: FileItem,
        newItem: FileItem
    ): Boolean =
        oldItem == newItem
}
