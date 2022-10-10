package com.example.win4

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.win4.databinding.FragmentMainBinding
import com.example.win4.model.BetModel
import com.example.win4.room.AppDatabase
import com.onesignal.OneSignal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainFragment : Fragment(), MainHolder.OnItemClickListener {

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

    val ONESIGNAL_APP_ID = "714b9f14-381d-4fc4-a93c-28d480557381"

    private lateinit var binding: FragmentMainBinding
    private lateinit var appDatabase: AppDatabase
    lateinit var betModelList: ArrayList<BetModel>
    private var cap: Double = 0.0
    private lateinit var adapter: MainHolder
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getDatabase(requireContext())
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(requireContext())
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        binding.floatingActionButton.setOnClickListener {
            val dialog = DialogFragmentMain()
            dialog.show(childFragmentManager, "dialog")
        }

        if (getCapitalBankroll()!!.isNotEmpty()) {
            binding.textViewBalance.visibility = View.VISIBLE
            binding.textViewBalance.text = "${getCapitalBankroll().toString()}€"
        }

        getBankrollList()
    }

    private fun getBankrollList() {
        val sharedPrefBankroll =
            requireContext().getSharedPreferences("bankrollPref", Context.MODE_PRIVATE)
        val bankrollName = sharedPrefBankroll.getString("bankrollName", "")
        if (!bankrollName.isNullOrEmpty()) {
            getBetList()
        }
    }

    private fun getBetList() {

        GlobalScope.launch {
            betModelList = appDatabase.betDao().getAll() as ArrayList<BetModel>
            launch(Dispatchers.Main) {
                cap = getCapitalBankroll()!!.toDouble()
                for (i in betModelList) {
                    if (i.status == "wait") {
                        binding.textViewBalance.text = cap.toString()
                    }
                }
                adapter = MainHolder(betModelList, this@MainFragment)
                recyclerView = binding.recyclerViewMain
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adapter
            }
        }
    }

    override fun onClick(position: Int, betModel: BetModel, s: String, textViewResult: TextView) {
        val odd = betModelList[position].odd
        val amount = betModelList[position].amount
        if (s == "win") {
            val result = odd.toDouble() * amount.toDouble()
            setCapitalBankRoll(getCapitalBankroll()!!.toDouble() + result)
            binding.textViewBalance.text = "${getCapitalBankroll().toString()}€"
            val winSum = (result - amount.toDouble()).toString()
            textViewResult.text = "+ $winSum"
            GlobalScope.launch {
                appDatabase.betDao().update("win", position)
            }
        } else if (s == "delete") {
            GlobalScope.launch {
                betModelList = appDatabase.betDao().getAll() as ArrayList<BetModel>
                launch(Dispatchers.Main) {
                    if (betModelList[position].status == "loss") {
                        var capWinLoss = getCapitalBankroll()!!.toDouble()
                        capWinLoss += betModelList[position].amount.toDouble()
                        setCapitalBankRoll(capWinLoss)
                        binding.textViewBalance.text = "${capWinLoss}€"
                        GlobalScope.launch {
                            appDatabase.betDao().delete(betModelList[position])
                            betModelList = appDatabase.betDao().getAll() as ArrayList<BetModel>
                            launch(Dispatchers.Main) {
                                adapter.notifyItemRemoved(position)
                                adapter = MainHolder(betModelList, this@MainFragment)
                                recyclerView.adapter = adapter
                            }
                        }
                    } else if (betModelList[position].status == "win") {
                        var capWinDel = getCapitalBankroll()!!.toDouble()
                        capWinDel -= betModelList[position].amount.toDouble()
                        setCapitalBankRoll(capWinDel)
                        binding.textViewBalance.text = "${capWinDel}€"
                        GlobalScope.launch {
                            appDatabase.betDao().delete(betModelList[position])
                            betModelList = appDatabase.betDao().getAll() as ArrayList<BetModel>
                            launch(Dispatchers.Main) {
                                adapter.notifyItemRemoved(position)
                                adapter = MainHolder(betModelList, this@MainFragment)
                                recyclerView.adapter = adapter
                            }
                        }
                    }
                }
            }

        } else if (s == "loss") {
            val amount = betModelList[position].amount.toDouble()
            binding.textViewBalance.text = cap.toString()
            textViewResult.text = (amount * -1).toString()
            GlobalScope.launch {
                appDatabase.betDao().update("loss", position)
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
}