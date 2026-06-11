package com.slotik.mobile.presentation.features.bookings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import com.slotik.mobile.domain.model.Booking
import com.slotik.mobile.domain.model.BookingStatus
import com.slotik.mobile.domain.model.ProfileSection
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.presentation.SlotikViewModel
import com.slotik.mobile.presentation.components.SlotikAvatar
import com.slotik.mobile.presentation.components.SlotikBottomBar
import com.slotik.mobile.presentation.components.SlotikCard
import com.slotik.mobile.presentation.components.SlotikDividerSpacer
import com.slotik.mobile.presentation.components.SlotikInfoChip
import com.slotik.mobile.presentation.components.SlotikScreenContainer
import com.slotik.mobile.presentation.navigation.SlotikDestination
import com.slotik.mobile.presentation.theme.SlotikDanger
import com.slotik.mobile.presentation.theme.SlotikPrimary
import com.slotik.mobile.presentation.theme.SlotikPrimaryLight
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
) {
    SlotikScreenContainer(
        bottomBar = {
            SlotikBottomBar(
                currentDestination = currentRoute,
                onNavigate = onNavigate,
            )
        },
    ) {
        Text("Профиль", style = MaterialTheme.typography.headlineLarge, color = SlotikTextPrimary)
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            SlotikAvatar(label = "А")
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text("Алексей Смирнов", style = MaterialTheme.typography.headlineMedium, color = SlotikTextPrimary)
                Text("+7 (999) 123-45-67", style = MaterialTheme.typography.bodyLarge, color = SlotikTextSecondary)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ProfileTab("Мои записи", section == ProfileSection.BOOKINGS) { onSectionChange(ProfileSection.BOOKINGS) }
            ProfileTab("Избранное", section == ProfileSection.FAVORITES) { onSectionChange(ProfileSection.FAVORITES) }
        }
        Spacer(modifier = Modifier.height(16.dp))

        when (section) {
            ProfileSection.BOOKINGS -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(currentBookings + bookingHistory, key = { it.id }) { booking ->
                        BookingCard(
                            booking = booking,
                            onCancelBooking = onCancelBooking,
                        )
                    }
                }
            }
            ProfileSection.FAVORITES -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(favoriteSpecialists, key = { it.id }) { specialist ->
                        SlotikCard(modifier = Modifier.clickable { onOpenSpecialist(specialist.id) }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                SlotikAvatar(label = specialist.name)
                                Column(modifier = Modifier.padding(start = 12.dp)) {
                                    Text(specialist.name, style = MaterialTheme.typography.titleMedium, color = SlotikTextPrimary)
                                    Text(specialist.specialty, style = MaterialTheme.typography.bodyLarge, color = SlotikTextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))
        Text("Настройки", style = MaterialTheme.typography.titleLarge, color = SlotikTextPrimary)
        Spacer(modifier = Modifier.height(12.dp))
        SettingsList(onSignOut = onSignOut)
    }
}

@Composable
private fun ProfileTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = if (selected) SlotikPrimary else SlotikTextSecondary,
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (selected) {
            Surface(
                modifier = Modifier.fillMaxWidth(0.8f).height(2.dp),
                color = SlotikPrimary,
            ) {}
        }
    }
}

@Composable
private fun BookingCard(
    booking: Booking,
    onCancelBooking: (String) -> Unit,
) {
    SlotikCard {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(booking.serviceName, style = MaterialTheme.typography.titleMedium, color = SlotikTextPrimary)
                Text(booking.specialistName, style = MaterialTheme.typography.bodyLarge, color = SlotikTextSecondary)
            }
            SlotikInfoChip(
                text = if (booking.status == BookingStatus.CONFIRMED) "Подтверждено" else "Завершено",
                background = if (booking.status == BookingStatus.CONFIRMED) SlotikPrimary else SlotikPrimaryLight,
                contentColor = SlotikSurface,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        SlotikDividerSpacer()
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            Text(
                text = SlotikViewModel.shortDayFormatter.format(booking.date),
                style = MaterialTheme.typography.bodyLarge,
                color = SlotikTextSecondary,
            )
            Text(
                text = SlotikViewModel.timeFormatter.format(booking.startTime),
                style = MaterialTheme.typography.bodyLarge,
                color = SlotikTextSecondary,
            )
        }
        if (booking.status == BookingStatus.CONFIRMED) {
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
        shape = RoundedCornerShape(24.dp),
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
            .padding(horizontal = 16.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(color = SlotikPrimaryLight, shape = RoundedCornerShape(999.dp)) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (danger) SlotikDanger else SlotikTextSecondary,
                    modifier = Modifier.padding(12.dp),
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = if (danger) SlotikDanger else SlotikTextPrimary,
                modifier = Modifier.padding(start = 12.dp),
            )
        }
        if (!danger) {
            Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = SlotikTextSecondary)
        }
    }
}
