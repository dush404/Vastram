package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppScreen
import com.example.data.KanbanOrder
import com.example.data.LocalAppLanguage
import com.example.data.VastramRepository
import com.example.data.tr
import com.example.ui.components.SpringBounceButton
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnPrimaryFixed
import com.example.ui.theme.OnSecondaryFixed
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OnTertiaryFixed
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryFixed

private data class KanbanStageFilter(
    val key: String,
    val label: String
)

@Composable
fun ClusterManagerScreen(
    onNavigate: (AppScreen) -> Unit,
    onToast: (String) -> Unit
) {
    val lang = LocalAppLanguage.current
    var selectedStageKey by remember { mutableStateOf("all") }
    var loggedExtraMeters by remember { mutableIntStateOf(0) }
    var batchRemittanceExecuted by remember { mutableStateOf(false) }

    val stageFilters = remember(lang) {
        listOf(
            KanbanStageFilter("all", lang.pick("All (47)", "ସମସ୍ତ (୪୭)", "सभी (47)")),
            KanbanStageFilter("booked", lang.pick("Booked (8)", "ବୁକ୍ (୮)", "बुक (8)")),
            KanbanStageFilter("weaving", lang.pick("Loom (19)", "ତନ୍ତ (୧୯)", "लूम (19)")),
            KanbanStageFilter("bandha", lang.pick("Bandha (12)", "ବାନ୍ଧ (୧୨)", "बांध (12)")),
            KanbanStageFilter("qc", lang.pick("GI QC (5)", "ଯାଞ୍ଚ (୫)", "जांच (5)")),
            KanbanStageFilter("dispatched", lang.pick("Sent (3)", "ପଠାଯାଇଛି (୩)", "भेजे (3)"))
        )
    }

    val allKanbanOrders = remember(lang) { VastramRepository.getKanbanOrders(lang) }
    val filteredKanbanItems = remember(selectedStageKey, allKanbanOrders) {
        if (selectedStageKey == "all") allKanbanOrders
        else allKanbanOrders.filter { it.stageKey == selectedStageKey }
    }

    val printWaybillToast = tr(
        "Waybill SP-OR-NUAP-2024-B2B108 Printed",
        "ୱେବିଲ୍ ପ୍ରିଣ୍ଟ ହେଲା",
        "वेबिल प्रिंट हुआ"
    )
    val remittanceToast = tr(
        "Direct UPI Payout sent to 18 weavers!",
        "୧୮ ବୁଣାକାରଙ୍କୁ ସିଧାସଳଖ UPI ପୈଠ ହେଲା!",
        "18 बुनकरों को सीधा UPI भुगतान भेजा गया!"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .testTag("cluster_manager_list"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Compact Cluster Header & 3 KPI Pills
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = tr("Nuapatna Co-op #04", "ନୂଆପାଟଣା ସଂଘ #୦୪", "नुआपाटना संघ #04"),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface
                        )
                        Text(
                            text = tr("18 Active Pit Looms", "୧୮ ସକ୍ରିୟ ଗାତ ତନ୍ତ", "18 सक्रिय पिट लूम"),
                            style = MaterialTheme.typography.labelSmall,
                            color = OnSurfaceVariant
                        )
                    }

                    Surface(
                        color = TertiaryFixed,
                        shape = CircleShape
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(Tertiary)
                            )
                            Text(
                                text = "92% On-Time",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnTertiaryFixed
                            )
                        }
                    }
                }

                // Visual KPI Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        color = SurfaceContainerLow,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = tr("LOOMS", "ତନ୍ତ", "करघे"),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurfaceVariant
                                )
                            )
                            Text(
                                text = tr("47 Sarees", "୪୭ ଶାଢ଼ୀ", "47 साड़ियाँ"),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = OnSurface
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier.weight(1f),
                        color = SurfaceContainerLow,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = tr("PIPELINE", "ମୂଲ୍ୟ", "मूल्य"),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurfaceVariant
                                )
                            )
                            Text(
                                text = tr("₹8.42L", "₹୮.୪୨ ଲକ୍ଷ", "₹8.42 लाख"),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = Primary
                            )
                        }
                    }
                }
            }
        }

        // 2. Stage Filter Chips
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = tr("Kanban", "କାର୍ଯ୍ୟ ତାଲିକା", "कानबान"),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface
                        )
                    }

                    Surface(
                        color = PrimaryFixed,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable { onNavigate(AppScreen.LOOM_DESK) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = OnPrimaryFixed,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = tr("Looms", "ତନ୍ତ", "करघा"),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = OnPrimaryFixed
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stageFilters.forEach { stage ->
                        KanbanStageChip(
                            label = stage.label,
                            selected = selectedStageKey == stage.key,
                            onClick = { selectedStageKey = stage.key },
                            testTag = "kanban_filter_${stage.key}"
                        )
                    }
                }
            }
        }

        // 3. Visual Kanban Order Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                filteredKanbanItems.forEach { order ->
                    val weftToastMsg = tr(
                        "+1 Saree Logged • ${order.orderCode}",
                        "+୧ ଶାଢ଼ୀ ରେକର୍ଡ • ${order.orderCode}",
                        "+1 साड़ी दर्ज • ${order.orderCode}"
                    )
                    KanbanOrderCard(
                        order = order,
                        extraMetersLogged = if (order.id == "B2B-108") loggedExtraMeters else 0,
                        onActionClick = {
                            when (order.stageKey) {
                                "weaving" -> {
                                    loggedExtraMeters += 1
                                    onToast(weftToastMsg)
                                }
                                "qc" -> onNavigate(AppScreen.QR_VERIFICATION)
                                "dispatched" -> onNavigate(AppScreen.ORDER_TRACKING)
                                else -> onToast(weftToastMsg)
                            }
                        }
                    )
                }
            }
        }

        // 4. Compact Speed Post Label + Direct Weaver Payout Actions
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SpringBounceButton(
                    onClick = { onToast(printWaybillToast) },
                    backgroundColor = SurfaceContainerLow,
                    contentColor = OnSurface,
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(vertical = 14.dp, horizontal = 12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("print_waybill_btn")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = tr("Waybill", "ୱେବିଲ୍", "वेबिल"),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface
                        )
                    }
                }

                SpringBounceButton(
                    onClick = {
                        batchRemittanceExecuted = true
                        onToast(remittanceToast)
                    },
                    backgroundColor = if (batchRemittanceExecuted) Tertiary else Primary,
                    contentColor = OnPrimary,
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(vertical = 14.dp, horizontal = 12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("execute_remittance_btn")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (batchRemittanceExecuted) Icons.Default.CheckCircle else Icons.Default.Send,
                            contentDescription = null,
                            tint = OnPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (batchRemittanceExecuted) tr("Paid 18", "୧୮ ପୈଠ", "18 भुगतान") else tr("Pay Weavers", "ବୁଣାକାର ପୈଠ", "बुनकर भुगतान"),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = OnPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KanbanStageChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val bgColor by animateColorAsState(
        targetValue = if (selected) Primary else SurfaceContainerLow,
        animationSpec = tween(180),
        label = "chip_bg"
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) OnPrimary else OnSurfaceVariant,
        animationSpec = tween(180),
        label = "chip_txt"
    )

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .clickable(onClick = onClick)
            .testTag(testTag),
        color = bgColor,
        shape = RoundedCornerShape(999.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) Primary else OutlineVariant.copy(alpha = 0.6f)
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                color = textColor
            ),
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun KanbanOrderCard(
    order: KanbanOrder,
    extraMetersLogged: Int,
    onActionClick: () -> Unit
) {
    val baseProgress = order.progressRatio ?: 0.5f
    val progress = (baseProgress + (extraMetersLogged * 0.08f)).coerceIn(0f, 1f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("kanban_card_${order.id}"),
        color = SurfaceContainerLowest,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, OutlineVariant.copy(alpha = 0.6f))
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
                    Surface(
                        color = PrimaryFixed,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = order.stageBadge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnPrimaryFixed
                            ),
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                    Text(
                        text = order.orderCode,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = OnSurfaceVariant
                    )
                }

                Text(
                    text = order.statusPill,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Primary
                )
            }

            Text(
                text = order.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = OnSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Progress Bar
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = order.leadWeaver,
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceVariant
                    )
                    Text(
                        text = order.progressLabel ?: order.clientDetail,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Tertiary
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(SurfaceContainerHigh)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(Tertiary)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.clientName,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = OnSurfaceVariant
                )

                SpringBounceButton(
                    onClick = onActionClick,
                    backgroundColor = Primary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("kanban_action_${order.id}")
                ) {
                    Text(
                        text = order.subNote,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}
