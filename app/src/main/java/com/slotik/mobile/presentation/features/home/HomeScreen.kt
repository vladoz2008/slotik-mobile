package com.slotik.mobile.presentation.features.home

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.slotik.mobile.domain.model.Booking
import com.slotik.mobile.domain.model.CategoryAccent
import com.slotik.mobile.domain.model.ServiceCategory
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.presentation.SlotikViewModel
import com.slotik.mobile.presentation.components.SlotikAvatar
import com.slotik.mobile.presentation.components.SlotikBottomBar
import com.slotik.mobile.presentation.components.SlotikCard
import com.slotik.mobile.presentation.components.SlotikInfoChip
import com.slotik.mobile.presentation.components.SlotikScreenContainer
import com.slotik.mobile.presentation.components.SlotikSectionTitle
import com.slotik.mobile.presentation.navigation.SlotikDestination
import com.slotik.mobile.presentation.theme.SlotikBackground
import com.slotik.mobile.presentation.theme.SlotikPink
import com.slotik.mobile.presentation.theme.SlotikPrimary
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
    onNavigate: (SlotikDestination) -> Unit,
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text("Добрый день,", style = MaterialTheme.typography.headlineMedium, color = SlotikPrimary)
                Text("Алексей", style = MaterialTheme.typography.titleLarge, color = SlotikTextPrimary)
            }
            SlotikAvatar(label = "А", accent = SlotikSky)
        }
        Spacer(modifier = Modifier.height(18.dp))

        Surface(
            color = SlotikSurface,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onOpenSearch),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Rounded.Search, contentDescription = null, tint = SlotikTextSecondary)
                Text(
                    text = "Найти услугу или врача",
                    style = MaterialTheme.typography.bodyLarge,
                    color = SlotikTextSecondary,
                    modifier = Modifier.padding(start = 12.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))
        CategoryGrid(categories = categories, onClick = onOpenSearch)
        Spacer(modifier = Modifier.height(28.dp))

        SlotikSectionTitle(title = "Ближайшие записи")
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
    onClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        categories.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { category ->
                    val (background, icon) = when (category.accent) {
                        CategoryAccent.SKY -> SlotikSky to Icons.Rounded.Psychology
                        CategoryAccent.PINK -> SlotikPink to Icons.Rounded.Favorite
                        CategoryAccent.BLUE -> SlotikSky.copy(alpha = 0.8f) to Icons.Rounded.MedicalServices
                        CategoryAccent.STONE -> SlotikStone to Icons.Rounded.SportsGymnastics
                    }
                    Surface(
                        color = background,
                        shape = RoundedCornerShape(26.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable(onClick = onClick),
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Surface(
                                shape = CircleShape,
                                color = SlotikSurface.copy(alpha = 0.5f),
                                modifier = Modifier.size(46.dp),
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(icon, contentDescription = null, tint = SlotikPrimary)
                                }
                            }
                            Spacer(modifier = Modifier.height(18.dp))
                            Text(category.title, style = MaterialTheme.typography.titleMedium, color = SlotikTextPrimary)
                            Text(category.subtitle, style = MaterialTheme.typography.bodyMedium, color = SlotikTextSecondary)
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
    val gradient = Brush.linearGradient(listOf(SlotikPrimary, SlotikPrimary.copy(alpha = 0.75f)))

    if (booking == null) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = SlotikSurface,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Пока нет записей", style = MaterialTheme.typography.titleMedium, color = SlotikTextPrimary)
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

    Surface(
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .background(gradient)
                .padding(20.dp),
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SlotikAvatar(label = booking.specialistName, accent = Color.White.copy(alpha = 0.18f))
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(booking.specialistName, style = MaterialTheme.typography.titleMedium, color = SlotikSurface)
                        Text(booking.serviceName, style = MaterialTheme.typography.bodyMedium, color = SlotikSurface.copy(alpha = 0.82f))
                    }
                }
                SlotikInfoChip(
                    text = SlotikViewModel.timeFormatter.format(booking.startTime),
                    background = Color.White.copy(alpha = 0.16f),
                    contentColor = SlotikSurface,
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Завтра, ${SlotikViewModel.dayFormatter.format(booking.date)}",
                style = MaterialTheme.typography.bodyLarge,
                color = SlotikSurface,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = booking.specialistSubtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = SlotikSurface.copy(alpha = 0.8f),
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Детали",
                style = MaterialTheme.typography.labelLarge,
                color = SlotikSurface,
                modifier = Modifier.clickable(onClick = onDetailsClick),
            )
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
            .size(width = 236.dp, height = 124.dp)
            .clickable(onClick = onClick),
    ) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceBetween) {
            SlotikAvatar(label = specialist.name, accent = SlotikSky)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Favorite, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(16.dp))
                Text(
                    text = specialist.rating.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SlotikTextPrimary,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(specialist.name, style = MaterialTheme.typography.titleMedium, color = SlotikTextPrimary)
        Text(specialist.specialty, style = MaterialTheme.typography.bodyMedium, color = SlotikTextSecondary)
        Spacer(modifier = Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SlotikInfoChip(text = "От ${specialist.priceFrom} ₽")
            SlotikInfoChip(text = specialist.location, background = SlotikBackground, contentColor = SlotikTextSecondary)
        }
    }
}
