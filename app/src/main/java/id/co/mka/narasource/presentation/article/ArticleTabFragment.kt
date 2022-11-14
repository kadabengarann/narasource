package id.co.mka.narasource.presentation.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.mka.narasource.R
import id.co.mka.narasource.core.domain.model.Article
import id.co.mka.narasource.core.ui.ArticleListAdapter
import id.co.mka.narasource.core.utils.ArticleListType
import id.co.mka.narasource.databinding.FragmentArticleTabBinding

@AndroidEntryPoint
class ArticleTabFragment(private var listType: ArticleListType) : Fragment() {

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
            layoutManager =
                when (listType) {
                    ArticleListType.PREVIEW_LIST -> GridLayoutManager(context, 2)
                    ArticleListType.FULL_LIST -> LinearLayoutManager(context)
                }
            setHasFixedSize(true)
            adapter = articleListAdapter
        }
        articleListAdapter.listType = listType
    }

    private fun observeData() {
        when (listType) {
            ArticleListType.PREVIEW_LIST -> homeDataDummy()
            ArticleListType.FULL_LIST -> searchDataDummy()
        }
    }
    private fun homeDataDummy() {
        val listData: MutableList<Article> = mutableListOf()
        for (i in 1..4) {
            val article = Article(
                name = "Title",
                desc = "Description",
                image = "https://picsum.photos/200/300"
            )
            listData.add(article)
        }
        articleListAdapter.listData = listData
    }
    private fun searchDataDummy() {
        val listData: MutableList<Article> = mutableListOf()
        for (i in 1..20) {
            val article = Article(
                name = "Title",
                desc = "Description",
                image = "https://picsum.photos/200/300"
            )
            listData.add(article)
        }
        articleListAdapter.listData = listData
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
