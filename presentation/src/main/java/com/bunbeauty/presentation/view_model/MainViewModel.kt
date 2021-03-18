package com.bunbeauty.presentation.view_model

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.repository.cafe.CafeRepo
import com.bunbeauty.presentation.navigator.MainNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

class MainViewModel @Inject constructor(private val cafeRepo: CafeRepo) : BaseViewModel<MainNavigator>() {

    override var navigator: WeakReference<MainNavigator>? = null

    fun refreshCafeList() {
        viewModelScope.launch(Dispatchers.IO) {
            cafeRepo.refreshCafeList()
        }
    }
}