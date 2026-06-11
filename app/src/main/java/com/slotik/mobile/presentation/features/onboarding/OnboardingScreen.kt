package com.slotik.mobile.presentation.features.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.People
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.slotik.mobile.presentation.components.SlotikPrimaryButton
import com.slotik.mobile.presentation.theme.SlotikBackground
import com.slotik.mobile.presentation.theme.SlotikPrimary
import com.slotik.mobile.presentation.theme.SlotikPrimaryLight
import com.slotik.mobile.presentation.theme.SlotikSurface
import com.slotik.mobile.presentation.theme.SlotikTextPrimary
import com.slotik.mobile.presentation.theme.SlotikTextSecondary

@Composable
fun OnboardingScreen(
    onContinue: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(SlotikBackground)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Surface(
            shape = RoundedCornerShape(32.dp),
            color = SlotikSurface,
            shadowElevation = 16.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .height(320.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(SlotikPrimaryLight, SlotikSurface),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Surface(
                    color = SlotikPrimary.copy(alpha = 0.12f),
                    shape = CircleShape,
                    modifier = Modifier.size(164.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.People,
                            contentDescription = null,
                            tint = SlotikPrimary,
                            modifier = Modifier.size(82.dp),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Быстрая запись\nк специалистам",
            style = MaterialTheme.typography.displaySmall,
            color = SlotikTextPrimary,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Забронируйте удобное время всего в\nнесколько кликов без лишних звонков.",
            style = MaterialTheme.typography.bodyLarge,
            color = SlotikTextSecondary,
        )
        Spacer(modifier = Modifier.height(28.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(width = 32.dp, height = 8.dp)
                    .background(SlotikPrimary, RoundedCornerShape(999.dp)),
            )
            repeat(2) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(SlotikPrimaryLight, CircleShape),
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        SlotikPrimaryButton(
            text = "Далее",
            onClick = onContinue,
        )
    }
}
