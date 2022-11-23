
package id.co.mka.narasource.presentation.findNarasumber

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FindNarasumberViewModel @Inject constructor() : ViewModel() {

    private val _narasumberCount = MutableLiveData(0)
    val narasumberCount: LiveData<Int?> = _narasumberCount

    fun postNarasumberCount(value: String) {
        _narasumberCount.value = value.toInt()
    }
}
