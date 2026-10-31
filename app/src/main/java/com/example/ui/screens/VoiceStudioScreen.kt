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
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppScreen
import com.example.data.tr
import com.example.ui.components.SpringBounceButton
import com.example.ui.components.VastramAsyncImage
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSecondaryFixed
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OnTertiaryFixed
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryFixed
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
fun VoiceStudioScreen(
    onNavigate: (AppScreen) -> Unit,
    onToast: (String) -> Unit
) {
    var isRecording by remember { mutableStateOf(true) }
    var elapsedSeconds by remember { mutableIntStateOf(24) }
    var publishState by remember { mutableIntStateOf(0) }

    val publishedToastMsg = tr("Published to Feed!", "ଫିଡ୍ ରେ ପ୍ରକାଶିତ ହେଲା!", "फ़ीड पर प्रकाशित!")
    val cameraReadyToast = tr("Camera Ready", "କ୍ୟାମେରା ପ୍ରସ୍ତୁତ", "कैमरा तैयार")
    val listenBackToast = tr("Playing Voice Note...", "ସ୍ୱର ବାଜୁଛି...", "वॉइस नोट चल रहा है...")

    LaunchedEffect(isRecording) {
        while (isRecording) {
            delay(1000)
            elapsedSeconds++
        }
    }

    LaunchedEffect(publishState) {
        if (publishState == 1) {
            delay(700)
            publishState = 2
            onToast(publishedToastMsg)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "voiceStudioPulse")
    val outerHaloScale by infiniteTransition.animateFloat(
        initialValue = 0.90f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "outerHalo"
    )
    val waveFactor by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(tween(420), RepeatMode.Reverse),
        label = "waveAnim"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("voice_studio_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Visual Shot Strip (3 Photos + Add Shot)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShotThumb(
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCQfnF_Dhcjn-PVmZml-FuhFhvVgLTUcHFBGP11GSV4RoPqskBG61hSUzRgulSoj6eIzhynicQGPheOeQzOn1B9RVVo1ahbeNRsPqN7X1pfwG_3uMAXz9dv7f_mRXB950dFtWBwMy2EDsSb8PBk3q9ZR0kT1RfvomB3B2buqM72P7uijz8d1VQs3soGc5irifFAwhi4uhEs0QgYR0v_9T1GqzLOQ53P9tWJpiFQJBnq",
                    caption = tr("Pallu", "ଆଞ୍ଚଳ", "पल्लू"),
                    modifier = Modifier.weight(1f)
                )
                ShotThumb(
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDiGpIP322oBf1KUodDCc8I1Yn-SBHALY9LoUsF-O-ocVC_hrg65kQ4Rwfz-6pINoI98I-OUSsNXH3QrQLsqKF3QTf8RhoDYihUgApWcyCuWI4mXmfV3saIEwqx1sW27CCOaFXAGfeaO2e88GAs4oCjRLY_bxRnhmNG9ejf71fAYI2w5syr0YhBB5wXTLEUKQ6SH8RSEl57rz0JT4u-wrGvohuOQkYBF3SZ1qKjsj8v",
                    caption = tr("Border", "ଧଡ଼ି", "बॉर्डर"),
                    modifier = Modifier.weight(1f)
                )
                ShotThumb(
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDJNIdYy8l1Yh7qwAf6s3n3pE6G2iC0Ye6-DmP3au0MsXV0-p0g8IiL1u6N37jn3ivpCZd2j3ZG3rKB0LWfuEu2PmuMtbKs-uGKhDMK6VY-Z_ZZ7YxHRMQccZf8wz7j-xiH5H11rV-JpL5NusWGO4_zpI9RBy1NNK5JnzwThkJSpC8OucNcLNcr-IMkFjk2RUFAzUYNuXBu3BfvkRUVdUXYYeQOavG25k270xHHgWxC",
                    caption = tr("Weave", "ବୁଣା", "बुनाई"),
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    onClick = { onToast(cameraReadyToast) },
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceContainerHigh,
                    border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(0.8f)
                        .testTag("add_photo_button")
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = tr("Add Photo", "ଫଟୋ ଯୋଡ଼ନ୍ତୁ", "फ़ोटो जोड़ें"),
                            tint = Primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }

        // 2. Giant Pulsing Mic + Waveform
        item {
            val mins = (elapsedSeconds / 60).toString().padStart(2, '0')
            val secs = (elapsedSeconds % 60).toString().padStart(2, '0')

            Surface(
                shape = RoundedCornerShape(22.dp),
                color = SurfaceContainer,
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier.size(144.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(138.dp)
                                .scale(if (isRecording) outerHaloScale else 1f)
                                .clip(CircleShape)
                                .background(Primary.copy(alpha = 0.12f))
                        )
                        Surface(
                            onClick = { isRecording = !isRecording },
                            shape = CircleShape,
                            color = if (isRecording) Primary else Tertiary,
                            shadowElevation = 8.dp,
                            modifier = Modifier
                                .size(98.dp)
                                .testTag("voice_mic_button")
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = if (isRecording) Icons.Default.Mic else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = OnPrimary,
                                    modifier = Modifier.size(38.dp)
                                )
                                Text(
                                    text = "$mins:$secs",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = OnPrimary
                                )
                            }
                        }
                    }

                    // Waveform Strip
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = SurfaceContainerHighest,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                    ) {
                        val baseBars = listOf(12, 24, 30, 16, 26, 32, 20, 30, 22, 14, 26, 32, 16, 28, 18, 12)
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            baseBars.forEachIndexed { index, barH ->
                                val dynamicFactor = if (isRecording) {
                                    ((waveFactor + (index % 4) * 0.18f) % 1.0f).coerceIn(0.35f, 1.0f)
                                } else 0.65f
                                Box(
                                    modifier = Modifier
                                        .width(5.dp)
                                        .height((barH * dynamicFactor).dp)
                                        .clip(CircleShape)
                                        .background(Primary)
                                )
                            }
                        }
                    }

                    // Play / Reset Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SpringBounceButton(
                            onClick = { onToast(listenBackToast) },
                            backgroundColor = SecondaryFixed,
                            contentColor = OnSecondaryFixed,
                            shape = CircleShape,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("listen_back_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = OnSecondaryFixed,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = tr("Listen", "ଶୁଣନ୍ତୁ", "सुनें"),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = OnSecondaryFixed
                                )
                            }
                        }

                        SpringBounceButton(
                            onClick = {
                                elapsedSeconds = 0
                                isRecording = true
                            },
                            backgroundColor = SurfaceContainerHigh,
                            contentColor = OnSurface,
                            shape = CircleShape,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("re_record_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = OnSurface,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = tr("Reset", "ପୁଣି", "रीसेट"),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = OnSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Auto-Structured Visual Listing Card
        item {
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = SurfaceContainerLowest,
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.45f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = tr("Bomkai Tussar Silk", "ବୋମକାଇ ତସର ପାଟ", "बोमकाई तसर सिल्क"),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            shape = CircleShape,
                            color = TertiaryFixed
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
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
                                    text = "₹16,500",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = OnTertiaryFixed
                                )
                            }
                        }
                    }

                    // 2x2 Visual Spec Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SpecPillBox(
                            label = tr("PATTERN", "ଢାଞ୍ଚା", "पैटर्न"),
                            value = tr("Pasapalli", "پଶାପାଲି", "पशापाली"),
                            modifier = Modifier.weight(1f)
                        )
                        SpecPillBox(
                            label = tr("DYE", "ରଙ୍ଗ", "डाई"),
                            value = tr("Aal Bark", "ଆଳ ଛାଲ", "आल छाल"),
                            modifier = Modifier.weight(1f)
                        )
                        SpecPillBox(
                            label = tr("LOOM", "ତନ୍ତ", "करघा"),
                            value = tr("22 Days", "୨୨ ଦିନ", "22 दिन"),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    SpringBounceButton(
                        onClick = { publishState = 1 },
                        backgroundColor = if (publishState == 2) Tertiary else Primary,
                        contentColor = OnPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("publish_voice_listing_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (publishState == 2) Icons.Default.CheckCircle else Icons.Default.CloudUpload,
                                contentDescription = null,
                                tint = OnPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (publishState == 2) {
                                    tr("Published Live!", "ପ୍ରକାଶିତ ହେଲା!", "प्रकाशित!")
                                } else {
                                    tr("Publish Listing", "ତାଲିକା ପ୍ରକାଶ କରନ୍ତୁ", "लिस्टिंग प्रकाशित करें")
                                },
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = OnPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ShotThumb(
    imageUrl: String,
    caption: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(0.8f)
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainerHigh)
    ) {
        VastramAsyncImage(
            url = imageUrl,
            contentDescription = caption,
            modifier = Modifier.fillMaxSize()
        )
        Surface(
            color = OnSurface.copy(alpha = 0.75f),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(5.dp)
        ) {
            Text(
                text = caption,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
            )
        }
    }
}

@Composable
private fun SpecPillBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = SurfaceContainerLow,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                color = OnSurfaceVariant,
                maxLines = 1
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = OnSurface,
                maxLines = 1
            )
        }
    }
}
