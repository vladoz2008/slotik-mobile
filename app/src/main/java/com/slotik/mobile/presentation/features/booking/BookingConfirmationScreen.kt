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
import androidx.compose.material3.Checkbox
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
import com.slotik.mobile.presentation.SlotikViewModel
import com.slotik.mobile.presentation.components.SlotikAvatar
import com.slotik.mobile.presentation.components.SlotikCard
import com.slotik.mobile.presentation.components.SlotikDividerSpacer
import com.slotik.mobile.presentation.components.SlotikPrimaryButton
import com.slotik.mobile.presentation.components.SlotikScreenContainer
import com.slotik.mobile.presentation.components.SlotikTopBar
import com.slotik.mobile.presentation.theme.SlotikPrimary
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

        SlotikCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SlotikAvatar(label = specialist.name)
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(specialist.name, style = MaterialTheme.typography.titleMedium, color = SlotikTextPrimary)
                    Text(
                        "${specialist.specialty}, ${specialist.experienceLabel}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlotikTextSecondary,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SlotikCard(modifier = Modifier.weight(1f)) {
                Text("Дата", style = MaterialTheme.typography.bodyMedium, color = SlotikPrimary)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "${SlotikViewModel.dayFormatter.format(selectedSlot.date)}, ${SlotikViewModel.weekdayFormatter.format(selectedSlot.date).replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.titleMedium,
                    color = SlotikTextPrimary,
                )
            }
            SlotikCard(modifier = Modifier.weight(1f)) {
                Text("Время", style = MaterialTheme.typography.bodyMedium, color = SlotikPrimary)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    SlotikViewModel.timeFormatter.format(selectedSlot.startTime),
                    style = MaterialTheme.typography.titleMedium,
                    color = SlotikTextPrimary,
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))
        SlotikCard {
            Text("Детали оплаты", style = MaterialTheme.typography.titleMedium, color = SlotikTextPrimary)
            Spacer(modifier = Modifier.height(18.dp))
            PriceLine("Первичный прием", "3 500 ₽")
            Spacer(modifier = Modifier.height(12.dp))
            PriceLine("Скидка (акция)", "-500 ₽", highlight = true)
            Spacer(modifier = Modifier.height(16.dp))
            SlotikDividerSpacer()
            Spacer(modifier = Modifier.height(16.dp))
            PriceLine("Итого к оплате", "3 000 ₽", emphasize = true)
        }

        Spacer(modifier = Modifier.height(18.dp))
        OutlinedTextField(
            value = comment,
            onValueChange = onCommentChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Комментарий к записи (необязательно)") },
            placeholder = { Text("Опишите ваши симптомы или пожелания...") },
            minLines = 4,
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.Top) {
            Checkbox(
                checked = consent,
                onCheckedChange = onConsentChange,
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

        Spacer(modifier = Modifier.height(18.dp))
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
            color = SlotikTextPrimary,
        )
        Text(
            text = value,
            style = if (emphasize) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.bodyLarge,
            color = if (highlight) SlotikPrimary else SlotikTextPrimary,
        )
    }
}
