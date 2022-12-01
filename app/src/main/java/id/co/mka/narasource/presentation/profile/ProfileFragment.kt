package id.co.mka.narasource.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.R
import id.co.mka.narasource.core.utils.DialogUtil
import id.co.mka.narasource.databinding.FragmentProfileBinding
import id.co.mka.narasource.presentation.auth.AuthActivity
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding
    private val profileViewModel: ProfileViewModel by viewModels()
    private var level = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding?.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = ""
        }
        binding?.toolbar?.title = ""
        setHasOptionsMenu(true)

        setupAction()
        observeData()
    }

    private fun observeData() {
        profileViewModel.level.observe(viewLifecycleOwner) {
            if (it != null) {
                level = it
                if (it == "narasumber") {
                    binding?.btnApplyNarasumber?.text = "Profil Narasumber"
                }
                binding?.btnApplyNarasumber?.isEnabled = true
            }
        }

        binding?.apply {
            tvUserName.text = "John Doe"
            tvTotalInterviews.text = getString(R.string.total_interview, 6)
        }
    }

    private fun setupAction() {
        binding?.apply {
            btnApplyNarasumber.setOnClickListener {
                if (level == "narasumber") {
                    try {
                        installNarasumberModule()
                    } catch (e: Exception) {
                        Log.e("ProfileFragment", "Module not found")
                        Log.d("ProfileFragment", "setupAction: ${e.message}")
                    }
                } else {
                    it.findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationBecomeNarasumber())
                }
            }
            btnLogout.setOnClickListener {
                DialogUtil.showDialog(
                    requireContext(),
                    "Perhatian",
                    "Apakah anda yakin ingin keluar?",
                    "Ya",
                    "Tidak"
                ) { _, _ ->
                    logout()
                }
            }
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            profileViewModel.logout()
        }
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun moveToNarasumberActivity() {
        val intent = Intent(requireContext(), Class.forName("id.co.mka.narasource.narasumber.presentation.narasumber.NarasumberActivity"))
        startActivity(intent)
    }

    private fun installNarasumberModule() {
        val splitInstallManager = SplitInstallManagerFactory.create(requireContext())
        val moduleNarasumber = "narasumber"
        if (splitInstallManager.installedModules.contains(moduleNarasumber)) {
            moveToNarasumberActivity()
            Log.d("ProfileFragment", "installNarasumberModule: Module already installed")
        } else {
            val request = SplitInstallRequest.newBuilder()
                .addModule(moduleNarasumber)
                .build()
            splitInstallManager.startInstall(request)
                .addOnSuccessListener {
                    moveToNarasumberActivity()
                    Log.d("ProfileFragment", "installNarasumberModule: Success installing module")
                }
                .addOnFailureListener {
                    Log.d("ProfileFragment", "installNarasumberModule: Failed installing module")
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
            }
            R.id.menu_one -> {
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_profile_to_navigation_setting_dialog)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
