package com.dimensicodes.virtualbatiktryon.ui.pattern

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dimensicodes.virtualbatiktryon.R
import com.dimensicodes.virtualbatiktryon.data.source.local.remote.response.BatikItem
import com.dimensicodes.virtualbatiktryon.data.source.local.remote.response.OriginItem
import com.dimensicodes.virtualbatiktryon.databinding.FragmentPatternBinding
import com.dimensicodes.virtualbatiktryon.ui.detail.DetailFragment
import com.dimensicodes.virtualbatiktryon.viewmodel.ViewModelFactory

class PatternFragment : Fragment() {
    private lateinit var patternFragmentBinding : FragmentPatternBinding
    private lateinit var patternViewModel: PatternViewModel
    private val batikList = ArrayList<BatikItem>()
    private val originBatikList = ArrayList<OriginItem>()
    private val mOrigin = ArrayList<String>()
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
        patternViewModel.getOrigin().observe(viewLifecycleOwner,{origin->
            Log.d(TAG, "onViewCreated: origin ->$origin")
            originBatikList.addAll(origin)
            for (i in origin.indices){
                mOrigin.add(origin[i].name!!)
            }
            spinnerBatikList()
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

    private fun spinnerBatikList() {
        val adapter = ArrayAdapter(activity as Context, android.R.layout.simple_spinner_dropdown_item, mOrigin)
        patternFragmentBinding.searchBox.adapter = adapter
        patternFragmentBinding.searchBox.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                batikList.clear()
                patternViewModel.getBatik().observe(viewLifecycleOwner,{ batik ->
                    Log.d(TAG, "onViewCreated: batik -> $batik")
                    for (i in batik.indices){
                        val batikOrigin = ArrayList<BatikItem>()
                        for (k in batik[i].origin!!){
                            Log.d(TAG, "onItemSelected: id origin $k")
                            Log.d(TAG, "onItemSelected: id ${originBatikList[position].id}")
                            if (k==originBatikList[position].id){
                                Log.d(TAG, "onItemSelected: batik insert ${batik[i]}")
                                batikOrigin.add(batik[i])
                            }
                        }
                        Log.d(TAG, "onItemSelected batik origin: $batikOrigin")
                            batikList.addAll(batikOrigin)
                            showRecyclerView()
                    }

                })
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    private fun showRecyclerView(){
        patternFragmentBinding.rvBatik.layoutManager = GridLayoutManager(context, 4)
        rvAdapter = GridBatikAdapter()
        rvAdapter.listBatik.addAll(batikList)
        patternFragmentBinding.rvBatik.adapter = rvAdapter
    }

}