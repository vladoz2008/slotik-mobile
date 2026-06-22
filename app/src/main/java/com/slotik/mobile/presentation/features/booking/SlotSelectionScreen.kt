package com.slotik.mobile.presentation.features.booking

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.WbTwilight
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
import com.slotik.mobile.domain.model.AvailabilitySlot
import com.slotik.mobile.domain.model.SlotPeriod
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.presentation.util.DateTimeFormatters
import com.slotik.mobile.presentation.components.SlotikAvatar
import com.slotik.mobile.presentation.components.SlotikPrimaryButton
import com.slotik.mobile.presentation.components.SlotikScreenContainer
import com.slotik.mobile.presentation.components.SlotikTopBar
import com.slotik.mobile.presentation.theme.SlotikPrimary
import com.slotik.mobile.presentation.theme.SlotikPrimaryDark
import com.slotik.mobile.presentation.theme.SlotikPrimaryLight
import com.slotik.mobile.presentation.theme.SlotikSky
import com.slotik.mobile.presentation.theme.SlotikSurface
import com.slotik.mobile.presentation.theme.SlotikSurfaceAlt
import com.slotik.mobile.presentation.theme.SlotikTextPrimary
import com.slotik.mobile.presentation.theme.SlotikTextSecondary

@Composable
fun SlotSelectionScreen(
    specialist: Specialist?,
    slots: List<AvailabilitySlot>,
    selectedSlot: AvailabilitySlot?,
    onBack: () -> Unit,
    onSelectSlot: (String) -> Unit,
    onContinue: () -> Unit,
) {
    if (specialist == null) return

    SlotikScreenContainer(
        bottomBar = {
            // Нижняя панель с итогами (закреплена)
            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = SlotikSurface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(
                                "ИТОГО К ОПЛАТЕ",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SlotikTextSecondary,
                            )
                            Text(
                                "3 500 ₽",
                                style = MaterialTheme.typography.headlineMedium,
                                color = SlotikTextPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                selectedSlot?.let { DateTimeFormatters.dayFormatter.format(it.date) } ?: "--",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SlotikTextSecondary,
                            )
                            Text(
                                selectedSlot?.let {
                                    "${DateTimeFormatters.timeFormatter.format(it.startTime)} – ${DateTimeFormatters.timeFormatter.format(it.endTime)}"
                                } ?: "--:--",
                                style = MaterialTheme.typography.titleMedium,
                                color = SlotikTextPrimary,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    SlotikPrimaryButton(text = "Записаться", onClick = onContinue)
                }
            }
        },
    ) {
        SlotikTopBar(title = "Выбор времени", showBack = true, onBack = onBack)

        // Карточка специалиста
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = SlotikSurface,
            shadowElevation = 3.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
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
                    Text(
                        text = "${specialist.rating} ★ (${specialist.reviewsCount} отзывов)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlotikPrimary,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Выберите дату",
                style = MaterialTheme.typography.titleLarge,
                color = SlotikTextPrimary,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                "Октябрь 2026",
                style = MaterialTheme.typography.titleMedium,
                color = SlotikPrimary,
                fontWeight = FontWeight.Medium,
            )
        }
        Spacer(modifier = Modifier.height(14.dp))

        // Дни
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(slots.distinctBy { it.date }, key = { it.date.toString() }) { slot ->
                val isSelected = selectedSlot?.date == slot.date
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .width(72.dp)
                            .shadow(
                                elevation = 5.dp,
                                shape = RoundedCornerShape(18.dp),
                                ambientColor = SlotikPrimary.copy(alpha = 0.28f),
                                spotColor = SlotikPrimary.copy(alpha = 0.35f),
                            )
                            .clip(RoundedCornerShape(18.dp))
                            .background(
                                Brush.linearGradient(listOf(SlotikPrimary, SlotikPrimaryDark)),
                            ),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 14.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = DateTimeFormatters.weekdayFormatter.format(slot.date).uppercase(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = SlotikSurface.copy(alpha = 0.80f),
                            )
                            Text(
                                text = slot.date.dayOfMonth.toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = SlotikSurface,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = SlotikSurface,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .width(72.dp)
                            .clickable { onSelectSlot(slot.id) },
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = DateTimeFormatters.weekdayFormatter.format(slot.date).uppercase(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = SlotikTextSecondary,
                            )
                            Text(
                                text = slot.date.dayOfMonth.toString(),
                                style = MaterialTheme.typography.headlineMedium,
                                color = SlotikTextPrimary,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))
        listOf(
            SlotPeriod.MORNING to "Утро",
            SlotPeriod.DAY to "День",
            SlotPeriod.EVENING to "Вечер",
        ).forEach { (period, title) ->
            val periodSlots = slots.filter { it.period == period }
            if (periodSlots.isEmpty()) return@forEach
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp),
            ) {
                Surface(
                    color = SlotikPrimaryLight,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.size(30.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = when (period) {
                                SlotPeriod.MORNING -> Icons.Rounded.WbSunny
                                SlotPeriod.DAY -> Icons.Rounded.AccessTime
                                SlotPeriod.EVENING -> Icons.Rounded.WbTwilight
                            },
                            contentDescription = null,
                            tint = SlotikPrimary,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = SlotikTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 10.dp),
                )
            }
            periodSlots.chunked(3).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    row.forEach { slot ->
                        SlotCard(
                            slot = slot,
                            isSelected = selectedSlot?.id == slot.id,
                            modifier = Modifier.weight(1f),
                            onClick = { onSelectSlot(slot.id) },
                        )
                    }
                    repeat(3 - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

    }
}

@Composable
private fun SlotCard(
    slot: AvailabilitySlot,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    when {
        !slot.isAvailable -> {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SlotikSurfaceAlt,
                modifier = modifier.height(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = DateTimeFormatters.timeFormatter.format(slot.startTime),
                        style = MaterialTheme.typography.titleMedium,
                        color = SlotikTextSecondary.copy(alpha = 0.4f),
                    )
                }
            }
        }
        isSelected -> {
            Box(
                modifier = modifier
                    .height(48.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(14.dp),
                        ambientColor = SlotikPrimary.copy(alpha = 0.28f),
                        spotColor = SlotikPrimary.copy(alpha = 0.32f),
                    )
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        Brush.linearGradient(listOf(SlotikPrimary, SlotikPrimaryDark)),
                    )
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = DateTimeFormatters.timeFormatter.format(slot.startTime),
                    style = MaterialTheme.typography.titleMedium,
                    color = SlotikSurface,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
        else -> {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = SlotikSurface,
                shadowElevation = 1.dp,
                modifier = modifier
                    .height(48.dp)
                    .clickable(onClick = onClick),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = DateTimeFormatters.timeFormatter.format(slot.startTime),
                        style = MaterialTheme.typography.titleMedium,
                        color = SlotikTextPrimary,
                    )
                }
            }
        }
    }
}
