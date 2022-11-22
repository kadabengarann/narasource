package id.co.mka.narasource.presentation.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.domain.model.Article
import id.co.mka.narasource.core.domain.usecase.ArticleUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articleUseCase: ArticleUseCase
) : ViewModel() {

    private val _listArticle = MutableLiveData<Resource<List<Article>>?>()
    val listArticle: LiveData<Resource<List<Article>>?> = _listArticle

    fun getPreviewArticle() {
        viewModelScope.launch {
            articleUseCase.getPreviewArticle().collect(_listArticle::setValue)
        }
    }
    fun getSearchResultArticle() {
        viewModelScope.launch {
            articleUseCase.searchArticle().collect(_listArticle::setValue)
        }
    }
}
