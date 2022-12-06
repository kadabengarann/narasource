package id.co.mka.narasource.narasumber.presentation.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.migration.CustomInjection.inject
import id.co.mka.narasource.R
import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.domain.model.Activity
import id.co.mka.narasource.core.utils.DateUtils
import id.co.mka.narasource.core.utils.DialogUtil
import id.co.mka.narasource.core.utils.loadImage
import id.co.mka.narasource.core.utils.setMargins
import id.co.mka.narasource.narasumber.ViewModelFactory
import id.co.mka.narasource.narasumber.databinding.FragmentNarasumberActivityDetailBinding
import id.co.mka.narasource.narasumber.utils.inject
import javax.inject.Inject

class ActivityDetailFragment : Fragment() {

    private var _binding: FragmentNarasumberActivityDetailBinding? = null
    private val binding get() = _binding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val activityDetailViewModel: ActivityDetailViewModel by activityViewModels {
        viewModelFactory
    }
    private var activityData: Activity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNarasumberActivityDetailBinding.inflate(inflater, container, false)
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

        if (arguments?.getInt("activityId") != 0) {
            val dataId = ActivityDetailFragmentArgs.fromBundle(arguments as Bundle).activityId
            dataId.let { if (activityData == null) activityDetailViewModel.getDetailActivity(it) }
        } else {
            val activityId = requireActivity().intent?.extras?.getInt(EXTRA_DATA)
            activityId?.let { if (activityData == null) activityDetailViewModel.getDetailActivity(it) }
        }

        observeData()
        setupAction()
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

    private fun setupAction() {
        binding?.apply {
            btnAccept.setOnClickListener {
                DialogUtil.showDialog(
                    requireContext(),
                    "Perhatian",
                    "Apakah anda yakin ingin menerima tawaran?",
                    "Ya",
                    "Tidak"
                ) { _, _ ->
                    reload(true)
                }
            }
            btnReject.setOnClickListener {
                DialogUtil.showDialog(
                    requireContext(),
                    "Perhatian",
                    "Apakah anda yakin ingin menolak tawaran?",
                    "Ya",
                    "Tidak"
                ) { _, _ ->
                    reload(false)
                }
            }
            btnSubmit.setOnClickListener {
                if (activityData?.meetingId != null) {
                    Toast.makeText(context, "Meeting page", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
                        tvStatusDescription.text = "(Tawaran Masuk)"
                        ivProfile.setImageResource(R.drawable.ic_profile)
                        btnSubmit.visibility = View.GONE
                        btnAccept.visibility = View.VISIBLE
                        btnReject.visibility = View.VISIBLE
                    }
                    "accepted" -> {
                        ivStatusDescription.setImageResource(R.drawable.ic_checklist_blue)
                        tvStatusDescription.text = "(Tawaran Diterima)"
                        ivProfile.loadImage(activityData?.narasumber?.image)
                        btnSubmit.background = enabledBackground
                        btnSubmit.isEnabled = true
                        btnSubmit.visibility = View.VISIBLE
                        btnAccept.visibility = View.GONE
                        btnReject.visibility = View.GONE
                    }
                    "rejected" -> {
                        btnSubmit.text = "Coba Lagi"
                        tvStatusDescription.text = "(Tawaran Ditolak)"
                        btnSubmit.background = enabledBackground
                        btnSubmit.isEnabled = true
                        btnSubmit.visibility = View.GONE
                        btnAccept.visibility = View.GONE
                        btnReject.visibility = View.GONE
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
                        ratingBar.rating = 3f
                    }
                    2 -> {
                        mainContent.setMargins(
                            bottom = 0
                        )
                        tvStatus.text = "Gagal"
                        ivStatus.setImageResource(R.drawable.ic_failed)
                        ivStatusDescription.visibility = View.GONE
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

    private fun reload(isAccepted: Boolean) {
        if (isAccepted) {
            activityDetailViewModel.getDetailActivity(2)
        } else {
            activityDetailViewModel.getDetailActivity(3)
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

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}
