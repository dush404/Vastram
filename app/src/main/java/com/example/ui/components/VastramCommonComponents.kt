package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DryCleaning
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.data.AppLanguage
import com.example.data.AppScreen
import com.example.data.LocalAppLanguage
import com.example.data.LocalUserRole
import com.example.data.UserRole
import com.example.data.VastramRepository
import com.example.data.tr
import com.example.ui.theme.InverseOnSurface
import com.example.ui.theme.InverseSurface
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OnTertiaryFixed
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryContainer
import com.example.ui.theme.TertiaryFixed
import com.example.ui.theme.WhatsAppBrandGreen

fun openWhatsAppUrl(context: Context, url: String, onFallbackToast: (String) -> Unit) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        onFallbackToast("WhatsApp: $url")
    }
}

@Composable
fun VastramAsyncImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(context)
            .data(url)
            .crossfade(250)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
        loading = {
            WovenPlaceholderBox(modifier = Modifier.fillMaxSize())
        },
        error = {
            WovenPlaceholderBox(modifier = Modifier.fillMaxSize())
        }
    )
}

@Composable
fun WovenPlaceholderBox(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "wovenShimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )
    Box(
        modifier = modifier
            .background(SurfaceContainerHigh)
            .alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = 24.dp.toPx()
            var x = 0f
            while (x < size.width) {
                drawLine(
                    color = OutlineVariant.copy(alpha = 0.35f),
                    start = Offset(x, 0f),
                    end = Offset(x, size.height),
                    strokeWidth = 1f
                )
                x += step
            }
            var y = 0f
            while (y < size.height) {
                drawLine(
                    color = OutlineVariant.copy(alpha = 0.35f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += step
            }
        }
    }
}

@Composable
fun VastramTopHeader(
    currentScreen: AppScreen,
    selectedLanguage: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
    onNavigate: (AppScreen) -> Unit,
    onBack: (() -> Unit)? = null,
    onSwitchRoleClick: (() -> Unit)? = null,
    onToast: (String) -> Unit
) {
    val role = LocalUserRole.current
    var showLangMenu by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
        tonalElevation = 1.dp,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Optional Back Button + Brand Title + GI Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 6.dp)
                ) {
                    if (onBack != null) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerHigh)
                                .testTag("header_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = tr("Back", "ପଛକୁ", "वापस"),
                                tint = OnSurface,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .clickable { onNavigate(AppScreen.DISCOVERY_FEED) }
                            .testTag("brand_header_title")
                    ) {
                        Text(
                            text = tr("VASTRAM", "ବସ୍ତ୍ରମ୍", "वस्त्रम्"),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.3).sp
                            ),
                            color = Primary,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                        Surface(
                            color = TertiaryFixed,
                            shape = CircleShape
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = OnTertiaryFixed,
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = "GI",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = OnTertiaryFixed,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }
                }

                // Right Actions: Role Switcher Pill + Language Pill + Avatar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Role Indicator & Switcher Pill (Buyer vs Seller)
                    if (onSwitchRoleClick != null) {
                        Surface(
                            onClick = onSwitchRoleClick,
                            shape = CircleShape,
                            color = if (role == UserRole.BUYER) PrimaryFixed else TertiaryFixed,
                            border = BorderStroke(
                                1.dp,
                                if (role == UserRole.BUYER) Primary.copy(alpha = 0.35f) else Tertiary.copy(alpha = 0.35f)
                            ),
                            modifier = Modifier
                                .height(30.dp)
                                .testTag("header_role_switch_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = if (role == UserRole.BUYER) Icons.Default.ShoppingBag else Icons.Default.Storefront,
                                    contentDescription = null,
                                    tint = if (role == UserRole.BUYER) Primary else Tertiary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = if (role == UserRole.BUYER) {
                                        tr("Buyer", "କ୍ରେତା", "खरीदार")
                                    } else {
                                        tr("Seller", "ବିକ୍ରେତା", "विक्रेता")
                                    },
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.5.sp
                                    ),
                                    color = if (role == UserRole.BUYER) Primary else OnTertiaryFixed,
                                    maxLines = 1,
                                    softWrap = false
                                )
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = tr("Switch Role", "ଭୂମିକା ବଦଳାନ୍ତୁ", "भूमिका बदलें"),
                                    tint = if (role == UserRole.BUYER) Primary else Tertiary,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }

                    // Single-Language Selector Pill
                    Box {
                        Surface(
                            onClick = { showLangMenu = true },
                            shape = CircleShape,
                            color = SurfaceContainerHigh,
                            border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .height(30.dp)
                                .testTag("language_selector_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = tr("Language", "ଭାଷା", "भाषा"),
                                    tint = Primary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = selectedLanguage.code,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.5.sp
                                    ),
                                    color = OnSurface,
                                    maxLines = 1,
                                    softWrap = false
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = OnSurfaceVariant,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showLangMenu,
                            onDismissRequest = { showLangMenu = false },
                            modifier = Modifier.background(SurfaceContainerLowest)
                        ) {
                            AppLanguage.entries.forEach { lang ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = lang.displayLabel,
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = if (lang == selectedLanguage) FontWeight.Bold else FontWeight.Medium
                                                ),
                                                color = if (lang == selectedLanguage) Primary else OnSurface
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(
                                                text = lang.code,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = if (lang == selectedLanguage) Primary else OnSurfaceVariant
                                            )
                                        }
                                    },
                                    onClick = {
                                        onLanguageChange(lang)
                                        showLangMenu = false
                                        onToast(
                                            lang.pick(
                                                "Language: English",
                                                "ଭାଷା: ଓଡ଼ିଆ",
                                                "भाषा: हिन्दी"
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }

                    // Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Primary.copy(alpha = 0.35f), CircleShape)
                            .clickable { onNavigate(AppScreen.WEAVER_PROFILE) }
                            .testTag("header_profile_avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        VastramAsyncImage(
                            url = VastramRepository.HEADER_PROFILE_AVATAR,
                            contentDescription = tr("Profile", "ପ୍ରୋଫାଇଲ୍", "प्रोफ़ाइल"),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Role-Specific Quick Icon Pills Ribbon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceContainerLow)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (role == UserRole.BUYER) {
                    QuickSuitePill(
                        label = tr("Saree", "ଶାଢ଼ୀ", "साड़ी"),
                        icon = Icons.Default.DryCleaning,
                        isSelected = currentScreen == AppScreen.PRODUCT_DETAIL,
                        onClick = { onNavigate(AppScreen.PRODUCT_DETAIL) },
                        testTag = "quick_pill_product_detail"
                    )
                    QuickSuitePill(
                        label = tr("GI QR", "GI QR", "GI QR"),
                        icon = Icons.Default.QrCodeScanner,
                        isSelected = currentScreen == AppScreen.QR_VERIFICATION,
                        onClick = { onNavigate(AppScreen.QR_VERIFICATION) },
                        testTag = "quick_pill_qr_verify"
                    )
                    QuickSuitePill(
                        label = tr("Transit", "ପରିବହନ", "ट्रांजिट"),
                        icon = Icons.Default.LocalShipping,
                        isSelected = currentScreen == AppScreen.ORDER_TRACKING,
                        onClick = { onNavigate(AppScreen.ORDER_TRACKING) },
                        testTag = "quick_pill_order_tracking"
                    )
                    QuickSuitePill(
                        label = tr("Reviews", "ସମୀକ୍ଷା", "समीक्षा"),
                        icon = Icons.Default.Star,
                        isSelected = currentScreen == AppScreen.SOCIAL_REVIEWS,
                        onClick = { onNavigate(AppScreen.SOCIAL_REVIEWS) },
                        testTag = "quick_pill_social_reviews"
                    )
                } else {
                    QuickSuitePill(
                        label = tr("Raw Yarn B2B", "କଞ୍ଚା ସୂତା B2B", "कच्चा सूत B2B"),
                        icon = Icons.Default.Inventory2,
                        isSelected = currentScreen == AppScreen.DISCOVERY_FEED,
                        onClick = { onNavigate(AppScreen.DISCOVERY_FEED) },
                        testTag = "quick_pill_raw_yarn"
                    )
                    QuickSuitePill(
                        label = tr("Voice AI", "ସ୍ୱର AI", "वॉइस AI"),
                        icon = Icons.Default.RecordVoiceOver,
                        isSelected = currentScreen == AppScreen.VOICE_STUDIO,
                        onClick = { onNavigate(AppScreen.VOICE_STUDIO) },
                        testTag = "quick_pill_voice_studio"
                    )
                    QuickSuitePill(
                        label = tr("Loom Desk", "ତନ୍ତ ଡେସ୍କ", "करघा डेस्क"),
                        icon = Icons.Default.PrecisionManufacturing,
                        isSelected = currentScreen == AppScreen.LOOM_DESK,
                        onClick = { onNavigate(AppScreen.LOOM_DESK) },
                        testTag = "quick_pill_loom_desk"
                    )
                    QuickSuitePill(
                        label = tr("Cluster", "କ୍ଲଷ୍ଟର", "क्लस्टर"),
                        icon = Icons.Default.Hub,
                        isSelected = currentScreen == AppScreen.CLUSTER_MANAGER,
                        onClick = { onNavigate(AppScreen.CLUSTER_MANAGER) },
                        testTag = "quick_pill_cluster_manager"
                    )
                }
            }
            HorizontalDivider(thickness = 0.5.dp, color = OutlineVariant.copy(alpha = 0.45f))
        }
    }
}

@Composable
private fun QuickSuitePill(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (isSelected) Primary else SurfaceContainerLowest,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) Primary else OutlineVariant.copy(alpha = 0.5f)
        ),
        modifier = Modifier.testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) OnPrimary else Primary,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                ),
                color = if (isSelected) OnPrimary else OnSurface,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

data class BottomNavItem(
    val screen: AppScreen,
    val icon: ImageVector,
    val customLabel: String,
    val testTag: String
)

@Composable
fun VastramBottomNavBar(
    currentScreen: AppScreen,
    onSelectScreen: (AppScreen) -> Unit
) {
    val lang = LocalAppLanguage.current
    val role = LocalUserRole.current

    val navItems = remember(lang, role) {
        if (role == UserRole.BUYER) {
            listOf(
                BottomNavItem(
                    screen = AppScreen.DISCOVERY_FEED,
                    icon = Icons.Default.GridView,
                    customLabel = lang.pick("Clothes", "ବସ୍ତ୍ର", "वस्त्र"),
                    testTag = "nav_explore"
                ),
                BottomNavItem(
                    screen = AppScreen.PRODUCT_DETAIL,
                    icon = Icons.Default.DryCleaning,
                    customLabel = lang.pick("Saree", "ଶାଢ଼ୀ", "साड़ी"),
                    testTag = "nav_product_detail"
                ),
                BottomNavItem(
                    screen = AppScreen.QR_VERIFICATION,
                    icon = Icons.Default.QrCodeScanner,
                    customLabel = lang.pick("GI QR", "GI QR", "GI QR"),
                    testTag = "nav_verify_qr"
                ),
                BottomNavItem(
                    screen = AppScreen.ORDER_TRACKING,
                    icon = Icons.Default.LocalShipping,
                    customLabel = lang.pick("Transit", "ଟ୍ରାକ୍", "ट्रांजिट"),
                    testTag = "nav_order_tracking"
                ),
                BottomNavItem(
                    screen = AppScreen.WEAVER_PROFILE,
                    icon = Icons.Default.Person,
                    customLabel = lang.pick("Atelier", "କର୍ମଶାଳା", "कार्यशाला"),
                    testTag = "nav_weaver_profile"
                )
            )
        } else {
            listOf(
                BottomNavItem(
                    screen = AppScreen.DISCOVERY_FEED,
                    icon = Icons.Default.Inventory2,
                    customLabel = lang.pick("Raw Yarn", "କଞ୍ଚା ସୂତା", "कच्चा सूत"),
                    testTag = "nav_explore"
                ),
                BottomNavItem(
                    screen = AppScreen.VOICE_STUDIO,
                    icon = Icons.Default.RecordVoiceOver,
                    customLabel = lang.pick("Voice AI", "ସ୍ୱର AI", "वॉइस AI"),
                    testTag = "nav_voice_studio"
                ),
                BottomNavItem(
                    screen = AppScreen.LOOM_DESK,
                    icon = Icons.Default.PrecisionManufacturing,
                    customLabel = lang.pick("Looms", "ତନ୍ତ", "करघा"),
                    testTag = "nav_loom_desk"
                ),
                BottomNavItem(
                    screen = AppScreen.CLUSTER_MANAGER,
                    icon = Icons.Default.Hub,
                    customLabel = lang.pick("Cluster", "କ୍ଲଷ୍ଟର", "क्लस्टर"),
                    testTag = "nav_cluster_manager"
                ),
                BottomNavItem(
                    screen = AppScreen.WEAVER_PROFILE,
                    icon = Icons.Default.Person,
                    customLabel = lang.pick("Atelier", "କର୍ମଶାଳା", "कार्यशाला"),
                    testTag = "nav_weaver_profile"
                )
            )
        }
    }

    val activeColor = if (role == UserRole.BUYER) Primary else Tertiary

    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
        tonalElevation = 3.dp,
        shadowElevation = 12.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            HorizontalDivider(thickness = 0.5.dp, color = OutlineVariant.copy(alpha = 0.45f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp)
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navItems.forEach { item ->
                    val isSelected = currentScreen == item.screen
                    val scale by animateFloatAsState(
                        targetValue = if (isSelected) 1.05f else 1.0f,
                        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
                        label = "navTabScale"
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .scale(scale)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectScreen(item.screen) }
                            .padding(horizontal = 2.dp, vertical = 4.dp)
                            .testTag(item.testTag),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) activeColor.copy(alpha = 0.14f)
                                    else Color.Transparent
                                )
                                .padding(horizontal = 10.dp, vertical = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.customLabel,
                                tint = if (isSelected) activeColor else OnSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.customLabel,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = if (isSelected) activeColor else OnSurfaceVariant,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpringBounceButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Primary,
    contentColor: Color = OnPrimary,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(14.dp),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 11.dp),
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 600f),
        label = "buttonBounce"
    )

    Surface(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(),
                onClick = onClick
            ),
        color = backgroundColor,
        contentColor = contentColor,
        shape = shape,
        border = border
    ) {
        Box(
            modifier = Modifier.padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
fun WhatsAppDirectButton(
    text: String,
    subText: String? = null,
    url: String,
    onToast: (String) -> Unit,
    modifier: Modifier = Modifier,
    useBrandGreen: Boolean = false
) {
    val context = LocalContext.current
    val bgColor = if (useBrandGreen) WhatsAppBrandGreen else Tertiary
    SpringBounceButton(
        onClick = { openWhatsAppUrl(context, url, onToast) },
        backgroundColor = bgColor,
        contentColor = Color.White,
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Canvas(modifier = Modifier.size(17.dp)) {
                drawCircle(
                    color = Color.White,
                    style = Stroke(width = 2.dp.toPx())
                )
                drawCircle(
                    color = Color.White,
                    radius = 3.dp.toPx()
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!subText.isNullOrEmpty()) {
                    Text(
                        text = subText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color.White.copy(alpha = 0.9f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun WeftDiamondPatternBackground(
    modifier: Modifier = Modifier,
    primaryColor: Color = Primary.copy(alpha = 0.14f),
    dotColor: Color = Tertiary.copy(alpha = 0.18f)
) {
    Canvas(modifier = modifier) {
        val tileSize = 32.dp.toPx()
        val half = tileSize / 2f
        var x = 0f
        while (x < size.width) {
            var y = 0f
            while (y < size.height) {
                val path = Path().apply {
                    moveTo(x, y + half)
                    lineTo(x + half, y)
                    lineTo(x + tileSize, y + half)
                    lineTo(x + half, y + tileSize)
                    close()
                }
                drawPath(
                    path = path,
                    color = primaryColor,
                    style = Stroke(width = 1.2.dp.toPx())
                )
                drawCircle(
                    color = dotColor,
                    radius = 2.2.dp.toPx(),
                    center = Offset(x + half, y + half)
                )
                y += tileSize
            }
            x += tileSize
        }
    }
}

@Composable
fun SimulatedQrCodeCanvas(
    modifier: Modifier = Modifier,
    color: Color = OnSurface
) {
    Canvas(modifier = modifier) {
        val grid = 12
        val cell = size.width / grid
        fun drawFinder(startX: Int, startY: Int) {
            drawRect(
                color = color,
                topLeft = Offset(startX * cell, startY * cell),
                size = androidx.compose.ui.geometry.Size(3 * cell, 3 * cell),
                style = Stroke(width = cell * 0.6f)
            )
            drawRect(
                color = color,
                topLeft = Offset((startX + 1) * cell, (startY + 1) * cell),
                size = androidx.compose.ui.geometry.Size(cell, cell)
            )
        }
        drawFinder(1, 1)
        drawFinder(8, 1)
        drawFinder(1, 8)

        val activeCells = listOf(
            5 to 1, 6 to 2, 5 to 3, 6 to 4, 4 to 5, 5 to 5, 7 to 5, 9 to 5,
            5 to 6, 6 to 6, 8 to 6, 10 to 6, 4 to 7, 7 to 7, 9 to 7,
            5 to 8, 6 to 8, 8 to 8, 9 to 8, 5 to 9, 7 to 9, 10 to 9,
            6 to 10, 8 to 10, 9 to 10, 10 to 10
        )
        activeCells.forEach { (cx, cy) ->
            drawRect(
                color = color,
                topLeft = Offset(cx * cell, cy * cell),
                size = androidx.compose.ui.geometry.Size(cell * 0.85f, cell * 0.85f)
            )
        }
    }
}

@Composable
fun VastramFloatingToast(
    message: String?,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = message != null,
        enter = fadeIn(tween(200)) + scaleIn(initialScale = 0.9f, animationSpec = tween(200)),
        exit = fadeOut(tween(200)) + scaleOut(targetScale = 0.9f, animationSpec = tween(200)),
        modifier = modifier
    ) {
        Surface(
            color = InverseSurface,
            contentColor = InverseOnSurface,
            shape = CircleShape,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = TertiaryFixed,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = message ?: "",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = InverseOnSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
