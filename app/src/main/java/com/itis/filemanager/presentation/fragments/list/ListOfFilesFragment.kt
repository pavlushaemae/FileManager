package com.itis.filemanager.presentation.fragments.list

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.itis.filemanager.BuildConfig
import com.itis.filemanager.R
import com.itis.filemanager.databinding.FragmentListOfFilesBinding
import com.itis.filemanager.presentation.recycler.files.FileAdapter
import com.itis.filemanager.presentation.recycler.files.utils.SpaceItemDecorator
import com.itis.filemanager.presentation.utils.Sort
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ListOfFilesFragment : Fragment(R.layout.fragment_list_of_files) {

    private var _binding: FragmentListOfFilesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListOfFilesViewModel by viewModel()

    private val fileAdapter: FileAdapter by lazy {
        FileAdapter(
            onItemClick = {
                viewModel.onItemClick(it)
            }, onShareClick = {
                viewModel.onShareClick(it)
            })
    }

    private val itemDecorator: SpaceItemDecorator by lazy {
        SpaceItemDecorator(requireContext(), SPACING_DP)
    }

    private val settings =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

    private val permission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            granted?.apply {
                when {
                    granted -> viewModel.onReadExternalStorageIsGranted(true)
                    !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                        showPermsOnSetting()
                    }
                    else -> {
                        showGivePermissions()
                    }
                }
            }
        }

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
        checkPermissions()
        viewModel.loadFilesToDb()
        viewModel.browseToRootDirectory()
        initSpinnerSort()
        initSpinnersSortAsc()
        initButtons()
    }

    private fun initButtons() {
        binding.btnShowChangedFiles.setOnClickListener {
            viewModel.getChangedFiles()
        }
    }

    private fun initAdapter() {
        binding.rvPosts.addItemDecoration(itemDecorator)
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
                openFile(it)
            }
            currentPath.observe(viewLifecycleOwner) {
                binding.currentPath.text = it
            }
            listOfFiles.observe(viewLifecycleOwner) {
                binding.tvEmpty.isVisible = it.isEmpty()
                fileAdapter.submitList(it)
                binding.rvPosts.scrollToPosition(0)
            }
            loading.observe(viewLifecycleOwner) {
                with(binding) {
                    pbLoading.isVisible = it
                    rvPosts.isVisible = !it
                }
            }
            shareFile.observe(viewLifecycleOwner) {
                shareFile(it)
            }
        }
    }

    private fun shareFile(file: File) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        val mType =
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
        val uri = FileProvider.getUriForFile(
            requireContext(),
            AUTHORITY,
            file
        )
        intent.type = mType
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        val chooserIntent = Intent.createChooser(
            intent,
            getString(R.string.share)
        )
        chooserIntent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(chooserIntent)
        }
    }

    private fun openFile(file: File) {
        val mType =
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
        val uri = FileProvider.getUriForFile(
            requireContext(),
            AUTHORITY,
            file
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, mType)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.onReadExternalStorageIsGranted(true)
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                requestPerms()
            }
            else -> {
                permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun requestPerms() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        activity?.requestPermissions(permissions, 0)
    }

    private fun showPermsOnSetting() {
        binding.run {
            Snackbar.make(
                root.rootView,
                getString(R.string.permissions_are_needed),
                Snackbar.LENGTH_LONG
            )
                .setAction(
                    getString(R.string.go_to_settings)
                ) {
                    openApplicationSettings()
                }
                .show()
        }
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + requireContext().packageName)
        )
        settings.launch(appSettingsIntent)
    }

    private fun showGivePermissions() {
        binding.run {
            Snackbar.make(
                binding.root.rootView,
                getString(R.string.give_permissions),
                Snackbar.LENGTH_LONG
            )
                .setAction(getString(R.string.allow)) { requestPerms() }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ASCENDING = "Ascending"
        private const val SPACING_DP = 8f
        private const val AUTHORITY = BuildConfig.APPLICATION_ID + ".provider"
    }
}
