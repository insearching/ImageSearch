package com.payback.imagesearch.ui.imageDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.payback.imagesearch.databinding.FragmentImageDetailsBinding
import com.payback.imagesearch.viewmodel.ImageDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageDetailsFragment : Fragment() {

    private val imageDetailsViewModel: ImageDetailsViewModel by viewModels()

    private var _binding: FragmentImageDetailsBinding? = null

    private val binding get() = _binding!!

    private val args: ImageDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageDetailsBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageDetailsViewModel.init(args.photoId)
        imageDetailsViewModel.viewState.observe(viewLifecycleOwner){
            binding.viewState = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}