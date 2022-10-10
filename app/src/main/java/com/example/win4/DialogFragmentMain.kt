package com.example.win4

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.win4.databinding.FragmentDialogMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


class DialogFragmentMain : DialogFragment() {

    private lateinit var binding: FragmentDialogMainBinding
    private val fragList = listOf(
        DialogFragmentBet.newInstance(),
        DialogFragmentBankroll.newInstance()
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        childFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout_container_dialog, DialogFragmentBet.newInstance())
            .commit()
        binding.tabLayoutDialog.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                childFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout_container_dialog, fragList[tab?.position!!])
                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


    }

    companion object {
        @JvmStatic
        fun newInstance() = DialogFragmentMain()
    }

}