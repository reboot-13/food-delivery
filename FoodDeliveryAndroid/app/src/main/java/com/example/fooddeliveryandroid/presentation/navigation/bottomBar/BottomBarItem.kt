package com.example.fooddeliveryandroid.presentation.navigation.bottomBar

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale


@Composable
    fun BottomBarItem(
        bottomDestination: BottomDestination,
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier
    ) {
        val scale by animateFloatAsState(
            targetValue = if (selected) 1.3f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "icon_scale"
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxHeight()
                .clickable(
                    onClick = onClick
                )
        ) {
            Icon(
                imageVector = bottomDestination.icon,
                contentDescription = bottomDestination.title,
                modifier = Modifier.scale(scale)
            )
            Text(bottomDestination.title)
        }
    }