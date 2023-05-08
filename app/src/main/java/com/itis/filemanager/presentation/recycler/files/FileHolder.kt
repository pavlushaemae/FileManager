package com.itis.filemanager.presentation.recycler.files

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.filemanager.databinding.ItemFileBinding
import com.itis.filemanager.presentation.recycler.files.model.FileItem

class FileHolder(
    private val binding: ItemFileBinding,
    private val onItemClick: (FileItem) -> Unit,
    private val onShareClick: (FileItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var fileItem: FileItem? = null

    init {
        with(binding) {
            root.setOnClickListener {
                fileItem?.let {
                    onItemClick(it)
                }
            }
            ivShare.setOnClickListener {
                fileItem?.let {
                    onShareClick(it)
                }
            }
        }

    }

    fun onBind(fileItem: FileItem) {
        this.fileItem = fileItem
        with(binding) {
            tvName.text = fileItem.name
            if (fileItem.name == "..") {
                ivShare.visibility = View.GONE
                linLayoutSecondaryInfo.visibility = View.GONE
                return
            }
            if (fileItem.isDirectory) ivShare.visibility = View.GONE
            ivIcon.setImageResource(fileItem.drawable)
            tvDateOfCreate.text = fileItem.dateOfCreate
            tvSize.text = fileItem.size.toString()
        }
    }


    companion object {

        fun create(
            parent: ViewGroup,
            onItemClick: (FileItem) -> Unit,
            onShareClick: (FileItem) -> Unit
        ): FileHolder = FileHolder(
            binding = ItemFileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick = onItemClick,
            onShareClick = onShareClick
        )
    }
}
