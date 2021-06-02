package com.bunbeauty.fooddeliveryadmin.presentation.main

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DeliveryRepo
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
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