package com.payback.imagesearch.ui.imageList

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.payback.imagesearch.R
import com.payback.imagesearch.data.mapper.DomainToObjectMapper.toListItem
import com.payback.imagesearch.databinding.FragmentImageListBinding
import com.payback.imagesearch.ui.components.MarginItemDecoration
import com.payback.imagesearch.ui.imageList.adapter.ImageListAdapter
import com.payback.imagesearch.viewmodel.ImageListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ImageListFragment : Fragment() {

    private val imageListViewModel: ImageListViewModel by viewModels()

    private var _binding: FragmentImageListBinding? = null

    private val binding get() = _binding!!

    private val initialQuery = "fruits"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageListBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ImageListAdapter { listItem ->
            showDialog {
                val action =
                    ImageListFragmentDirections
                        .actionImageListFragmentToImageDetailsFragment(listItem.id)
                view.findNavController().navigate(action)
            }
        }
        with(binding) {
            imageList.layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) LinearLayoutManager(
                    requireContext()
                ) else GridLayoutManager(requireContext(), 2)

            imageList.layoutManager = requireContext().layoutManager
            imageList.adapter = adapter
            imageList.addItemDecoration(
                MarginItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.margin),
                    spanCount = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2,
                )
            )

            viewModel = imageListViewModel

            searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { imageListViewModel.loadImages(it) }
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    return true
                }
            })
        }

        with(imageListViewModel) {
            initModel()
            loadImages(initialQuery)
            viewState.observe(viewLifecycleOwner) {
                adapter.updateItems(it?.photos?.asSequence()?.map { hit -> hit.toListItem }
                    ?.toList() ?: emptyList())
                binding.viewState = it
            }
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.imageList.layoutManager = requireContext().layoutManager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog(proceedCallback: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.info)
            .setMessage(R.string.dialog_proceed_message)
            .setPositiveButton(
            android.R.string.ok
        ) { dialog, _ ->
            dialog.dismiss()
            proceedCallback()
        }.setNegativeButton(
            android.R.string.cancel
        ) { dialog, _ ->
            dialog.dismiss()
        }.create().show()
    }

    private val Context.layoutManager: RecyclerView.LayoutManager
        get() =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) LinearLayoutManager(
                requireContext()
            ) else GridLayoutManager(this, 2)

}