package com.slotik.mobile.presentation.features.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.slotik.mobile.R
import com.slotik.mobile.presentation.components.SlotikPrimaryButton
import com.slotik.mobile.presentation.theme.SlotikBackground
import com.slotik.mobile.presentation.theme.SlotikPrimary
import com.slotik.mobile.presentation.theme.SlotikPrimaryDark
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
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Иллюстративная карточка с мягкой тенью и градиентным фоном
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 18.dp,
                    shape = RoundedCornerShape(32.dp),
                    ambientColor = SlotikPrimary.copy(alpha = 0.18f),
                    spotColor = SlotikPrimary.copy(alpha = 0.22f),
                )
                .clip(RoundedCornerShape(32.dp))
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(SlotikPrimaryLight, SlotikSurface),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_slotik_logo),
                contentDescription = "Слотик",
                modifier = Modifier.size(180.dp),
            )
        }

        Spacer(modifier = Modifier.height(44.dp))
        Text(
            text = "Быстрая запись\nк специалистам",
            style = MaterialTheme.typography.displaySmall,
            color = SlotikTextPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Забронируйте удобное время всего в\nнесколько кликов без лишних звонков.",
            style = MaterialTheme.typography.bodyLarge,
            color = SlotikTextSecondary,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Индикатор страниц с градиентной активной точкой
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(width = 28.dp, height = 8.dp)
                    .background(
                        Brush.linearGradient(listOf(SlotikPrimary, SlotikPrimaryDark)),
                        RoundedCornerShape(999.dp),
                    ),
            )
            repeat(2) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(SlotikPrimaryLight, CircleShape),
                )
            }
        }
        Spacer(modifier = Modifier.height(36.dp))
        SlotikPrimaryButton(
            text = "Далее",
            onClick = onContinue,
        )
    }
}
