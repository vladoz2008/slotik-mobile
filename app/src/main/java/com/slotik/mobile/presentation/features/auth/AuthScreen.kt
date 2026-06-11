package com.slotik.mobile.presentation.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.slotik.mobile.domain.model.AuthMode
import com.slotik.mobile.presentation.components.SlotikPrimaryButton
import com.slotik.mobile.presentation.theme.SlotikBackground
import com.slotik.mobile.presentation.theme.SlotikDivider
import com.slotik.mobile.presentation.theme.SlotikPrimary
import com.slotik.mobile.presentation.theme.SlotikPrimaryLight
import com.slotik.mobile.presentation.theme.SlotikSurface
import com.slotik.mobile.presentation.theme.SlotikTextPrimary
import com.slotik.mobile.presentation.theme.SlotikTextSecondary

@Composable
fun AuthScreen(
    authMode: AuthMode,
    email: String,
    password: String,
    canSubmit: Boolean,
    onAuthModeChange: (AuthMode) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(SlotikBackground)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 40.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SlotikPrimary,
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = null,
                tint = SlotikSurface,
                modifier = Modifier.padding(14.dp),
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Добро пожаловать",
            style = MaterialTheme.typography.headlineMedium,
            color = SlotikTextPrimary,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Войдите, чтобы продолжить",
            style = MaterialTheme.typography.bodyLarge,
            color = SlotikTextSecondary,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        Spacer(modifier = Modifier.height(28.dp))
        Surface(
            color = SlotikDivider,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(modifier = Modifier.padding(4.dp)) {
                AuthModeChip(
                    text = "Войти",
                    selected = authMode == AuthMode.LOGIN,
                    onClick = { onAuthModeChange(AuthMode.LOGIN) },
                    modifier = Modifier.weight(1f),
                )
                AuthModeChip(
                    text = "Регистрация",
                    selected = authMode == AuthMode.REGISTER,
                    onClick = { onAuthModeChange(AuthMode.REGISTER) },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email или телефон") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
        )
        Spacer(modifier = Modifier.height(14.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
            trailingIcon = { Icon(Icons.Rounded.VisibilityOff, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Пароль") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Забыли пароль?",
            color = SlotikPrimary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.End),
        )
        Spacer(modifier = Modifier.height(18.dp))
        SlotikPrimaryButton(
            text = if (authMode == AuthMode.LOGIN) "Войти" else "Создать аккаунт",
            enabled = canSubmit,
            onClick = onSubmit,
        )

        Spacer(modifier = Modifier.height(36.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.weight(1f).height(1.dp),
                color = SlotikDivider,
            ) {}
            Text(
                text = "Или войдите через",
                style = MaterialTheme.typography.bodyMedium,
                color = SlotikTextSecondary,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Surface(
                modifier = Modifier.weight(1f).height(1.dp),
                color = SlotikDivider,
            ) {}
        }
        Spacer(modifier = Modifier.height(18.dp))
        SocialButton("Продолжить с Google", background = Color.White)
        Spacer(modifier = Modifier.height(12.dp))
        SocialButton("Продолжить с Apple", background = Color.White, useAppleIcon = true)
    }
}

@Composable
private fun AuthModeChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = if (selected) SlotikSurface else Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = if (selected) 4.dp else 0.dp,
        modifier = modifier.clickable(onClick = onClick),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = if (selected) SlotikPrimary else SlotikTextSecondary,
            modifier = Modifier.padding(vertical = 14.dp),
        )
    }
}

@Composable
private fun SocialButton(
    text: String,
    background: Color,
    useAppleIcon: Boolean = false,
) {
    Surface(
        color = background,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (useAppleIcon) {
                Text(
                    text = "A",
                    style = MaterialTheme.typography.titleMedium,
                    color = SlotikTextPrimary,
                )
            } else {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = SlotikPrimaryLight,
                    modifier = Modifier.size(24.dp),
                ) {}
            }
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = SlotikTextPrimary,
                modifier = Modifier.padding(start = 12.dp),
            )
        }
    }
}
