package com.itis.filemanager.presentation.recycler.files

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.filemanager.databinding.ItemFileBinding
import com.itis.filemanager.presentation.recycler.files.model.FileItem

class FileHolder(
    private val binding: ItemFileBinding,
    private val onItemClick: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var fileItem: FileItem? = null

    init {
        binding.root.setOnClickListener {
            fileItem?.let {
                onItemClick()
            }
        }
    }

    fun onBind(fileItem: FileItem) {
        this.fileItem = fileItem
        with(binding) {
//            ivImage.load(subscriptionUiModel.imageLink) {
//                crossfade(true)
//                    .transformations(CircleCropTransformation())
//        }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClick: () -> Unit
        ): FileHolder = FileHolder(
            binding = ItemFileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick = onItemClick
        )
    }
}
