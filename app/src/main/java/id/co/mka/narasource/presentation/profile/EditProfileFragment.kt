package id.co.mka.narasource.presentation.profile

import android.app.AlertDialog.THEME_HOLO_LIGHT
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import id.co.mka.narasource.R
import id.co.mka.narasource.core.utils.DialogUtil
import id.co.mka.narasource.databinding.FragmentEditProfileBinding
import java.util.*

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
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

        fillProfile()
        setupAction()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
            }
            R.id.menu_one -> {
                DialogUtil.showDialog(
                    requireContext(),
                    "Perhatian",
                    "Perubahan akan disimpan. Apakah anda ingin melanjutkan?",
                    "Ya",
                    "Tidak"
                ) { _, _ ->
                    updateProfile()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fillProfile() {
        binding?.apply {
            inputName.setText("Nara")
            inputDateOfBirth.setText("12/12/1990")
            inputGender.check(inputGender.getChildAt(0).id)
        }
    }

    private fun setupAction() {
        binding?.apply {
            profileCircle.setOnClickListener {
                Toast.makeText(requireContext(), "Change Profile Picture", Toast.LENGTH_SHORT).show()
            }
            touchDateOfBirth.setOnClickListener {
                showDatePickerDialog()
            }
        }
    }

    private fun updateProfile() {
        DialogUtil.showDialog(
            requireContext(),
            "Sukses",
            "Perubahan yang telah dilakukan berhasil disimpan.",
            "OK",
            null
        ) { _, _ ->
            requireActivity().onBackPressed()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = 1989
        val month = 11
        val day = 12

        datePickerDialog = DatePickerDialog(
            requireContext(),
            THEME_HOLO_LIGHT,
            { view, year, month, dayOfMonth ->
                val date = "$dayOfMonth/${month + 1}/$year"
                binding?.inputDateOfBirth?.setText(date)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
