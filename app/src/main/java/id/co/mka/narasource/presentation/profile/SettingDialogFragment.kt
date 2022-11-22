package id.co.mka.narasource.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.co.mka.narasource.R
import id.co.mka.narasource.databinding.FragmentSettingDialogBinding

class SettingDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSettingDialogBinding? = null
    private val binding get() = _binding
    private lateinit var navigator: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigator = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_profile)
        setupAction()
    }

    private fun setupAction() {
        binding?.touchEditProfile?.setOnClickListener {
            navigator.navigate(SettingDialogFragmentDirections.actionNavigationSettingDialogToNavigationEditProfile())
        }
        binding?.touchChangePassword?.setOnClickListener {
            navigator.navigate(SettingDialogFragmentDirections.actionNavigationSettingDialogToNavigationChangePassword())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
