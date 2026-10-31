package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppScreen
import com.example.data.tr
import com.example.ui.components.SpringBounceButton
import com.example.ui.components.VastramAsyncImage
import com.example.ui.components.WhatsAppDirectButton
import com.example.ui.theme.ErrorContainer
import com.example.ui.theme.OnErrorContainer
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnPrimaryContainer
import com.example.ui.theme.OnPrimaryFixed
import com.example.ui.theme.OnSecondary
import com.example.ui.theme.OnSecondaryContainer
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OnTertiary
import com.example.ui.theme.OnTertiaryContainer
import com.example.ui.theme.OnTertiaryFixed
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.PrimaryFixedDim
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.SecondaryFixedDim
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryContainer
import com.example.ui.theme.TertiaryFixed
import com.example.ui.theme.TertiaryFixedDim

@Composable
fun LoomDeskScreen(
    onNavigate: (AppScreen) -> Unit,
    onToast: (String) -> Unit
) {
    var isPlayingVoice by remember { mutableStateOf(false) }
    var isOrderAccepted by remember { mutableStateOf(false) }

    val newOrdersToast = tr(
        "3 New Orders • ₹44,200",
        "୩ଟି ନୂତନ ଅର୍ଡର • ₹୪୪,୨୦୦",
        "3 नए ऑर्डर • ₹44,200"
    )
    val buyerQueryToast = tr(
        "Query #VST-8821",
        "ଅନୁରୋଧ #VST-8821",
        "प्रश्न #VST-8821"
    )
    val orderAcceptedToast = tr(
        "Order #VST-8821 Accepted!",
        "ଅର୍ଡର #VST-8821 ଗୃହୀତ!",
        "ऑर्डर #VST-8821 स्वीकार किया गया!"
    )
    val orderResetToast = tr(
        "Status Reset",
        "ସ୍ଥିତି ପୁନଃସ୍ଥାପିତ",
        "स्थिति रीसेट"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "loomDeskBounce")
    val bellOffsetY by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(tween(450), RepeatMode.Reverse),
        label = "bellBounce"
    )
    val statusDotScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "statusDot"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("loom_desk_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Weaver Identity Bar + Sunlight Status Chip
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SurfaceContainer,
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(PrimaryFixed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = OnPrimaryFixed,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = tr("Pradipta Meher", "ପ୍ରଦୀପ୍ତ ମେହେର", "प्रदीप्त मेहेर"),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = OnSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Surface(shape = CircleShape, color = TertiaryFixed) {
                                    Text(
                                        text = tr("LOOM #03", "ତନ୍ତ #୦୩", "करघा #03"),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = OnTertiaryFixed,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .scale(statusDotScale)
                                        .clip(CircleShape)
                                        .background(Tertiary)
                                )
                                Text(
                                    text = tr("Nuapatna • Active", "ନୂଆପାଟଣା • ସକ୍ରିୟ", "नुआपाटना • सक्रिय"),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = OnSurfaceVariant
                                )
                            }
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = SurfaceContainerHigh
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = tr("Sunlight", "ସୂର୍ଯ୍ୟାଲୋକ", "सनलाइट"),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // 2x2 Giant Visual Touch Grid (Minimal text, big numbers & icons)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SunlightTouchTile(
                        backgroundColor = Tertiary,
                        iconBoxBg = TertiaryContainer,
                        iconTint = OnTertiaryContainer,
                        icon = Icons.Default.NotificationsActive,
                        iconOffsetY = bellOffsetY,
                        countNumber = tr("3", "୩", "3"),
                        countBadge = "₹44.2K",
                        countColor = TertiaryFixed,
                        subCountColor = TertiaryFixedDim,
                        primaryTitle = tr("New Orders", "ନୂତନ ଅର୍ଡର", "नए ऑर्डर"),
                        subtitle = tr("Direct", "ସିଧାସଳଖ", "सीधे"),
                        subtitleColor = TertiaryFixed,
                        titleColor = OnTertiary,
                        onClick = { onToast(newOrdersToast) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("sunlight_tile_new_orders")
                    )

                    SunlightTouchTile(
                        backgroundColor = Secondary,
                        iconBoxBg = SecondaryContainer,
                        iconTint = OnSecondaryContainer,
                        icon = Icons.Default.Inventory2,
                        iconOffsetY = 0f,
                        countNumber = tr("2", "୨", "2"),
                        countBadge = tr("GI SEAL", "GI ସିଲ୍", "GI सील"),
                        countColor = SecondaryFixed,
                        subCountColor = SecondaryFixedDim,
                        primaryTitle = tr("Ready Ship", "ପଠାଇବା ପ୍ରସ୍ତୁତ", "भेजने को तैयार"),
                        subtitle = tr("Packed", "ପ୍ୟାକ୍", "पैक"),
                        subtitleColor = SecondaryFixed,
                        titleColor = OnSecondary,
                        onClick = { onNavigate(AppScreen.CLUSTER_MANAGER) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("sunlight_tile_ready_ship")
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SunlightTouchTile(
                        backgroundColor = SurfaceContainerHighest,
                        iconBoxBg = PrimaryFixed,
                        iconTint = Primary,
                        icon = Icons.Default.LocalShipping,
                        iconOffsetY = 0f,
                        countNumber = tr("4", "୪", "4"),
                        countBadge = tr("LIVE", "ଲାଇଭ୍", "लाइव"),
                        countColor = Primary,
                        subCountColor = OnSurfaceVariant,
                        primaryTitle = tr("In Transit", "ରାସ୍ତାରେ", "रास्ते में"),
                        subtitle = tr("Speed Post", "ସ୍ପିଡ୍ ପୋଷ୍ଟ", "स्पीड पोस्ट"),
                        subtitleColor = OnSurfaceVariant,
                        titleColor = OnSurface,
                        onClick = { onNavigate(AppScreen.ORDER_TRACKING) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("sunlight_tile_in_transit")
                    )

                    SunlightTouchTile(
                        backgroundColor = PrimaryContainer,
                        iconBoxBg = ErrorContainer,
                        iconTint = OnErrorContainer,
                        icon = Icons.Default.HelpCenter,
                        iconOffsetY = 0f,
                        countNumber = tr("1", "୧", "1"),
                        countBadge = tr("AUDIO", "ଅଡିଓ", "ऑडियो"),
                        countColor = PrimaryFixed,
                        subCountColor = PrimaryFixedDim,
                        primaryTitle = tr("Buyer Note", "ଗ୍ରାହକ ବାର୍ତ୍ତା", "ग्राहक नोट"),
                        subtitle = tr("Voice", "ସ୍ୱର", "आवाज़"),
                        subtitleColor = PrimaryFixed,
                        titleColor = OnPrimaryContainer,
                        onClick = { onToast(buyerQueryToast) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("sunlight_tile_buyer_query")
                    )
                }
            }
        }

        // Active Order Spotlight Card
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SurfaceContainer,
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.45f)),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = tr("Active Order", "ସକ୍ରିୟ ଅର୍ଡର", "सक्रिय ऑर्डर"),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface
                            )
                        }
                        Surface(shape = CircleShape, color = SurfaceContainerHigh) {
                            Text(
                                text = "#VST-8821",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Buyer & Saree Visual Row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceContainerHigh)
                        ) {
                            VastramAsyncImage(
                                url = "https://lh3.googleusercontent.com/aida-public/AB6AXuDOEYNEwcL2pVLbOR0eSmZystfAePWeKnN-uUbHGkssGVxI9KGEzheRs1DnYWcrVupvK5M_lRxqtUcQcFwtQnbFWx7t97r6NAfkf0VW4kDFLKNRbs01mCjKRrw3yFMImROZ8X4GHGze69p49C4aTOWhoqQlhI3ex5_MbnM3t2hUZYW3AERNgP2UrmuvT2oLO3MKXXxpkMly14Iduo-gVvy3_QMuaWlOtCTiiyp8kAJt",
                                contentDescription = tr("Ikat Silk", "ଇକତ ପାଟ", "इकत सिल्क"),
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = tr("Ananya Sen", "ଅନନ୍ୟା ସେନ୍", "अनन्या सेन"),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = OnSurface,
                                    maxLines = 1
                                )
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = Tertiary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = tr("Mumbai • Ikat Silk", "ମୁମ୍ବାଇ • ଇକତ ପାଟ", "मुंबई • इकत सिल्क"),
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "₹14,800",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Primary
                                )
                                Surface(shape = RoundedCornerShape(6.dp), color = TertiaryFixed) {
                                    Text(
                                        text = tr("PAID", "ପୈଠ", "भुगतान"),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = OnTertiaryFixed,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Compact Voice Note Bar
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceContainerLowest,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SpringBounceButton(
                                onClick = { isPlayingVoice = !isPlayingVoice },
                                backgroundColor = if (isPlayingVoice) Tertiary else Primary,
                                contentColor = OnPrimary,
                                shape = CircleShape,
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("play_buyer_voice_button")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isPlayingVoice) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = OnPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = if (isPlayingVoice) tr("Pause", "ବନ୍ଦ", "रोकें") else tr("Play Voice", "ସ୍ୱର ଶୁଣନ୍ତୁ", "आवाज़ सुनें"),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = OnPrimary
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val bars = listOf(8, 14, 20, 12, 18, 10, 16, 20, 12, 8)
                                bars.forEachIndexed { idx, h ->
                                    Box(
                                        modifier = Modifier
                                            .width(3.dp)
                                            .height(h.dp)
                                            .clip(CircleShape)
                                            .background(if (idx < 6 || isPlayingVoice) Primary else OutlineVariant)
                                    )
                                }
                            }

                            Text(
                                text = "0:18",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnSurfaceVariant
                            )
                        }
                    }

                    // Action Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WhatsAppDirectButton(
                            text = tr("WhatsApp", "WhatsApp", "WhatsApp"),
                            url = "https://wa.me/919876543210",
                            onToast = onToast,
                            modifier = Modifier.weight(1f)
                        )

                        SpringBounceButton(
                            onClick = {
                                isOrderAccepted = !isOrderAccepted
                                onToast(if (isOrderAccepted) orderAcceptedToast else orderResetToast)
                            },
                            backgroundColor = if (isOrderAccepted) Tertiary else Primary,
                            contentColor = OnPrimary,
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("accept_order_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = if (isOrderAccepted) Icons.Default.TaskAlt else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = OnPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = if (isOrderAccepted) tr("Accepted", "ଗୃହୀତ", "स्वीकृत") else tr("Accept", "ଗ୍ରହଣ କରନ୍ତୁ", "स्वीकार करें"),
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = OnPrimary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SunlightTouchTile(
    backgroundColor: Color,
    iconBoxBg: Color,
    iconTint: Color,
    icon: ImageVector,
    iconOffsetY: Float,
    countNumber: String,
    countBadge: String,
    countColor: Color,
    subCountColor: Color,
    primaryTitle: String,
    subtitle: String,
    subtitleColor: Color,
    titleColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SpringBounceButton(
        onClick = onClick,
        backgroundColor = backgroundColor,
        contentColor = titleColor,
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(14.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconBoxBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = primaryTitle,
                        tint = iconTint,
                        modifier = Modifier
                            .size(24.dp)
                            .offset(y = iconOffsetY.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = countNumber,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = countColor
                    )
                    Text(
                        text = countBadge,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = subCountColor
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = primaryTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = subtitleColor,
                    maxLines = 1
                )
            }
        }
    }
}
