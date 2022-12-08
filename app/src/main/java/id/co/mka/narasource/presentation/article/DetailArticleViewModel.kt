package id.co.mka.narasource.presentation.article

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.domain.model.Article
import id.co.mka.narasource.core.domain.usecase.ArticleUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailArticleViewModel @Inject constructor(
    private val articleUseCase: ArticleUseCase
) : ViewModel() {

    private val _article = MutableLiveData<Resource<Article>?>()
    val article: LiveData<Resource<Article>?> = _article

    fun getDetailArticle(id: String) {
        viewModelScope.launch {
            articleUseCase.getDetailArticle(id).collect(_article::setValue)
        }
    }
}
