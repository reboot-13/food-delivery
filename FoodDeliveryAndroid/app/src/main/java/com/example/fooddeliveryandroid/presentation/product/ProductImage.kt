package com.example.fooddeliveryandroid.presentation.product

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.fooddeliveryandroid.R
import com.example.fooddeliveryandroid.data.remote.network.resolveImageUrl

@Composable
fun ProductImage(
    imageUrl: String?,
    imageDescription: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(resolveImageUrl(imageUrl) + "?v=3")
            .crossfade(true)
            .build(),
        contentDescription = imageDescription,
        placeholder = painterResource( R.drawable.empty_product_image),
        error = painterResource(R.drawable.empty_product_image),

        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),
        contentScale = ContentScale.Crop)
}