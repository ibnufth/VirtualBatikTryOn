package com.dimensicodes.virtualbatiktryon.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dimensicodes.virtualbatiktryon.R
import com.dimensicodes.virtualbatiktryon.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private lateinit var detailFragmentBinding : FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailFragmentBinding = FragmentDetailBinding.inflate(inflater, container, false)
        return detailFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}