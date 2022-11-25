package id.co.mka.narasource.presentation.findNarasumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.R
import id.co.mka.narasource.core.domain.model.Payment
import id.co.mka.narasource.core.ui.PaymentListAdapter
import id.co.mka.narasource.databinding.FragmentFourthFormBinding

@AndroidEntryPoint
class FourthFormFragment : Fragment() {

    private var _binding: FragmentFourthFormBinding? = null
    private val binding get() = _binding

    private val paymentListAdapterVA = PaymentListAdapter()
    private val paymentlistadapterEwall = PaymentListAdapter()

    private var listPayment = mutableListOf<Payment>()
    private var listPayment2 = mutableListOf<Payment>()
    private var listPayment3 = mutableListOf<Payment>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFourthFormBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding?.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = ""
        }
        setHasOptionsMenu(true)

        toggleButton()
        setupView()
        observeData()
        setupAction()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeData() {
        listPayment = PaymentData.listPayment as MutableList<Payment>
        listPayment2 = PaymentData.listVA as MutableList<Payment>
        listPayment3 = PaymentData.listEWallet as MutableList<Payment>

        paymentListAdapterVA.listData = listPayment2
        paymentlistadapterEwall.listData = listPayment3
    }

    private fun setupView() {
        binding?.rvVaList?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            itemAnimator = null
            adapter = paymentListAdapterVA
        }
        binding?.rvEwalletList?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            itemAnimator = null
            adapter = paymentlistadapterEwall
        }
    }

    private fun setupAction() {
        binding?.apply {
            btnSubmit.setOnClickListener {
                val action = FourthFormFragmentDirections.actionNavFourthFormToNavPaymentTimer()
                findNavController().navigate(action)
            }
        }
        paymentListAdapterVA.onItemClick = { selectedData ->
            updateList(selectedData)
        }
        paymentlistadapterEwall.onItemClick = { selectedData ->
            updateList(selectedData)
        }
    }

    private fun updateList(selectedData: Payment) {
        listPayment = listPayment.map {
            if (it.id == selectedData.id) {
                Payment(selectedData.id, selectedData.name, selectedData.image, true)
            } else {
                Payment(it.id, it.name, it.image, false)
            }
        } as MutableList<Payment>

        listPayment2 = listPayment2.map {
            if (it.id == selectedData.id) {
                Payment(selectedData.id, selectedData.name, selectedData.image, true)
            } else {
                Payment(it.id, it.name, it.image, false)
            }
        } as MutableList<Payment>

        listPayment3 = listPayment3.map {
            if (it.id == selectedData.id) {
                Payment(selectedData.id, selectedData.name, selectedData.image, true)
            } else {
                Payment(it.id, it.name, it.image, false)
            }
        } as MutableList<Payment>

        paymentListAdapterVA.listData = listPayment2
        paymentlistadapterEwall.listData = listPayment3
        toggleButton()
    }

    private fun toggleButton() {
        val enabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button)
        val disabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_disabled)

        var isChecked = false
        listPayment.forEach {
            if (it.selected) isChecked = true
        }
        if (isChecked) {
            binding?.btnSubmit?.background = enabledBackground
            binding?.btnSubmit?.isEnabled = true
        } else {
            binding?.btnSubmit?.background = disabledBackground
            binding?.btnSubmit?.isEnabled = false
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
