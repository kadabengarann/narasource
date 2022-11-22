package id.co.mka.narasource.presentation.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import id.co.mka.narasource.core.domain.model.Notification
import id.co.mka.narasource.core.ui.NotificationListAdapter
import id.co.mka.narasource.core.utils.ARG_SECTION_NUMBER
import id.co.mka.narasource.databinding.FragmentNotificationTabBinding

class NotificationTabFragment : Fragment() {

    private var _binding: FragmentNotificationTabBinding? = null
    private val binding get() = _binding

    private val notificationListAdapter = NotificationListAdapter()
    private var index = 0

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
    }
    private fun setupView() {
        binding?.rvArticleTabList?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = notificationListAdapter
        }
        notificationListAdapter.onItemClick = { selectedData ->
            Toast.makeText(requireContext(), "$selectedData Clicked", Toast.LENGTH_SHORT).show()
//            navigator.navigate(SearchArticleFragmentDirections.actionNavigationSearchArticleToNavigationDetailArticleActivity(selectedData))
        }
    }

    private fun observeData(isRead: Boolean) {
        val listData: MutableList<Notification> = mutableListOf()
        for (i in 1..20) {
            val notification = Notification(
                title = "Notifikasi $i",
                description = "Kamu telah terdaftar pada NaraSource.",
                timeStamp = "Hari ini, 17:49",
                status = if (isRead) 1 else 0
            )
            listData.add(notification)
        }
        notificationListAdapter.listData = listData
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
