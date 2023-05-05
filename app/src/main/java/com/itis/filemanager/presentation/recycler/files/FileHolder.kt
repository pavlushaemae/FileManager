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
            ivIcon.setImageResource(fileItem.drawable)
            tvName.text = fileItem.name
            tvDateOfCreate.text = fileItem.dateOfCreate?.format("hh:mm dd.MM.YYYY").orEmpty()
            tvSize.text = (fileItem.size / KILOBYTES).toString()

        }
    }

    fun Date.format(format: String): String {
        val dateFormat: DateFormat = SimpleDateFormat(format, Locale.getDefault())
        return dateFormat.format(this)
    }

    companion object {

        private const val KILOBYTES = 1024

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
