package id.co.mka.narasource.presentation.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.R
import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.ui.ArticleListAdapter
import id.co.mka.narasource.core.utils.ArticleListType
import id.co.mka.narasource.databinding.FragmentArticleTabBinding
import id.co.mka.narasource.presentation.home.HomeFragmentDirections
import id.co.mka.narasource.presentation.searchArticle.SearchArticleFragmentDirections

@AndroidEntryPoint
class ArticleTabFragment(private var listType: ArticleListType = ArticleListType.PREVIEW_LIST) : Fragment() {

    private val articleViewModel: ArticleViewModel by viewModels()

    private var _binding: FragmentArticleTabBinding? = null
    private val binding get() = _binding

    private val articleListAdapter = ArticleListAdapter()
    private lateinit var navigator: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentArticleTabBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigator = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_dash)

        observeData()
        setupView()
    }

    private fun setupView() {
        binding?.rvArticleTabList?.apply {
            when (listType) {
                ArticleListType.PREVIEW_LIST -> {
                    layoutManager = GridLayoutManager(context, 2)
                    overScrollMode = View.OVER_SCROLL_NEVER
                }
                ArticleListType.FULL_LIST -> {
                    layoutManager = LinearLayoutManager(context)
                    overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
                }
            }
            setHasFixedSize(true)
            adapter = articleListAdapter
        }
        articleListAdapter.listType = listType
        articleListAdapter.onItemClick = { selectedData ->
            when (listType) {
                ArticleListType.PREVIEW_LIST -> navigator.navigate(
                    HomeFragmentDirections.actionNavigationHomeToNavigationDetailArticleActivity(
                        selectedData
                    )
                )
                else -> navigator.navigate(
                    SearchArticleFragmentDirections.actionNavigationSearchArticleToNavigationDetailArticleActivity(
                        selectedData
                    )
                )
            }
        }
    }

    private fun observeData() {
        when (listType) {
            ArticleListType.PREVIEW_LIST -> articleViewModel.getPreviewArticle()
            ArticleListType.FULL_LIST -> articleViewModel.getSearchResultArticle()
        }
        articleViewModel.listArticle.observe(viewLifecycleOwner) { article ->
            if (article != null) {
                when (article) {
                    is Resource.Loading -> {
                        binding?.progressBar?.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        binding?.rvArticleTabList?.visibility = View.VISIBLE
                        articleListAdapter.listData = article.data
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
