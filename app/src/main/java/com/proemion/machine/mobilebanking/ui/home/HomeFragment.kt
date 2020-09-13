package com.proemion.machine.mobilebanking.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.proemion.machine.mobilebanking.MainActivity
import com.proemion.machine.mobilebanking.R
import com.proemion.machine.mobilebanking.StatementActivity
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_ACCOUNT_ID
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_ACC_BALANCE
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_ACC_NUM
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_EMAIL
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_IBAN
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_ID
import com.proemion.machine.mobilebanking.StaticComponent.StaticConfig.OWNER_NAME
import com.proemion.machine.mobilebanking.ui.TransactionActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transaction_view.setOnClickListener {
            val intent = Intent(requireContext(), TransactionActivity::class.java)
            intent.putExtra(OWNER_ID, (activity as MainActivity).ownerID)
            requireContext().startActivity(intent)
        }

        statement_view.setOnClickListener {
            val activity = activity as MainActivity
            val intent = Intent(requireContext(), StatementActivity::class.java)
            intent.putExtra(OWNER_ID, activity.ownerID)
            intent.putExtra(OWNER_ACCOUNT_ID, activity.ownerAccountId)
            intent.putExtra(OWNER_NAME, activity.ownerName)
            intent.putExtra(OWNER_EMAIL, activity.ownerEmail)
            intent.putExtra(OWNER_IBAN, activity.ownerIBAN)
            intent.putExtra(OWNER_ACC_NUM, activity.ownerACCNum)
            intent.putExtra(OWNER_ACC_BALANCE, activity.ownerBalance)
            requireContext().startActivity(intent)
        }
    }
}