package id.co.mka.narasource.narasumber.presentation.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import id.co.mka.narasource.narasumber.databinding.FragmentNarasumberActivityRatingBinding

class ActivityRatingFragment : Fragment() {

    private var _binding: FragmentNarasumberActivityRatingBinding? = null
    private val binding get() = _binding

    private val activityDetailViewModel: ActivityDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNarasumberActivityRatingBinding.inflate(inflater, container, false)
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

        val rating = 3.0f

        binding?.apply {
            ratingBar.rating = rating
            inputRatingDesc.setText("Rating Descrption")
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
