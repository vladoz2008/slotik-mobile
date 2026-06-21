package com.slotik.mobile.presentation.features.booking

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.slotik.mobile.domain.model.AvailabilitySlot
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.presentation.util.DateTimeFormatters
import com.slotik.mobile.presentation.components.SlotikAvatar
import com.slotik.mobile.presentation.components.SlotikCard
import com.slotik.mobile.presentation.components.SlotikDividerSpacer
import com.slotik.mobile.presentation.components.SlotikPrimaryButton
import com.slotik.mobile.presentation.components.SlotikScreenContainer
import com.slotik.mobile.presentation.components.SlotikTopBar
import com.slotik.mobile.presentation.theme.SlotikBackground
import com.slotik.mobile.presentation.theme.SlotikPrimary
import com.slotik.mobile.presentation.theme.SlotikPrimaryLight
import com.slotik.mobile.presentation.theme.SlotikSky
import com.slotik.mobile.presentation.theme.SlotikSuccess
import com.slotik.mobile.presentation.theme.SlotikSuccessLight
import com.slotik.mobile.presentation.theme.SlotikTextPrimary
import com.slotik.mobile.presentation.theme.SlotikTextSecondary

@Composable
fun BookingConfirmationScreen(
    specialist: Specialist?,
    selectedSlot: AvailabilitySlot?,
    comment: String,
    consent: Boolean,
    canConfirm: Boolean,
    onBack: () -> Unit,
    onCommentChange: (String) -> Unit,
    onConsentChange: (Boolean) -> Unit,
    onConfirm: () -> Unit,
) {
    if (specialist == null || selectedSlot == null) return

    SlotikScreenContainer {
        SlotikTopBar(title = "Подтвердить запись", showBack = true, onBack = onBack)

        // Карточка специалиста
        SlotikCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SlotikAvatar(label = specialist.name, accent = SlotikSky)
                Column(modifier = Modifier.padding(start = 14.dp)) {
                    Text(
                        specialist.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = SlotikTextPrimary,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        "${specialist.specialty}, ${specialist.experienceLabel}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlotikTextSecondary,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Дата и время
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SlotikCard(modifier = Modifier.weight(1f)) {
                Text(
                    "Дата",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlotikPrimary,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "${DateTimeFormatters.dayFormatter.format(selectedSlot.date)}, ${DateTimeFormatters.weekdayFormatter.format(selectedSlot.date).replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.titleMedium,
                    color = SlotikTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            SlotikCard(modifier = Modifier.weight(1f)) {
                Text(
                    "Время",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlotikPrimary,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    DateTimeFormatters.timeFormatter.format(selectedSlot.startTime),
                    style = MaterialTheme.typography.titleMedium,
                    color = SlotikTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Детали оплаты
        SlotikCard {
            Text(
                "Детали оплаты",
                style = MaterialTheme.typography.titleMedium,
                color = SlotikTextPrimary,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            PriceLine("Первичный прием", "3 500 ₽")
            Spacer(modifier = Modifier.height(10.dp))
            PriceLine("Скидка (акция)", "−500 ₽", highlight = true)
            Spacer(modifier = Modifier.height(14.dp))
            SlotikDividerSpacer()
            Spacer(modifier = Modifier.height(14.dp))
            PriceLine("Итого к оплате", "3 000 ₽", emphasize = true)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Комментарий
        OutlinedTextField(
            value = comment,
            onValueChange = onCommentChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Комментарий к записи (необязательно)") },
            placeholder = { Text("Опишите ваши симптомы или пожелания...") },
            minLines = 3,
            shape = RoundedCornerShape(16.dp),
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Согласие
        Row(verticalAlignment = Alignment.Top) {
            Checkbox(
                checked = consent,
                onCheckedChange = onConsentChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = SlotikPrimary,
                    uncheckedColor = SlotikTextSecondary.copy(alpha = 0.5f),
                ),
            )
            Text(
                text = buildAnnotatedString {
                    append("Я согласен с правилами отмены и ")
                    withStyle(SpanStyle(color = SlotikPrimary, fontWeight = FontWeight.SemiBold)) {
                        append("политикой")
                    }
                    append(" обработки персональных данных.")
                },
                style = MaterialTheme.typography.bodyMedium,
                color = SlotikTextSecondary,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clickable { onConsentChange(!consent) },
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        SlotikPrimaryButton(
            text = "Подтвердить запись",
            enabled = canConfirm,
            onClick = onConfirm,
        )
    }
}

@Composable
private fun PriceLine(
    label: String,
    value: String,
    highlight: Boolean = false,
    emphasize: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = if (emphasize) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            color = if (emphasize) SlotikTextPrimary else SlotikTextSecondary,
            fontWeight = if (emphasize) FontWeight.SemiBold else FontWeight.Normal,
        )
        Text(
            text = value,
            style = if (emphasize) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge,
            color = when {
                emphasize -> SlotikPrimary
                highlight -> SlotikSuccess
                else -> SlotikTextPrimary
            },
            fontWeight = if (emphasize) FontWeight.Bold else FontWeight.Normal,
        )
    }
}
