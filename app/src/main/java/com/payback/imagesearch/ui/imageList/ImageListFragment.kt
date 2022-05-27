package com.payback.imagesearch.ui.imageList

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.payback.imagesearch.databinding.FragmentImageListBinding
import com.payback.imagesearch.ui.imageList.adapter.DomainToObjectMapper.toListItem
import com.payback.imagesearch.ui.imageList.adapter.ImageListAdapter
import com.payback.imagesearch.viewmodel.ImageListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageListFragment : Fragment() {

    private val imageListViewModel: ImageListViewModel by viewModels()

    private var _binding: FragmentImageListBinding? = null

    private val binding get() = _binding!!

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
            val action =
                ImageListFragmentDirections
                    .actionImageListFragmentToImageDetailsFragment(listItem.id)
            view.findNavController().navigate(action)
        }
        with(binding) {
            imageList.layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) LinearLayoutManager(
                    requireContext()
                ) else GridLayoutManager(requireContext(), 2)

            imageList.layoutManager = requireContext().layoutManager
            imageList.adapter = adapter
            viewModel = imageListViewModel
        }

        imageListViewModel.initModel()
        imageListViewModel.viewState.observe(viewLifecycleOwner) {
            adapter.updateItems(it.imagePhotos?.hits?.asSequence()?.map { hit -> hit.toListItem }
                ?.toList() ?: emptyList())
            binding.viewState = it
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

    private val Context.layoutManager: RecyclerView.LayoutManager
        get() =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) LinearLayoutManager(
                requireContext()
            ) else GridLayoutManager(this, 2)

}