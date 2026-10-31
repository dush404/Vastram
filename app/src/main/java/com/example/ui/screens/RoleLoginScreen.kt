package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DryCleaning
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.UserRole
import com.example.data.VastramRepository
import com.example.data.tr
import com.example.ui.components.VastramAsyncImage
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryFixed

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoleLoginScreen(
    selectedLanguage: AppLanguage,
    onSelectLanguage: (AppLanguage) -> Unit,
    onLoginWithRole: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRole by remember { mutableStateOf(UserRole.BUYER) }
    var credentialInput by remember { mutableStateOf("+91 94370 18290") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Surface)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .testTag("role_login_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Minimal Top Brand + Language Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "V",
                        color = OnPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Column {
                    Text(
                        text = tr("VASTRAM", "ବସ୍ତ୍ରମ୍", "वस्त्रम्"),
                        style = MaterialTheme.typography.titleMedium.copy(
                            letterSpacing = 1.2.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = OnSurface
                    )
                    Text(
                        text = tr("GI HANDLOOM", "GI ହସ୍ତତନ୍ତ", "GI हथकरघा"),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Primary
                    )
                }
            }

            // Single-Tap Language Pills
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(SurfaceContainerHigh)
                    .border(1.dp, OutlineVariant.copy(alpha = 0.6f), RoundedCornerShape(50))
                    .padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                AppLanguage.entries.forEach { lang ->
                    val isLangSelected = lang == selectedLanguage
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (isLangSelected) Primary else Color.Transparent)
                            .clickable { onSelectLanguage(lang) }
                            .padding(horizontal = 9.dp, vertical = 5.dp)
                            .testTag("login_lang_${lang.code}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lang.code,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isLangSelected) OnPrimary else OnSurface
                        )
                    }
                }
            }
        }

        // Visual Hero Card (Changes dynamically for Buyer vs Seller)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(172.dp)
                .clip(RoundedCornerShape(22.dp))
                .border(1.dp, OutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(22.dp))
        ) {
            VastramAsyncImage(
                url = if (selectedRole == UserRole.BUYER) {
                    VastramRepository.IMG_BOMKAI_HERO
                } else {
                    VastramRepository.IMG_RAW_SILK_CONES
                },
                contentDescription = tr("Vastram Portal", "ବସ୍ତ୍ରମ୍ ପୋର୍ଟାଲ୍", "वस्त्रम् पोर्टल"),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(OnSurface.copy(alpha = 0.45f))
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (selectedRole == UserRole.BUYER) Primary else Tertiary
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = if (selectedRole == UserRole.BUYER) Icons.Filled.ShoppingBag else Icons.Filled.Storefront,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = if (selectedRole == UserRole.BUYER) {
                                tr("BUYER · CLOTHES ONLY", "କ୍ରେତା · କେବଳ ବସ୍ତ୍ର", "खरीदार · केवल वस्त्र")
                            } else {
                                tr("SELLER · LOOMS & RAW YARN", "ବିକ୍ରେତା · ତନ୍ତ ଓ କଞ୍ଚା ସୂତା", "विक्रेता · करघा व कच्चा सूत")
                            },
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }

                Text(
                    text = if (selectedRole == UserRole.BUYER) {
                        tr("Shop GI Handlooms", "GI ହସ୍ତତନ୍ତ ଶାଢ଼ୀ କିଣନ୍ତୁ", "GI हथकरघा साड़ियां खरीदें")
                    } else {
                        tr("Sell Weaves & Trade Yarn", "ଶାଢ଼ୀ ବিক୍ରି ଓ କଞ୍ଚା ସୂତା କାରବାର", "बुनाई बेचें और कच्चा सूत व्यापार")
                    },
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }

        // Visual Role Cards (Buyer vs Seller)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RoleVisualCard(
                isSelected = selectedRole == UserRole.BUYER,
                title = tr("Buyer", "କ୍ରେତା", "खरीदार"),
                subtitle = tr("Buy Clothes", "ବସ୍ତ୍ର କିଣନ୍ତୁ", "वस्त्र खरीदें"),
                primaryIcon = Icons.Filled.ShoppingBag,
                accentColor = Primary,
                containerColor = PrimaryFixed,
                featurePills = listOf(
                    Icons.Filled.DryCleaning to tr("Sarees", "ଶାଢ଼ୀ", "साड़ी"),
                    Icons.Filled.QrCodeScanner to tr("GI QR", "GI QR", "GI QR"),
                    Icons.Filled.LocalShipping to tr("Transit", "ଟ୍ରାକ୍", "ट्रैक")
                ),
                onSelect = {
                    selectedRole = UserRole.BUYER
                    credentialInput = "+91 94370 18290"
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("select_buyer_role_card")
            )

            RoleVisualCard(
                isSelected = selectedRole == UserRole.SELLER,
                title = tr("Seller", "ବିକ୍ରେତା", "विक्रेता"),
                subtitle = tr("Loom & Raw Yarn", "ତନ୍ତ ଓ କଞ୍ଚା ସୂତା", "करघा व कच्चा सूत"),
                primaryIcon = Icons.Filled.Storefront,
                accentColor = Tertiary,
                containerColor = TertiaryFixed,
                featurePills = listOf(
                    Icons.Filled.Inventory2 to tr("Raw Yarn", "କଞ୍ଚା ସୂତା", "कच्चा सूत"),
                    Icons.Filled.Mic to tr("Voice AI", "ସ୍ୱର AI", "वॉइस AI"),
                    Icons.Filled.Factory to tr("Looms", "ତନ୍ତ", "करघा")
                ),
                onSelect = {
                    selectedRole = UserRole.SELLER
                    credentialInput = "GI-OD-159-BGH"
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("select_seller_role_card")
            )
        }

        // Clean Login Box
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = SurfaceContainerLow,
            border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Verified,
                        contentDescription = null,
                        tint = if (selectedRole == UserRole.BUYER) Primary else Tertiary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (selectedRole == UserRole.BUYER) {
                            tr("Buyer Login · Finished Clothes", "କ୍ରେତା ଲଗଇନ୍ · କେବଳ ବସ୍ତ୍ର", "खरीदार लॉगिन · तैयार वस्त्र")
                        } else {
                            tr("Seller Login · Raw Yarn B2B + Looms", "ବିକ୍ରେତା ଲଗଇନ୍ · କଞ୍ଚା ସୂତା ଓ ତନ୍ତ", "विक्रेता लॉगिन · कच्चा सूत + करघा")
                        },
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = OnSurface
                    )
                }

                OutlinedTextField(
                    value = credentialInput,
                    onValueChange = { credentialInput = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_credential_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    label = {
                        Text(
                            text = if (selectedRole == UserRole.BUYER) {
                                tr("Mobile", "ମୋବାଇଲ୍", "मोबाइल")
                            } else {
                                tr("Weaver GI ID", "ବୁଣାକାର GI ID", "बुनकर GI ID")
                            }
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (selectedRole == UserRole.BUYER) Primary else Tertiary,
                        unfocusedBorderColor = OutlineVariant,
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest
                    )
                )

                Button(
                    onClick = { onLoginWithRole(selectedRole) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("continue_role_login_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedRole == UserRole.BUYER) Primary else Tertiary,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = if (selectedRole == UserRole.BUYER) Icons.Filled.ShoppingBag else Icons.Filled.Storefront,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedRole == UserRole.BUYER) {
                            tr("Continue as Buyer", "କ୍ରେତା ଭାବେ ପ୍ରବେଶ", "खरीदार के रूप में प्रवेश")
                        } else {
                            tr("Continue as Seller", "ବିକ୍ରେତା ଭାବେ ପ୍ରବେଶ", "विक्रेता के रूप में प्रवेश")
                        },
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Instant 1-Tap Role Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onLoginWithRole(UserRole.BUYER) }
                    .testTag("quick_buyer_login_btn"),
                shape = RoundedCornerShape(12.dp),
                color = SurfaceContainerLowest,
                border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingBag,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tr("1-Tap Buyer", "୧-ଟ୍ୟାପ୍ କ୍ରେତା", "1-टैप खरीदार"),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = OnSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onLoginWithRole(UserRole.SELLER) }
                    .testTag("quick_seller_login_btn"),
                shape = RoundedCornerShape(12.dp),
                color = TertiaryFixed.copy(alpha = 0.55f),
                border = BorderStroke(1.dp, Tertiary.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Inventory2,
                        contentDescription = null,
                        tint = Tertiary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tr("1-Tap Seller B2B", "୧-ଟ୍ୟାପ୍ ବିକ୍ରେତା", "1-टैप विक्रेता"),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Tertiary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RoleVisualCard(
    isSelected: Boolean,
    title: String,
    subtitle: String,
    primaryIcon: ImageVector,
    accentColor: Color,
    containerColor: Color,
    featurePills: List<Pair<ImageVector, String>>,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) SurfaceContainerLowest else SurfaceContainerLow,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) accentColor else OutlineVariant.copy(alpha = 0.6f)
        ),
        shadowElevation = if (isSelected) 4.dp else 0.dp
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
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) accentColor else containerColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = primaryIcon,
                        contentDescription = title,
                        tint = if (isSelected) Color.White else accentColor,
                        modifier = Modifier.size(21.dp)
                    )
                }

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                featurePills.forEach { (icon, label) ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SurfaceContainerHigh
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = OnSurface,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = OnSurface,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
