package com.slotik.mobile.presentation.features.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.slotik.mobile.domain.model.AvailabilitySlot
import com.slotik.mobile.domain.model.SlotPeriod
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.presentation.SlotikViewModel
import com.slotik.mobile.presentation.components.SlotikAvatar
import com.slotik.mobile.presentation.components.SlotikPrimaryButton
import com.slotik.mobile.presentation.components.SlotikScreenContainer
import com.slotik.mobile.presentation.components.SlotikTopBar
import com.slotik.mobile.presentation.theme.SlotikPrimary
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

    SlotikScreenContainer {
        SlotikTopBar(title = "Специалисты", showBack = true, onBack = onBack)

        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SlotikSurface,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SlotikAvatar(label = specialist.name)
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(specialist.name, style = MaterialTheme.typography.titleMedium, color = SlotikTextPrimary)
                    Text(specialist.specialty, style = MaterialTheme.typography.bodyLarge, color = SlotikTextSecondary)
                    Text(
                        text = "${specialist.rating} (${specialist.reviewsCount} отзывов)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlotikPrimary,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Выберите дату", style = MaterialTheme.typography.titleLarge, color = SlotikTextPrimary)
            Text("Октябрь 2026", style = MaterialTheme.typography.titleMedium, color = SlotikPrimary)
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(slots.distinctBy { it.date }, key = { it.date.toString() }) { slot ->
                val isSelected = selectedSlot?.date == slot.date
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = if (isSelected) SlotikPrimary else SlotikSurface,
                    modifier = Modifier.width(72.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = SlotikViewModel.weekdayFormatter.format(slot.date).uppercase(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSelected) SlotikSurface else SlotikTextSecondary,
                        )
                        Text(
                            text = slot.date.dayOfMonth.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = if (isSelected) SlotikSurface else SlotikTextPrimary,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        listOf(
            SlotPeriod.MORNING to "Утро",
            SlotPeriod.DAY to "День",
            SlotPeriod.EVENING to "Вечер",
        ).forEach { (period, title) ->
            val periodSlots = slots.filter { it.period == period }
            if (periodSlots.isEmpty()) return@forEach
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = when (period) {
                        SlotPeriod.MORNING -> Icons.Rounded.WbSunny
                        SlotPeriod.DAY -> Icons.Rounded.AccessTime
                        SlotPeriod.EVENING -> Icons.Rounded.WbTwilight
                    },
                    contentDescription = null,
                    tint = SlotikTextSecondary,
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = SlotikTextSecondary,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            periodSlots.chunked(3).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
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
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(18.dp))
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = SlotikSurface,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text("ИТОГО К ОПЛАТЕ", style = MaterialTheme.typography.bodyMedium, color = SlotikTextSecondary)
                        Text("3 500 ₽", style = MaterialTheme.typography.headlineMedium, color = SlotikTextPrimary)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            selectedSlot?.let { SlotikViewModel.dayFormatter.format(it.date) } ?: "--",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SlotikTextSecondary,
                        )
                        Text(
                            selectedSlot?.let { "${SlotikViewModel.timeFormatter.format(it.startTime)} - ${SlotikViewModel.timeFormatter.format(it.endTime)}" } ?: "--:--",
                            style = MaterialTheme.typography.titleMedium,
                            color = SlotikTextPrimary,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                SlotikPrimaryButton(text = "Записаться", onClick = onContinue)
            }
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
    val background = when {
        slot.isAvailable.not() -> SlotikSurfaceAlt
        isSelected -> SlotikPrimary
        else -> SlotikSurface
    }
    val contentColor = when {
        slot.isAvailable.not() -> SlotikTextSecondary.copy(alpha = 0.5f)
        isSelected -> SlotikSurface
        else -> SlotikTextPrimary
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = background,
        modifier = modifier
            .height(48.dp)
            .clickable(enabled = slot.isAvailable, onClick = onClick),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = SlotikViewModel.timeFormatter.format(slot.startTime),
                style = MaterialTheme.typography.titleMedium,
                color = contentColor,
            )
        }
    }
}
