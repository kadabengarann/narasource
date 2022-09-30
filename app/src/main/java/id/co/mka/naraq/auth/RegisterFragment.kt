package id.co.mka.naraq.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import id.co.mka.naraq.R
import id.co.mka.naraq.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleButton()
        inputListener()
        setupAction()
    }

    private fun setupAction() {
        binding?.tvLoginSwitch?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.btnRegister?.setOnClickListener {
            if (validateInput()) {
                // do smth
            }
        }
    }

    private fun inputListener() {
        binding?.apply {
            inputName.doAfterTextChanged {
                toggleButton()
            }
            inputUsername.doAfterTextChanged {
                toggleButton()
            }
            inputEmail.doAfterTextChanged {
                toggleButton()
                binding?.inputLayoutEmail?.error = null
            }
            inputPassword.doAfterTextChanged {
                toggleButton()
                binding?.inputLayoutPassword?.error = null
            }
            inputConfirmPassword.doAfterTextChanged {
                toggleButton()
                binding?.inputLayoutConfirmPassword?.error = null
            }
            checkboxTerms.setOnCheckedChangeListener { _, _ ->
                toggleButton()
            }
        }
    }

    private fun validateInput(): Boolean {
        binding?.inputLayoutEmail?.error = validEmail()
        binding?.inputLayoutPassword?.error = validPassword()
        binding?.inputLayoutConfirmPassword?.error = validConfirmPassword()

        val validEmail = binding?.inputLayoutEmail?.helperText == null
        val validPassword = binding?.inputLayoutPassword?.helperText == null
        val validConfirmPassword = binding?.inputLayoutConfirmPassword?.helperText == null

        return validEmail && validPassword && validConfirmPassword
    }

    private fun toggleButton() {
        val inputName = binding?.inputName?.text?.isNotEmpty()
        val inputUsename = binding?.inputUsername?.text?.isNotEmpty()
        val inputEmail = binding?.inputEmail?.text?.isNotEmpty()
        val inputPassword = binding?.inputPassword?.text?.isNotEmpty()
        val inputConfirmPassword = binding?.inputConfirmPassword?.text?.isNotEmpty()
        val isCheckedTerms = binding?.checkboxTerms?.isChecked
        val enabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button)
        val disabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_disabled)

        if (inputName == true && inputUsename == true && inputEmail == true && inputPassword == true && inputConfirmPassword == true && isCheckedTerms == true) {
            binding?.btnRegister?.background = enabledBackground
            binding?.btnRegister?.isEnabled = true
        } else {
            binding?.btnRegister?.background = disabledBackground
            binding?.btnRegister?.isEnabled = false
        }
    }
    private fun validPassword(): String? {
        val passwordText = binding?.inputPassword?.text.toString()
        if (passwordText.length < 8) {
            return "Minimum 8 Character Password"
        }
        if (!passwordText.matches(".*[A-Z].*".toRegex())) {
            return "Must Contain 1 Upper-case Character"
        }
        if (!passwordText.matches(".*[a-z].*".toRegex())) {
            return "Must Contain 1 Lower-case Character"
        }
        if (!passwordText.matches(".*[@#\$%^&+=].*".toRegex())) {
            return "Must Contain 1 Special Character (@#\$%^&+=)"
        }
        return null
    }

    private fun validConfirmPassword(): String? {
        val passwordText = binding?.inputPassword?.text.toString()
        val rePasswordText = binding?.inputConfirmPassword?.text.toString()
        if (passwordText != rePasswordText) {
            return "Must same"
        }
        return null
    }

    private fun validEmail(): String? {
        val emailText = binding?.inputEmail?.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return "Invalid Email Address"
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
