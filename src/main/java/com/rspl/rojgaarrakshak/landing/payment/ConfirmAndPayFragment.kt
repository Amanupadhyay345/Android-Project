package com.rspl.rojgaarrakshak.landing.payment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rspl.rojgaarrakshak.R

class ConfirmAndPayFragment : Fragment() {

    companion object {
        fun newInstance() = ConfirmAndPayFragment()
    }

    private lateinit var viewModel: ConfirmAndPayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirm_and_pay, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConfirmAndPayViewModel::class.java)
        // TODO: Use the ViewModel
    }

}