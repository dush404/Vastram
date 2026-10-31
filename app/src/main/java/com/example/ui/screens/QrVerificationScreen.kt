package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppScreen
import com.example.data.VastramRepository
import com.example.data.tr
import com.example.ui.components.SpringBounceButton
import com.example.ui.components.VastramAsyncImage
import com.example.ui.components.WhatsAppDirectButton
import com.example.ui.theme.InverseSurface
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OnTertiary
import com.example.ui.theme.OnTertiaryFixed
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryContainer
import com.example.ui.theme.TertiaryFixed
import kotlinx.coroutines.delay

@Composable
fun QrVerificationScreen(
    onNavigate: (AppScreen) -> Unit,
    onToast: (String) -> Unit
) {
    var isPlayingAudio by remember { mutableStateOf(false) }
    var claimState by remember { mutableIntStateOf(0) }

    val certificateToast = tr(
        "Certificate Claimed #OD-9921!",
        "ପ୍ରମାଣପତ୍ର ଜାରି ହେଲା #OD-9921!",
        "प्रमाणपत्र जारी #OD-9921!"
    )

    LaunchedEffect(claimState) {
        if (claimState == 1) {
            delay(800)
            claimState = 2
            onToast(certificateToast)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "qrScanPulse")
    val reticleScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "reticleRing"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("qr_verification_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Visual Scanner Reticle HUD
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(InverseSurface)
            ) {
                VastramAsyncImage(
                    url = VastramRepository.IMG_KOTPAD_WEAVE,
                    contentDescription = tr("Pallu Scan", "ପଣତ ସ୍କାନ୍", "पल्लू स्कैन"),
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.55f)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = InverseSurface.copy(alpha = 0.88f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = null,
                                    tint = TertiaryFixed,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = tr("NFC + QR MATCH", "NFC + QR ସଠିକ୍", "NFC + QR मिलान"),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = SurfaceContainerLowest
                                )
                            }
                        }

                        Text(
                            text = "20.52° N · 85.62° E",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp
                            ),
                            color = TertiaryFixed
                        )
                    }

                    // Center Laser Target
                    Box(
                        modifier = Modifier
                            .size(104.dp)
                            .align(Alignment.CenterHorizontally),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .scale(reticleScale)
                                .clip(CircleShape)
                                .background(Tertiary.copy(alpha = 0.25f))
                        )
                        Canvas(modifier = Modifier.size(88.dp)) {
                            drawCircle(
                                color = TertiaryFixed,
                                style = Stroke(width = 4.dp.toPx())
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = SurfaceContainerLowest.copy(alpha = 0.95f)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = Tertiary,
                                    modifier = Modifier.size(26.dp)
                                )
                                Text(
                                    text = "100% GI",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                    color = Tertiary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        // 2. Compact Cryptographic Seal Card
        item {
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = SurfaceContainerLowest,
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.45f)),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(TertiaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = OnTertiary,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = CircleShape,
                                color = TertiaryFixed
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = OnTertiaryFixed,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Text(
                                        text = "GI-OD-2026-NUA-8831",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = OnTertiaryFixed
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tr("Khandua Pata Silk", "ଖଣ୍ଡୁଆ ପାଟ ରେଶମ", "खंडुआ पाट रेशम"),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface
                            )
                        }
                    }

                    // 2 Visual Spec Tiles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SurfaceContainerLow,
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Fingerprint,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Column {
                                    Text(
                                        text = tr("4-Ply Tussar", "୪-ପ୍ଲାଏ ତସର", "4-प्लाई तसर"),
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = OnSurface
                                    )
                                    Text(
                                        text = tr("Loom #BM-104", "ତନ୍ତ #BM-104", "करघा #BM-104"),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = OnSurfaceVariant
                                    )
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SurfaceContainerLow,
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Eco,
                                    contentDescription = null,
                                    tint = Tertiary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Column {
                                    Text(
                                        text = tr("Aal Root Dye", "ଆଳ ମୂଳ ରଙ୍ଗ", "आल जड़ डाई"),
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = OnSurface
                                    )
                                    Text(
                                        text = tr("24 Loom Days", "୨୪ ବୁଣା ଦିନ", "24 बुनाई दिन"),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = OnSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    // Compact Acoustic Proof Strip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceContainerHigh,
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
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isPlayingAudio) Tertiary else Primary)
                                        .clickable { isPlayingAudio = !isPlayingAudio }
                                        .testTag("qr_audio_play_button"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isPlayingAudio) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = OnPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = tr("Shuttle Acoustic Signature", "ତନ୍ତ ଶବ୍ଦ ସ୍ୱାକ୍ଷର", "करघा ध्वनि हस्ताक्षर"),
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = OnSurface
                                    )
                                    Text(
                                        text = "0:18 · Nuapatna Loom #BM-104",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = OnSurfaceVariant
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Claim Certificate Action
                    SpringBounceButton(
                        onClick = { claimState = 1 },
                        backgroundColor = if (claimState == 2) Tertiary else Primary,
                        contentColor = OnPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("claim_ownership_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (claimState == 2) Icons.Default.CheckCircle else Icons.Default.AssignmentTurnedIn,
                                contentDescription = null,
                                tint = OnPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (claimState == 2) {
                                    tr("Certificate Issued #OD-9921", "ପ୍ରମାଣପତ୍ର ଜାରି #OD-9921", "प्रमाणपत्र जारी #OD-9921")
                                } else {
                                    tr("Claim GI Ownership Seal", "GI ମାଲିକାନା ପ୍ରମାଣପତ୍ର ନିଅନ୍ତୁ", "GI स्वामित्व प्रमाणपत्र लें")
                                },
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = OnPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    WhatsAppDirectButton(
                        text = tr("Share GI Certificate on WhatsApp", "ହ୍ୱାଟ୍ସଆପ୍ ରେ GI ପ୍ରମାଣପତ୍ର ପଠାନ୍ତୁ", "व्हाट्सएप पर GI प्रमाणपत्र साझा करें"),
                        url = "https://wa.me/919437000108",
                        onToast = onToast
                    )
                }
            }
        }
    }
}
