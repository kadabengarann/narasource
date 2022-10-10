package id.co.mka.naraq.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.naraq.R
import id.co.mka.naraq.core.data.Resource
import id.co.mka.naraq.databinding.FragmentLoginBinding

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userEmail = LoginFragmentArgs.fromBundle(arguments as Bundle).userEmail
        userEmail.let {
            binding?.inputEmail?.setText(it)
        }
        toggleButton()
        inputListener()
        setupAction()
        observeUI()
    }

    private fun observeUI() {
        authViewModel.loginResult.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Resource.Success -> {
                        Toast.makeText(requireContext(), "Login berhasil", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding?.tvRegisterSwitch?.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionNavigationLoginToNavigationRegister())
        }
        binding?.btnLogin?.setOnClickListener {
            if (validateInput()) {
                // email = user@email.com password = User1234@
                val email = binding?.inputEmail?.text.toString().trim()
                val password = binding?.inputPassword?.text.toString().trim()
                authViewModel.login(email, password)
            }
        }
    }

    private fun inputListener() {
        binding?.apply {
            inputEmail.doAfterTextChanged {
                toggleButton()
                binding?.inputLayoutEmail?.error = null
            }
            inputPassword.doAfterTextChanged {
                toggleButton()
                binding?.inputLayoutPassword?.error = null
            }
        }
    }

    private fun validateInput(): Boolean {
        binding?.inputLayoutEmail?.error = validEmail()
        binding?.inputLayoutPassword?.error = validPassword()

        val validEmail = binding?.inputLayoutEmail?.error == null
        val validPassword = binding?.inputLayoutPassword?.error == null

        return validEmail && validPassword
    }

    private fun toggleButton() {
        val inputEmail = binding?.inputEmail?.text?.isNotEmpty()
        val inputPassword = binding?.inputPassword?.text?.isNotEmpty()
        val enabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button)
        val disabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_disabled)

        if (inputEmail == true && inputPassword == true) {
            binding?.btnLogin?.background = enabledBackground
            binding?.btnLogin?.isEnabled = true
        } else {
            binding?.btnLogin?.background = disabledBackground
            binding?.btnLogin?.isEnabled = false
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
