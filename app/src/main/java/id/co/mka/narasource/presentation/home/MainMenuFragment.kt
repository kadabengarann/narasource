package id.co.mka.narasource.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.co.mka.narasource.R
import id.co.mka.narasource.databinding.FragmentMainMenuBinding

class MainMenuFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding
    private lateinit var navigator: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigator = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_dash)
        setupAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAction() {
        binding?.touchNotification?.setOnClickListener {
            dismiss()
//            navigator.navigate(MainMenuFragmentDirections.actionNavigationMainMenuToNavigationNotification())
        }
        binding?.touchProfile?.setOnClickListener {
            navigator.navigate(MainMenuFragmentDirections.actionNavigationMainMenuDialogToNavigationProfileActivity())
            dismiss()
        }
    }
}
