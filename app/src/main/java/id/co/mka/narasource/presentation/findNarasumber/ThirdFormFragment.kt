package id.co.mka.narasource.presentation.findNarasumber

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.core.domain.model.Session
import id.co.mka.narasource.core.ui.SessionRecapListAdapter
import id.co.mka.narasource.databinding.FragmentThirdFormBinding

@AndroidEntryPoint
class ThirdFormFragment : Fragment() {

    private var _binding: FragmentThirdFormBinding? = null
    private val binding get() = _binding

    private val findNarasumberViewModel: FindNarasumberViewModel by activityViewModels()

    private val sessionRecapListAdapter = SessionRecapListAdapter()
    private var listSession = listOf<Session>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdFormBinding.inflate(inflater, container, false)
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

    private fun observeData() {
        findNarasumberViewModel.narasumberCount.observe(requireActivity()) {
            if (it != null) {
                binding?.tvSubtitle?.text = "$it Sesi"
                val total = "Rp ${it.times(20)}k"
                binding?.tvTotalValue?.text = total
            }
        }
        findNarasumberViewModel.listSession.observe(requireActivity()) {
            if (it != null) {
                sessionRecapListAdapter.listData = it
                this.listSession = it
            }
        }
    }

    private fun setupView() {
        binding?.rvSessionRecapList?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = sessionRecapListAdapter
            addItemDecoration(
                object : DividerItemDecoration(context, VERTICAL) {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        val position = parent.getChildAdapterPosition(view)

                        if (position == state.itemCount - 1) {
                            outRect.setEmpty()
                        } else {
                            super.getItemOffsets(outRect, view, parent, state)
                        }
                    }
                }
            )
        }
    }

    private fun setupAction() {
        binding?.apply {
            btnSubmit.setOnClickListener {
                val action = ThirdFormFragmentDirections.actionNavThirdFormToNavFourthForm()
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
