package id.co.mka.narasource.core.data.source.remote.datasource

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import id.co.mka.narasource.core.data.source.remote.network.ApiResponse
import id.co.mka.narasource.core.data.source.remote.network.ArticleService
import id.co.mka.narasource.core.data.source.remote.response.ArticleResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteArticleDataSource @Inject constructor(
//    retrofit: Retrofit,
    @ApplicationContext appContext: Context
) {
    private val articleService = ArticleService(appContext)

    fun getPreviewArticle(): Flow<ApiResponse<List<ArticleResponse>>> {
        return flow {
            try {
                emit(ApiResponse.Loading())
                val response = articleService.getPreviewArticle()
                val dataArray = response
                delay(1000L)
                if (dataArray.isNotEmpty()) emit(ApiResponse.Success(response)) else emit(
                    ApiResponse.Empty
                )
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
    fun searchArticle(): Flow<ApiResponse<List<ArticleResponse>>> {
        return flow {
            try {
                val response = articleService.searchArticle()
                val dataArray = response
                emit(ApiResponse.Loading())
                delay(1000L)
                if (dataArray.isNotEmpty()) emit(ApiResponse.Success(response)) else emit(
                    ApiResponse.Empty
                )
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}
