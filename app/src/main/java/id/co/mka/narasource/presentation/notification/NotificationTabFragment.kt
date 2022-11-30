package id.co.mka.narasource.presentation.notification

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.ui.NotificationListAdapter
import id.co.mka.narasource.core.utils.ARG_SECTION_NUMBER
import id.co.mka.narasource.databinding.FragmentNotificationTabBinding
import id.co.mka.narasource.presentation.activity.DetailActivity

@AndroidEntryPoint
class NotificationTabFragment : Fragment() {

    private var _binding: FragmentNotificationTabBinding? = null
    private val binding get() = _binding
    private val notificationListViewModel: NotificationListViewModel by viewModels()

    private val notificationListAdapter = NotificationListAdapter()
    private var index = 0
    private var level = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationTabBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        index = arguments?.getInt(ARG_SECTION_NUMBER, 0)!!
        if (arguments != null) {
            when (index) {
                1 -> {
                    if (savedInstanceState == null) {
                        observeData(false)
                    }
                }
                2 -> {
                    if (savedInstanceState == null) {
                        observeData(true)
                    }
                }
            }
        }
        setupView()
        setupAction()
    }

    private fun setupAction() {
        notificationListAdapter.onItemClick = { selectedData ->
            if (selectedData.destinationId != 0) {
                when (selectedData.userType) {
                    "narasumber" -> {
                        Toast.makeText(context, "Narasumber Notification", Toast.LENGTH_SHORT).show()
                    }
                    "customer" -> {
                        val toDetailActivity = Intent(requireContext(), DetailActivity::class.java)
                        toDetailActivity.putExtra(DetailActivity.EXTRA_DATA, selectedData.id)
                        startActivity(toDetailActivity)
                    }
                }
            }
        }
    }

    private fun setupView() {
        binding?.rvArticleTabList?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = notificationListAdapter
        }
    }

    private fun observeData(isRead: Boolean) {
        notificationListViewModel.level.observe(viewLifecycleOwner) {
            if (it != null) {
                level = it
            }
        }
        notificationListViewModel.getNotification(isRead)
        notificationListViewModel.notifications.observe(requireActivity()) { notifications ->
            if (notifications != null) {
                when (notifications) {
                    is Resource.Loading -> {
                        notificationListAdapter.listData = emptyList()
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        binding?.rvArticleTabList?.visibility = View.VISIBLE
                        when (level) {
                            "customer" -> {
                                val filteredNotification = notifications.data?.filter { it.userType == "customer" }
                                notificationListAdapter.listData = filteredNotification
                            }
                            "narasumber" -> {
                                notificationListAdapter.listData = notifications.data
                            }
                            else -> {
                                val filteredNotification = notifications.data?.filter { it.userType == "customer" }
                                notificationListAdapter.listData = filteredNotification
                            }
                        }
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        binding?.rvArticleTabList?.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
