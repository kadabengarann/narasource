
package id.co.mka.narasource.presentation.findNarasumber

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mka.narasource.core.domain.model.Session
import javax.inject.Inject

@HiltViewModel
class FindNarasumberViewModel @Inject constructor() : ViewModel() {
    private val _listSession = MutableLiveData<List<Session>?>()
    val listSession: LiveData<List<Session>?> = _listSession

    private val _listSessionComplete = MutableLiveData(false)
    val listSessionComplete: LiveData<Boolean> = _listSessionComplete

    private val _narasumberCount = MutableLiveData(0)
    val narasumberCount: LiveData<Int?> = _narasumberCount

    fun setListSession() {
        _listSession.value = emptyList()
        val list = mutableListOf<Session>()
        val length = _narasumberCount.value ?: 0
        for (i in 1..length) {
            list.add(Session(i, null, null, null))
        }
        _listSession.value = list
    }

    fun postNarasumberCount(value: String) {
        _narasumberCount.value = value.toInt()
    }

    fun updateListSession(list: List<Session>) {
        _listSession.value = list
        checkListSession()
    }

    private fun checkListSession() {
        val list = _listSession.value ?: emptyList()
        if (list.isNotEmpty()) {
            var isComplete = true
            for (session in list) {
                if (session.date == null || session.timeStart == null || session.timeEnd == null) {
                    isComplete = false
                    break
                }
            }
            _listSessionComplete.value = isComplete
        }
    }
}
