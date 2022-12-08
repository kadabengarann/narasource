package id.co.mka.narasource.core.domain.usecase

import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.domain.model.Article
import id.co.mka.narasource.core.domain.repository.IArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ArticleUseCase {
    fun getPreviewArticle(): Flow<Resource<List<Article>>>
    fun getDetailArticle(id: String): Flow<Resource<Article>>
    suspend fun searchArticle(): Flow<Resource<List<Article>>>
}

class ArticleInteractor @Inject constructor(
    private val articleRepository: IArticleRepository
) : ArticleUseCase {
    override fun getPreviewArticle() =
        articleRepository.getPreviewArticle()

    override fun getDetailArticle(id: String) =
        articleRepository.getDetailArticle(id)

    override suspend fun searchArticle() =
        articleRepository.searchArticle()
}
