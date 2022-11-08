package id.co.mka.narasource.presentation.profile

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import id.co.mka.narasource.R
import id.co.mka.narasource.core.utils.DialogUtil
import id.co.mka.narasource.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
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
                if (validateInput()) {
                    btnSaveClick()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun btnSaveClick() {
        DialogUtil.showDialog(
            requireContext(),
            "Perhatian",
            "Perubahan akan disimpan. Apakah anda ingin melanjutkan?",
            "Ya",
            "Tidak"
        ) { _, _ ->
            changePassword()
        }
    }

    private fun changePassword() {
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
    private fun validateInput(): Boolean {
        val inputOldPassword = binding?.inputOldPassword?.text?.isNotEmpty()
        val inputPassword = binding?.inputNewPassword?.text?.isNotEmpty()
        val inputConfirmPassword = binding?.inputConfirmNewPassword?.text?.isNotEmpty()

        if (inputOldPassword == false || inputPassword == false || inputConfirmPassword == false) {
            DialogUtil.showDialog(
                requireContext(),
                "Perhatian",
                "Mohon lengkapi data yang diperlukan.",
                "OK",
                null,
                null
            )
            return false
        }

        binding?.inputLayoutNewPassword?.error = validPassword()
        binding?.inputLayoutConfirmNewPassword?.error = validConfirmPassword()

        val validPassword = binding?.inputLayoutNewPassword?.error == null
        val validConfirmPassword = binding?.inputLayoutConfirmNewPassword?.error == null

        return validPassword && validConfirmPassword
    }

    private fun validPassword(): String? {
        val passwordText = binding?.inputNewPassword?.text.toString().trim()
        if (passwordText.length < 8) {
            return "Minimal 8 karakter"
        }
        if (!passwordText.matches(".*[A-Z].*".toRegex())) {
            return "Harus mengandung huruf besar"
        }
        if (!passwordText.matches(".*[a-z].*".toRegex())) {
            return "Harus mengandung huruf kecil"
        }
        if (!passwordText.matches(".*[@#\$%^&+=].*".toRegex())) {
            return "Harus mengandung karakter spesial (@#\$%^&+=)"
        }
        return null
    }

    private fun validConfirmPassword(): String? {
        val passwordText = binding?.inputNewPassword?.text.toString().trim()
        val rePasswordText = binding?.inputConfirmNewPassword?.text.toString().trim()
        if (passwordText != rePasswordText) {
            return "Password tidak sama"
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
