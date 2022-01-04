package com.bunbeauty.data.repository

import com.bunbeauty.data.dao.CategoryDao
import com.bunbeauty.domain.repo.CategoryRepo
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepo {

}