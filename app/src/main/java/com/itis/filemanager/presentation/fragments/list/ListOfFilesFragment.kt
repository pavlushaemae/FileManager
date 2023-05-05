package com.itis.filemanager.presentation.fragments.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.itis.filemanager.presentation.recycler.files.utils.SpaceItemDecorator
import com.itis.filemanager.BuildConfig
import com.itis.filemanager.R
import com.itis.filemanager.databinding.FragmentListOfFilesBinding
import com.itis.filemanager.presentation.recycler.files.FileAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListOfFilesFragment : Fragment() {

    private lateinit var binding: FragmentListOfFilesBinding

    private val viewModel: ListOfFilesViewModel by viewModel()

    private var fileAdapter: FileAdapter? = null

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
        observeViewModel()
        activity?.requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        viewModel.initAdapter()
        viewModel.browseToRootDirectory()
        binding.rvPosts.addItemDecoration(
            SpaceItemDecorator(requireContext(), 16f)
        )
        viewModel.loadFilesToDb()
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_array,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item
        ).also {
            it.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
            binding.spinnerSort.adapter = it
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_asc,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item
        ).also {
            it.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
            binding.spinnerSortAsc.adapter = it
        }
        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent?.getItemAtPosition(position).also {
                    it?.let {
                        viewModel.sortBy(it.toString())
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

        }
        binding.spinnerSortAsc.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    parent?.getItemAtPosition(position).also {
                        parent?.getItemAtPosition(position).also {
                            it?.let {
                                viewModel.sortByAsc(it.toString())
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            }
        binding.btnAoa.setOnClickListener {
            viewModel.getChangedFiles()
        }
    }

    private fun observeViewModel() {
        with(viewModel) {
            adapter.flowWithLifecycle(lifecycle)
                .onEach {
                    it?.let {
                        fileAdapter = it
                        binding.rvPosts.adapter = fileAdapter
                    }
                }.launchIn(lifecycleScope)
            openFile.flowWithLifecycle(lifecycle)
                .onEach {
                    it?.let {
                        val mType =
                            MimeTypeMap.getSingleton().getMimeTypeFromExtension(it.extension)
                        val u = FileProvider.getUriForFile(
                            requireContext(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            it
                        )
                        val i = Intent(Intent.ACTION_VIEW)
                        i.setDataAndType(u, mType)
                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        startActivity(i)
                    }
                }.launchIn(lifecycleScope)
            lifecycleScope.launch {
                currentPath.collect {
                    binding.currentPath.text = it
                }
            }
            listOfFiles.flowWithLifecycle(lifecycle)
                .onEach {
                    fileAdapter?.submitList(it)
                }.launchIn(lifecycleScope)
            shareFile.flowWithLifecycle(lifecycle)
                .onEach {
                    it?.let {
                        val intent = Intent()
                        intent.action = Intent.ACTION_SEND
                        val mType =
                            MimeTypeMap.getSingleton().getMimeTypeFromExtension(it.extension)
                        val u = FileProvider.getUriForFile(
                            requireContext(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            it
                        )
                        intent.type = mType
                        intent.putExtra(Intent.EXTRA_STREAM, u)
                        val chooserIntent = Intent.createChooser(
                            intent,
                            "Share"
                        )
                        if (chooserIntent.resolveActivity(requireActivity().packageManager) != null) {
                            startActivity(chooserIntent)
                        }
                    }
                }.launchIn(lifecycleScope)
        }
    }
}
