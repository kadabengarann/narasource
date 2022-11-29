package id.co.mka.narasource.presentation.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import id.co.mka.narasource.R
import id.co.mka.narasource.databinding.FragmentActivityRatingBinding

class ActivityRatingFragment : Fragment() {

    private var _binding: FragmentActivityRatingBinding? = null
    private val binding get() = _binding

    private val activityDetailViewModel: ActivityDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActivityRatingBinding.inflate(inflater, container, false)
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
        toggleButton()

        val args = ActivityRatingFragmentArgs.fromBundle(requireArguments())
        val rating = args.rating

        binding?.apply {
            ratingBar.rating = rating
            inputRatingDesc.doAfterTextChanged {
                toggleButton()
            }
            btnSubmit.setOnClickListener {
                activityDetailViewModel.ratingDesc.value = inputRatingDesc.text.toString()
                requireActivity().onBackPressed()
            }
        }
        if (rating > 3.0) {
            binding?.grRatingDesc?.visibility = View.VISIBLE
        } else {
            binding?.grRatingDesc?.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggleButton() {
        val inputRatingDesc = binding?.inputRatingDesc?.text?.isNotEmpty()
        val enabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button)
        val disabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_disabled)

        if (inputRatingDesc == true) {
            binding?.btnSubmit?.background = enabledBackground
            binding?.btnSubmit?.isEnabled = true
        } else {
            binding?.btnSubmit?.background = disabledBackground
            binding?.btnSubmit?.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
