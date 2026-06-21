package com.slotik.mobile.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.EventNote
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.slotik.mobile.presentation.navigation.SlotikDestination
import com.slotik.mobile.presentation.theme.SlotikAvatarGradientStart
import com.slotik.mobile.presentation.theme.SlotikBackground
import com.slotik.mobile.presentation.theme.SlotikDivider
import com.slotik.mobile.presentation.theme.SlotikPrimary
import com.slotik.mobile.presentation.theme.SlotikPrimaryDark
import com.slotik.mobile.presentation.theme.SlotikPrimaryLight
import com.slotik.mobile.presentation.theme.SlotikSurface
import com.slotik.mobile.presentation.theme.SlotikTextPrimary
import com.slotik.mobile.presentation.theme.SlotikTextSecondary

data class SlotikBottomItem(
    val label: String,
    val destination: SlotikDestination,
)

@Composable
fun SlotikScreenContainer(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    bottomBar: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier.background(SlotikBackground),
        containerColor = SlotikBackground,
        bottomBar = {
            bottomBar?.invoke()
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(contentPadding),
            content = content,
        )
    }
}

@Composable
fun SlotikTopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    showBack: Boolean = false,
    onBack: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showBack) {
                Surface(
                    shape = CircleShape,
                    color = SlotikPrimaryLight.copy(alpha = 0.7f),
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { onBack?.invoke() },
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Назад",
                            tint = SlotikPrimary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(start = if (showBack) 10.dp else 0.dp)) {
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlotikTextSecondary,
                    )
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = SlotikTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        trailing?.invoke()
    }
}

@Composable
fun SlotikPrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val gradient = Brush.linearGradient(
        colors = if (enabled) {
            listOf(SlotikPrimary, SlotikPrimaryDark)
        } else {
            listOf(SlotikPrimary.copy(alpha = 0.38f), SlotikPrimaryDark.copy(alpha = 0.38f))
        },
    )
    Box(
        modifier = modifier
            .shadow(
                elevation = if (enabled) 6.dp else 0.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = SlotikPrimary.copy(alpha = 0.25f),
                spotColor = SlotikPrimary.copy(alpha = 0.35f),
            )
            .clip(RoundedCornerShape(18.dp))
            .background(gradient)
            .clickable(enabled = enabled, onClick = onClick)
            .fillMaxWidth()
            .padding(vertical = 18.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = SlotikSurface,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun SlotikSectionTitle(
    title: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = SlotikTextPrimary,
            fontWeight = FontWeight.SemiBold,
        )
        if (actionLabel != null && onActionClick != null) {
            Text(
                text = actionLabel,
                style = MaterialTheme.typography.labelLarge,
                color = SlotikPrimary,
                modifier = Modifier.clickable(onClick = onActionClick),
            )
        }
    }
}

@Composable
fun SlotikAvatar(
    label: String,
    modifier: Modifier = Modifier,
    accent: Color = SlotikPrimaryLight,
) {
    val gradientBrush = Brush.radialGradient(
        colors = listOf(SlotikAvatarGradientStart, accent),
    )
    Box(
        modifier = modifier
            .size(56.dp)
            .shadow(
                elevation = 2.dp,
                shape = CircleShape,
                ambientColor = SlotikPrimary.copy(alpha = 0.12f),
                spotColor = SlotikPrimary.copy(alpha = 0.12f),
            )
            .clip(CircleShape)
            .background(gradientBrush),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label.take(1).uppercase(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = SlotikPrimary,
        )
    }
}

@Composable
fun SlotikInfoChip(
    text: String,
    modifier: Modifier = Modifier,
    background: Color = SlotikPrimaryLight,
    contentColor: Color = SlotikPrimary,
) {
    Surface(
        modifier = modifier,
        color = background,
        shape = RoundedCornerShape(999.dp),
    ) {
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
        )
    }
}

@Composable
fun SlotikBottomBar(
    currentDestination: SlotikDestination,
    onNavigate: (SlotikDestination) -> Unit,
) {
    val items = listOf(
        SlotikBottomItem("Главная", SlotikDestination.HOME),
        SlotikBottomItem("Поиск", SlotikDestination.SPECIALISTS),
        SlotikBottomItem("Записи", SlotikDestination.BOOKINGS),
        SlotikBottomItem("Профиль", SlotikDestination.PROFILE),
    )

    NavigationBar(
        containerColor = SlotikSurface,
        tonalElevation = 0.dp,
        modifier = Modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor = Color.Black.copy(alpha = 0.08f),
            ),
    ) {
        items.forEach { item ->
            val isSelected = currentDestination == item.destination
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.destination) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SlotikPrimary,
                    selectedTextColor = SlotikPrimary,
                    indicatorColor = SlotikPrimaryLight,
                    unselectedIconColor = SlotikTextSecondary.copy(alpha = 0.7f),
                    unselectedTextColor = SlotikTextSecondary.copy(alpha = 0.7f),
                ),
                icon = {
                    Icon(
                        imageVector = when (item.destination) {
                            SlotikDestination.HOME -> Icons.Rounded.Home
                            SlotikDestination.SPECIALISTS -> Icons.Rounded.Search
                            SlotikDestination.BOOKINGS -> Icons.AutoMirrored.Rounded.EventNote
                            SlotikDestination.PROFILE -> Icons.Rounded.Person
                            SlotikDestination.SLOT_SELECTION -> Icons.Rounded.CalendarMonth
                            SlotikDestination.BOOKING_CONFIRMATION -> Icons.Rounded.CalendarMonth
                        },
                        contentDescription = item.label,
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    )
                },
            )
        }
    }
}

@Composable
fun SlotikCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color.Black.copy(alpha = 0.06f),
                spotColor = Color.Black.copy(alpha = 0.06f),
            ),
        colors = CardDefaults.cardColors(containerColor = SlotikSurface),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            content = content,
        )
    }
}

@Composable
fun SlotikDividerSpacer() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(SlotikDivider),
    )
}
