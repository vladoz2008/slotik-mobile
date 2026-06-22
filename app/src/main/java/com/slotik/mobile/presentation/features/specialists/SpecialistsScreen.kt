package com.slotik.mobile.presentation.features.specialists

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.presentation.components.SlotikAvatar
import com.slotik.mobile.presentation.components.SlotikBottomBar
import com.slotik.mobile.presentation.components.SlotikDividerSpacer
import com.slotik.mobile.presentation.components.SlotikInfoChip
import com.slotik.mobile.presentation.components.SlotikPrimaryButton
import com.slotik.mobile.presentation.components.SlotikScreenContainer
import com.slotik.mobile.presentation.components.SlotikTopBar
import com.slotik.mobile.presentation.navigation.SlotikDestination
import com.slotik.mobile.presentation.theme.SlotikBackground
import com.slotik.mobile.presentation.theme.SlotikDivider
import com.slotik.mobile.presentation.theme.SlotikPrimary
import com.slotik.mobile.presentation.theme.SlotikPrimaryLight
import com.slotik.mobile.presentation.theme.SlotikSky
import com.slotik.mobile.presentation.theme.SlotikSurface
import com.slotik.mobile.presentation.theme.SlotikTextPrimary
import com.slotik.mobile.presentation.theme.SlotikTextSecondary

@Composable
fun SpecialistsScreen(
    searchQuery: String,
    specialists: List<Specialist>,
    favoriteIds: Set<String>,
    onSearchChange: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onSpecialistClick: (String) -> Unit,
    onNavigate: (SlotikDestination) -> Unit,
) {
    SlotikScreenContainer(
        bottomBar = {
            SlotikBottomBar(
                currentDestination = SlotikDestination.SPECIALISTS,
                onNavigate = onNavigate,
            )
        },
    ) {
        SlotikTopBar(title = "Специалисты")
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            leadingIcon = {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = null,
                    tint = SlotikPrimary,
                )
            },
            placeholder = { Text("Поиск специалиста или услуги...") },
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(14.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SlotikInfoChip(text = "Все фильтры", background = SlotikBackground, contentColor = SlotikTextSecondary)
            SlotikInfoChip(text = "Сегодня")
            SlotikInfoChip(text = "Цена")
            SlotikInfoChip(text = "Рейтинг")
        }
        Spacer(modifier = Modifier.height(18.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            specialists.forEach { specialist ->
                SpecialistCard(
                    specialist = specialist,
                    isFavorite = specialist.id in favoriteIds,
                    onFavoriteClick = { onFavoriteClick(specialist.id) },
                    onBookClick = { onSpecialistClick(specialist.id) },
                )
            }
        }
    }
}

@Composable
fun EmptySpecialistsScreen(
    onBackToSearch: () -> Unit,
    onResetFilters: () -> Unit,
    onNavigate: (SlotikDestination) -> Unit,
) {
    SlotikScreenContainer(
        bottomBar = {
            SlotikBottomBar(
                currentDestination = SlotikDestination.SPECIALISTS,
                onNavigate = onNavigate,
            )
        },
    ) {
        SlotikTopBar(title = "Специалисты", showBack = true, onBack = onBackToSearch)
        Spacer(modifier = Modifier.height(80.dp))
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(140.dp)
                .background(SlotikPrimaryLight, RoundedCornerShape(999.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Rounded.Search,
                contentDescription = null,
                tint = SlotikPrimary,
                modifier = Modifier.size(60.dp),
            )
        }
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "Ничего не найдено",
            style = MaterialTheme.typography.headlineLarge,
            color = SlotikTextPrimary,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "По вашему запросу нет подходящих\nспециалистов. Попробуйте изменить\nпараметры поиска.",
            style = MaterialTheme.typography.bodyLarge,
            color = SlotikTextSecondary,
        )
        Spacer(modifier = Modifier.height(28.dp))
        SlotikPrimaryButton(text = "Найти специалистов", onClick = onBackToSearch)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Сбросить фильтры",
            style = MaterialTheme.typography.labelLarge,
            color = SlotikPrimary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable(onClick = onResetFilters),
        )
    }
}

@Composable
private fun SpecialistCard(
    specialist: Specialist,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onBookClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = SlotikSurface,
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    SlotikAvatar(label = specialist.name, accent = SlotikSky)
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = specialist.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = SlotikTextPrimary,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = specialist.specialty,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SlotikTextSecondary,
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFFFF8E1),
                                    shape = RoundedCornerShape(999.dp),
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp),
                        ) {
                            Icon(
                                Icons.Rounded.Star,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(14.dp),
                            )
                            Text(
                                text = "${specialist.rating} (${specialist.reviewsCount})",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF92400E),
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(start = 4.dp),
                            )
                        }
                    }
                }
                Icon(
                    imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isFavorite) Color(0xFFB42318) else SlotikTextSecondary.copy(alpha = 0.5f),
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(24.dp)
                        .clickable(onClick = onFavoriteClick),
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            SlotikDividerSpacer()
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column {
                    Text(
                        text = "от ${specialist.priceFrom} ₽",
                        style = MaterialTheme.typography.titleMedium,
                        color = SlotikPrimary,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "за приём",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlotikTextSecondary,
                    )
                }
                SlotikPrimaryButton(
                    text = "Записаться",
                    modifier = Modifier.fillMaxWidth(0.42f),
                    onClick = onBookClick,
                )
            }
        }
    }
}
