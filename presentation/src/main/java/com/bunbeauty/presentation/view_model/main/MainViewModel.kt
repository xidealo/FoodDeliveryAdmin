package com.bunbeauty.presentation.view_model.main

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DeliveryRepo
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val cafeRepo: CafeRepo,
    private val deliveryRepo: DeliveryRepo,
) : BaseViewModel() {

    fun refreshCafeList() {
        viewModelScope.launch {
            cafeRepo.refreshCafeList()
        }
    }

    fun refreshDelivery() {
        viewModelScope.launch {
            deliveryRepo.refreshDelivery()
        }
    }
}