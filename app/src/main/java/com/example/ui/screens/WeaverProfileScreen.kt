package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppScreen
import com.example.data.LocalAppLanguage
import com.example.data.VastramRepository
import com.example.data.tr
import com.example.ui.components.SpringBounceButton
import com.example.ui.components.VastramAsyncImage
import com.example.ui.components.WeftDiamondPatternBackground
import com.example.ui.components.WhatsAppDirectButton
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OnTertiary
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryFixed

@Composable
fun WeaverProfileScreen(
    onNavigate: (AppScreen) -> Unit,
    onToast: (String) -> Unit
) {
    val lang = LocalAppLanguage.current
    val atelierItems = remember(lang) { VastramRepository.getAtelierItems(lang) }

    var isFollowing by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }
    var isReelPlaying by remember { mutableStateOf(false) }

    val followToast = tr("Following Atelier", "ଅନୁସରଣ କରାଗଲା", "फॉलो किया गया")
    val unfollowToast = tr("Unfollowed", "ଅନୁସରଣ ହଟାଗଲା", "अनफॉलो")
    val bookmarkToast = tr("Saved", "ସଂରକ୍ଷିତ", "सहेजा गया")
    val reelPlayToast = tr("Playing Loom Sound (0:42)", "ତନ୍ତ ଶବ୍ଦ ଚାଲୁଅଛି (୦:୪୨)", "करघा ध्वनि चल रही है (0:42)")

    val pulseTransition = rememberInfiniteTransition(label = "weaverPulse")
    val liveScale by pulseTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "livePing"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("weaver_profile_list"),
        contentPadding = PaddingValues(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Visual Weaver Portrait Header + Icon Badges
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceContainerHigh)
            ) {
                WeftDiamondPatternBackground(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(104.dp)
                                .clip(CircleShape)
                                .border(3.dp, Primary, CircleShape)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerLowest)
                        ) {
                            VastramAsyncImage(
                                url = VastramRepository.HEADER_PROFILE_AVATAR,
                                contentDescription = tr("Shri Bijaya Kumar Patra", "ଶ୍ରୀ ବିଜୟ କୁମାର ପାତ୍ର", "श्री विजय कुमार पात्र"),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        }

                        Surface(
                            color = Tertiary,
                            shape = CircleShape
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .scale(liveScale)
                                        .clip(CircleShape)
                                        .background(TertiaryFixed)
                                )
                                Text(
                                    text = tr("LIVE", "ଲାଇଭ୍", "लाइव"),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = OnTertiary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = tr("Shri Bijaya Kumar Patra", "ଶ୍ରୀ ବିଜୟ କୁମାର ପାତ୍ର", "श्री विजय कुमार पात्र"),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = OnSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Compact Icon Trust Pills
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = SurfaceContainerLowest
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = Tertiary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "GI #WVR-204",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Tertiary
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = SurfaceContainerLowest
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GraphicEq,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = tr("Nuapatna", "ନୂଆପାଟଣା", "नुआपाटना"),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Secondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Compact 4-Metric Strip
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MiniStatPill(
                    value = tr("28 Yrs", "୨୮ ବର୍ଷ", "28 वर्ष"),
                    label = tr("Craft", "ଅଭିଜ୍ଞତା", "अनुभव"),
                    modifier = Modifier.weight(1f)
                )
                MiniStatPill(
                    value = "142",
                    label = tr("Sarees", "ଶାଢ଼ୀ", "साड़ी"),
                    modifier = Modifier.weight(1f)
                )
                MiniStatPill(
                    value = "4.9 ★",
                    label = tr("Rating", "ରେଟିଂ", "रेटिंग"),
                    modifier = Modifier.weight(1f)
                )
                MiniStatPill(
                    value = "6",
                    label = tr("Looms", "ତନ୍ତ", "करघे"),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 3. Action Buttons
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                WhatsAppDirectButton(
                    text = tr("WhatsApp Master Weaver", "ବୁଣାକାରଙ୍କ ସହ ହ୍ୱାଟ୍ସଆପ୍", "मास्टर बुनकर से व्हाट्सएप"),
                    url = "https://wa.me/919437123456",
                    onToast = onToast,
                    useBrandGreen = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SpringBounceButton(
                        onClick = {
                            isFollowing = !isFollowing
                            onToast(if (isFollowing) followToast else unfollowToast)
                        },
                        backgroundColor = if (isFollowing) Primary else SurfaceContainerHigh,
                        contentColor = if (isFollowing) OnPrimary else OnSurface,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("follow_atelier_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isFollowing) Icons.Default.Check else Icons.Default.PersonAdd,
                                contentDescription = null,
                                tint = if (isFollowing) OnPrimary else Primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (isFollowing) {
                                    tr("Following", "ଅନୁସୃତ", "फॉलो किया")
                                } else {
                                    tr("Follow", "ଅନୁସରଣ", "फॉलो")
                                },
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isFollowing) OnPrimary else OnSurface
                            )
                        }
                    }

                    SpringBounceButton(
                        onClick = {
                            isBookmarked = !isBookmarked
                            onToast(bookmarkToast)
                        },
                        backgroundColor = SurfaceContainerHigh,
                        contentColor = OnSurface,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("bookmark_atelier_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                tint = if (isBookmarked) Primary else OnSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (isBookmarked) {
                                    tr("Saved", "ସଂଗୃହିତ", "सहेजा")
                                } else {
                                    tr("Save", "ସଂଗ୍ରହ", "सहेजें")
                                },
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface
                            )
                        }
                    }
                }
            }
        }

        // 4. Visual 3-Column Atelier Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.GridOn,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = tr("Atelier Portfolio", "କର୍ମଶାଳା ଗ୍ୟାଲେରୀ", "कार्यशाला गैलरी"),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = OnSurface
                    )
                }

                atelierItems.chunked(3).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { item ->
                            val isReel = item.id == "atelier_6"
                            Surface(
                                onClick = {
                                    if (isReel) {
                                        isReelPlaying = !isReelPlaying
                                        onToast(reelPlayToast)
                                    } else {
                                        onNavigate(AppScreen.PRODUCT_DETAIL)
                                    }
                                },
                                shape = RoundedCornerShape(14.dp),
                                color = SurfaceContainerLowest,
                                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.35f)),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("atelier_item_${item.id}")
                            ) {
                                Column {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(1f)
                                            .background(SurfaceContainer)
                                    ) {
                                        VastramAsyncImage(
                                            url = item.imageUrl,
                                            contentDescription = item.title,
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        if (isReel) {
                                            Box(
                                                modifier = Modifier
                                                    .size(34.dp)
                                                    .align(Alignment.Center)
                                                    .clip(CircleShape)
                                                    .background(SurfaceContainerLowest.copy(alpha = 0.9f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = if (isReelPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                    contentDescription = null,
                                                    tint = Primary,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        } else {
                                            Surface(
                                                color = Primary.copy(alpha = 0.9f),
                                                shape = RoundedCornerShape(4.dp),
                                                modifier = Modifier
                                                    .align(Alignment.TopStart)
                                                    .padding(5.dp)
                                            ) {
                                                Text(
                                                    text = item.badge,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontSize = 8.5.sp,
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }

                                    Column(modifier = Modifier.padding(6.dp)) {
                                        Text(
                                            text = item.title,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = OnSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = item.price,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = Primary,
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
    }
}

@Composable
private fun MiniStatPill(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceContainerLowest,
        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.45f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Primary,
                maxLines = 1
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = OnSurfaceVariant,
                maxLines = 1
            )
        }
    }
}
