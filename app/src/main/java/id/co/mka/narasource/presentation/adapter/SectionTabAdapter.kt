package id.co.mka.narasource.presentation.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.co.mka.narasource.core.utils.ARG_SECTION_NUMBER
import id.co.mka.narasource.core.utils.ArticleListType
import id.co.mka.narasource.core.utils.SectionTabType
import id.co.mka.narasource.presentation.article.ArticleTabFragment

class SectionTabAdapter(fragment: Fragment, private val fragmentCount: Int, private val tabType: SectionTabType) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = fragmentCount

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = when (tabType) {
            SectionTabType.HOME_ARTICLE -> ArticleTabFragment(ArticleListType.PREVIEW_LIST)
            SectionTabType.SEARCH_ARTICLE -> ArticleTabFragment(ArticleListType.FULL_LIST)
        }
        fragment.arguments = Bundle().apply {
            putInt(ARG_SECTION_NUMBER, position + 1)
        }
        return fragment
    }
}
