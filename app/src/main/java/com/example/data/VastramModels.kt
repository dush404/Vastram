package com.example.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf

enum class UserRole {
    BUYER,
    SELLER
}

val LocalUserRole = compositionLocalOf { UserRole.BUYER }

enum class AppLanguage(val code: String, val displayLabel: String) {
    ENGLISH("EN", "English"),
    ODIA("ଓଡ଼ିଆ", "ଓଡ଼ିଆ"),
    HINDI("हिन्दी", "हिन्दी");

    fun pick(en: String, or: String, hi: String): String = when (this) {
        ENGLISH -> en
        ODIA -> or
        HINDI -> hi
    }
}

val LocalAppLanguage = compositionLocalOf { AppLanguage.ENGLISH }

@Composable
@ReadOnlyComposable
fun tr(en: String, or: String, hi: String): String {
    return LocalAppLanguage.current.pick(en, or, hi)
}

enum class AppScreen {
    DISCOVERY_FEED,
    PRODUCT_DETAIL,
    WEAVER_PROFILE,
    QR_VERIFICATION,
    VOICE_STUDIO,
    LOOM_DESK,
    ORDER_TRACKING,
    CLUSTER_MANAGER,
    SOCIAL_REVIEWS;

    fun label(lang: AppLanguage): String = when (this) {
        DISCOVERY_FEED -> lang.pick("Explore", "ଖୋଜନ୍ତୁ", "खोजें")
        PRODUCT_DETAIL -> lang.pick("Saree", "ଶାଢ଼ୀ", "साड़ी")
        WEAVER_PROFILE -> lang.pick("Atelier", "କର୍ମଶାଳା", "कार्यशाला")
        QR_VERIFICATION -> lang.pick("Verify QR", "QR ଯାଞ୍ଚ", "QR जांच")
        VOICE_STUDIO -> lang.pick("Voice AI", "ସ୍ୱର AI", "वॉइस AI")
        LOOM_DESK -> lang.pick("Loom Desk", "ତନ୍ତ ଡେସ୍କ", "करघा डेस्क")
        ORDER_TRACKING -> lang.pick("Transit", "ପରିବହନ", "ट्रांजिट")
        CLUSTER_MANAGER -> lang.pick("Cluster", "କ୍ଲଷ୍ଟର", "क्लस्टर")
        SOCIAL_REVIEWS -> lang.pick("Reviews", "ସମୀକ୍ଷା", "समीक्षा")
    }
}

data class StoryCircle(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val isLive: Boolean = false,
    val targetScreen: AppScreen = AppScreen.DISCOVERY_FEED
)

data class FeedPost(
    val id: String,
    val weaverName: String,
    val weaverAvatarUrl: String,
    val badgeLabel: String,
    val locationCluster: String,
    val loomId: String,
    val productImageUrl: String,
    val topBadgeText: String,
    val bottomBadgeText: String,
    val hasAudioNarration: Boolean,
    val audioTitle: String = "",
    val audioSubtitle: String = "",
    val initialLikes: Int,
    val commentsCount: Int,
    val whatsappText: String,
    val whatsappUrl: String,
    val priceDisplay: String,
    val payoutSubtitle: String,
    val certificateChip: String,
    val highlightTitle: String,
    val captionBody: String,
    val techFootnote: String,
    val hashtags: List<String>
)

data class RawMaterialPost(
    val id: String,
    val supplierName: String,
    val supplierAvatarUrl: String,
    val clusterBadge: String,
    val depotId: String,
    val materialImageUrl: String,
    val categoryPill: String,
    val gradePill: String,
    val title: String,
    val specChips: List<String>,
    val pricePerUnit: String,
    val moqPill: String,
    val stockPill: String,
    val whatsappText: String,
    val whatsappUrl: String
)

data class GridSareeItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val price: String,
    val badge: String,
    val imageUrl: String
)

data class AtelierItem(
    val id: String,
    val title: String,
    val price: String,
    val badge: String,
    val imageUrl: String
)

data class KanbanOrder(
    val id: String,
    val orderCode: String,
    val stageKey: String,
    val stageBadge: String,
    val statusPill: String,
    val title: String,
    val clientName: String,
    val clientDetail: String,
    val leadWeaver: String,
    val subNote: String,
    val progressRatio: Float? = null,
    val progressLabel: String? = null
)

data class PatronReviewPost(
    val id: String,
    val patronName: String,
    val roleBadge: String,
    val locationAndWeave: String,
    val avatarUrl: String,
    val mainPhotoUrl: String,
    val packshotUrl: String,
    val giStampText: String,
    val sareeTitle: String,
    val sareePriceSplit: String,
    val initialLikes: Int,
    val certBadge: String,
    val quoteText: String,
    val hashtags: List<String>,
    val timeAgo: String,
    val isInitiallyLiked: Boolean = false,
    val isInitiallyBookmarked: Boolean = false,
    val hasAudioQr: Boolean = true
)

object VastramRepository {
    const val HEADER_PROFILE_AVATAR =
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCuZRYkGpJFnuOPUHai1qYIxedA4z5tkLORA1LRRqnS5D1m8K72M30LmM54ZKd1OH4FpCwG_WRcoRN6oRquy3ZKgR-z0gq5_bGzjVZ711YbN2DE_CTDaqUDIC8sloeYnugJr2QcEg2dpDGxFXRwbbLNhCvNLRQ8mx9Dxri3LDfWH0bqKPTXRckFQNtq1UaIjnLzzpdoqbmrCfKOdpLSpludq_o4BT9L-Vf_Hte-BzLT"

    const val IMG_BOMKAI_HERO =
        "https://lh3.googleusercontent.com/aida-public/AB6AXuCYvecjUjNclBt1ETqRpyeTKhUYe1RCYmTs1dghjzPDDvOrgQsBVWlIUZSnRI6OMJBub1HqCrVr6d-ytU6k9KfmTTCdVunxiftbE4nhixDqL8imz1S8ZKNwO3aZojy6-PPYaQV8T7iI0OX31ZCxn-BZczqBkWBQdCB65140brwqeIk4_qWa44ho6B8QUAzAiQU-N9tFwOX-L9BDIcjKLoEx8jhiCc6tbVjOCNclaH2V"

    const val IMG_KOTPAD_WEAVE =
        "https://lh3.googleusercontent.com/aida-public/AB6AXuBtQgNLRfAw1GiNchfk1X4TuRrQEj7t7Sz7HsUYwUrKKIunoXwCBtMLSvIvrBeKBEma5Z4B8gIczdOwnvaSP8mgxUY9sKKhT7xhRit_iE5LS0Ipzm_rV-p9OtQeLbukTC5EqpPRyUYJeYqFVMVSOBAMyWl_pHkGaWMfd3b7YKeEBJhLlELFrOv6yHIiz465PClXHJgo4ePcySzhSI6ZQvmIsL4ETBOWjJ9uSXY5gzmO"

    const val IMG_RAW_SILK_CONES =
        "https://images.unsplash.com/photo-1606722590583-6951b5ea92ad?auto=format&fit=crop&w=1000&q=85"

    const val IMG_AAL_DYE_VAT =
        "https://images.unsplash.com/photo-1528459801416-a9e53bbf4e17?auto=format&fit=crop&w=1000&q=85"

    const val IMG_TUSSAR_HANKS =
        "https://images.unsplash.com/photo-1558769132-cb1aea458c5e?auto=format&fit=crop&w=1000&q=85"

    fun getStories(
        lang: AppLanguage = AppLanguage.ENGLISH,
        role: UserRole = UserRole.BUYER
    ): List<StoryCircle> {
        return if (role == UserRole.BUYER) {
            listOf(
                StoryCircle(
                    id = "sambalpuri_live",
                    title = lang.pick("Sambalpuri", "ସମ୍ବଲପୁରୀ", "संबलपुरी"),
                    subtitle = lang.pick("Sarees", "ଶାଢ଼ୀ", "साड़ी"),
                    imageUrl = IMG_BOMKAI_HERO,
                    isLive = true,
                    targetScreen = AppScreen.PRODUCT_DETAIL
                ),
                StoryCircle(
                    id = "gi_certified",
                    title = lang.pick("GI Scan", "GI ଯାଞ୍ଚ", "GI स्कैन"),
                    subtitle = lang.pick("QR Seal", "QR ସିଲ୍", "QR सील"),
                    imageUrl = IMG_KOTPAD_WEAVE,
                    isLive = false,
                    targetScreen = AppScreen.QR_VERIFICATION
                ),
                StoryCircle(
                    id = "nuapatna_atelier",
                    title = lang.pick("Nuapatna", "ନୂଆପାଟଣା", "नुआपाटना"),
                    subtitle = lang.pick("Weavers", "ବୁଣାକାର", "बुनकर"),
                    imageUrl = HEADER_PROFILE_AVATAR,
                    isLive = false,
                    targetScreen = AppScreen.WEAVER_PROFILE
                ),
                StoryCircle(
                    id = "order_transit",
                    title = lang.pick("Transit", "ପରିବହନ", "ट्रांजिट"),
                    subtitle = lang.pick("Track", "ଟ୍ରାକ୍", "ट्रैक"),
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCQfnF_Dhcjn-PVmZml-FuhFhvVgLTUcHFBGP11GSV4RoPqskBG61hSUzRgulSoj6eIzhynicQGPheOeQzOn1B9RVVo1ahbeNRsPqN7X1pfwG_3uMAXz9dv7f_mRXB950dFtWBwMy2EDsSb8PBk3q9ZR0kT1RfvomB3B2buqM72P7uijz8d1VQs3soGc5irifFAwhi4uhEs0QgYR0v_9T1GqzLOQ53P9tWJpiFQJBnq",
                    isLive = false,
                    targetScreen = AppScreen.ORDER_TRACKING
                ),
                StoryCircle(
                    id = "patron_lore",
                    title = lang.pick("Reviews", "ସମୀକ୍ଷା", "समीक्षा"),
                    subtitle = "5.0 ★",
                    imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDiGpIP322oBf1KUodDCc8I1Yn-SBHALY9LoUsF-O-ocVC_hrg65kQ4Rwfz-6pINoI98I-OUSsNXH3QrQLsqKF3QTf8RhoDYihUgApWcyCuWI4mXmfV3saIEwqx1sW27CCOaFXAGfeaO2e88GAs4oCjRLY_bxRnhmNG9ejf71fAYI2w5syr0YhBB5wXTLEUKQ6SH8RSEl57rz0JT4u-wrGvohuOQkYBF3SZ1qKjsj8v",
                    isLive = false,
                    targetScreen = AppScreen.SOCIAL_REVIEWS
                )
            )
        } else {
            listOf(
                StoryCircle(
                    id = "raw_silk_b2b",
                    title = lang.pick("Silk Yarn", "ରେଶମ ସୂତା", "रेशम सूत"),
                    subtitle = lang.pick("B2B Lot", "B2B ଲଟ୍", "B2B लॉट"),
                    imageUrl = IMG_RAW_SILK_CONES,
                    isLive = true,
                    targetScreen = AppScreen.DISCOVERY_FEED
                ),
                StoryCircle(
                    id = "voice_studio",
                    title = lang.pick("Voice AI", "ସ୍ୱର AI", "वॉइस AI"),
                    subtitle = lang.pick("Speak", "କୁହନ୍ତୁ", "बोलें"),
                    imageUrl = HEADER_PROFILE_AVATAR,
                    isLive = false,
                    targetScreen = AppScreen.VOICE_STUDIO
                ),
                StoryCircle(
                    id = "loom_desk",
                    title = lang.pick("Loom Desk", "ତନ୍ତ ଡେସ୍କ", "करघा डेस्क"),
                    subtitle = lang.pick("3 Orders", "୩ ଅର୍ଡର", "3 ऑर्डर"),
                    imageUrl = IMG_BOMKAI_HERO,
                    isLive = false,
                    targetScreen = AppScreen.LOOM_DESK
                ),
                StoryCircle(
                    id = "cluster_hub",
                    title = lang.pick("Cluster", "କ୍ଲଷ୍ଟର", "क्लस्टर"),
                    subtitle = lang.pick("47 Looms", "୪୭ ତନ୍ତ", "47 करघे"),
                    imageUrl = IMG_KOTPAD_WEAVE,
                    isLive = false,
                    targetScreen = AppScreen.CLUSTER_MANAGER
                ),
                StoryCircle(
                    id = "natural_dye_b2b",
                    title = lang.pick("Aal Dye", "ଆଳ ରଙ୍ଗ", "आल डाई"),
                    subtitle = lang.pick("Koraput", "କୋରାପୁଟ", "कोरापुट"),
                    imageUrl = IMG_AAL_DYE_VAT,
                    isLive = false,
                    targetScreen = AppScreen.DISCOVERY_FEED
                )
            )
        }
    }

    fun getRawMaterialPosts(lang: AppLanguage = AppLanguage.ENGLISH): List<RawMaterialPost> = listOf(
        RawMaterialPost(
            id = "raw_silk_1",
            supplierName = lang.pick("Nuapatna Yarn Co-op", "ନୂଆପାଟଣା ସୂତା ସମିତି", "नुआपाटना सूत समिति"),
            supplierAvatarUrl = HEADER_PROFILE_AVATAR,
            clusterBadge = lang.pick("SELLER B2B", "ବିକ୍ରେତା B2B", "विक्रेता B2B"),
            depotId = lang.pick("DEPOT #NP-08", "ଡିପୋ #NP-08", "डिपो #NP-08"),
            materialImageUrl = IMG_RAW_SILK_CONES,
            categoryPill = lang.pick("MULBERRY WARP YARN", "ମଲବରୀ ତାନା ସୂତା", "शहतूत ताना सूत"),
            gradePill = lang.pick("2/120 Grade-A", "୨/୧୨୦ ଗ୍ରେଡ୍-A", "2/120 ग्रेड-A"),
            title = lang.pick(
                "2/120 Raw Mulberry Silk Warp Cones",
                "୨/୧୨୦ କଞ୍ଚା ମଲବରୀ ପାଟ ସୂତା",
                "2/120 कच्चा शहतूत रेशम ताना कोन"
            ),
            specChips = listOf(
                lang.pick("100% Pure Silk", "୧୦୦% ଶୁଦ୍ଧ ପାଟ", "100% शुद्ध रेशम"),
                lang.pick("Bandha Ready", "ବାନ୍ଧ ଉପଯୋଗୀ", "बांध तैयार"),
                lang.pick("Zero Twist Loss", "ମଜବୁତ ସୂତା", "मजबूत धागा")
            ),
            pricePerUnit = lang.pick("₹4,200 / kg", "₹୪,୨୦୦ / କି.ଗ୍ରା.", "₹4,200 / कि.ग्रा."),
            moqPill = lang.pick("Min 5 kg", "ସର୍ବନିମ୍ନ ୫ କି.ଗ୍ରା.", "न्यूनतम 5 कि.ग्रा."),
            stockPill = lang.pick("140 kg Ready", "୧୪୦ କି.ଗ୍ରା. ମହଜୁଦ", "140 कि.ग्रा. उपलब्ध"),
            whatsappText = lang.pick("Order Yarn Lot", "ସୂତା ଅର୍ଡର କରନ୍ତୁ", "सूत ऑर्डर करें"),
            whatsappUrl = "https://wa.me/919437000101?text=Order%202/120%20Mulberry%20Silk%20Yarn"
        ),
        RawMaterialPost(
            id = "raw_dye_2",
            supplierName = lang.pick("Koraput Aal Dye Guild", "କୋରାପୁଟ ଆଳ ରଙ୍ଗ ସଂଘ", "कोरापुट आल डाई संघ"),
            supplierAvatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDKklZkkvhVWPrtQjTHHJudvR1T-S4yQo3BqEwdwWyXvZ9E7LggwP2riee8eI383skTLFR0QA0GmJEIyMiWWdRnA-S6npg8Sctj5YPSwUKOMq29Nt3iQwCo10aqb-VQqJNaqvaXtY_EvAFyr4CSrqjvEZb64D7kaelLQH26PGEQIoQIiY8LaA7s70n7vf6bM6JA2dszzcstddJqQd-GgRr6edZuCcg-k62KaYsjcS1q",
            clusterBadge = lang.pick("SELLER B2B", "ବିକ୍ରେତା B2B", "विक्रेता B2B"),
            depotId = lang.pick("VAT #KP-19", "ଭାଟ୍ #KP-19", "वैट #KP-19"),
            materialImageUrl = IMG_AAL_DYE_VAT,
            categoryPill = lang.pick("ORGANIC DYE LOT", "ଜୈବିକ ରଙ୍ଗ ଲଟ୍", "जैविक डाई लॉट"),
            gradePill = lang.pick("Sun-CuredRoot", "ଶୁଦ୍ଧ ଆଳ ମୂଳ", "शुद्ध आल जड़"),
            title = lang.pick(
                "Organic Aal Bark & Indigo Vat Cake",
                "ଜୈବିକ ଆଳ ଛାଲି ଓ ନୀଳ ରଙ୍ଗ ଲଟ୍",
                "जैविक आल छाल और नील डाई केक"
            ),
            specChips = listOf(
                lang.pick("0% Chemical", "ରସାୟନ ମୁକ୍ତ", "0% रसायन"),
                lang.pick("Castor Mordant", "ଜଡ଼ା ତେଲ ପ୍ରସ୍ତୁତ", "अरंडी तेल तैयार"),
                lang.pick("Kotpad Grade", "କୋଟପାଡ଼ ମାନକ", "कोटपाड़ ग्रेड")
            ),
            pricePerUnit = lang.pick("₹1,850 / 5kg", "₹୧,୮୫୦ / ୫ କି.ଗ୍ରା.", "₹1,850 / 5 कि.ग्रा."),
            moqPill = lang.pick("Min 2 Lots", "ସର୍ବନିମ୍ନ ୨ ଲଟ୍", "न्यूनतम 2 लॉट"),
            stockPill = lang.pick("45 Lots Ready", "୪୫ ଲଟ୍ ମହଜୁଦ", "45 लॉट उपलब्ध"),
            whatsappText = lang.pick("Order Dye Vat", "ରଙ୍ଗ ଅର୍ଡର କରନ୍ତୁ", "डाई ऑर्डर करें"),
            whatsappUrl = "https://wa.me/919437000102?text=Order%20Koraput%20Aal%20Dye%20Lot"
        ),
        RawMaterialPost(
            id = "raw_tussar_3",
            supplierName = lang.pick("Mayurbhanj Tussar Bank", "ମୟୂରଭଞ୍ଜ ତସର ବ୍ୟାଙ୍କ", "मयूरभंज तसर बैंक"),
            supplierAvatarUrl = HEADER_PROFILE_AVATAR,
            clusterBadge = lang.pick("SELLER B2B", "ବିକ୍ରେତା B2B", "विक्रेता B2B"),
            depotId = lang.pick("REEL #MB-04", "ରିଲ୍ #MB-04", "रील #MB-04"),
            materialImageUrl = IMG_TUSSAR_HANKS,
            categoryPill = lang.pick("WILD TUSSAR HANKS", "ବନ୍ୟ ତସର ଲଚ୍ଛା", "वन्य तसर लच्छा"),
            gradePill = lang.pick("80s Hand-Reeled", "୮୦ କାଉଣ୍ଟ ହାତକଟା", "80s हाथ-कता"),
            title = lang.pick(
                "Golden Wild Tussar & Ghicha Silk Hanks",
                "ସୁନେଲି ବନ୍ୟ ତସର ଓ ଘିଚା ରେଶମ ସୂତା",
                "सुनहरा वन्य तसर और घिचा रेशम लच्छा"
            ),
            specChips = listOf(
                lang.pick("Unbleached", "ଅଣ-ବ୍ଲିଚ୍", "अनब्लीच्ड"),
                lang.pick("Jala Pallu Grade", "ଜାଲା ଆଞ୍ଚଳ ମାନକ", "जाला पल्लू ग्रेड"),
                lang.pick("4-Ply Strong", "୪-ପ୍ଲାଏ ମଜବୁତ", "4-प्लाई मजबूत")
            ),
            pricePerUnit = lang.pick("₹3,600 / Bundle", "₹୩,୬୦୦ / ବଣ୍ଡଲ୍", "₹3,600 / बंडल"),
            moqPill = lang.pick("Min 3 Bundles", "ସର୍ବନିମ୍ନ ୩ ବଣ୍ଡଲ୍", "न्यूनतम 3 बंडल"),
            stockPill = lang.pick("80 Bundles", "୮୦ ବଣ୍ଡଲ୍ ମହଜୁଦ", "80 बंडल उपलब्ध"),
            whatsappText = lang.pick("Order Tussar Bundle", "ତସର ବଣ୍ଡଲ୍ କିଣନ୍ତୁ", "तसर बंडल खरीदें"),
            whatsappUrl = "https://wa.me/919437000103?text=Order%20Mayurbhanj%20Tussar%20Hanks"
        )
    )

    fun getFeedPosts(lang: AppLanguage = AppLanguage.ENGLISH): List<FeedPost> = listOf(
        FeedPost(
            id = "post_raghunath",
            weaverName = lang.pick("Shri Raghunath Meher", "ଶ୍ରୀ ରଘୁନାଥ ମେହେର", "श्री रघुनाथ मेहेर"),
            weaverAvatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDKklZkkvhVWPrtQjTHHJudvR1T-S4yQo3BqEwdwWyXvZ9E7LggwP2riee8eI383skTLFR0QA0GmJEIyMiWWdRnA-S6npg8Sctj5YPSwUKOMq29Nt3iQwCo10aqb-VQqJNaqvaXtY_EvAFyr4CSrqjvEZb64D7kaelLQH26PGEQIoQIiY8LaA7s70n7vf6bM6JA2dszzcstddJqQd-GgRr6edZuCcg-k62KaYsjcS1q",
            badgeLabel = lang.pick("GI ODISHA", "GI ଓଡ଼ିଶା", "GI ओडिशा"),
            locationCluster = lang.pick("Sonepur", "ସୋନପୁର", "सोनपुर"),
            loomId = lang.pick("Loom #108", "ତନ୍ତ #୧୦୮", "करघा #108"),
            productImageUrl = IMG_BOMKAI_HERO,
            topBadgeText = lang.pick("Double Ikat", "ଡବଲ୍ ଇକତ", "डबल इकत"),
            bottomBadgeText = lang.pick("18d Loom", "୧୮ ଦିନ ବୁଣା", "18 दिन करघा"),
            hasAudioNarration = false,
            initialLikes = 342,
            commentsCount = 28,
            whatsappText = lang.pick("Buy Direct · WhatsApp", "ସିଧା କିଣନ୍ତୁ · ହ୍ୱାଟ୍ସଆପ୍", "सीधे खरीदें · व्हाट्सएप"),
            whatsappUrl = "https://wa.me/919437000108?text=Interested%20in%20Shankha-Chakra%20Bomkai%20Silk",
            priceDisplay = lang.pick("₹18,500 · 88% Weaver", "₹୧୮,୫୦୦ · ୮୮% ବୁଣାକାର", "₹18,500 · 88% बुनकर"),
            payoutSubtitle = lang.pick("Zero Middleman", "ମଧ୍ୟସ୍ଥି ମୁକ୍ତ", "बिचौलिया मुक्त"),
            certificateChip = "GI #OD-108",
            highlightTitle = lang.pick("Bomkai Patta Silk", "ବୋମକାଇ ପାଟ ଶାଢ଼ୀ", "बोमकाई पाट सिल्क"),
            captionBody = lang.pick(
                "100s Mulberry Silk · Natural Lac Dye",
                "୧୦୦ କାଉଣ୍ଟ ପାଟ · ପ୍ରାକୃତିକ ଲାଖ ରଙ୍ଗ",
                "100s शहतूत रेशम · प्राकृतिक लाख रंग"
            ),
            techFootnote = lang.pick("Pit Loom #108 · Sonepur", "ଗାତ ତନ୍ତ #୧୦୮ · ସୋନପୁର", "पिट लूम #108 · सोनपुर"),
            hashtags = listOf("#Sambalpuri", "#GIVerified")
        ),
        FeedPost(
            id = "post_subhadra",
            weaverName = lang.pick("Smt. Subhadra Mirgan", "ଶ୍ରୀମତୀ ସୁଭଦ୍ରା ମିର୍ଗାନ", "श्रीमती सुभद्रा मिरगान"),
            weaverAvatarUrl = HEADER_PROFILE_AVATAR,
            badgeLabel = lang.pick("ORGANIC DYE", "ଜୈବିକ ରଙ୍ଗ", "जैविक डाई"),
            locationCluster = lang.pick("Kotpad", "କୋଟପାଡ଼", "कोटपाड़"),
            loomId = lang.pick("Loom #14", "ତନ୍ତ #୧୪", "करघा #14"),
            productImageUrl = IMG_KOTPAD_WEAVE,
            topBadgeText = lang.pick("Aal Root Dye", "ଆଳ ମୂଳ ରଙ୍ଗ", "आल जड़ रंग"),
            bottomBadgeText = "",
            hasAudioNarration = true,
            audioTitle = lang.pick("Loom Audio · 0:24", "ତନ୍ତ ଧ୍ୱନି · ୦:୨୪", "करघा ऑडियो · 0:24"),
            audioSubtitle = lang.pick("3-Shuttle Pit Loom", "୩-ସଟଲ୍ ଗାତ ତନ୍ତ", "3-शटल पिट लूम"),
            initialLikes = 289,
            commentsCount = 19,
            whatsappText = lang.pick("Buy Direct · WhatsApp", "ସିଧା କିଣନ୍ତୁ · ହ୍ୱାଟ୍ସଆପ୍", "सीधे खरीदें · व्हाट्सएप"),
            whatsappUrl = "https://wa.me/919437000014?text=Interested%20in%20Kotpad%20Aal%20Saree",
            priceDisplay = lang.pick("₹14,200 · 90% Weaver", "₹୧୪,୨୦୦ · ୯୦% ବୁଣାକାର", "₹14,200 · 90% बुनकर"),
            payoutSubtitle = lang.pick("Tribal Guild Direct", "ସିଧା ବୁଣାକାର ସଂଘ", "सीधे बुनकर संघ"),
            certificateChip = "GI #OD-014",
            highlightTitle = lang.pick("Kotpad Organic Cotton", "କୋଟପାଡ଼ ଜୈବିକ କପା", "कोटपाड़ जैविक कॉटन"),
            captionBody = lang.pick(
                "21d Sun-Cured Aal Dye · 60s Cotton",
                "୨୧ ଦିନ ଆଳ ରଙ୍ଗ · ୬୦ କାଉଣ୍ଟ କପା",
                "21 दिन आल डाई · 60s देसी कॉटन"
            ),
            techFootnote = lang.pick("Pit Loom #14 · Koraput", "ଗାତ ତନ୍ତ #୧୪ · କୋରାପୁଟ", "पिट लूम #14 · कोरापुट"),
            hashtags = listOf("#Kotpad", "#NaturalDye")
        )
    )

    fun getMasonryItems(lang: AppLanguage = AppLanguage.ENGLISH): List<GridSareeItem> = listOf(
        GridSareeItem(
            id = "grid_1",
            title = lang.pick("Bichitrapuri Silk", "ବିଚିତ୍ରପୁରୀ ପାଟ", "बिचित्रपुरी सिल्क"),
            subtitle = lang.pick("Bargarh · 18d", "ବରଗଡ଼ · ୧୮ ଦିନ", "बरगढ़ · 18 दिन"),
            price = "₹18,500",
            badge = "GI VERIFIED",
            imageUrl = IMG_BOMKAI_HERO
        ),
        GridSareeItem(
            id = "grid_2",
            title = lang.pick("Kotpad Aal Cotton", "କୋଟପାଡ଼ ଆଳ କପା", "कोटपाड़ आल कॉटन"),
            subtitle = lang.pick("Koraput · 21d", "କୋରାପୁଟ · ୨୧ ଦିନ", "कोरापुट · 21 दिन"),
            price = "₹14,200",
            badge = lang.pick("ORGANIC", "ଜୈବିକ", "जैविक"),
            imageUrl = IMG_KOTPAD_WEAVE
        ),
        GridSareeItem(
            id = "grid_3",
            title = lang.pick("Khandua Patta", "ଖଣ୍ଡୁଆ ପାଟ", "खंडुआ पाट"),
            subtitle = lang.pick("Nuapatna · 16d", "ନୂଆପାଟଣା · ୧୬ ଦିନ", "नुआपाटना · 16 दिन"),
            price = "₹21,000",
            badge = lang.pick("TEMPLE SILK", "ମନ୍ଦିର ପାଟ", "मंदिर रेशम"),
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCQfnF_Dhcjn-PVmZml-FuhFhvVgLTUcHFBGP11GSV4RoPqskBG61hSUzRgulSoj6eIzhynicQGPheOeQzOn1B9RVVo1ahbeNRsPqN7X1pfwG_3uMAXz9dv7f_mRXB950dFtWBwMy2EDsSb8PBk3q9ZR0kT1RfvomB3B2buqM72P7uijz8d1VQs3soGc5irifFAwhi4uhEs0QgYR0v_9T1GqzLOQ53P9tWJpiFQJBnq"
        ),
        GridSareeItem(
            id = "grid_4",
            title = lang.pick("Gopalpur Tussar", "ଗୋପାଳପୁର ତସର", "गोपालपुर तसर"),
            subtitle = lang.pick("Jajpur · 14d", "ଯାଜପୁର · ୧୪ ଦିନ", "जाजपुर · 14 दिन"),
            price = "₹16,800",
            badge = lang.pick("WILD SILK", "ବନ୍ୟ ତସର", "वन्य तसर"),
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDiGpIP322oBf1KUodDCc8I1Yn-SBHALY9LoUsF-O-ocVC_hrg65kQ4Rwfz-6pINoI98I-OUSsNXH3QrQLsqKF3QTf8RhoDYihUgApWcyCuWI4mXmfV3saIEwqx1sW27CCOaFXAGfeaO2e88GAs4oCjRLY_bxRnhmNG9ejf71fAYI2w5syr0YhBB5wXTLEUKQ6SH8RSEl57rz0JT4u-wrGvohuOQkYBF3SZ1qKjsj8v"
        )
    )

    fun getAtelierItems(lang: AppLanguage = AppLanguage.ENGLISH): List<AtelierItem> = listOf(
        AtelierItem(
            id = "atelier_1",
            title = lang.pick("Khandua Patta", "ଖଣ୍ଡୁଆ ପାଟ", "खंडुआ पाट"),
            price = "₹18,500",
            badge = lang.pick("IN STOCK", "ମହଜୁଦ", "उपलब्ध"),
            imageUrl = IMG_BOMKAI_HERO
        ),
        AtelierItem(
            id = "atelier_2",
            title = lang.pick("Pasapalli Silk", "ପଶାପାଲି ପାଟ", "पशापाली सिल्क"),
            price = "₹22,400",
            badge = "GI SEALED",
            imageUrl = IMG_KOTPAD_WEAVE
        ),
        AtelierItem(
            id = "atelier_3",
            title = lang.pick("Gita Govinda", "ଗୀତ ଗୋବିନ୍ଦ", "गीत गोविंद"),
            price = "₹28,900",
            badge = lang.pick("1 LEFT", "୧ଟି ବାକି", "1 शेष"),
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCQfnF_Dhcjn-PVmZml-FuhFhvVgLTUcHFBGP11GSV4RoPqskBG61hSUzRgulSoj6eIzhynicQGPheOeQzOn1B9RVVo1ahbeNRsPqN7X1pfwG_3uMAXz9dv7f_mRXB950dFtWBwMy2EDsSb8PBk3q9ZR0kT1RfvomB3B2buqM72P7uijz8d1VQs3soGc5irifFAwhi4uhEs0QgYR0v_9T1GqzLOQ53P9tWJpiFQJBnq"
        ),
        AtelierItem(
            id = "atelier_4",
            title = lang.pick("Bomkai Zari", "ବୋମକାଇ ଜରି", "बोमकाई ज़री"),
            price = "₹19,200",
            badge = lang.pick("READY", "ପ୍ରସ୍ତୁତ", "तैयार"),
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDiGpIP322oBf1KUodDCc8I1Yn-SBHALY9LoUsF-O-ocVC_hrg65kQ4Rwfz-6pINoI98I-OUSsNXH3QrQLsqKF3QTf8RhoDYihUgApWcyCuWI4mXmfV3saIEwqx1sW27CCOaFXAGfeaO2e88GAs4oCjRLY_bxRnhmNG9ejf71fAYI2w5syr0YhBB5wXTLEUKQ6SH8RSEl57rz0JT4u-wrGvohuOQkYBF3SZ1qKjsj8v"
        ),
        AtelierItem(
            id = "atelier_5",
            title = lang.pick("Aal Stole", "ଆଳ ଚାଦର", "आल स्टोल"),
            price = "₹6,800",
            badge = lang.pick("NEW", "ନୂଆ", "नया"),
            imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDJNIdYy8l1Yh7qwAf6s3n3pE6G2iC0Ye6-DmP3au0MsXV0-p0g8IiL1u6N37jn3ivpCZd2j3ZG3rKB0LWfuEu2PmuMtbKs-uGKhDMK6VY-Z_ZZ7YxHRMQccZf8wz7j-xiH5H11rV-JpL5NusWGO4_zpI9RBy1NNK5JnzwThkJSpC8OucNcLNcr-IMkFjk2RUFAzUYNuXBu3BfvkRUVdUXYYeQOavG25k270xHHgWxC"
        ),
        AtelierItem(
            id = "atelier_6",
            title = lang.pick("Loom Sound", "ତନ୍ତ ଶବ୍ଦ", "करघा ध्वनि"),
            price = lang.pick("Live Audio", "ଲାଇଭ୍ ଅଡିଓ", "लाइव ऑडियो"),
            badge = "REEL",
            imageUrl = HEADER_PROFILE_AVATAR
        )
    )

    fun getKanbanOrders(lang: AppLanguage = AppLanguage.ENGLISH): List<KanbanOrder> = listOf(
        KanbanOrder(
            id = "B2B-108",
            orderCode = "#OD-B2B-108",
            stageKey = "weaving",
            stageBadge = lang.pick("STAGE 2 · PIT LOOM", "ପର୍ଯ୍ୟାୟ ୨ · ଗାତ ତନ୍ତ", "चरण 2 · पिट लूम"),
            statusPill = lang.pick("70% WOVEN", "୭୦% ବୁଣା", "70% बुना"),
            title = lang.pick("12x Sambalpuri Pasapalli Silk", "୧୨x ସମ୍ବଲପୁରୀ ପଶାପାଲି ପାଟ", "12x संबलपुरी पशापाली सिल्क"),
            clientName = lang.pick("Kala Niketan Guild", "କଳା ନିକେତନ ସଂଘ", "कला निकेतन गिल्ड"),
            clientDetail = lang.pick("₹2,16,000 · Due 18 Nov", "₹୨,୧୬,୦୦୦ · ୧୮ ନଭେମ୍ବର", "₹2,16,000 · 18 नवंबर"),
            leadWeaver = lang.pick("Shri Bijaya Patra (4 Looms)", "ଶ୍ରୀ ବିଜୟ ପାତ୍ର (୪ ତନ୍ତ)", "श्री विजय पात्र (4 करघे)"),
            subNote = lang.pick("8/12 Sarees Completed", "୮/୧୨ ଶାଢ଼ୀ ସମ୍ପୂର୍ଣ୍ଣ", "8/12 साड़ियां पूर्ण"),
            progressRatio = 0.70f,
            progressLabel = lang.pick("8 / 12 Sarees", "୮ / ୧୨ ଶାଢ଼ୀ", "8 / 12 साड़ियां")
        ),
        KanbanOrder(
            id = "B2B-104",
            orderCode = "#OD-B2B-104",
            stageKey = "qc",
            stageBadge = lang.pick("STAGE 4 · GI SEAL", "ପର୍ଯ୍ୟାୟ ୪ · GI ସିଲ୍", "चरण 4 · GI सील"),
            statusPill = lang.pick("QC READY", "ଯାଞ୍ଚ ପ୍ରସ୍ତୁତ", "जांच तैयार"),
            title = lang.pick("6x Nuapatna Khandua Bridal", "୬x ନୂଆପାଟଣା ଖଣ୍ଡୁଆ ପାଟ", "6x नुआपाटना खंडुआ ब्राइडल"),
            clientName = lang.pick("Utkalika Emporium", "ଉତ୍କଳିକା ଏମ୍ପୋରିୟମ୍", "उत्कलिका एम्पोरियम"),
            clientDetail = lang.pick("₹1,11,000 · Ready Today", "₹୧,୧୧,୦୦୦ · ଆଜି ପ୍ରସ୍ତୁତ", "₹1,11,000 · आज तैयार"),
            leadWeaver = lang.pick("Shri Raghunath Meher", "ଶ୍ରୀ ରଘୁନାଥ ମେହେର", "श्री रघुनाथ मेहेर"),
            subNote = lang.pick("Pallu NFC Tagged", "ଆଞ୍ଚଳ NFC ସଂଯୁକ୍ତ", "पल्लू NFC टैग"),
            progressRatio = 0.92f
        ),
        KanbanOrder(
            id = "B2B-099",
            orderCode = "#OD-B2B-099",
            stageKey = "dispatched",
            stageBadge = lang.pick("STAGE 5 · TRANSIT", "ପର୍ଯ୍ୟାୟ ୫ · ପଠାଯାଇଛି", "चरण 5 · ट्रांजिट"),
            statusPill = lang.pick("SPEED POST", "ସ୍ପିଡ୍ ପୋଷ୍ଟ", "स्पीड पोस्ट"),
            title = lang.pick("8x Kotpad Organic Aal Cotton", "୮x କୋଟପାଡ଼ ଜୈବିକ ଆଳ କପା", "8x कोटपाड़ जैविक आल कॉटन"),
            clientName = lang.pick("Dastkar Collective", "ଦସ୍ତକାର କଲେକ୍ଟିଭ୍", "दस्तकार कलेक्टिव"),
            clientDetail = lang.pick("₹1,13,600 · #SP-OD982341IN", "₹୧,୧୩,୬୦୦ · #SP-OD982341IN", "₹1,13,600 · #SP-OD982341IN"),
            leadWeaver = lang.pick("Smt. Subhadra Mirgan", "ଶ୍ରୀମତୀ ସୁଭଦ୍ରା ମିର୍ଗାନ", "श्रीमती सुभद्रा मिरगान"),
            subNote = lang.pick("Bhubaneswar Hub", "ଭୁବନେଶ୍ୱର ହବ୍", "भुवनेश्वर हब"),
            progressRatio = 1.0f
        )
    )

    fun getPatronReviews(lang: AppLanguage = AppLanguage.ENGLISH): List<PatronReviewPost> = listOf(
        PatronReviewPost(
            id = "rev_1",
            patronName = lang.pick("Ananya Mohapatra", "ଅନନ୍ୟା ମହାପାତ୍ର", "अनन्या महापात्रा"),
            roleBadge = lang.pick("Patron", "ଗ୍ରାହକ", "ग्राहक"),
            locationAndWeave = lang.pick("Bhubaneswar · Bomkai", "ଭୁବନେଶ୍ୱର · ବୋମକାଇ", "भुवनेश्वर · बोमकाई"),
            avatarUrl = HEADER_PROFILE_AVATAR,
            mainPhotoUrl = IMG_BOMKAI_HERO,
            packshotUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuCQfnF_Dhcjn-PVmZml-FuhFhvVgLTUcHFBGP11GSV4RoPqskBG61hSUzRgulSoj6eIzhynicQGPheOeQzOn1B9RVVo1ahbeNRsPqN7X1pfwG_3uMAXz9dv7f_mRXB950dFtWBwMy2EDsSb8PBk3q9ZR0kT1RfvomB3B2buqM72P7uijz8d1VQs3soGc5irifFAwhi4uhEs0QgYR0v_9T1GqzLOQ53P9tWJpiFQJBnq",
            giStampText = "GI #OD-BMK-409",
            sareeTitle = lang.pick("Shankha-Chakra Bomkai Silk", "ଶଙ୍ଖ-ଚକ୍ର ବୋମକାଇ ପାଟ", "शंख-चक्र बोमकाई सिल्क"),
            sareePriceSplit = lang.pick("₹18,500 · 88% Weaver", "₹୧୮,୫୦୦ · ୮୮% ବୁଣାକାର", "₹18,500 · 88% बुनकर"),
            initialLikes = 214,
            certBadge = "5.0 ★",
            quoteText = lang.pick(
                "“Crisp double-ikat motifs & verified Loom #108 QR.”",
                "“ସଠିକ୍ ଡବଲ୍-ଇକତ ବାନ୍ଧ ଓ ପ୍ରମାଣିତ ତନ୍ତ #୧୦୮ QR।”",
                "“सटीक डबल-इकत मोटिफ और सत्यापित करघा #108 QR।”"
            ),
            hashtags = listOf("#SambalpuriLove", "#GIVerified"),
            timeAgo = lang.pick("2d ago", "୨ ଦିନ ପୂର୍ବେ", "2 दिन पहले"),
            isInitiallyLiked = true,
            isInitiallyBookmarked = false,
            hasAudioQr = true
        ),
        PatronReviewPost(
            id = "rev_2",
            patronName = lang.pick("Dr. Meenakshi Iyer", "ଡ. ମୀନାକ୍ଷୀ ଆୟର", "डॉ. मीनाक्षी अय्यर"),
            roleBadge = lang.pick("Collector", "ସଂଗ୍ରାହକ", "संग्राहक"),
            locationAndWeave = lang.pick("Bengaluru · Kotpad", "ବେଙ୍ଗାଲୁରୁ · କୋଟପାଡ଼", "बेंगलुरु · कोटपाड़"),
            avatarUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDKklZkkvhVWPrtQjTHHJudvR1T-S4yQo3BqEwdwWyXvZ9E7LggwP2riee8eI383skTLFR0QA0GmJEIyMiWWdRnA-S6npg8Sctj5YPSwUKOMq29Nt3iQwCo10aqb-VQqJNaqvaXtY_EvAFyr4CSrqjvEZb64D7kaelLQH26PGEQIoQIiY8LaA7s70n7vf6bM6JA2dszzcstddJqQd-GgRr6edZuCcg-k62KaYsjcS1q",
            mainPhotoUrl = IMG_KOTPAD_WEAVE,
            packshotUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDiGpIP322oBf1KUodDCc8I1Yn-SBHALY9LoUsF-O-ocVC_hrg65kQ4Rwfz-6pINoI98I-OUSsNXH3QrQLsqKF3QTf8RhoDYihUgApWcyCuWI4mXmfV3saIEwqx1sW27CCOaFXAGfeaO2e88GAs4oCjRLY_bxRnhmNG9ejf71fAYI2w5syr0YhBB5wXTLEUKQ6SH8RSEl57rz0JT4u-wrGvohuOQkYBF3SZ1qKjsj8v",
            giStampText = "GI #OD-KTP-112",
            sareeTitle = lang.pick("Organic Aal Kotpad Cotton", "ଜୈବିକ ଆଳ କୋଟପାଡ଼ କପା", "जैविक आल कोटपाड़ कॉटन"),
            sareePriceSplit = lang.pick("₹14,200 · 90% Weaver", "₹୧୪,୨୦୦ · ୯୦% ବୁଣାକାର", "₹14,200 · 90% बुनकर"),
            initialLikes = 168,
            certBadge = "5.0 ★",
            quoteText = lang.pick(
                "“Earth-dyed Aal cotton with 3-shuttle temple border.”",
                "“ପ୍ରାକୃତିକ ଆଳ ରଙ୍ଗ କପା ଓ ୩-ସଟଲ୍ କୁମ୍ଭ ଧଡ଼ି।”",
                "“प्राकृतिक आल रंग कॉटन और 3-शटल मंदिर बॉर्डर।”"
            ),
            hashtags = listOf("#KotpadWeave", "#ZeroMiddleman"),
            timeAgo = lang.pick("5d ago", "୫ ଦିନ ପୂର୍ବେ", "5 दिन पहले"),
            isInitiallyLiked = false,
            isInitiallyBookmarked = true,
            hasAudioQr = true
        )
    )
}
