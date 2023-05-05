package com.itis.filemanager.presentation.recycler.files

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itis.filemanager.databinding.ItemFileBinding
import com.itis.filemanager.presentation.recycler.files.model.FileItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FileHolder(
    private val binding: ItemFileBinding,
    private val onItemClick: (FileItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var fileItem: FileItem? = null

    init {
        binding.root.setOnClickListener {
            fileItem?.let {
                onItemClick(it)
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
            tvName.text = fileItem.name
            if (fileItem.name != "..") {
                tvDateOfCreate.text = fileItem.dateOfCreate?.format("hh:mm dd.MM").orEmpty()
                tvSize.text = fileItem.size.toString()
            }
        }
    }
    fun Date.format(format: String): String {
        val dateFormat: DateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(this)
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClick: (FileItem) -> Unit
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
