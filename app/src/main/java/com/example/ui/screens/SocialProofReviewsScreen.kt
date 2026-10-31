package com.example.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppScreen
import com.example.data.LocalAppLanguage
import com.example.data.PatronReviewPost
import com.example.data.VastramRepository
import com.example.data.tr
import com.example.ui.components.SpringBounceButton
import com.example.ui.components.VastramAsyncImage
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OnTertiaryFixed
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.SaffronGold
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryFixed
import kotlinx.coroutines.delay

@Composable
fun SocialProofReviewsScreen(
    onNavigate: (AppScreen) -> Unit,
    onToast: (String) -> Unit
) {
    val lang = LocalAppLanguage.current
    val baseReviews = remember(lang) { VastramRepository.getPatronReviews(lang) }
    val reviews = remember(lang) {
        mutableStateListOf<PatronReviewPost>().apply { addAll(baseReviews) }
    }
    var selectedStarRating by remember { mutableIntStateOf(5) }
    var reviewText by remember(lang) {
        mutableStateOf(
            lang.pick(
                "Pure double ikat Sankha-Padma weave! #SambalpuriLove",
                "ଅସଲି ଡବଲ୍ ଇକତ ଶଙ୍ଖ-ପଦ୍ମ ବୁଣା! #SambalpuriLove",
                "शुद्ध डबल इकत शंख-पद्म बुनाई! #SambalpuriLove"
            )
        )
    }
    var reviewPublished by remember { mutableStateOf(false) }
    val likedMap = remember {
        mutableStateMapOf<String, Boolean>().apply {
            baseReviews.forEach { put(it.id, it.isInitiallyLiked) }
        }
    }
    val bookmarkedMap = remember {
        mutableStateMapOf<String, Boolean>().apply {
            baseReviews.forEach { put(it.id, it.isInitiallyBookmarked) }
        }
    }

    val publishedToast = tr(
        "Review Posted!",
        "ମତାମତ ପ୍ରକାଶିତ ହେଲା!",
        "समीक्षा पोस्ट हुई!"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .testTag("social_proof_reviews_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Visual Trust Strip
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SurfaceContainerLow,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = tr("Patron Drapes", "ଗ୍ରାହକ ଫଟୋ", "ग्राहक फ़ोटो"),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface
                        )
                        Text(
                            text = tr("99.4% GI Verified", "୯୯.୪% GI ପ୍ରମାଣିତ", "99.4% GI सत्यापित"),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Tertiary
                        )
                    }
                    Surface(
                        color = TertiaryFixed,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = tr("4.9 ★ (340+)", "୪.୯ ★ (୩୪୦+)", "4.9 ★ (340+)"),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnTertiaryFixed
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // 2. Compact Photo + Star Review Box
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SurfaceContainerLow,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.7f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            (1..5).forEach { star ->
                                val isSelected = star <= selectedStarRating
                                IconButton(
                                    onClick = { selectedStarRating = star },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .testTag("review_star_$star")
                                ) {
                                    Icon(
                                        imageVector = if (isSelected) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = "$star stars",
                                        tint = if (isSelected) SaffronGold else OutlineVariant,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        Surface(
                            color = TertiaryFixed,
                            shape = CircleShape
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = OnTertiaryFixed,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = tr("GI Buyer", "GI କ୍ରେତା", "GI खरीदार"),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnTertiaryFixed
                                    )
                                )
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, OutlineVariant, RoundedCornerShape(10.dp))
                        ) {
                            VastramAsyncImage(
                                url = baseReviews[0].mainPhotoUrl,
                                contentDescription = "Saree Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        OutlinedTextField(
                            value = reviewText,
                            onValueChange = {
                                reviewText = it
                                reviewPublished = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("review_note_input"),
                            maxLines = 2,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = OutlineVariant,
                                focusedContainerColor = SurfaceContainerLowest,
                                unfocusedContainerColor = SurfaceContainerLowest
                            )
                        )
                    }

                    val newPatronName = tr("You", "ଆପଣ", "आप")
                    val newRoleBadge = tr("Patron", "ଗ୍ରାହକ", "ग्राहक")
                    val newLocationWeave = tr("Bhubaneswar", "ଭୁବନେଶ୍ୱର", "भुवनेश्वर")
                    val newGiStamp = "GI #OD-8841"
                    val newSareeTitle = tr("Khandua Pata", "ଖଣ୍ଡୁଆ ପାଟ", "खंडुआ पाट")
                    val newPriceSplit = "₹18,500"
                    val newTimeAgo = tr("Now", "ଏବେ", "अभी")

                    SpringBounceButton(
                        onClick = {
                            if (reviewText.isNotBlank()) {
                                val newPost = PatronReviewPost(
                                    id = "rev_${reviews.size + 1}",
                                    patronName = newPatronName,
                                    roleBadge = newRoleBadge,
                                    locationAndWeave = newLocationWeave,
                                    avatarUrl = VastramRepository.HEADER_PROFILE_AVATAR,
                                    mainPhotoUrl = baseReviews[0].mainPhotoUrl,
                                    packshotUrl = baseReviews[0].packshotUrl,
                                    giStampText = newGiStamp,
                                    sareeTitle = newSareeTitle,
                                    sareePriceSplit = newPriceSplit,
                                    initialLikes = 1,
                                    certBadge = "$selectedStarRating.0 ★",
                                    quoteText = reviewText,
                                    hashtags = listOf("#SambalpuriLove"),
                                    timeAgo = newTimeAgo,
                                    isInitiallyLiked = true,
                                    isInitiallyBookmarked = false,
                                    hasAudioQr = true
                                )
                                reviews.add(0, newPost)
                                likedMap[newPost.id] = true
                                reviewPublished = true
                                onToast(publishedToast)
                            }
                        },
                        backgroundColor = if (reviewPublished) Tertiary else Primary,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(vertical = 10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("publish_review_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (reviewPublished) Icons.Default.CheckCircle else Icons.Default.Send,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (reviewPublished) tr("Posted", "ପ୍ରକାଶିତ", "पोस्ट हुआ") else tr("Post Photo Review", "ଫଟୋ ମତାମତ ଦିଅନ୍ତୁ", "फ़ोटो समीक्षा पोस्ट करें"),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 3. Visual Patron Photo Cards
        items(reviews, key = { it.id }) { post ->
            val isLiked = likedMap[post.id] ?: post.isInitiallyLiked
            val isBookmarked = bookmarkedMap[post.id] ?: post.isInitiallyBookmarked

            PatronReviewCard(
                post = post,
                isLiked = isLiked,
                isBookmarked = isBookmarked,
                onToggleLike = { likedMap[post.id] = !isLiked },
                onToggleBookmark = { bookmarkedMap[post.id] = !isBookmarked },
                onInspectSaree = { onNavigate(AppScreen.PRODUCT_DETAIL) }
            )
        }
    }
}

@Composable
private fun PatronReviewCard(
    post: PatronReviewPost,
    isLiked: Boolean,
    isBookmarked: Boolean,
    onToggleLike: () -> Unit,
    onToggleBookmark: () -> Unit,
    onInspectSaree: () -> Unit
) {
    var showHeartOverlay by remember { mutableStateOf(false) }
    val heartScale by animateFloatAsState(
        targetValue = if (showHeartOverlay) 1.15f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "review_heart"
    )

    LaunchedEffect(showHeartOverlay) {
        if (showHeartOverlay) {
            delay(650)
            showHeartOverlay = false
        }
    }

    val likeCount = post.initialLikes +
        (if (isLiked && !post.isInitiallyLiked) 1 else 0) -
        (if (!isLiked && post.isInitiallyLiked) 1 else 0)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("review_card_${post.id}"),
        color = SurfaceContainerLowest,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.6f))
    ) {
        Column {
            // Compact Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    VastramAsyncImage(
                        url = post.avatarUrl,
                        contentDescription = post.patronName,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = post.patronName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = post.locationAndWeave,
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }

                Surface(
                    color = TertiaryFixed,
                    shape = CircleShape
                ) {
                    Text(
                        text = post.certBadge,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnTertiaryFixed
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Visual Photo Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                if (!isLiked) onToggleLike()
                                showHeartOverlay = true
                            }
                        )
                    }
            ) {
                VastramAsyncImage(
                    url = post.mainPhotoUrl,
                    contentDescription = post.sareeTitle,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Linked Saree Mini Chip
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                        .clickable(onClick = onInspectSaree),
                    color = SurfaceContainerLowest.copy(alpha = 0.94f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "${post.sareeTitle} • ${post.sareePriceSplit}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface
                        )
                        Icon(
                            imageVector = Icons.Default.NorthEast,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }

                if (heartScale > 0.05f) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = OnPrimary,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(72.dp)
                            .scale(heartScale)
                    )
                }
            }

            // Icon Actions + Short Caption
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButton(
                            onClick = onToggleLike,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("review_like_${post.id}")
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isLiked) Primary else OnSurface
                            )
                        }
                        Text(
                            text = "$likeCount",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface
                        )
                    }

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = if (isBookmarked) Primary else OnSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = post.quoteText,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
