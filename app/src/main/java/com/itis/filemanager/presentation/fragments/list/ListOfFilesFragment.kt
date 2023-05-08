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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.itis.filemanager.BuildConfig
import com.itis.filemanager.R
import com.itis.filemanager.databinding.FragmentListOfFilesBinding
import com.itis.filemanager.presentation.recycler.files.FileAdapter
import com.itis.filemanager.presentation.recycler.files.utils.SpaceItemDecorator
import com.itis.filemanager.presentation.utils.Sort
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListOfFilesFragment : Fragment(R.layout.fragment_list_of_files) {

    private var _binding: FragmentListOfFilesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListOfFilesViewModel by viewModel()

    private var fileAdapter: FileAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOfFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        observeViewModel()
        activity?.requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        viewModel.loadFilesToDb()
        viewModel.browseToRootDirectory()
        binding.rvPosts.addItemDecoration(
            SpaceItemDecorator(requireContext(), SPACING_DP)
        )
        initSpinnerSort()
        initSpinnersSortAsc()
        binding.btnShowChangedFiles.setOnClickListener {
            viewModel.getChangedFiles()
        }
    }

    private fun initAdapter() {
        fileAdapter = FileAdapter(
            onItemClick = {
                viewModel.onItemClick(it)
            }, onShareClick = {
                viewModel.onShareClick(it)
            })
        binding.rvPosts.addItemDecoration(SpaceItemDecorator(requireContext(), SPACING_DP))
        binding.rvPosts.adapter = fileAdapter
    }

    private fun initSpinnerSort() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_array,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item
        ).also {
            it.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
            binding.spinnerSort.adapter = it
        }
        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent?.getItemAtPosition(position)?.let {
                    viewModel.sortBy(Sort.from(it.toString()))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    private fun initSpinnersSortAsc() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort_asc,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item
        ).also {
            it.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
            binding.spinnerSortAsc.adapter = it
        }

        binding.spinnerSortAsc.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    parent?.getItemAtPosition(position)?.let {
                        viewModel.sortByAsc(it.toString() == ASCENDING)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            }
    }

    private fun observeViewModel() {
        with(viewModel) {
            openFile.observe(viewLifecycleOwner) {
                val mType =
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension(it.extension)
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    it
                )
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(uri, mType)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            }
            currentPath.observe(viewLifecycleOwner) {
                binding.currentPath.text = it
            }
            listOfFiles.observe(viewLifecycleOwner) {
                binding.tvEmpty.isVisible = it.isEmpty()
                fileAdapter?.submitList(it)
                binding.rvPosts.scrollToPosition(0)
            }
            loading.observe(viewLifecycleOwner) {
                with(binding) {
                    if (it) {
                        pbLoading.visibility = View.VISIBLE
                        rvPosts.visibility = View.GONE
                    } else {
                        pbLoading.visibility = View.GONE
                        rvPosts.visibility = View.VISIBLE
                    }
                }
            }
            shareFile.observe(viewLifecycleOwner) {
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
                    getString(R.string.share)
                )
                chooserIntent.resolveActivity(requireActivity().packageManager)?.let {
                    startActivity(chooserIntent)
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ASCENDING = "Ascending"
        private const val SPACING_DP = 8f
    }
}
