package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DryCleaning
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppScreen
import com.example.data.FeedPost
import com.example.data.GridSareeItem
import com.example.data.LocalAppLanguage
import com.example.data.LocalUserRole
import com.example.data.RawMaterialPost
import com.example.data.StoryCircle
import com.example.data.UserRole
import com.example.data.VastramRepository
import com.example.data.tr
import com.example.ui.components.SpringBounceButton
import com.example.ui.components.VastramAsyncImage
import com.example.ui.components.WhatsAppDirectButton
import com.example.ui.theme.ErrorColor
import com.example.ui.theme.InverseOnSurface
import com.example.ui.theme.InverseSurface
import com.example.ui.theme.OnErrorColor
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnPrimaryFixed
import com.example.ui.theme.OnSecondaryContainer
import com.example.ui.theme.OnSecondaryFixed
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OnTertiaryFixed
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryFixed
import kotlinx.coroutines.delay

@Composable
fun DiscoveryFeedScreen(
    onNavigate: (AppScreen) -> Unit,
    onToast: (String) -> Unit
) {
    val lang = LocalAppLanguage.current
    val role = LocalUserRole.current

    val stories = remember(lang, role) { VastramRepository.getStories(lang, role) }
    val feedPosts = remember(lang) { VastramRepository.getFeedPosts(lang) }
    val masonryItems = remember(lang) { VastramRepository.getMasonryItems(lang) }
    val baseRawMaterials = remember(lang) { VastramRepository.getRawMaterialPosts(lang) }
    val sellerRawMaterialList = remember(lang) {
        mutableStateListOf<RawMaterialPost>().apply { addAll(baseRawMaterials) }
    }

    // For Buyer: false = Editorial Sarees, true = Grid Sarees (NO raw materials ever shown to Buyer)
    var isMasonryMode by remember { mutableStateOf(false) }

    // For Seller: true = Raw Materials B2B Exchange (circulates ONLY between Sellers), false = Finished Loom Showcase
    var showSellerRawMaterials by remember(role) { mutableStateOf(role == UserRole.SELLER) }
    var showAddRawLotComposer by remember { mutableStateOf(false) }

    val likedMap = remember { mutableStateMapOf<String, Boolean>() }
    val bookmarkedMap = remember { mutableStateMapOf<String, Boolean>() }
    var isAudioPlaying by remember { mutableStateOf(false) }

    val pulseTransition = rememberInfiniteTransition(label = "discoveryPulse")
    val pulseScale by pulseTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "pulseDot"
    )

    val savedVaultToast = tr("Saved", "ସଂରକ୍ଷିତ", "सहेजा गया")
    val removedVaultToast = tr("Removed", "ହଟାଗଲା", "हटाया गया")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("discovery_feed_list"),
        contentPadding = PaddingValues(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Sub-header & Role-Specific Mode Switcher
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(if (role == UserRole.SELLER) Tertiary else Primary)
                    )
                    Text(
                        text = if (role == UserRole.SELLER) {
                            tr("Seller Exchange", "ବିକ୍ରେତା ବଜାର", "विक्रेता बाज़ार")
                        } else {
                            tr("Handloom Clothes", "ହସ୍ତତନ୍ତ ବସ୍ତ୍ର", "हथकरघा वस्त्र")
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = OnSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (role == UserRole.SELLER) {
                    // Seller-only toggle: Raw Materials (Seller B2B) vs Loom Showcase
                    Surface(
                        shape = CircleShape,
                        color = SurfaceContainerHigh,
                        border = BorderStroke(0.5.dp, OutlineVariant.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ViewModeChip(
                                title = tr("Raw Yarn", "କଞ୍ଚା ସୂତା", "कच्चा सूत"),
                                icon = Icons.Default.Inventory2,
                                isSelected = showSellerRawMaterials,
                                activeColor = Tertiary,
                                onClick = { showSellerRawMaterials = true },
                                testTag = "seller_mode_raw_materials"
                            )
                            ViewModeChip(
                                title = tr("Sarees", "ଶାଢ଼ୀ", "साड़ी"),
                                icon = Icons.Default.DryCleaning,
                                isSelected = !showSellerRawMaterials,
                                activeColor = Primary,
                                onClick = { showSellerRawMaterials = false },
                                testTag = "seller_mode_finished_sarees"
                            )
                        }
                    }
                } else {
                    // Buyer-only toggle: Editorial vs Grid (Strictly clothes only)
                    Surface(
                        shape = CircleShape,
                        color = SurfaceContainerHigh,
                        border = BorderStroke(0.5.dp, OutlineVariant.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ViewModeChip(
                                title = tr("Feed", "ଫିଡ୍", "फ़ीड"),
                                icon = Icons.Default.ViewStream,
                                isSelected = !isMasonryMode,
                                activeColor = Primary,
                                onClick = { isMasonryMode = false },
                                testTag = "view_mode_editorial"
                            )
                            ViewModeChip(
                                title = tr("Grid", "ଗ୍ରିଡ୍", "ग्रिड"),
                                icon = Icons.Default.Dashboard,
                                isSelected = isMasonryMode,
                                activeColor = Primary,
                                onClick = { isMasonryMode = true },
                                testTag = "view_mode_masonry"
                            )
                        }
                    }
                }
            }
        }

        // Horizontal Stories Bar
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.testTag("stories_row")
            ) {
                items(stories, key = { it.id }) { story ->
                    StoryBubbleItem(
                        story = story,
                        onClick = { onNavigate(story.targetScreen) }
                    )
                }
            }
        }

        // Main Content: Seller B2B Raw Materials vs Buyer Finished Clothes
        item {
            if (role == UserRole.SELLER && showSellerRawMaterials) {
                SellerRawMaterialSection(
                    rawMaterials = sellerRawMaterialList,
                    showComposer = showAddRawLotComposer,
                    onToggleComposer = { showAddRawLotComposer = !showAddRawLotComposer },
                    onAddLot = { newPost ->
                        sellerRawMaterialList.add(0, newPost)
                        showAddRawLotComposer = false
                    },
                    onToast = onToast
                )
            } else {
                AnimatedContent(
                    targetState = isMasonryMode,
                    transitionSpec = {
                        fadeIn(tween(220)) togetherWith fadeOut(tween(160))
                    },
                    label = "feedModeSwitch"
                ) { showGrid ->
                    if (!showGrid) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            feedPosts.forEach { post ->
                                val isLiked = likedMap[post.id] ?: false
                                val isBookmarked = bookmarkedMap[post.id] ?: false
                                EditorialFeedCard(
                                    post = post,
                                    isLiked = isLiked,
                                    isBookmarked = isBookmarked,
                                    isAudioPlaying = isAudioPlaying && post.hasAudioNarration,
                                    onToggleLike = { likedMap[post.id] = !isLiked },
                                    onToggleBookmark = {
                                        bookmarkedMap[post.id] = !isBookmarked
                                        onToast(if (!isBookmarked) savedVaultToast else removedVaultToast)
                                    },
                                    onToggleAudio = { isAudioPlaying = !isAudioPlaying },
                                    onInspectProduct = { onNavigate(AppScreen.PRODUCT_DETAIL) },
                                    onInspectWeaver = { onNavigate(AppScreen.WEAVER_PROFILE) },
                                    onVerifyQr = { onNavigate(AppScreen.QR_VERIFICATION) },
                                    onToast = onToast
                                )
                            }
                        }
                    } else {
                        MasonryExploreGrid(
                            items = masonryItems,
                            onSelectSaree = { item ->
                                onToast(item.title)
                                onNavigate(AppScreen.PRODUCT_DETAIL)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SellerRawMaterialSection(
    rawMaterials: List<RawMaterialPost>,
    showComposer: Boolean,
    onToggleComposer: () -> Unit,
    onAddLot: (RawMaterialPost) -> Unit,
    onToast: (String) -> Unit
) {
    var yarnTitleInput by remember { mutableStateOf("2/100 Sambalpuri Bandha Silk") }
    var priceInput by remember { mutableStateOf("₹3,950 / kg") }
    var moqInput by remember { mutableStateOf("Min 4 kg") }

    val lotPublishedMsg = tr("Raw Material Lot Posted to Sellers", "କଞ୍ଚା ସୂତା ଲଟ୍ ପ୍ରକାଶିତ ହେଲା", "कच्चा सूत लॉट पोस्ट किया गया")
    val defaultSupplier = tr("Bargarh Weaver Guild", "ବରଗଡ଼ ବୁଣାକାର ସଂଘ", "बरगढ़ बुनकर संघ")
    val defaultBadge = tr("SELLER B2B", "ବିକ୍ରେତା B2B", "विक्रेता B2B")
    val defaultCategory = tr("SURPLUS YARN LOT", "ବଳକା ସୂତା ଲଟ୍", "अतिरिक्त सूत लॉट")
    val defaultGrade = tr("Grade-A", "ଗ୍ରେଡ୍-A", "ग्रेड-A")
    val defaultStock = tr("60 kg Ready", "୬୦ କି.ଗ୍ରା. ପ୍ରସ୍ତୁତ", "60 कि.ग्रा. तैयार")
    val defaultWaText = tr("Order Yarn Lot", "ସୂତା ଅର୍ଡର କରନ୍ତୁ", "सूत ऑर्डर करें")
    val defaultSpec1 = tr("100% Pure", "୧୦୦% ଶୁଦ୍ଧ", "100% शुद्ध")
    val defaultSpec2 = tr("Co-op Tested", "ସମିତି ପରୀକ୍ଷିତ", "समिति परीक्षित")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Compact Visual Seller-Only B2B Banner + "+ Post Lot" Action
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = TertiaryFixed.copy(alpha = 0.45f),
            border = BorderStroke(1.dp, Tertiary.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Tertiary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = tr("SELLER-TO-SELLER ONLY", "କେବଳ ବିକ୍ରେତାଙ୍କ ମଧ୍ୟରେ", "केवल विक्रेताओं के बीच"),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Tertiary
                        )
                        Text(
                            text = tr("Raw Silk Yarn & Natural Dyes", "କଞ୍ଚା ପାଟ ସୂତା ଓ ପ୍ରାକୃତିକ ରଙ୍ଗ", "कच्चा रेशम सूत व प्राकृतिक डाई"),
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Surface(
                    onClick = onToggleComposer,
                    shape = CircleShape,
                    color = Tertiary,
                    modifier = Modifier.testTag("toggle_raw_lot_composer_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = tr("Sell Yarn", "ସୂତା ବିକନ୍ତୁ", "सूत बेचें"),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }

        // Quick Seller Raw Material Composer
        AnimatedVisibility(visible = showComposer) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = SurfaceContainerLowest,
                border = BorderStroke(1.dp, Tertiary.copy(alpha = 0.45f)),
                shadowElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = tr("Post Surplus Yarn / Dye Lot (Seller B2B)", "ବିକ୍ରେତା B2B ପାଇଁ ସୂତା / ରଙ୍ଗ ପୋଷ୍ଟ କରନ୍ତୁ", "विक्रेता B2B के लिए सूत / डाई पोस्ट करें"),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Tertiary
                    )
                    OutlinedTextField(
                        value = yarnTitleInput,
                        onValueChange = { yarnTitleInput = it },
                        singleLine = true,
                        label = { Text(tr("Material", "ସାମଗ୍ରୀ", "सामग्री")) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("raw_lot_title_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Tertiary,
                            unfocusedBorderColor = OutlineVariant
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = priceInput,
                            onValueChange = { priceInput = it },
                            singleLine = true,
                            label = { Text(tr("Rate", "ଦର", "दर")) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Tertiary,
                                unfocusedBorderColor = OutlineVariant
                            )
                        )
                        OutlinedTextField(
                            value = moqInput,
                            onValueChange = { moqInput = it },
                            singleLine = true,
                            label = { Text("MOQ") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Tertiary,
                                unfocusedBorderColor = OutlineVariant
                            )
                        )
                    }
                    SpringBounceButton(
                        onClick = {
                            if (yarnTitleInput.isNotBlank()) {
                                onAddLot(
                                    RawMaterialPost(
                                        id = "raw_custom_${rawMaterials.size + 1}",
                                        supplierName = defaultSupplier,
                                        supplierAvatarUrl = VastramRepository.HEADER_PROFILE_AVATAR,
                                        clusterBadge = defaultBadge,
                                        depotId = "LOT #BG-24",
                                        materialImageUrl = VastramRepository.IMG_RAW_SILK_CONES,
                                        categoryPill = defaultCategory,
                                        gradePill = defaultGrade,
                                        title = yarnTitleInput,
                                        specChips = listOf(defaultSpec1, defaultSpec2),
                                        pricePerUnit = priceInput,
                                        moqPill = moqInput,
                                        stockPill = defaultStock,
                                        whatsappText = defaultWaText,
                                        whatsappUrl = "https://wa.me/919437000105"
                                    )
                                )
                                onToast(lotPublishedMsg)
                            }
                        },
                        backgroundColor = Tertiary,
                        contentColor = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("publish_raw_lot_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = tr("Publish to Sellers", "ବିକ୍ରେତାଙ୍କୁ ପଠାନ୍ତୁ", "विक्रेताओं को पोस्ट करें"),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 3 Seller-to-Seller Raw Material Posts
        rawMaterials.forEach { rawPost ->
            RawMaterialSellerCard(
                post = rawPost,
                onToast = onToast
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RawMaterialSellerCard(
    post: RawMaterialPost,
    onToast: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = SurfaceContainerLow,
        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.45f)),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("raw_material_card_${post.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Supplier Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    VastramAsyncImage(
                        url = post.supplierAvatarUrl,
                        contentDescription = post.supplierName,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = post.supplierName,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Surface(
                                shape = CircleShape,
                                color = TertiaryFixed
                            ) {
                                Text(
                                    text = post.clusterBadge,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = OnTertiaryFixed,
                                    maxLines = 1,
                                    softWrap = false,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = post.depotId,
                            style = MaterialTheme.typography.labelSmall,
                            color = Tertiary,
                            maxLines = 1
                        )
                    }
                }
            }

            // Visual Raw Material Photo with Category + Grade Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.15f)
                    .background(SurfaceContainer)
            ) {
                VastramAsyncImage(
                    url = post.materialImageUrl,
                    contentDescription = post.title,
                    modifier = Modifier.fillMaxSize()
                )

                Surface(
                    shape = CircleShape,
                    color = InverseSurface.copy(alpha = 0.88f),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = TertiaryFixed,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = post.categoryPill,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = InverseOnSurface,
                            maxLines = 1
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = SurfaceContainerLowest.copy(alpha = 0.94f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
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
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = post.gradePill,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface,
                            maxLines = 1
                        )
                    }
                }
            }

            // Minimal Visual Specs + Wholesale Price + Direct Seller Order CTA
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Visual Icon Chips (Rate, MOQ, Ready Stock)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = PrimaryFixed,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = post.pricePerUnit,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = OnPrimaryFixed,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SurfaceContainerHighest,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Scale,
                                contentDescription = null,
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = post.moqPill,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = TertiaryFixed,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = null,
                                tint = OnTertiaryFixed,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = post.stockPill,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = OnTertiaryFixed,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                // Spec Pills
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    post.specChips.forEach { chip ->
                        Surface(
                            shape = CircleShape,
                            color = SurfaceContainerLowest,
                            border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = chip,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = OnSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                WhatsAppDirectButton(
                    text = post.whatsappText,
                    url = post.whatsappUrl,
                    onToast = onToast
                )
            }
        }
    }
}

@Composable
private fun ViewModeChip(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    activeColor: Color = Primary,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (isSelected) SurfaceContainerLowest else Color.Transparent,
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        modifier = Modifier.testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) activeColor else OnSurfaceVariant,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                ),
                color = if (isSelected) activeColor else OnSurfaceVariant,
                maxLines = 1,
                softWrap = false
            )
        }
    }
}

@Composable
private fun StoryBubbleItem(
    story: StoryCircle,
    onClick: () -> Unit
) {
    val ringColor = when {
        story.isLive -> Primary
        story.id == "gi_certified" -> Tertiary
        else -> Secondary
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(68.dp)
            .clickable(onClick = onClick)
            .testTag("story_${story.id}")
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .border(2.dp, ringColor, CircleShape)
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerLowest)
            ) {
                VastramAsyncImage(
                    url = story.imageUrl,
                    contentDescription = story.title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            if (story.isLive) {
                Surface(
                    color = ErrorColor,
                    shape = CircleShape,
                    shadowElevation = 2.dp
                ) {
                    Text(
                        text = tr("LIVE", "ସିଧା", "लाइव"),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 8.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = OnErrorColor,
                        maxLines = 1,
                        softWrap = false,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = story.title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (story.isLive) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 10.5.sp
            ),
            color = OnSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EditorialFeedCard(
    post: FeedPost,
    isLiked: Boolean,
    isBookmarked: Boolean,
    isAudioPlaying: Boolean,
    onToggleLike: () -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleAudio: () -> Unit,
    onInspectProduct: () -> Unit,
    onInspectWeaver: () -> Unit,
    onVerifyQr: () -> Unit,
    onToast: (String) -> Unit
) {
    var showDoubleTapHeart by remember { mutableStateOf(false) }

    LaunchedEffect(showDoubleTapHeart) {
        if (showDoubleTapHeart) {
            delay(680)
            showDoubleTapHeart = false
        }
    }

    val likeScale by animateFloatAsState(
        targetValue = if (isLiked) 1.16f else 1.0f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 500f),
        label = "likeHeartScale"
    )

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = SurfaceContainerLow,
        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f)),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("feed_card_${post.id}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Compact Visual Author Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onInspectWeaver() }
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        VastramAsyncImage(
                            url = post.weaverAvatarUrl,
                            contentDescription = post.weaverName,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(SurfaceContainerLowest)
                                .padding(1.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (post.id == "post_raghunath") Icons.Default.Verified else Icons.Default.Eco,
                                contentDescription = null,
                                tint = Tertiary,
                                modifier = Modifier.size(11.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = post.weaverName,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp
                                ),
                                color = OnSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            Surface(
                                shape = CircleShape,
                                color = if (post.id == "post_raghunath") TertiaryFixed else SurfaceContainerHighest
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = if (post.id == "post_raghunath") Icons.Default.Shield else Icons.Default.Spa,
                                        contentDescription = null,
                                        tint = if (post.id == "post_raghunath") OnTertiaryFixed else OnSurfaceVariant,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Text(
                                        text = post.badgeLabel,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = if (post.id == "post_raghunath") OnTertiaryFixed else OnSurfaceVariant,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = post.locationCluster,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = OnSurfaceVariant,
                                maxLines = 1
                            )
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant
                            )
                            Text(
                                text = post.loomId,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Tertiary,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }

                IconButton(
                    onClick = { onInspectProduct() },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Visual Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(SurfaceContainer)
                    .pointerInput(post.id) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (!isLiked) onToggleLike()
                                showDoubleTapHeart = true
                            },
                            onTap = { onInspectProduct() }
                        )
                    }
                    .testTag("saree_showcase_${post.id}")
            ) {
                VastramAsyncImage(
                    url = post.productImageUrl,
                    contentDescription = post.highlightTitle,
                    modifier = Modifier.fillMaxSize()
                )

                Surface(
                    shape = CircleShape,
                    color = SurfaceContainerLowest.copy(alpha = 0.92f),
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = post.topBadgeText,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface,
                            maxLines = 1
                        )
                    }
                }

                if (post.bottomBadgeText.isNotEmpty()) {
                    Surface(
                        shape = CircleShape,
                        color = SurfaceContainerLowest.copy(alpha = 0.92f),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassBottom,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = post.bottomBadgeText,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface,
                                maxLines = 1
                            )
                        }
                    }
                }

                if (post.hasAudioNarration) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SurfaceContainerLowest.copy(alpha = 0.95f),
                        shadowElevation = 4.dp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (isAudioPlaying) Tertiary else Primary)
                                        .clickable { onToggleAudio() }
                                        .testTag("audio_narration_toggle"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isAudioPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = OnPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Text(
                                    text = post.audioTitle,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = OnSurface,
                                    maxLines = 1
                                )
                            }
                            AudioMiniWaveform(isPlaying = isAudioPlaying)
                        }
                    }
                }

                this@Column.AnimatedVisibility(
                    visible = showDoubleTapHeart,
                    enter = fadeIn(tween(150)) + scaleIn(initialScale = 0.5f, animationSpec = spring(dampingRatio = 0.5f)),
                    exit = fadeOut(tween(250)) + scaleOut(targetScale = 1.25f),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(Primary.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = PrimaryFixed,
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }
            }

            // Minimal Icon-Driven Action & Price Footer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Action Icons + Title on one visual row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = post.highlightTitle,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = OnSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .scale(likeScale)
                                .clickable { onToggleLike() }
                                .testTag("like_button_${post.id}")
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isLiked) Primary else OnSurface,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = (post.initialLikes + if (isLiked) 1 else 0).toString(),
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = Tertiary,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onVerifyQr() }
                        )

                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = if (isBookmarked) Primary else OnSurfaceVariant,
                            modifier = Modifier
                                .size(22.dp)
                                .clickable { onToggleBookmark() }
                                .testTag("bookmark_button_${post.id}")
                        )
                    }
                }

                // Price + GI Chip Strip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onInspectProduct() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = Tertiary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = post.priceDisplay,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface,
                                maxLines = 1
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SurfaceContainerLowest
                        ) {
                            Text(
                                text = post.certificateChip,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                color = OnSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Concise 1-line spec + WhatsApp Direct CTA
                Text(
                    text = post.captionBody,
                    style = MaterialTheme.typography.labelMedium,
                    color = OnSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                WhatsAppDirectButton(
                    text = post.whatsappText,
                    url = post.whatsappUrl,
                    onToast = onToast
                )
            }
        }
    }
}

@Composable
private fun AudioMiniWaveform(isPlaying: Boolean) {
    val transition = rememberInfiniteTransition(label = "miniWave")
    val anim by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(450), RepeatMode.Reverse),
        label = "barFactor"
    )
    val baseHeights = listOf(8.dp, 16.dp, 12.dp, 6.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.padding(end = 4.dp)
    ) {
        baseHeights.forEachIndexed { idx, h ->
            val factor = if (isPlaying) ((anim + idx * 0.2f) % 1.0f).coerceIn(0.35f, 1.0f) else 1f
            Box(
                modifier = Modifier
                    .width(2.5.dp)
                    .height(h * factor)
                    .clip(CircleShape)
                    .background(if (idx < 3) Primary else Outline)
            )
        }
    }
}

@Composable
private fun MasonryExploreGrid(
    items: List<GridSareeItem>,
    onSelectSaree: (GridSareeItem) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.chunked(2).forEach { rowPair ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowPair.forEach { item ->
                    Surface(
                        onClick = { onSelectSaree(item) },
                        shape = RoundedCornerShape(16.dp),
                        color = SurfaceContainerLow,
                        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f)),
                        shadowElevation = 2.dp,
                        modifier = Modifier.weight(1f)
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
                                Surface(
                                    color = TertiaryFixed,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = item.badge,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = OnTertiaryFixed,
                                        maxLines = 1,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier.padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = OnSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = item.price,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Primary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
                if (rowPair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
