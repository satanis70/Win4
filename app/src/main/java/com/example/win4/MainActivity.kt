package com.example.win4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.win4.databinding.ActivityMainBinding
import com.example.win4.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fragList = listOf(
        MainFragment.newInstance(),
        FragmentStats.newInstance(),
        FragmentAccount.newInstance()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout_container, MainFragment.newInstance())
            .commit()
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout_container, fragList[tab?.position!!])
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


    }
}