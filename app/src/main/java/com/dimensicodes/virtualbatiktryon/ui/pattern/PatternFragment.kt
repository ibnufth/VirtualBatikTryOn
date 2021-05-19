package com.dimensicodes.virtualbatiktryon.ui.pattern

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.dimensicodes.virtualbatiktryon.R
import com.dimensicodes.virtualbatiktryon.databinding.FragmentPatternBinding
import com.dimensicodes.virtualbatiktryon.ui.detail.DetailFragment

class PatternFragment : Fragment() {
    private lateinit var patternFragmentBinding : FragmentPatternBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        patternFragmentBinding = FragmentPatternBinding.inflate(inflater,container,false)
        return patternFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        patternFragmentBinding.rvBatik.setHasFixedSize(true)
        patternFragmentBinding.rvBatik.layoutManager = GridLayoutManager(context,4)
        val gridBatikAdapter = GridBatikAdapter()
        patternFragmentBinding.rvBatik.adapter = gridBatikAdapter

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

}