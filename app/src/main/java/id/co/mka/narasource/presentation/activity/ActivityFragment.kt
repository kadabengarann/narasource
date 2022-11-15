package id.co.mka.narasource.presentation.activity

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.R
import id.co.mka.narasource.core.ui.ActivityListAdapter
import id.co.mka.narasource.core.utils.ActivityFilterType
import id.co.mka.narasource.databinding.FragmentActivityBinding

@AndroidEntryPoint
class ActivityFragment : Fragment() {

    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding

    private lateinit var activityListViewModel: ActivityListViewModel

    private val activitiesListAdapter = ActivityListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActivityBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding?.toolbar)
            supportActionBar?.title = ""
        }
        setHasOptionsMenu(true)

        activityListViewModel = ViewModelProvider(this)[ActivityListViewModel::class.java]

        setupView()
        observeData()
    }

    private fun observeData() {
        activityListViewModel.activities.observe(requireActivity()) {
            activitiesListAdapter.listData = it
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_open -> {
                view?.findNavController()
                    ?.navigate(R.id.navigation_main_menu_dialog)
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
