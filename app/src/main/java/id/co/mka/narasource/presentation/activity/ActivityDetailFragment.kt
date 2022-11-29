package id.co.mka.narasource.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.R
import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.domain.model.Activity
import id.co.mka.narasource.core.utils.DateUtils
import id.co.mka.narasource.core.utils.loadImage
import id.co.mka.narasource.core.utils.setMargins
import id.co.mka.narasource.databinding.FragmentActivityDetailBinding
import id.co.mka.narasource.presentation.findNarasumber.FindNarasumberActivity

@AndroidEntryPoint
class ActivityDetailFragment : Fragment() {

    private var _binding: FragmentActivityDetailBinding? = null
    private val binding get() = _binding

    private val activityDetailViewModel: ActivityDetailViewModel by activityViewModels()
    private var activityData: Activity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActivityDetailBinding.inflate(inflater, container, false)
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
        val activityId = requireActivity().intent?.extras?.getInt(EXTRA_DATA)
        activityId?.let { if (activityData == null) activityDetailViewModel.getDetailActivity(it) }

        observeData()
        setupAction()
    }

    private fun setupAction() {
        binding?.apply {
            ratingBar.onRatingBarChangeListener =
                RatingBar.OnRatingBarChangeListener { _, rating, _ ->
                    if (activityDetailViewModel.ratingVal.value != rating) {
                        activityDetailViewModel.ratingVal.value = rating
                        navigateToRatingPage(rating)
                    }
                }
            btnSubmit.setOnClickListener {
                if (activityData?.meetingId != null) {
                    Toast.makeText(context, "Meeting page", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeData() {
        activityDetailViewModel.detailActivity.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Resource.Loading -> {
                        showLoading(true)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        activityData = it.data
                        setupData()
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun navigateToRatingPage(rating: Float) {
        val action = ActivityDetailFragmentDirections.actionNavigationActivityDetailToNavigationActivityRating(rating)
        findNavController().navigate(action)
    }

    private fun setupData() {
        val enabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button)
        val disabledBackground = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button_disabled)
        binding?.apply {
            if (activityData != null) {
                tvCategorySession.text = activityData?.category
                tvDateSession.text = activityData?.date?.let { DateUtils.formaToDate(it) }
                chipSessionDuration.text = activityData?.timeStart?.let {
                    activityData?.timeEnd?.let { it1 ->
                        DateUtils.getMinutesRange(
                            it,
                            it1
                        )
                    }
                } + " Menit"
                tvSessionTime.text = "${activityData?.timeStart?.let { DateUtils.formatToTime(it) }} - ${activityData?.timeEnd?.let {
                    DateUtils.formatToTime(
                        it
                    )
                }}"
                tvSessionTitle.text = activityData?.title
                tvSessionDescription.text = activityData?.description
                when (activityData?.confirmationStatus) {
                    "pending" -> {
                        ivStatusDescription.setImageResource(R.drawable.ic_loading_blue)
                        tvStatusDescription.text = "(Sedang Mencari)"
                        ivProfile.setImageResource(R.drawable.ic_profile)
                        btnSubmit.background = disabledBackground
                        btnSubmit.isEnabled = false
                    }
                    "accepted" -> {
                        ivStatusDescription.setImageResource(R.drawable.ic_checklist_blue)
                        tvStatusDescription.text = "(Diterima)"
                        ivProfile.loadImage(activityData?.narasumber?.image)
                        btnSubmit.background = enabledBackground
                        btnSubmit.isEnabled = true
                    }
                    "rejected" -> {
                        btnSubmit.text = "Coba Lagi"
                        btnSubmit.background = enabledBackground
                        btnSubmit.isEnabled = true
                    }
                }

                when (activityData?.status) {
                    0 -> {
                        tvStatus.text = "Berlangsung"
                        ivStatus.setImageResource(R.drawable.ic_processing)
                    }
                    1 -> {
                        mainContent.setMargins(
                            bottom = 0
                        )
                        buttonContainer.visibility = View.GONE
                        tvStatus.text = "Sukses"
                        ivStatus.setImageResource(R.drawable.ic_success)
                        ivStatusDescription.visibility = View.GONE
                        tvStatusDescription.text = "(Wawancara Selesai)"
                        groupRating.visibility = View.VISIBLE
                    }
                    2 -> {
                        tvStatus.text = "Gagal"
                        ivStatus.setImageResource(R.drawable.ic_failed)
                        ivStatusDescription.visibility = View.GONE
                        tvStatusDescription.text = "(Narasumber Tidak Ditemukan)"
                        btnSubmit.setOnClickListener {
                            val intent = Intent(requireContext(), FindNarasumberActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
        if (activityDetailViewModel.ratingVal.value != null && activityDetailViewModel.ratingDesc.value != null) {
            binding?.tvHintRating?.text = ""
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding?.incProgress?.progressOverlay?.visibility = if (isLoading) View.VISIBLE else View.GONE
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

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}
