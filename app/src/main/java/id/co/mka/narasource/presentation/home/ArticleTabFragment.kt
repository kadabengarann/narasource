package id.co.mka.narasource.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import id.co.mka.narasource.core.domain.model.Article
import id.co.mka.narasource.core.ui.ArticleListAdapter
import id.co.mka.narasource.databinding.FragmentArticleTabBinding

class ArticleTabFragment : Fragment() {

    private var _binding: FragmentArticleTabBinding? = null
    private val binding get() = _binding

    private val articleListAdapter = ArticleListAdapter()

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

        setupView()
        observeData()
    }

    private fun setupView() {
        binding?.rvArticleTabList?.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = articleListAdapter
        }
    }

    private fun observeData() {
        val listData: MutableList<Article> = mutableListOf()
        for (i in 1..10) {
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
