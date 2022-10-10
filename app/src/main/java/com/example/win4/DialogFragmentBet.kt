package com.example.win4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.DialogFragment
import com.example.win4.databinding.FragmentDialogBetBinding
import com.example.win4.model.BetModel
import com.example.win4.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DialogFragmentBet : DialogFragment() {

    private lateinit var binding: FragmentDialogBetBinding
    private lateinit var appDatabase: AppDatabase
    var betModelList: ArrayList<BetModel> = ArrayList()
    private var cap: Double = 0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogBetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getDatabase(requireContext())
        getBankroll()
        binding.buttonBetValidate.setOnClickListener {
            val nameofBet = binding.editTextNameOfBet.text.toString()
            val odd = binding.editTextOdd.text.toString()
            val amount = binding.editTextAmount.text.toString()
            if (nameofBet.isNotEmpty() && odd.isNotEmpty() && amount.isNotEmpty()) {
                writeBet(nameofBet, odd, amount, getCapitalBankroll().toString())
                setCapitalBankRoll(getCapitalBankroll()!!.toDouble() - amount.toDouble())
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }

    }

    private fun getCapitalBankroll(): String? {
        val sharedPrefBankroll =
            requireContext().getSharedPreferences("bankrollPref", Context.MODE_PRIVATE)
        return sharedPrefBankroll.getString("capital", "")
    }

    private fun setCapitalBankRoll(capital: Double) {
        val sharedPrefBankroll =
            requireContext().getSharedPreferences("bankrollPref", Context.MODE_PRIVATE)
        sharedPrefBankroll.edit {
            putString("capital", capital.toString())
        }
    }

    private fun getBankroll() {
        val sharedPref =
            requireActivity().getSharedPreferences("bankrollPref", Context.MODE_PRIVATE)
        val bankroll = sharedPref.getString("bankrollName", "")
        val capital = sharedPref.getString("capital", "")
        if (!bankroll.isNullOrEmpty() && !capital.isNullOrEmpty()) {
            val spinner = binding.spinner
            val spinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                arrayOf(bankroll)
            )
            spinner.adapter = spinnerAdapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        } else if (bankroll!!.isEmpty()) {
            Toast.makeText(requireContext(), "Create bankroll!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun writeBet(nameofBet: String, odd: String, amount: String, capital: String) {

        GlobalScope.launch(Dispatchers.IO) {
            betModelList = appDatabase.betDao().getAll() as ArrayList<BetModel>
            val betModel =
                BetModel(null, betModelList.size, nameofBet, odd, amount, "wait", capital)
            appDatabase.betDao().insert(betModel)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = DialogFragmentBet()
    }
}