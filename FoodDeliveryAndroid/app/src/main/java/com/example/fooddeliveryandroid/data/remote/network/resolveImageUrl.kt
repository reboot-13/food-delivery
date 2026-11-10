package com.example.fooddeliveryandroid.data.remote.network

import com.example.fooddeliveryandroid.di.NetworkModule.BASE_URL

fun resolveImageUrl(imageUrl: String?): String? {
    if (imageUrl.isNullOrBlank()) return null

    return BASE_URL.trimEnd('/') + "/" +
            imageUrl.trimStart('/')
}