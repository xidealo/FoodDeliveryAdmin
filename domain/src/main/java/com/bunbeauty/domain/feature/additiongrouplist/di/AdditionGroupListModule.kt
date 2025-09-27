package com.bunbeauty.domain.feature.additiongrouplist.di

import com.bunbeauty.domain.feature.additiongrouplist.GetSeparatedAdditionGroupListUseCase
import com.bunbeauty.domain.feature.additiongrouplist.UpdateVisibleAdditionGroupListUseCase
import com.bunbeauty.domain.feature.additiongrouplist.editadditiongroup.EditAdditionGroupUseCase
import com.bunbeauty.domain.feature.additiongrouplist.editadditiongroup.GetAdditionGroupUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.GetAdditionGroupListFromMenuProductUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.editadditiongroupformenuproduct.GetAdditionGroupWithAdditionsForMenuProductUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectaddition.GetSelectedAdditionListUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectaddition.GetSelectedAdditionListsUeCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectadditiongroup.GetSeparatedSelectableAdditionGroupListUseCase
import org.koin.dsl.module

fun additionGroupListModule() = module {
    factory {
        GetSeparatedAdditionGroupListUseCase(
            additionGroupRepo = get(),
            dataStoreRepo = get()
        )
    }
    factory {
        UpdateVisibleAdditionGroupListUseCase(
            additionGroupRepo = get(),
            dataStoreRepo = get()
        )
    }
    factory {
        GetAdditionGroupListFromMenuProductUseCase(
            menuProductRepo = get(),
            dataStoreRepo = get()
        )
    }

    factory {
        GetAdditionGroupWithAdditionsForMenuProductUseCase(
            menuProductRepo = get(),
            dataStoreRepo = get()
        )
    }

    factory {
        GetSelectedAdditionListsUeCase(
            menuProductRepo = get()
        )
    }

    factory {
        GetSelectedAdditionListUseCase()
    }

    factory {
        GetAdditionGroupUseCase(
            additionGroupRepo = get(),
            dataStoreRepo = get()
        )
    }

    factory {
        EditAdditionGroupUseCase(
            additionGroupRepo = get(),
            dataStoreRepo = get()
        )
    }
    factory {
        GetSeparatedSelectableAdditionGroupListUseCase(
            getSeparatedAdditionGroupListUseCase = get(),
            menuProductToAdditionGroupRepository = get()
        )
    }
}
