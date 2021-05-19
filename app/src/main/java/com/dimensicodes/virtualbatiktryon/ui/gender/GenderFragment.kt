package com.dimensicodes.virtualbatiktryon.ui.gender

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dimensicodes.virtualbatiktryon.R
import com.dimensicodes.virtualbatiktryon.databinding.FragmentGenderBinding
import com.dimensicodes.virtualbatiktryon.ui.pattern.PatternFragment

class GenderFragment : Fragment(), View.OnClickListener {

    private lateinit var genderFragmentBinding : FragmentGenderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        genderFragmentBinding = FragmentGenderBinding.inflate(inflater,container,false)
        return genderFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        genderFragmentBinding.btnFemale1.setOnClickListener(this)
        genderFragmentBinding.btnFemale2.setOnClickListener(this)
        genderFragmentBinding.btnFemale3.setOnClickListener(this)
        genderFragmentBinding.btnFemale4.setOnClickListener(this)
        genderFragmentBinding.btnFemale5.setOnClickListener(this)
        genderFragmentBinding.btnFemale6.setOnClickListener(this)
        genderFragmentBinding.btnMale1.setOnClickListener(this)
        genderFragmentBinding.btnMale2.setOnClickListener(this)
        genderFragmentBinding.btnMale3.setOnClickListener(this)
        genderFragmentBinding.btnMale4.setOnClickListener(this)
        genderFragmentBinding.btnMale5.setOnClickListener(this)
        genderFragmentBinding.btnMale6.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_female_1 -> nextFragment("fml1")
            R.id.btn_female_2 -> nextFragment("fml2")
            R.id.btn_female_3 -> nextFragment("fml3")
            R.id.btn_female_4 -> nextFragment("fml4")
            R.id.btn_female_5 -> nextFragment("fml5")
            R.id.btn_female_6 -> nextFragment("fml6")
            R.id.btn_male_1 -> nextFragment("ml1")
            R.id.btn_male_2 -> nextFragment("ml2")
            R.id.btn_male_3 -> nextFragment("ml3")
            R.id.btn_male_4 -> nextFragment("ml4")
            R.id.btn_male_5 -> nextFragment("ml5")
            R.id.btn_male_6 -> nextFragment("ml6")
        }
    }
    private fun nextFragment(code: String){
        val mPatternFragment = PatternFragment()

        val mFragmentManager = fragmentManager
        mFragmentManager?.beginTransaction()?.apply {
            replace(R.id.frame_container,mPatternFragment,PatternFragment::class.java.simpleName)
            addToBackStack(null)
            commit()
        }
    }
}