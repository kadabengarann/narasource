package id.co.mka.narasource.core.data.repository

import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.data.source.remote.datasource.RemoteArticleDataSource
import id.co.mka.narasource.core.data.source.remote.network.ApiResponse
import id.co.mka.narasource.core.domain.model.Article
import id.co.mka.narasource.core.domain.repository.IArticleRepository
import id.co.mka.narasource.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val remoteArticleDataSource: RemoteArticleDataSource
) : IArticleRepository {
    override fun getPreviewArticle(): Flow<Resource<List<Article>>> {
        return remoteArticleDataSource.getPreviewArticle().map {
            when (it) {
                is ApiResponse.Success -> {
                    val data = DataMapper.mapArticleResponsesToDomain(it.data)
                    Resource.Success(data)
                }
                is ApiResponse.Empty -> Resource.Success(emptyList())
                is ApiResponse.Error -> Resource.Error(it.errorMessage)
                is ApiResponse.Loading -> Resource.Loading()
            }
        }
    }

    override fun searchArticle(): Flow<Resource<List<Article>>> {
        return remoteArticleDataSource.searchArticle().map {
            when (it) {
                is ApiResponse.Success -> {
                    val data = DataMapper.mapArticleResponsesToDomain(it.data)
                    Resource.Success(data)
                }
                is ApiResponse.Empty -> Resource.Success(emptyList())
                is ApiResponse.Error -> Resource.Error(it.errorMessage)
                is ApiResponse.Loading -> Resource.Loading()
            }
        }
    }
}
