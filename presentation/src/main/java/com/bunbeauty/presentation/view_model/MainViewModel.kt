package com.bunbeauty.presentation.view_model

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.repository.cafe.CafeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

class MainViewModel @Inject constructor(private val cafeRepo: CafeRepo) : BaseViewModel() {

    fun refreshCafeList() {
        viewModelScope.launch(Dispatchers.IO) {
            cafeRepo.refreshCafeList()
        }
    }
}