package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Token
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppScreen
import com.example.data.VastramRepository
import com.example.data.tr
import com.example.ui.components.SimulatedQrCodeCanvas
import com.example.ui.components.SpringBounceButton
import com.example.ui.components.VastramAsyncImage
import com.example.ui.components.WhatsAppDirectButton
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSecondaryFixed
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OnTertiaryFixed
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryFixed
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductDetailScreen(
    onNavigate: (AppScreen) -> Unit,
    onToast: (String) -> Unit
) {
    var isHotspotOpen by remember { mutableStateOf(false) }
    var isMagnified by remember { mutableStateOf(false) }
    var verifyState by remember { mutableIntStateOf(0) }
    var commissionState by remember { mutableIntStateOf(0) }

    val verifiedToast = tr("GI Verified #OD04-819", "GI ପ୍ରମାଣିତ #OD04-819", "GI सत्यापित #OD04-819")
    val escrowToast = tr("Escrow Slot Locked (₹5,000)", "ଏସ୍କ୍ରୋ ଲକ୍ (₹୫,୦୦୦)", "एस्क्रो लॉक (₹5,000)")
    val macroOnToast = tr("Macro Zoom ON", "ମ୍ୟାକ୍ରୋ ଜୁମ୍ ଚାଲୁ", "मैक्रो ज़ूम चालू")
    val macroOffToast = tr("Standard View", "ସାଧାରଣ ଦୃଶ୍ୟ", "सामान्य दृश्य")

    LaunchedEffect(verifyState) {
        if (verifyState == 1) {
            delay(500)
            verifyState = 2
            onToast(verifiedToast)
        }
    }

    LaunchedEffect(commissionState) {
        if (commissionState == 1) {
            delay(650)
            commissionState = 2
            onToast(escrowToast)
            delay(2000)
            commissionState = 0
        }
    }

    val heroScale by animateFloatAsState(
        targetValue = if (isMagnified) 1.26f else 1.0f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 350f),
        label = "magnifierScale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "productPulse")
    val hotspotPulse by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.32f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "hotspotRing"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("product_detail_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Visual Hero Image + Zoom + Hotspot
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(SurfaceContainerHigh)
                    .clickable { if (isHotspotOpen) isHotspotOpen = false }
            ) {
                VastramAsyncImage(
                    url = VastramRepository.IMG_BOMKAI_HERO,
                    contentDescription = tr("Bomkai Silk Saree", "ବୋମକାଇ ପାଟ ଶାଢ଼ୀ", "बोमकाई रेशम साड़ी"),
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(heroScale)
                )

                // Top Badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = SurfaceContainerLowest.copy(alpha = 0.92f)
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
                                text = "GI OD-04",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Tertiary
                            )
                        }
                    }

                    Surface(
                        onClick = {
                            isMagnified = !isMagnified
                            onToast(if (isMagnified) macroOnToast else macroOffToast)
                        },
                        shape = CircleShape,
                        color = if (isMagnified) Primary else SurfaceContainerLowest.copy(alpha = 0.92f),
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("zoom_toggle_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = tr("Zoom", "ଜୁମ୍", "ज़ूम"),
                                tint = if (isMagnified) OnPrimary else OnSurface,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Motif Hotspot
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 32.dp, bottom = 44.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .scale(hotspotPulse)
                            .clip(CircleShape)
                            .background(Primary.copy(alpha = 0.28f))
                    )
                    Surface(
                        onClick = { isHotspotOpen = !isHotspotOpen },
                        shape = CircleShape,
                        color = Primary,
                        modifier = Modifier
                            .size(34.dp)
                            .align(Alignment.Center)
                            .testTag("motif_hotspot_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Grain,
                                contentDescription = tr("Motif", "ମୋଟିଫ୍", "मोटिफ"),
                                tint = OnPrimary,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }

                // Minimal Visual Tooltip
                AnimatedVisibility(
                    visible = isHotspotOpen,
                    enter = fadeIn(tween(180)) + scaleIn(initialScale = 0.9f),
                    exit = fadeOut(tween(150)) + scaleOut(targetScale = 0.9f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 14.dp, bottom = 86.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceContainerLowest.copy(alpha = 0.96f),
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = tr("Pasapalli & Matsya Motif", "ପଶାପାଲି ଓ ମତ୍ସ୍ୟ ବାନ୍ଧ", "पशापाली व मत्स्य मोटिफ"),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = OnSurface
                                )
                                Text(
                                    text = tr("14h / meter · Twin Jala", "୧୪ ଘଣ୍ଟା / ମିଟର · ଜାଲା", "14 घंटे / मीटर · जाला"),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Tertiary
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Title + Price + Visual Spec Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = tr("Bomkai Patta Silk Saree", "ବୋମକାଇ ପାଟ ଶାଢ଼ୀ", "बोमकाई पाट रेशम साड़ी"),
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface
                        )
                        Text(
                            text = tr("Shankha & Matsya Pallu", "ଶଙ୍ଖ ଓ ମତ୍ସ୍ୟ ପଣତ", "शंख व मत्स्य पल्लू"),
                            style = MaterialTheme.typography.labelLarge,
                            color = Primary
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "₹18,500",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface
                        )
                        Surface(
                            shape = CircleShape,
                            color = TertiaryFixed
                        ) {
                            Text(
                                text = tr("88% Weaver", "୮୮% ବୁଣାକାର", "88% बुनकर"),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnTertiaryFixed,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // 4 Icon Spec Tiles (Visual-first instead of text paragraphs)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VisualSpecChip(
                        icon = Icons.Default.HourglassBottom,
                        value = tr("18 Days", "୧୮ ଦିନ", "18 दिन"),
                        label = tr("Pit Loom", "ଗାତ ତନ୍ତ", "पिट लूम"),
                        modifier = Modifier.weight(1f)
                    )
                    VisualSpecChip(
                        icon = Icons.Default.Eco,
                        value = tr("100% Pure", "୧୦୦% ଶୁଦ୍ଧ", "100% शुद्ध"),
                        label = tr("Aal Dye", "ଆଳ ରଙ୍ଗ", "आल डाई"),
                        modifier = Modifier.weight(1f)
                    )
                    VisualSpecChip(
                        icon = Icons.Default.Architecture,
                        value = "100s",
                        label = tr("Mulberry", "ପାଟ ସୂତା", "शहतूत रेशम"),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 3. Compact Weaver Card
        item {
            Surface(
                onClick = { onNavigate(AppScreen.WEAVER_PROFILE) },
                shape = RoundedCornerShape(16.dp),
                color = SurfaceContainerLow,
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        VastramAsyncImage(
                            url = "https://lh3.googleusercontent.com/aida-public/AB6AXuDKklZkkvhVWPrtQjTHHJudvR1T-S4yQo3BqEwdwWyXvZ9E7LggwP2riee8eI383skTLFR0QA0GmJEIyMiWWdRnA-S6npg8Sctj5YPSwUKOMq29Nt3iQwCo10aqb-VQqJNaqvaXtY_EvAFyr4CSrqjvEZb64D7kaelLQH26PGEQIoQIiY8LaA7s70n7vf6bM6JA2dszzcstddJqQd-GgRr6edZuCcg-k62KaYsjcS1q",
                            contentDescription = "Weaver Avatar",
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = tr("Shri Raghunath Meher", "ଶ୍ରୀ ରଘୁନାଥ ମେହେର", "श्री रघुनाथ मेहेर"),
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = OnSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Icon(
                                    imageVector = Icons.Default.WorkspacePremium,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                            Text(
                                text = tr("Nuapatna · Loom #108", "ନୂଆପାଟଣା · ତନ୍ତ #୧୦୮", "नुआपाटना · करघा #108"),
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = TertiaryFixed
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = OnTertiaryFixed,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "₹16,280",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnTertiaryFixed
                            )
                        }
                    }
                }
            }
        }

        // 4. Compact QR Certificate Strip
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = SurfaceContainer,
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceContainerLowest,
                        modifier = Modifier
                            .size(64.dp)
                            .clickable { onNavigate(AppScreen.QR_VERIFICATION) }
                    ) {
                        SimulatedQrCodeCanvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(6.dp)
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
                            Icon(
                                imageVector = Icons.Default.Token,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = tr("GI Ledger #OD04-819", "GI ଲେଜର #OD04-819", "GI लेजर #OD04-819"),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface
                            )
                        }
                        Text(
                            text = "20.48° N, 85.83° E · 0x89bf",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                            color = OnSurfaceVariant
                        )
                    }

                    SpringBounceButton(
                        onClick = { verifyState = 1 },
                        backgroundColor = if (verifyState == 2) Tertiary else Primary,
                        contentColor = OnPrimary,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                        modifier = Modifier.testTag("verify_hash_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (verifyState == 2) Icons.Default.CheckCircle else Icons.Default.QrCodeScanner,
                                contentDescription = null,
                                tint = OnPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (verifyState == 2) {
                                    tr("Verified", "ପ୍ରମାଣିତ", "सत्यापित")
                                } else {
                                    tr("Verify", "ଯାଞ୍ଚ", "जांचें")
                                },
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnPrimary
                            )
                        }
                    }
                }
            }
        }

        // 5. Direct Purchase & Commission Actions
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                WhatsAppDirectButton(
                    text = tr("Buy Direct on WhatsApp · ₹18,500", "ସିଧା କିଣନ୍ତୁ · ₹୧୮,୫୦୦", "सीधे खरीदें · ₹18,500"),
                    subText = tr("Dispatched from Nuapatna Loom", "ନୂଆପାଟଣା ତନ୍ତରୁ ପଠାଯିବ", "नुआपाटना करघे से डिस्पैच"),
                    url = "https://wa.me/919437000108",
                    onToast = onToast,
                    useBrandGreen = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SpringBounceButton(
                        onClick = { commissionState = 1 },
                        backgroundColor = if (commissionState == 2) Tertiary else Primary,
                        contentColor = OnPrimary,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("lock_escrow_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (commissionState == 2) Icons.Default.Check else Icons.Default.Security,
                                contentDescription = null,
                                tint = OnPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = tr("Book Loom (₹5k)", "ତନ୍ତ ବୁକ୍ (₹୫,୦୦୦)", "करघा बुक (₹5k)"),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = OnPrimary,
                                maxLines = 1
                            )
                        }
                    }

                    SpringBounceButton(
                        onClick = { onNavigate(AppScreen.ORDER_TRACKING) },
                        backgroundColor = SecondaryFixed,
                        contentColor = OnSecondaryFixed,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = OnSecondaryFixed,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = tr("Track Transit", "ପରିବହନ ଦେଖନ୍ତୁ", "ट्रांजिट देखें"),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = OnSecondaryFixed,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VisualSpecChip(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceContainerLow,
        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.45f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = OnSurface,
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
