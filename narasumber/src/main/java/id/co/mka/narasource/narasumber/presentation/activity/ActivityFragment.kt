package id.co.mka.narasource.narasumber.presentation.activity

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.mka.narasource.R
import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.ui.ActivityListAdapter
import id.co.mka.narasource.core.utils.ActivityFilterType
import id.co.mka.narasource.narasumber.ViewModelFactory
import id.co.mka.narasource.narasumber.databinding.FragmentNarasumberActivityBinding
import id.co.mka.narasource.narasumber.utils.inject
import javax.inject.Inject

class ActivityFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val activityListViewModel: ActivityListViewModel by viewModels {
        viewModelFactory
    }

    private var _binding: FragmentNarasumberActivityBinding? = null
    private val binding get() = _binding

    private val activitiesListAdapter = ActivityListAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNarasumberActivityBinding.inflate(inflater, container, false)
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

        setupView()
        observeData()
    }

    private fun observeData() {
        activityListViewModel.activities.observe(requireActivity()) { activity ->
            if (activity != null) {
                when (activity) {
                    is Resource.Loading -> {
                        activitiesListAdapter.listData = emptyList()
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        binding?.rvActivitiesList?.visibility = View.VISIBLE
                        activitiesListAdapter.listData = activity.data
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        binding?.rvActivitiesList?.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupView() {
        val activitiesCategory = resources.getStringArray(R.array.activities)
        setupSpinner(activitiesCategory)

        binding?.rvActivitiesList?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = activitiesListAdapter
        }
    }

    private fun setupSpinner(activitiesCategory: Array<String>) {
        val spinner = binding?.spnCategory
        if (spinner != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_selected,
                activitiesCategory
            )
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

            spinner.adapter = adapter
        }
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                activityListViewModel.filter(
                    when (position) {
                        1 -> ActivityFilterType.ACTIVE_ACTIVITY
                        2 -> ActivityFilterType.SUCCESS_ACTIVITY
                        3 -> ActivityFilterType.FAILED_ACTIVITY
                        else -> ActivityFilterType.ALL_ACTIVITY
                    }
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                activityListViewModel.filter(ActivityFilterType.ALL_ACTIVITY)
            }
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
