package com.bunbeauty.domain.feature.menu.addmenuproduct

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateproduct.MenuProductCategoriesException
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.exception.updateproduct.MenuProductPhotoLinkException
import com.bunbeauty.domain.model.menuproduct.MenuProductPost
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import javax.inject.Inject

class AddMenuProductUseCase @Inject constructor(
    private val menuProductRepo: MenuProductRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(menuProductPost: MenuProductPost) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        when {
            menuProductPost.name.isEmpty() -> throw MenuProductNameException()
            menuProductPost.newPrice == 0 -> throw MenuProductNewPriceException()
            menuProductPost.description.isEmpty() -> throw MenuProductDescriptionException()
            menuProductPost.photoLink.isEmpty() -> throw MenuProductPhotoLinkException()
            menuProductPost.categories.isEmpty() -> throw MenuProductCategoriesException()
        }

        menuProductRepo.post(token = token, menuProductPost = menuProductPost)
    }
}
