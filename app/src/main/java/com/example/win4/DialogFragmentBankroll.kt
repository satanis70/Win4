package com.example.win4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.win4.databinding.FragmentDialogBankrollBinding
import com.example.win4.databinding.FragmentMainBinding

class DialogFragmentBankroll : DialogFragment() {

    private lateinit var binding: FragmentDialogBankrollBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogBankrollBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBankrollValidate.setOnClickListener {
            if (binding.editTextBankrollName.text.isNotEmpty() && binding.editTextCapital.text.isNotEmpty()) {
                addBankRoll()
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }
    }

    private fun addBankRoll() {
        val sharedPrefBankroll =
            requireContext().getSharedPreferences("bankrollPref", Context.MODE_PRIVATE)
        val bankrollName = binding.editTextBankrollName.text.toString()
        val capital = binding.editTextCapital.text.toString()
        sharedPrefBankroll.edit {
            putString("bankrollName", bankrollName)
            putString("capital", capital)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = DialogFragmentBankroll()
    }
}