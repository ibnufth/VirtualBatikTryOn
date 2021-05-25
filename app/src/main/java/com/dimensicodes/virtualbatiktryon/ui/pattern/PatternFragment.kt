package com.dimensicodes.virtualbatiktryon.ui.pattern

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimensicodes.virtualbatiktryon.R
import com.dimensicodes.virtualbatiktryon.data.source.local.remote.response.BatikItem
import com.dimensicodes.virtualbatiktryon.databinding.FragmentPatternBinding
import com.dimensicodes.virtualbatiktryon.ui.detail.DetailFragment
import com.dimensicodes.virtualbatiktryon.viewmodel.ViewModelFactory

class PatternFragment : Fragment() {
    private lateinit var patternFragmentBinding : FragmentPatternBinding
    private lateinit var patternViewModel: PatternViewModel
    private val batikList = ArrayList<BatikItem>()
    private lateinit var rvAdapter : GridBatikAdapter
    companion object{
        private const val TAG = "PatternFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        patternFragmentBinding = FragmentPatternBinding.inflate(inflater,container,false)
        return patternFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        patternViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireActivity()))[PatternViewModel::class.java]

        patternFragmentBinding.rvBatik.setHasFixedSize(true)
        patternViewModel.getBatik().observe(viewLifecycleOwner,{ batik ->
            Log.d(TAG, "onViewCreated: $batik")
            batikList.addAll(batik)
            patternFragmentBinding.rvBatik.layoutManager = GridLayoutManager(context, 4)
            rvAdapter = GridBatikAdapter()
            rvAdapter.listBatik.addAll(batikList)
            patternFragmentBinding.rvBatik.adapter = rvAdapter
        })

        patternFragmentBinding.btnProcess.setOnClickListener {
            val mPatternFragment = DetailFragment()

            val mFragmentManager = fragmentManager
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container,mPatternFragment,DetailFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }

    }
    private fun showRecyclerView(){



    }

}