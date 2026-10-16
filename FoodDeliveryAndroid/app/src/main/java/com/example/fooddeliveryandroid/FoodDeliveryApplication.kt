package com.example.fooddeliveryandroid

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodDeliveryApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(
            BuildConfig.MAPKIT_API_KEY
        )
    }
}