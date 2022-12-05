package id.co.mka.narasource.narasumber.presentation.narasumber

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import id.co.mka.narasource.narasumber.databinding.FragmentNarasumberBinding

class NarasumberFragment : Fragment() {

    private var _binding: FragmentNarasumberBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNarasumberBinding.inflate(inflater, container, false)
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

    private fun setupAction() {
        binding?.apply {
            touchActivity.setOnClickListener {
                it.findNavController().navigate(NarasumberFragmentDirections.actionNavigationNarasumberToNavigationActivity())
            }
            touchRating.setOnClickListener {
                it.findNavController().navigate(NarasumberFragmentDirections.actionNavigationNarasumberToNavigationRating())
            }
            touchFiles.setOnClickListener {
                it.findNavController().navigate(NarasumberFragmentDirections.actionNavigationNarasumberToNavigationFiles())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
