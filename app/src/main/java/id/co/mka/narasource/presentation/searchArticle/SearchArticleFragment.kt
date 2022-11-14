package id.co.mka.narasource.presentation.searchArticle

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import id.co.mka.narasource.R
import id.co.mka.narasource.core.utils.SectionTabType
import id.co.mka.narasource.core.utils.TAB_ARTICLE_TITLES
import id.co.mka.narasource.databinding.FragmentSearchArticleBinding
import id.co.mka.narasource.presentation.adapter.SectionTabAdapter

class SearchArticleFragment : Fragment() {

    private var _binding: FragmentSearchArticleBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchArticleBinding.inflate(inflater, container, false)
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

        setupTabLayout(view)
        setupAction()
    }

    private fun setupAction() {
        binding?.apply {
            btnSearchArticle.setOnClickListener {
                showSearchOverlay()
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

    private fun setupTabLayout(view: View) {
        val tabTitles = TAB_ARTICLE_TITLES
        val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)
        viewPager.adapter = SectionTabAdapter(this, tabTitles.size, SectionTabType.SEARCH_ARTICLE)
        viewPager.offscreenPageLimit = tabTitles.size / tabTitles.size
        val tabs: TabLayout = view.findViewById(R.id.tab_home_article)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun showSearchOverlay() {
        Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_search)
            setCancelable(true)
            this.window?.setBackgroundDrawableResource(android.R.color.transparent)
            show()

            val button = this.findViewById<Button>(R.id.btn_search)

            this.findViewById<TextInputEditText>(R.id.input_search_input)?.doOnTextChanged { _, _, _, count ->
                button.isEnabled = count > 0
            }
            button.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
