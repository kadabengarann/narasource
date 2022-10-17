package id.co.mka.naraq.core.data.source.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("session")

@Singleton
class SessionService @Inject constructor(@ApplicationContext appContext: Context) {
    private val dataStore = appContext.dataStore

    suspend fun setToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map {
            if (it[TOKEN_KEY] != null) it[TOKEN_KEY] else ""
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it.apply {
                remove(TOKEN_KEY)
            }
        }
    }

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("TOKEN")
    }
}
