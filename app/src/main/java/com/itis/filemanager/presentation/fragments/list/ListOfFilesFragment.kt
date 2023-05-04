package com.itis.filemanager.presentation.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itis.filemanager.R
import com.itis.filemanager.databinding.FragmentListOfFilesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListOfFilesFragment: Fragment(R.layout.fragment_list_of_files) {

    private lateinit var binding: FragmentListOfFilesBinding

    private val viewModel: ListOfFilesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListOfFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}
