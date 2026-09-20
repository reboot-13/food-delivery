package com.example.fooddeliveryandroid.presentation.order

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fooddeliveryandroid.domain.model.enums.OrderStatus

@Composable
fun OrderStatusBlockActive(
    activeStatus: OrderStatus,
    withRightArrow: Boolean = false
) {
    val statuses = OrderStatus.entries.dropLast(1)

    val icons = listOf(
        Icons.Outlined.Receipt,
        Icons.Outlined.Restaurant,
        Icons.Outlined.LocalShipping,
        Icons.Outlined.CheckCircle
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {

        statuses.forEachIndexed { index, status ->

            val isCompleted = status.ordinal < activeStatus.ordinal
            val isCurrent = status == activeStatus

            OrderStatusItem(
                status = status,
                icon = icons[index],
                isCompleted = isCompleted,
                isCurrent = isCurrent
            )

            if (index < statuses.lastIndex) {
                OrderStatusConnector(
                    isCompleted = status.ordinal < activeStatus.ordinal
                )
            }
        }

        if (withRightArrow) {
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = "Открыть заказ",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(
                        start = 4.dp,
                        top = 8.dp
                    )
                    .size(32.dp)
            )
        }
    }

}


@Composable
private fun OrderStatusItem(
    status: OrderStatus,
    icon: ImageVector,
    isCompleted: Boolean,
    isCurrent: Boolean
) {
    val colors = MaterialTheme.colorScheme

    val containerColor by animateColorAsState(
        targetValue = when {
            isCompleted -> colors.primary
            isCurrent -> colors.primary
            else -> colors.surfaceVariant
        },
        animationSpec = tween(350),
        label = "status_container_color"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            isCompleted || isCurrent -> colors.onPrimary
            else -> colors.onSurfaceVariant
        },
        animationSpec = tween(350),
        label = "status_content_color"
    )

    val infiniteTransition = rememberInfiniteTransition(
        label = "current_status_animation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isCurrent) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "status_pulse"
    )

    Column(
        modifier = Modifier
            .animateContentSize()
            .padding(horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Surface(
            modifier = Modifier
                .size(42.dp)
                .scale(pulseScale),
            shape = CircleShape,
            color = containerColor,
            contentColor = contentColor,
            tonalElevation = if (isCurrent) 3.dp else 0.dp
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {

                AnimatedContent(
                    targetState = isCompleted,
                    transitionSpec = {
                        (fadeIn(
                            animationSpec = tween(200)
                        ) + scaleIn(
                            animationSpec = tween(250)
                        )) togetherWith
                                fadeOut(
                                    animationSpec = tween(100)
                                )
                    },
                    label = "status_icon"
                ) { completed ->

                    Icon(
                        imageVector = if (completed) {
                            Icons.Outlined.Check
                        } else {
                            icon
                        },
                        contentDescription = status.description,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        Text(
            text = status.description,
            style = MaterialTheme.typography.labelSmall,
            color = if (isCurrent) {
                colors.onBackground
            } else {
                colors.onSurfaceVariant
            },
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}


@Composable
private fun RowScope.OrderStatusConnector(
    isCompleted: Boolean
) {
    val colors = MaterialTheme.colorScheme

    val lineColor by animateColorAsState(
        targetValue = if (isCompleted) {
            colors.primary
        } else {
            colors.surfaceVariant
        },
        animationSpec = tween(500),
        label = "status_connector_color"
    )

    HorizontalDivider(
        modifier = Modifier
            .weight(1f)
            .padding(
                top = 24.dp,
            ),
        thickness = 2.dp,
        color = lineColor
    )
}