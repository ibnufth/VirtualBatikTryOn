package com.dimensicodes.virtualbatiktryon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dimensicodes.virtualbatiktryon.ui.gender.GenderFragment

class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mFragmentManager = supportFragmentManager
        val mGenderFragment = GenderFragment()
        val fragment = mFragmentManager.findFragmentByTag(GenderFragment::class.java.simpleName)

        if (fragment !is GenderFragment){
            mFragmentManager
                .beginTransaction()
                .add(R.id.frame_container, mGenderFragment, GenderFragment::class.java.simpleName)
                .commit()
        }
    }
}