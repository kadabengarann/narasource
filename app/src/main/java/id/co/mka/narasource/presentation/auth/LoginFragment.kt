package id.co.mka.narasource.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.R
import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.utils.DialogUtil
import id.co.mka.narasource.databinding.FragmentLoginBinding
import id.co.mka.narasource.presentation.main.MainActivity

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
        observeData()
    }

    private fun observeData() {
        authViewModel.loginResult.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Resource.Success -> {
                        showLoading(false)
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        activity?.finish()
                    }
                    is Resource.Error -> it.message?.let { it1 -> showError(it1) }
                    is Resource.Loading -> showLoading(true)
                }
            }
        }
    }

    private fun setupAction() {
        binding?.tvRegisterSwitch?.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionNavigationLoginToNavigationRegister())
        }
        binding?.tvForgotPassword?.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionNavigationLoginToNavigationForgotPassword())
        }
        binding?.btnLogin?.setOnClickListener {
            if (validateInput()) {
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
        return binding?.inputLayoutEmail?.error == null
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

    private fun showLoading(isLoading: Boolean) {
        binding?.incProgress?.progressOverlay?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(msg: String) {
        showLoading(false)
        DialogUtil.showDialog(
            requireContext(),
            getString(R.string.auth_failed),
            msg,
            "OK",
            null,
            null
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
