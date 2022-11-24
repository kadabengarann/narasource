package id.co.mka.narasource.presentation.findNarasumber

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.R
import id.co.mka.narasource.core.domain.model.Session
import id.co.mka.narasource.core.ui.SessionInputListAdapter
import id.co.mka.narasource.core.utils.DateUtils
import id.co.mka.narasource.databinding.FragmentSecondFormBinding
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SecondFormFragment : Fragment() {

    private var _binding: FragmentSecondFormBinding? = null
    private val binding get() = _binding

    private val findNarasumberViewModel: FindNarasumberViewModel by activityViewModels()

    private val sessionInputListAdapter = SessionInputListAdapter()
    private lateinit var datePickerDialog: DatePickerDialog

    private var listSession = listOf<Session>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondFormBinding.inflate(inflater, container, false)
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

    private fun observeData() {
        findNarasumberViewModel.setListSession()
        findNarasumberViewModel.listSession.observe(requireActivity()) { listSession ->
            if (listSession != null) {
                sessionInputListAdapter.listData = listSession
                this.listSession = listSession
            }
        }
        findNarasumberViewModel.listSessionComplete.observe(requireActivity()) { isListSession ->
            if (isListSession) {
                binding?.apply {
                    btnSubmit.isEnabled = true
                    btnSubmit.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button)
                }
            } else {
                binding?.apply {
                    btnSubmit.isEnabled = false
                    btnSubmit.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_disabled)
                }
            }
        }
    }

    private fun setupView() {
        binding?.rvSessionInputList?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = sessionInputListAdapter
        }
        sessionInputListAdapter.listData = emptyList()
    }

    private fun setupAction() {
        binding?.apply {
            tvFormTitle.setOnClickListener {
                sessionInputListAdapter.listData = emptyList()
            }
            btnSubmit.setOnClickListener {
                val action = SecondFormFragmentDirections.actionNavSecondFormToNavThirdForm()
                findNavController().navigate(action)
            }
        }
        sessionInputListAdapter.apply {
            onDateClick = {
                showDatePickerDialog(it)
            }
            onTimeStartClick = {
                showTimePicker(it, "startTimePicker")
            }
            onTimeEndClick = {
                showTimePicker(it, "endTimePicker")
            }
        }
    }

    private fun showTimePicker(data: Session, tag: String) {
        val myCalendar = Calendar.getInstance()

        val timePicker = TimePickerDialog(context, { _: TimePicker?, hourOfDay: Int, minute: Int ->
            val myFormat = "HH:mm" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            myCalendar.set(Calendar.MINUTE, minute)
            val newTime = sdf.format(myCalendar.time)
            val newVal = Session(
                data.session,
                data.date,
                data.timeStart,
                data.timeEnd
            )
            val fullTimestamp = data.date?.let { DateUtils.getFullTimestamp(it, newTime) }
            var isTimeValid = true
            if (tag == "startTimePicker") {
                if (fullTimestamp?.let { DateUtils.isTimeBeforeCurrentTime(it) } == true) {
                    isTimeValid = false
                    Toast.makeText(context, "Waktu harus minimal 1 jam dari waktu sekarang", Toast.LENGTH_LONG).show()
                } else {
                    newVal.timeStart = fullTimestamp
                }
            } else {
                if (fullTimestamp?.let {
                    data.timeStart?.let { it1 ->
                        DateUtils.isValidRangeTime(
                                it1,
                                it
                            )
                    }
                } == false
                ) {
                    isTimeValid = false
                    Toast.makeText(context, "Waktu berakhir tidak valid", Toast.LENGTH_LONG).show()
                } else {
                    newVal.timeEnd = fullTimestamp
                }
            }
            if (isTimeValid) {
                listSession = listSession.map {
                    if (it.session == data.session) {
                        newVal
                    } else {
                        it
                    }
                }
                findNarasumberViewModel.updateListSession(listSession)
            }
        }, myCalendar[Calendar.HOUR_OF_DAY], myCalendar[Calendar.MINUTE], true)
        timePicker.show()
    }

    private fun showDatePickerDialog(data: Session) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(
            requireContext(),
            AlertDialog.THEME_HOLO_LIGHT,
            { _, year, month, dayOfMonth ->

                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateValue = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(calendar.time)
                val newVal = Session(
                    data.session,
                    dateValue,
                    data.timeStart,
                    data.timeEnd
                )
                listSession = listSession.map {
                    if (it.session == data.session) {
                        newVal
                    } else {
                        it
                    }
                }
                findNarasumberViewModel.updateListSession(listSession)
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker
            .minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
