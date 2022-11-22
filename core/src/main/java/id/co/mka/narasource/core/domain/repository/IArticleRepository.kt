package id.co.mka.narasource.core.domain.repository

import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.domain.model.Article
import kotlinx.coroutines.flow.Flow

interface IArticleRepository {
    fun getPreviewArticle(): Flow<Resource<List<Article>>>
    fun searchArticle(): Flow<Resource<List<Article>>>
}
