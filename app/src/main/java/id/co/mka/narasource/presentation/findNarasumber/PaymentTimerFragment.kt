package id.co.mka.narasource.presentation.findNarasumber

import android.content.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.core.utils.TimerService
import id.co.mka.narasource.core.utils.getTimeStringFromDouble
import id.co.mka.narasource.databinding.FragmentPaymentTimerBinding

@AndroidEntryPoint
class PaymentTimerFragment : Fragment() {

    private var _binding: FragmentPaymentTimerBinding? = null
    private val binding get() = _binding
    private lateinit var serviceTimerIntent: Intent
    private var timerStarted = false
    private var time = 86400.0 // 24 hours

    private val findNarasumberViewModel: FindNarasumberViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentTimerBinding.inflate(inflater, container, false)
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

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            binding?.chipPaymetTimer?.text = getTimeStringFromDouble(time)
            if (time == 0.0) {
                stopTimer()
            }
        }
    }

    private fun observeData() {
        // get time remaining from viewmodel
        binding?.chipPaymetTimer?.text = getTimeStringFromDouble(time)
    }

    private fun setupView() {
        serviceTimerIntent = Intent(requireContext(), TimerService::class.java)
        requireActivity().registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
        startTimer()
    }

    private fun setupAction() {
        binding?.apply {
            btnCopyPaymentDestination.setOnClickListener {
                val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", "8878800001231230")
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(), "Berhasil disalin", Toast.LENGTH_LONG).show()
            }
            tvPaymentTotal.setOnClickListener {
                val action = PaymentTimerFragmentDirections.actionNavPaymentTimerToNavPaymentSuccess()
                findNavController().navigate(action)
            }
        }
    }

    private fun startTimer() {
        serviceTimerIntent.putExtra(TimerService.TIME_EXTRA, time)
        requireActivity().startService(serviceTimerIntent)
        timerStarted = true
    }

    private fun stopTimer() {
        requireActivity().stopService(serviceTimerIntent)
        timerStarted = false
        val action = PaymentTimerFragmentDirections.actionNavPaymentTimerToNavPaymentSuccess()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requireActivity().stopService(serviceTimerIntent)
    }
}
