package com.example.win4

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import com.example.win4.databinding.FragmentAccountBinding
import com.example.win4.databinding.FragmentStatsBinding
import com.example.win4.room.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FragmentAccount : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var appDatabase: AppDatabase
    var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getDatabase(requireContext())
        binding.buttonClearData.setOnClickListener {
            if (count == 1) {
                clearData()
                Toast.makeText(requireContext(), "Data cleared", Toast.LENGTH_SHORT).show()
            } else {
                count += 1
                Toast.makeText(
                    requireContext(),
                    "Click again to clear the data",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun clearData() {
        val sharedPrefBankroll =
            requireContext().getSharedPreferences("bankrollPref", Context.MODE_PRIVATE)
        sharedPrefBankroll.edit {
            clear()
        }
        GlobalScope.launch {
            appDatabase.betDao().deleteDb()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = FragmentAccount()
    }
}