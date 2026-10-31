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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MarkunreadMailbox
import androidx.compose.material.icons.filled.Moving
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppScreen
import com.example.data.tr
import com.example.ui.components.SpringBounceButton
import com.example.ui.components.VastramAsyncImage
import com.example.ui.components.WhatsAppDirectButton
import com.example.ui.theme.InverseOnSurface
import com.example.ui.theme.InversePrimary
import com.example.ui.theme.InverseSurface
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnPrimaryFixed
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OnTertiary
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.Secondary
import com.example.ui.theme.SurfaceBright
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryFixed
import kotlinx.coroutines.delay

@Composable
fun OrderTrackingScreen(
    onNavigate: (AppScreen) -> Unit,
    onToast: (String) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var showCopiedNotice by remember { mutableStateOf(false) }

    val copiedToastMsg = tr(
        "Copied #SP-OD982341IN",
        "#SP-OD982341IN କପି ହେଲା",
        "#SP-OD982341IN कॉपी हो गया"
    )
    val videoToastMsg = tr(
        "Playing Loom Inspection Video...",
        "ତନ୍ତ ଯାଞ୍ଚ ଭିଡିଓ ଚାଲୁଛି...",
        "करघा वीडियो चलाया जा रहा है..."
    )
    val downloadCertToast = tr(
        "Downloading GI Certificate PDF...",
        "GI ପ୍ରମାଣପତ୍ର PDF ଡାଉନଲୋଡ୍ ହେଉଛି...",
        "GI प्रमाणपत्र PDF डाउनलोड हो रहा है..."
    )

    LaunchedEffect(showCopiedNotice) {
        if (showCopiedNotice) {
            delay(1800)
            showCopiedNotice = false
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "transitAnimations")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Restart),
        label = "syncSpin"
    )
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "pulseScale"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("order_tracking_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Live Status Pill Bar
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = SurfaceContainerLow,
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f)),
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
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Tertiary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = null,
                                tint = Tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = tr("DAY 4 / 6", "ଦିନ ୪ / ୬", "दिन 4 / 6"),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Primary
                            )
                            Text(
                                text = tr("In Transit", "ରାସ୍ତାରେ ଅଛି", "रास्ते में"),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface
                            )
                        }
                    }

                    Surface(shape = CircleShape, color = SurfaceContainerHighest) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(Tertiary)
                            )
                            Text(
                                text = tr("Speed Post", "ସ୍ପିଡ୍ ପୋଷ୍ଟ", "स्पीड पोस्ट"),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Order Visual Card
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SurfaceContainer,
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.45f)),
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
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "#VST-9421",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = OnSurfaceVariant
                                )
                                Surface(shape = CircleShape, color = PrimaryFixed) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Palette,
                                            contentDescription = null,
                                            tint = OnPrimaryFixed,
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Text(
                                            text = tr("Ikat", "ଇକତ", "इकत"),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = OnPrimaryFixed
                                        )
                                    }
                                }
                            }
                            Text(
                                text = tr("Passapalli Silk Saree", "ପାସ୍ସାପାଲି ପାଟ ଶାଢ଼ୀ", "पासापाली सिल्क साड़ी"),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = tr("Nuapatna Cluster", "ନୂଆପାଟଣା କ୍ଲଷ୍ଟର", "नुआपाटना क्लस्टर"),
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant
                            )
                        }

                        VastramAsyncImage(
                            url = "https://lh3.googleusercontent.com/aida-public/AB6AXuDKoUx52I_JQYxYCSyu0jlcyqmEVqob_Nbti4n8Nx4CQgcwdbDhx7VrXIIM1S_gztWhKnZzlfQw93KNOMgI_HFB_gXaZzJPHaT-xzGkEHiHaIwSO5U7ye2xKEqSUIWz7IA8hConvvYtEkSWlUbzMl8GUIXCM3o8sMql7jZwJQBNsNdXAjo0ADDw-wxqgP4pD1DfiUS5K2_hkz04BR4EIoqFGc77uJvG8LJP0K9cd7bI",
                            contentDescription = tr("Passapalli Silk", "ପାସ୍ସାପାଲି ପାଟ", "पासापाली सिल्क"),
                            modifier = Modifier
                                .size(68.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }

                    // Compact Escrow Split Strip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceContainerLowest,
                        modifier = Modifier.fillMaxWidth()
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
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalance,
                                    contentDescription = null,
                                    tint = Tertiary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = tr("88% Direct to Weaver", "୮୮% ସିଧାସଳଖ ବୁଣାକାରଙ୍କୁ", "88% सीधा बुनकर को"),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Tertiary
                                )
                            }
                            Text(
                                text = "₹17,400",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface
                            )
                        }
                    }
                }
            }
        }

        // Visual Route Map + Tracking Copy
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SurfaceContainerLow,
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Explore,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = tr("Cuttack → Mumbai", "କଟକ → ମୁମ୍ବାଇ", "कटक → मुंबई"),
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface
                            )
                        }
                        Surface(shape = CircleShape, color = TertiaryFixed.copy(alpha = 0.4f)) {
                            Text(
                                text = tr("2 Days Left", "୨ ଦିନ ବାକି", "2 दिन शेष"),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Tertiary,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SurfaceContainerHigh)
                    ) {
                        VastramAsyncImage(
                            url = "https://lh3.googleusercontent.com/aida-public/AB6AXuCBQrszh2r4yv8NmUV0eu2-f7AzcRCMAH-mYHA-_T1Ys48a8B60W4AM4oiYiTmER4o9C5xH0HfPnV-jdT09YrsHQhwq9dQefQeNhBoVZneVUlWTUG3e0yuK_cGt69ujPefwNJOZFZRDv0RTHVFvpfn0ll_vBaZa568diSKlJe1FiobteLpnw8S2uEnPsxBdqZnLp2nuIMmon03SSTnJu-UwDs_MJWYXSB8rQErAbOje",
                            contentDescription = tr("Route Map", "ମାନଚିତ୍ର", "मार्ग मानचित्र"),
                            modifier = Modifier.fillMaxSize()
                        )

                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .background(InverseSurface.copy(alpha = 0.82f))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tr("Nuapatna", "ନୂଆପାଟଣା", "नुआपाटना"),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = InverseOnSurface
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Moving,
                                        contentDescription = null,
                                        tint = TertiaryFixed,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "1,640 km",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SurfaceBright
                                    )
                                }
                                Text(
                                    text = tr("Mumbai", "ମୁମ୍ବାଇ", "मुंबई"),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = InversePrimary
                                )
                            }
                        }
                    }

                    // Tracking ID Bar
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceContainerLowest,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MarkunreadMailbox,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "#SP-OD982341IN",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = OnSurface
                                )
                            }

                            SpringBounceButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString("SP-OD982341IN"))
                                    showCopiedNotice = true
                                    onToast(copiedToastMsg)
                                },
                                backgroundColor = SurfaceContainerHigh,
                                contentColor = Primary,
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
                                modifier = Modifier.testTag("copy_tracking_button")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = if (showCopiedNotice) tr("Copied", "କପି ହେଲା", "कॉपी") else tr("Copy", "କପି", "कॉपी"),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (showCopiedNotice) Tertiary else Primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Visual Milestone Steps (Clean icon + photo strip, no long paragraphs)
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SurfaceContainerLowest,
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.45f)),
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
                        Text(
                            text = tr("Loom Milestones", "କାରିଗରୀ ପର୍ଯ୍ୟାୟ", "करघा चरण"),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface
                        )
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    CompactMilestoneRow(
                        title = tr("1. Silk Yarn Blessed", "୧. ରେଶମ ସୂତା ପୂଜା", "1. रेशम धागा पूजन"),
                        dateLabel = "14 Oct",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCd1kICmFS5Kopbbqjm1c0cG51gHtUpOzMpW030UTjT3ZkuMfUhS9oL_v79_bNyAcJ_bE7a-zYRwr8AhN9rLj-ysXLDRFrLlaqUzgsdOkjsAd0O4igQrAKPNXV7TNch0jO6w8jLdWk9A73qV8hlXvjMPlElTYl-_pcOP8RngRLE-qtcXlYbU0WqU5oj2c67a7pqTS9VDzOmz8pf82CgRw6PVDkTJZHi2J-M4tA4B3wv",
                        isCompleted = true,
                        isActive = false
                    )

                    CompactMilestoneRow(
                        title = tr("2. Pit Loom Weave (18d)", "୨. ଗାତ ତନ୍ତ ବୁଣା (୧୮ ଦିନ)", "2. पिट लूम बुनाई (18d)"),
                        dateLabel = "22 Oct",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDxTFulN-abtat47EHTz9CbeAxvUq-W7jK220xRAyIsMyu-DV6MrDTgoEVBc_1kyM3hITtHzDoMr6Pk2iuWWXnt-sHWGD00tSZqkkyO_HVqsnoKxVQmfVbe_-ugnRuS5qMap_GKXeS7MbUueoMN0siW4AffplJHdKaGipz6_1vMG2gPqm-P21f_pXiSfO1hoBRi25sxJaTTqOdCgeEe0nwx1OLENnS1Jd4vprV4viTl",
                        isCompleted = true,
                        isActive = false
                    )

                    CompactMilestoneRow(
                        title = tr("3. GI QR Pallu Seal", "୩. GI QR ଆଞ୍ଚଳ ସିଲ୍", "3. GI QR पल्लू सील"),
                        dateLabel = "26 Oct",
                        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDw5WyDujtTljdeo8YTq72ycO5v3-spF2LX7WDnJ-Y2tRiPbrultoA_g-xT5OtgGeqaGRePD3A1D05ucMKXiFK5xQdGbgvcOnxxiRu39OgMR_Ztqf2SCLIoVJFTw8m849UyrxKPMlqOI3LrX9UdY4GPigbWSNtRTF2Bzjp8oDQn0mbpY93fKNIEDyTOVU20BcCDK0LEOb4I0nnH4rwd3l3Baeskt1JIwETzZfTZQZla",
                        isCompleted = true,
                        isActive = false,
                        onVideoClick = { onToast(videoToastMsg) }
                    )

                    CompactMilestoneRow(
                        title = tr("4. Kolkata Hub Transit", "୪. କୋଲକାତା ହବ୍", "4. कोलकाता हब"),
                        dateLabel = tr("Live", "ଲାଇଭ୍", "लाइव"),
                        imageUrl = null,
                        isCompleted = false,
                        isActive = true,
                        spinAngle = spinAngle
                    )

                    CompactMilestoneRow(
                        title = tr("5. Doorstep Delivery", "୫. ଗୃହ ବିତରଣ", "5. होम डिलीवरी"),
                        dateLabel = tr("ETA 2d", "୨ ଦିନ", "2 दिन"),
                        imageUrl = null,
                        isCompleted = false,
                        isActive = false
                    )
                }
            }
        }

        // Bottom Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                WhatsAppDirectButton(
                    text = tr("Weaver Chat", "ବୁଣାକାର ଚାଟ୍", "बुनकर चैट"),
                    url = "https://wa.me/919437000000",
                    onToast = onToast,
                    modifier = Modifier.weight(1f)
                )

                SpringBounceButton(
                    onClick = { onToast(downloadCertToast) },
                    backgroundColor = SurfaceContainerHighest,
                    contentColor = OnSurface,
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(vertical = 13.dp, horizontal = 12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("download_gi_invoice_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = tr("GI PDF", "GI PDF", "GI PDF"),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactMilestoneRow(
    title: String,
    dateLabel: String,
    imageUrl: String?,
    isCompleted: Boolean,
    isActive: Boolean,
    spinAngle: Float = 0f,
    onVideoClick: (() -> Unit)? = null
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isActive) PrimaryFixed.copy(alpha = 0.25f) else SurfaceContainerLow,
        border = BorderStroke(
            1.dp,
            if (isActive) Primary.copy(alpha = 0.4f) else OutlineVariant.copy(alpha = 0.35f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
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
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCompleted -> Tertiary
                                isActive -> Primary
                                else -> SurfaceContainerHighest
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when {
                            isCompleted -> Icons.Default.Done
                            isActive -> Icons.Default.Sync
                            else -> Icons.Default.Lock
                        },
                        contentDescription = null,
                        tint = if (isCompleted) OnTertiary else if (isActive) OnPrimary else Outline,
                        modifier = Modifier
                            .size(15.dp)
                            .let { if (isActive) it.rotate(spinAngle) else it }
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = OnSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = dateLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isActive) Primary else OnSurfaceVariant
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (onVideoClick != null) {
                    SpringBounceButton(
                        onClick = onVideoClick,
                        backgroundColor = PrimaryFixed,
                        contentColor = OnPrimaryFixed,
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = null,
                                tint = OnPrimaryFixed,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "0:42",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = OnPrimaryFixed
                            )
                        }
                    }
                }

                if (imageUrl != null) {
                    VastramAsyncImage(
                        url = imageUrl,
                        contentDescription = title,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}
