package com.slotik.mobile.presentation.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SportsGymnastics
import androidx.compose.material.icons.rounded.Star
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.slotik.mobile.R
import com.slotik.mobile.domain.model.Booking
import com.slotik.mobile.domain.model.CategoryAccent
import com.slotik.mobile.domain.model.ServiceCategory
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.presentation.util.DateTimeFormatters
import com.slotik.mobile.presentation.components.SlotikAvatar
import com.slotik.mobile.presentation.components.SlotikBottomBar
import com.slotik.mobile.presentation.components.SlotikCard
import com.slotik.mobile.presentation.components.SlotikInfoChip
import com.slotik.mobile.presentation.components.SlotikScreenContainer
import com.slotik.mobile.presentation.components.SlotikSectionTitle
import com.slotik.mobile.presentation.navigation.SlotikDestination
import com.slotik.mobile.presentation.theme.SlotikBackground
import com.slotik.mobile.presentation.theme.SlotikDivider
import com.slotik.mobile.presentation.theme.SlotikPink
import com.slotik.mobile.presentation.theme.SlotikPrimary
import com.slotik.mobile.presentation.theme.SlotikPrimaryDark
import com.slotik.mobile.presentation.theme.SlotikPrimaryLight
import com.slotik.mobile.presentation.theme.SlotikSky
import com.slotik.mobile.presentation.theme.SlotikStone
import com.slotik.mobile.presentation.theme.SlotikSurface
import com.slotik.mobile.presentation.theme.SlotikTextPrimary
import com.slotik.mobile.presentation.theme.SlotikTextSecondary

@Composable
fun HomeScreen(
    categories: List<ServiceCategory>,
    nextBooking: Booking?,
    featuredSpecialists: List<Specialist>,
    onOpenSearch: () -> Unit,
    onOpenSpecialist: (String) -> Unit,
    onOpenBookingDetails: () -> Unit,
    onOpenCategory: (String) -> Unit,
    onNavigate: (SlotikDestination) -> Unit,
    userFirstName: String,
) {
    SlotikScreenContainer(
        bottomBar = {
            SlotikBottomBar(
                currentDestination = SlotikDestination.HOME,
                onNavigate = onNavigate,
            )
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_slotik_logo),
                contentDescription = "Слотик",
                modifier = Modifier.size(34.dp),
            )
            Text(
                text = "Слотик",
                style = MaterialTheme.typography.titleLarge,
                color = SlotikPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp),
            )
        }

        // Шапка с приветствием
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = "Добрый день,",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SlotikTextSecondary,
                )
                Text(
                    text = userFirstName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = SlotikTextPrimary,
                    fontWeight = FontWeight.Bold,
                )
            }
            SlotikAvatar(label = userFirstName.ifBlank { "?" }, accent = SlotikSky)
        }
        Spacer(modifier = Modifier.height(18.dp))

        // Строка поиска
        Surface(
            color = SlotikSurface,
            shape = RoundedCornerShape(18.dp),
            shadowElevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpenSearch),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = null,
                    tint = SlotikPrimary,
                    modifier = Modifier.size(22.dp),
                )
                Text(
                    text = "Найти услугу или врача",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SlotikTextSecondary,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
        SlotikSectionTitle(title = "Категории")
        Spacer(modifier = Modifier.height(14.dp))
        CategoryGrid(categories = categories, onCategoryClick = onOpenCategory)
        Spacer(modifier = Modifier.height(28.dp))

        SlotikSectionTitle(title = "Ближайшая запись")
        Spacer(modifier = Modifier.height(14.dp))
        UpcomingBookingCard(
            booking = nextBooking,
            onDetailsClick = onOpenBookingDetails,
        )

        Spacer(modifier = Modifier.height(28.dp))
        SlotikSectionTitle(title = "Рекомендуем вам", actionLabel = "Все", onActionClick = onOpenSearch)
        Spacer(modifier = Modifier.height(14.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(featuredSpecialists, key = { it.id }) { specialist ->
                FeaturedSpecialistCard(
                    specialist = specialist,
                    onClick = { onOpenSpecialist(specialist.id) },
                )
            }
        }
    }
}

@Composable
private fun CategoryGrid(
    categories: List<ServiceCategory>,
    onCategoryClick: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        categories.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { category ->
                    val (background, icon) = when (category.accent) {
                        CategoryAccent.SKY -> SlotikSky to Icons.Rounded.Psychology
                        CategoryAccent.PINK -> SlotikPink to Icons.Rounded.Favorite
                        CategoryAccent.BLUE -> SlotikSky.copy(alpha = 0.7f) to Icons.Rounded.MedicalServices
                        CategoryAccent.STONE -> SlotikStone to Icons.Rounded.SportsGymnastics
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(24.dp),
                                ambientColor = Color.Black.copy(alpha = 0.05f),
                                spotColor = Color.Black.copy(alpha = 0.05f),
                            )
                            .clip(RoundedCornerShape(24.dp))
                            .background(background)
                            .clickable { onCategoryClick(category.id) },
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Surface(
                                shape = CircleShape,
                                color = SlotikSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(48.dp),
                                shadowElevation = 1.dp,
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        icon,
                                        contentDescription = null,
                                        tint = SlotikPrimary,
                                        modifier = Modifier.size(26.dp),
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                category.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = SlotikTextPrimary,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                category.subtitle,
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

@Composable
private fun UpcomingBookingCard(
    booking: Booking?,
    onDetailsClick: () -> Unit,
) {
    if (booking == null) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = SlotikSurface,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Пока нет записей",
                    style = MaterialTheme.typography.titleMedium,
                    color = SlotikTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Зайдите в каталог специалистов и выберите удобный слот.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlotikTextSecondary,
                )
            }
        }
        return
    }

    val gradient = Brush.linearGradient(
        colors = listOf(SlotikPrimary, SlotikPrimaryDark),
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = SlotikPrimary.copy(alpha = 0.3f),
                spotColor = SlotikPrimary.copy(alpha = 0.4f),
            )
            .clip(RoundedCornerShape(24.dp))
            .background(gradient),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SlotikAvatar(
                        label = booking.specialistName,
                        accent = Color.White.copy(alpha = 0.20f),
                    )
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            booking.specialistName,
                            style = MaterialTheme.typography.titleMedium,
                            color = SlotikSurface,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            booking.serviceName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SlotikSurface.copy(alpha = 0.80f),
                        )
                    }
                }
                SlotikInfoChip(
                    text = DateTimeFormatters.timeFormatter.format(booking.startTime),
                    background = Color.White.copy(alpha = 0.18f),
                    contentColor = SlotikSurface,
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.18f)),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "Завтра, ${DateTimeFormatters.dayFormatter.format(booking.date)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = SlotikSurface,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = booking.specialistSubtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SlotikSurface.copy(alpha = 0.75f),
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.18f),
                    modifier = Modifier.clickable(onClick = onDetailsClick),
                ) {
                    Text(
                        text = "Детали →",
                        style = MaterialTheme.typography.labelLarge,
                        color = SlotikSurface,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedSpecialistCard(
    specialist: Specialist,
    onClick: () -> Unit,
) {
    SlotikCard(
        modifier = Modifier
            .size(width = 220.dp, height = 130.dp)
            .clickable(onClick = onClick),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            SlotikAvatar(label = specialist.name, accent = SlotikSky)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFF8E1),
                        shape = RoundedCornerShape(999.dp),
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Icon(
                    Icons.Rounded.Star,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = specialist.rating.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF92400E),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 3.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
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
        Spacer(modifier = Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            SlotikInfoChip(text = "от ${specialist.priceFrom} ₽")
            SlotikInfoChip(
                text = specialist.location,
                background = SlotikBackground,
                contentColor = SlotikTextSecondary,
            )
        }
    }
}
