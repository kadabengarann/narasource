package id.co.mka.narasource.presentation.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.co.mka.narasource.R
import id.co.mka.narasource.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleButton()
        inputListener()
        setupAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAction() {
        binding?.tvBackToLogin?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.btnResetPassword?.setOnClickListener {
            if (validateInput()) {
                // email = user@email.com password = User1234@
                val email = binding?.inputEmail?.text.toString().trim()
                resetPassword()
            }
        }
    }

    private fun resetPassword() {
        MaterialAlertDialogBuilder(requireContext(), R.style.Theme_NaraSource_AlertDialog).apply {
            setTitle(getString(R.string.succsess))
            setMessage(getString(R.string.reset_password_success))
            setPositiveButton("OK", null)
            create()
            show()
        }
    }

    private fun toggleButton() {
        val inputEmail = binding?.inputEmail?.text?.isNotEmpty()
        val enabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button)
        val disabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_disabled)

        if (inputEmail == true) {
            binding?.btnResetPassword?.background = enabledBackground
            binding?.btnResetPassword?.isEnabled = true
        } else {
            binding?.btnResetPassword?.background = disabledBackground
            binding?.btnResetPassword?.isEnabled = false
        }
    }

    private fun inputListener() {
        binding?.apply {
            inputEmail.doAfterTextChanged {
                toggleButton()
                binding?.inputLayoutEmail?.error = null
            }
        }
    }

    private fun validateInput(): Boolean {
        binding?.inputLayoutEmail?.error = validEmail()

        return binding?.inputLayoutEmail?.error == null
    }

    private fun validEmail(): String? {
        val emailText = binding?.inputEmail?.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }
        return null
    }
}
