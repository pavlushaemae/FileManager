package com.itis.filemanager.presentation.utils

enum class Sort(val value: String) {
    SORT_NAME("Name"),
    SORT_EXTENSION("Extension"),
    SORT_SIZE("Size"),
    SORT_DATE("Date");

    companion object {
        fun from(value: String): Sort = values().first { it.value == value }
    }
}
