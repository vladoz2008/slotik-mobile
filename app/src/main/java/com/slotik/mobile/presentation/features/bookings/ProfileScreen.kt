package com.slotik.mobile.presentation.features.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.slotik.mobile.domain.model.Booking
import com.slotik.mobile.domain.model.BookingStatus
import com.slotik.mobile.domain.model.ProfileSection
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.presentation.util.DateTimeFormatters
import com.slotik.mobile.presentation.components.SlotikAvatar
import com.slotik.mobile.presentation.components.SlotikBottomBar
import com.slotik.mobile.presentation.components.SlotikCard
import com.slotik.mobile.presentation.components.SlotikDividerSpacer
import com.slotik.mobile.presentation.components.SlotikInfoChip
import com.slotik.mobile.presentation.components.SlotikScreenContainer
import com.slotik.mobile.presentation.navigation.SlotikDestination
import com.slotik.mobile.presentation.theme.SlotikBackground
import com.slotik.mobile.presentation.theme.SlotikDanger
import com.slotik.mobile.presentation.theme.SlotikDivider
import com.slotik.mobile.presentation.theme.SlotikPrimary
import com.slotik.mobile.presentation.theme.SlotikPrimaryDark
import com.slotik.mobile.presentation.theme.SlotikPrimaryLight
import com.slotik.mobile.presentation.theme.SlotikSky
import com.slotik.mobile.presentation.theme.SlotikSuccessLight
import com.slotik.mobile.presentation.theme.SlotikSurface
import com.slotik.mobile.presentation.theme.SlotikTextPrimary
import com.slotik.mobile.presentation.theme.SlotikTextSecondary

@Composable
fun ProfileScreen(
    currentRoute: SlotikDestination,
    currentBookings: List<Booking>,
    bookingHistory: List<Booking>,
    favoriteSpecialists: List<Specialist>,
    section: ProfileSection,
    onSectionChange: (ProfileSection) -> Unit,
    onCancelBooking: (String) -> Unit,
    onOpenSpecialist: (String) -> Unit,
    onNavigate: (SlotikDestination) -> Unit,
    onSignOut: () -> Unit,
    userName: String = "Пользователь",
    userPhone: String = "",
) {
    SlotikScreenContainer(
        bottomBar = {
            SlotikBottomBar(
                currentDestination = currentRoute,
                onNavigate = onNavigate,
            )
        },
    ) {
        Text(
            "Профиль",
            style = MaterialTheme.typography.headlineLarge,
            color = SlotikTextPrimary,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Шапка профиля с градиентной карточкой
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(22.dp),
                    ambientColor = SlotikPrimary.copy(alpha = 0.18f),
                    spotColor = SlotikPrimary.copy(alpha = 0.22f),
                )
                .clip(RoundedCornerShape(22.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(SlotikPrimary, SlotikPrimaryDark),
                    ),
                ),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SlotikAvatar(
                    label = userName,
                    modifier = Modifier.size(64.dp),
                    accent = Color.White.copy(alpha = 0.22f),
                )
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        userName,
                        style = MaterialTheme.typography.titleLarge,
                        color = SlotikSurface,
                        fontWeight = FontWeight.Bold,
                    )
                    if (userPhone.isNotBlank()) {
                        Text(
                            userPhone,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SlotikSurface.copy(alpha = 0.78f),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // BUG-01 fix: вкладки всегда одинаковой высоты, только цвет меняется
        Surface(
            color = SlotikSurface,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                ProfileTab(
                    text = "Мои записи",
                    selected = section == ProfileSection.BOOKINGS,
                    modifier = Modifier.weight(1f),
                    onClick = { onSectionChange(ProfileSection.BOOKINGS) },
                )
                ProfileTab(
                    text = "Избранное",
                    selected = section == ProfileSection.FAVORITES,
                    modifier = Modifier.weight(1f),
                    onClick = { onSectionChange(ProfileSection.FAVORITES) },
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (section) {
            ProfileSection.BOOKINGS -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    (currentBookings + bookingHistory).forEach { booking ->
                        BookingCard(
                            booking = booking,
                            onCancelBooking = onCancelBooking,
                        )
                    }
                }
            }
            ProfileSection.FAVORITES -> {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    favoriteSpecialists.forEach { specialist ->
                        SlotikCard(modifier = Modifier.clickable { onOpenSpecialist(specialist.id) }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                SlotikAvatar(label = specialist.name, accent = SlotikSky)
                                Column(modifier = Modifier.padding(start = 12.dp)) {
                                    Text(
                                        specialist.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = SlotikTextPrimary,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                    Text(
                                        specialist.specialty,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = SlotikTextSecondary,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Text(
            "Настройки",
            style = MaterialTheme.typography.titleLarge,
            color = SlotikTextPrimary,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(12.dp))
        SettingsList(onSignOut = onSignOut)
    }
}

// BUG-01 fix: индикатор всегда занимает фиксированную высоту, только цвет меняется
@Composable
private fun ProfileTab(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        color = if (selected) SlotikPrimaryLight else Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) SlotikPrimary else SlotikTextSecondary,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            )
        }
    }
}

@Composable
private fun BookingCard(
    booking: Booking,
    onCancelBooking: (String) -> Unit,
) {
    val isConfirmed = booking.status == BookingStatus.CONFIRMED
    SlotikCard {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    booking.serviceName,
                    style = MaterialTheme.typography.titleMedium,
                    color = SlotikTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    booking.specialistName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlotikTextSecondary,
                )
            }
            SlotikInfoChip(
                text = if (isConfirmed) "Подтверждено" else "Завершено",
                background = if (isConfirmed) SlotikPrimaryLight else SlotikSuccessLight,
                contentColor = if (isConfirmed) SlotikPrimary else Color(0xFF1B5E20),
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        SlotikDividerSpacer()
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = DateTimeFormatters.shortDayFormatter.format(booking.date),
                style = MaterialTheme.typography.bodyMedium,
                color = SlotikTextSecondary,
            )
            Text(
                text = DateTimeFormatters.timeFormatter.format(booking.startTime),
                style = MaterialTheme.typography.bodyMedium,
                color = SlotikTextSecondary,
            )
        }
        if (isConfirmed) {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Отменить запись",
                style = MaterialTheme.typography.labelLarge,
                color = SlotikDanger,
                modifier = Modifier.clickable { onCancelBooking(booking.id) },
            )
        }
    }
}

@Composable
private fun SettingsList(
    onSignOut: () -> Unit,
) {
    Surface(
        color = SlotikSurface,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            SettingsRow("Личные данные", Icons.Rounded.PersonOutline)
            SlotikDividerSpacer()
            SettingsRow("Уведомления", Icons.Rounded.Notifications)
            SlotikDividerSpacer()
            SettingsRow("Способы оплаты", Icons.Rounded.CreditCard)
            SlotikDividerSpacer()
            SettingsRow("Выйти", Icons.AutoMirrored.Rounded.Logout, danger = true, onClick = onSignOut)
        }
    }
}

@Composable
private fun SettingsRow(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    danger: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 18.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = if (danger) SlotikDanger.copy(alpha = 0.10f) else SlotikPrimaryLight,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(40.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = if (danger) SlotikDanger else SlotikPrimary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (danger) SlotikDanger else SlotikTextPrimary,
                fontWeight = if (danger) FontWeight.Medium else FontWeight.Normal,
                modifier = Modifier.padding(start = 14.dp),
            )
        }
        if (!danger) {
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = SlotikTextSecondary.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
