package com.bunbeauty.domain.usecase

import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.CityRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.repo.MenuProductToAdditionGroupRepository
import com.bunbeauty.domain.repo.NonWorkingDayRepo
import com.bunbeauty.domain.repo.PhotoRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo

class LogoutUseCase(
    private val userAuthorizationRepo: UserAuthorizationRepo,
    private val orderRepo: OrderRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepo: CafeRepo,
    private val cityRepo: CityRepo,
    private val menuProductRepo: MenuProductRepo,
    private val nonWorkingDayRepo: NonWorkingDayRepo,
    private val photoRepo: PhotoRepo,
    private val additionGroupRepo: AdditionGroupRepo,
    private val additionRepo: AdditionRepo,
    private val categoryRepo: CategoryRepo,
    private val menuProductToAdditionGroupRepository: MenuProductToAdditionGroupRepository
) {

    suspend operator fun invoke() {
        userAuthorizationRepo.clearNotificationToken()
        orderRepo.clearCache()
        dataStoreRepo.clearCache()
        cafeRepo.clearCache()
        cityRepo.clearCache()
        menuProductRepo.clearCache()
        nonWorkingDayRepo.clearCache()
        additionGroupRepo.clearCache()
        photoRepo.clearCache()
        additionRepo.clearCache()
        categoryRepo.clearCache()
        menuProductToAdditionGroupRepository.clearCache()
    }
}
