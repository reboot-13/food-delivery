package com.example.fooddeliveryandroid.domain.mapper

import com.example.fooddeliveryandroid.data.local.entity.CategoryEntity
import com.example.fooddeliveryandroid.data.remote.dto.CategoryResponse
import com.example.fooddeliveryandroid.data.remote.dto.CategoryShortResponse
import com.example.fooddeliveryandroid.domain.model.Category

object CategoryMapper {
    fun responseToModel(categoryResponse: CategoryShortResponse): Category {
        return Category(
            id = categoryResponse.id,
            name = categoryResponse.name
        )
    }

    fun entityToModel(categoryEntity: CategoryEntity): Category {
        return Category(
            id = categoryEntity.id,
            name =  categoryEntity.name
        )
    }

    fun responseToEntity(response: CategoryShortResponse): CategoryEntity {
        return CategoryEntity(
            id = response.id,
            name =  response.name
        )
    }
}