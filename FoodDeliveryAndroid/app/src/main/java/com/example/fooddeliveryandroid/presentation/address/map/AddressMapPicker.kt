package com.example.fooddeliveryandroid.presentation.address.map

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.fooddeliveryandroid.presentation.address.addAddress.AddressMapDefaults
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.mapview.MapView
import java.lang.ref.WeakReference

@Composable
fun AddressMapPicker (
    modifier: Modifier = Modifier,
    onMapCenterChanged: (
            latitude: Double,
            longitude: Double,
            zoom: Float
            ) -> Unit,
    onMapMovingChanged: (Boolean) -> Unit
){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnMapCenterChanged by rememberUpdatedState(
        onMapCenterChanged
    )

    val mapView = remember(context) {
        MapView(context).apply {
            mapWindow.map.move(
                CameraPosition(
                    Point(
                        AddressMapDefaults.LATITUDE,
                        AddressMapDefaults.LONGITUDE,
                    ),
                    AddressMapDefaults.ZOOM,
                    0f, 0f
                )
            )
        }
    }

    val cameraListener = remember (mapView) {
        CameraListener { _, cameraPosition, reason, finished ->
            when {
                reason == CameraUpdateReason.GESTURES -> {
                    onMapMovingChanged(true)
                }
            }

            if (finished) {
                val target = cameraPosition.target

                onMapMovingChanged(false)

                currentOnMapCenterChanged(
                    target.latitude,
                    target.longitude,
                    cameraPosition.zoom
                )
            }
        }
    }

    val cameraListenerReference = remember (cameraListener) {
        WeakReference(cameraListener)
    }


    AndroidView(
        factory = { mapView },
        modifier = modifier
    )

    DisposableEffect(lifecycleOwner, mapView) {
        val map = mapView.mapWindow.map
        var isStarted = false


        fun startMapView() {
            if (!isStarted) {
                mapView.onStart()
                isStarted = true
            }
        }

        fun stopMapView() {
            if (isStarted) {
                mapView.onStop()
                isStarted = false
            }
        }

        map.addCameraListener(cameraListenerReference)

        val observer = LifecycleEventObserver {_, event ->
            when (event) {
                Lifecycle.Event.ON_START -> startMapView()
                Lifecycle.Event.ON_STOP -> stopMapView()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        if (
            lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
            ) {
            startMapView()
        }

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            map.removeCameraListener(cameraListenerReference)
            stopMapView()
            mapView.destroy()
        }
    }

    LaunchedEffect(mapView) {
        currentOnMapCenterChanged(
            AddressMapDefaults.LATITUDE,
            AddressMapDefaults.LONGITUDE,
            AddressMapDefaults.ZOOM
        )
    }
}