package com.example.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BrandSignatureBadge
import com.example.ui.theme.AvaBlack
import com.example.ui.theme.AvaCard
import com.example.ui.theme.AvaCardBorder
import com.example.ui.theme.AvaDivider
import com.example.ui.theme.AvaError
import com.example.ui.theme.AvaPurpleContainer
import com.example.ui.theme.AvaPurpleLight
import com.example.ui.theme.AvaPurplePrimary
import com.example.ui.theme.AvaSuccess
import com.example.ui.theme.AvaTextPrimary
import com.example.ui.theme.AvaTextSecondary
import com.example.ui.viewmodel.MainViewModel
import com.example.util.AppLanguage

@Composable
fun HelpAndReportScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val currentLang by viewModel.appLanguage.collectAsState()
    val isAr = currentLang == AppLanguage.ARABIC
    var selectedTab by remember { mutableIntStateOf(0) }

    // Report Form States
    var targetType by remember { mutableStateOf("USER") }
    var targetIdentifier by remember { mutableStateOf("") }
    var selectedReason by remember { mutableStateOf("INAPPROPRIATE_CONTENT") }
    var reportDetails by remember { mutableStateOf("") }
    var reportSubmitted by remember { mutableStateOf(false) }

    // Support message state
    var supportMessage by remember { mutableStateOf("") }
    var supportSubmitted by remember { mutableStateOf(false) }

    val tabs = if (isAr) {
        listOf("تقديم بلاغ", "الأسئلة الشائعة", "تواصل مع الإدارة")
    } else {
        listOf("Submit Report", "Help & FAQs", "Contact Support")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AvaBlack)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.testTag("help_report_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = AvaTextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isAr) "مركز المساعدة والإبلاغ" else "Help & Report Center",
                    color = AvaTextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            BrandSignatureBadge()
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = AvaBlack,
            contentColor = AvaPurpleLight,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = AvaPurplePrimary
                )
            },
            divider = { Box(modifier = Modifier.height(1.dp).background(AvaDivider)) }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == index) AvaPurpleLight else AvaTextSecondary
                        )
                    }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    // --- SUBMIT REPORT TAB ---
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = AvaCard),
                            border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(AvaCardBorder))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ReportProblem,
                                        contentDescription = null,
                                        tint = AvaError,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isAr) "إبلاغ عن محتوى أو مستخدم مخالف" else "Report Violating User or Post",
                                        color = AvaTextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (isAr)
                                        "يلتزم فريق AVA Studio بتوفير بيئة إبداعية آمنة. تُراجع جميع البلاغات مباشرة من قبل لوحة تحكم الإدارة والمؤسس."
                                    else
                                        "AVA Studio is dedicated to a safe creative space. All reports are immediately reviewed via the Founder & Admin console.",
                                    color = AvaTextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }

                    // Type Selector
                    item {
                        Text(
                            text = if (isAr) "نوع البلاغ" else "Report Target Type",
                            color = AvaTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val types = listOf(
                                Pair("USER", if (isAr) "مستخدم" else "User"),
                                Pair("POST", if (isAr) "منشور" else "Post"),
                                Pair("COMMENT", if (isAr) "تعليق" else "Comment")
                            )
                            types.forEach { (typeKey, label) ->
                                FilterChip(
                                    selected = targetType == typeKey,
                                    onClick = { targetType = typeKey },
                                    label = { Text(label, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = AvaPurpleContainer,
                                        selectedLabelColor = AvaPurpleLight,
                                        containerColor = AvaCard,
                                        labelColor = AvaTextSecondary
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = targetType == typeKey,
                                        borderColor = if (targetType == typeKey) AvaPurplePrimary else AvaCardBorder
                                    )
                                )
                            }
                        }
                    }

                    // Target Identifier Field
                    item {
                        Text(
                            text = if (isAr) "اسم المستخدم أو رابط/معرّف المنشور" else "Target Username or Post ID",
                            color = AvaTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = targetIdentifier,
                            onValueChange = { targetIdentifier = it },
                            placeholder = {
                                Text(
                                    if (isAr) "مثال: @username أو post_102" else "e.g. @username or post_102",
                                    color = AvaTextSecondary
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("report_target_field"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AvaPurplePrimary,
                                unfocusedBorderColor = AvaCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }

                    // Reason Selector
                    item {
                        Text(
                            text = if (isAr) "سبب البلاغ" else "Violation Category",
                            color = AvaTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val reasons = listOf(
                            Pair("INAPPROPRIATE_CONTENT", if (isAr) "محتوى غير لائق أو مسيء" else "Inappropriate Content"),
                            Pair("HARASSMENT", if (isAr) "مضايقة أو تنمر" else "Harassment or Bullying"),
                            Pair("SPAM", if (isAr) "رسائل مضللة أو سبام" else "Spam or Deceptive Content"),
                            Pair("IMPERSONATION", if (isAr) "انتحال شخصية أو هوية" else "Impersonation"),
                            Pair("COPYRIGHT", if (isAr) "انتهاك حقوق ملكية فكرية" else "Copyright Violation")
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            reasons.forEach { (key, label) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (selectedReason == key) AvaPurpleContainer.copy(alpha = 0.5f) else AvaCard)
                                        .border(
                                            1.dp,
                                            if (selectedReason == key) AvaPurplePrimary else AvaCardBorder,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { selectedReason = key }
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (selectedReason == key) Icons.Default.CheckCircle else Icons.Default.Help,
                                        contentDescription = null,
                                        tint = if (selectedReason == key) AvaPurpleLight else AvaTextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = label,
                                        color = if (selectedReason == key) AvaTextPrimary else AvaTextSecondary,
                                        fontSize = 13.sp,
                                        fontWeight = if (selectedReason == key) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    // Details text area
                    item {
                        Text(
                            text = if (isAr) "تفاصيل إضافية عن المخالفة" else "Additional Details",
                            color = AvaTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = reportDetails,
                            onValueChange = { reportDetails = it },
                            placeholder = {
                                Text(
                                    if (isAr) "يرجى توضيح سبب البلاغ لمساعدة فريق الإدارة في اتخاذ الإجراء المناسب..."
                                    else "Please clarify context to assist founder & moderation team...",
                                    color = AvaTextSecondary
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .testTag("report_details_field"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AvaPurplePrimary,
                                unfocusedBorderColor = AvaCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }

                    // Submit feedback or confirmation
                    item {
                        if (reportSubmitted) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = AvaSuccess.copy(alpha = 0.15f)),
                                border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(AvaSuccess))
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = AvaSuccess,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = if (isAr) "تم استلام البلاغ وسجل في لوحة الإدارة للمراجعة الفورية."
                                        else "Report logged into Founder Admin panel for immediate review.",
                                        color = AvaSuccess,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Button(
                            onClick = {
                                if (targetIdentifier.isNotBlank() || reportDetails.isNotBlank()) {
                                    viewModel.submitReport(
                                        targetType = targetType,
                                        targetId = targetIdentifier.ifBlank { "unspecified" },
                                        reason = selectedReason,
                                        details = reportDetails
                                    )
                                    reportSubmitted = true
                                    targetIdentifier = ""
                                    reportDetails = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("submit_report_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAr) "إرسال البلاغ للإدارة" else "Submit Report",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                1 -> {
                    // --- FAQS TAB ---
                    item {
                        Text(
                            text = if (isAr) "الأسئلة الشائعة حول AVA Studio" else "Frequently Asked Questions",
                            color = AvaTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    val faqs = if (isAr) {
                        listOf(
                            Pair("ما هو تطبيق AVA Studio؟", "AVA Studio هو منصة اجتماعية ومجموعة استوديوهات إبداعية متكاملة (محرر الصور، استوديو الإعلانات، استوديو الصور الشخصية، ومصمم القصص مع مساعد الذكاء الاصطناعي بيتو AI). صُمم ونُفّذ بواسطة أيهم (Made by Ayham)."),
                            Pair("كيف أحصل على عملات AVA Coins؟", "يمكنك جمع العملات اليومية مجانًا من المحفظة أو من خلال إتمام التصاميم والتفاعل في المنشورات واستكشاف المبدعين."),
                            Pair("كيف يعمل مساعد بيتو AI؟", "بيتو AI هو المساعد الذكي المدمج لمساعدتك في كتابة نصوص إعلانات جذابة، توليد أفكار جلسات تصوير، واقتراح فلاتر وتدرجات لونية تناسب هويتك البصرية."),
                            Pair("ما هي شارة التوثيق الرسمية؟", "شارة التوثيق (Verified Badge) تُمنح للحسابات المعتمدة والمؤسس وصناع المحتوى المميزين عبر لوحة تحكم الإدارة."),
                            Pair("كيف أبلغ عن محتوى مسيء أو انتهاك؟", "يمكنك استخدام تبويب 'تقديم بلاغ' أعلاه وتحديد نوع البلاغ واسم الحساب، وسيظهر فورًا في لوحة تحكم المؤسس لاتخاذ الإجراء المناسب كالحظر أو إزالة المحتوى.")
                        )
                    } else {
                        listOf(
                            Pair("What is AVA Studio?", "AVA Studio is a premier creative social suite combining a Social Feed, Personal Photo Studio, Ad Studio, Story Designer, Image Editor, and Betou AI assistant. Signature: Made by Ayham."),
                            Pair("How do I earn AVA Coins?", "You can claim daily coins via the Coins Wallet (+50 coins every day) and earn more by publishing visual projects and interacting with the community."),
                            Pair("How does Betou AI assist creators?", "Betou AI generates ad copy headlines, photoshoot camera ideas, portrait composition tips, and optimal color palettes tailored to your artistic projects."),
                            Pair("How does user verification work?", "Verified checkmarks are granted to official creators and founder Ayham via the Founder Dashboard."),
                            Pair("How do I report abuse?", "Submit the target user or post details in the 'Submit Report' tab. Reports immediately route to the founder and moderation console for enforcement actions.")
                        )
                    }

                    items(faqs) { (q, a) ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = AvaCard),
                            border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(AvaCardBorder))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = q,
                                    color = AvaPurpleLight,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = a,
                                    color = AvaTextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }

                2 -> {
                    // --- CONTACT SUPPORT TAB ---
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = AvaCard),
                            border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(AvaCardBorder))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Mail,
                                        contentDescription = null,
                                        tint = AvaPurpleLight,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (isAr) "تواصل مباشر مع فريق التطوير والإدارة" else "Contact Founder & Support Team",
                                        color = AvaTextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isAr)
                                        "نسعد بتلقي ملاحظاتك، اقتراحات التطوير، والتعاون الإبداعي مباشرة مع المؤسس أيهم."
                                    else
                                        "We welcome your feature inquiries, product feedback, and collaboration requests directly with founder Ayham.",
                                    color = AvaTextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = if (isAr) "اكتب رسالتك أو استفسارك" else "Write your message or inquiry",
                            color = AvaTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = supportMessage,
                            onValueChange = { supportMessage = it },
                            placeholder = {
                                Text(
                                    if (isAr) "اكتب رسالتك بالتفصيل هنا..." else "Type your inquiry or message here...",
                                    color = AvaTextSecondary
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .testTag("support_message_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AvaPurplePrimary,
                                unfocusedBorderColor = AvaCardBorder,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                    }

                    item {
                        if (supportSubmitted) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = AvaSuccess.copy(alpha = 0.15f)),
                                border = CardDefaults.outlinedCardBorder().copy(brush = SolidColor(AvaSuccess))
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = AvaSuccess,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = if (isAr) "تم إرسال رسالتك بنجاح! شكرًا لتواصلك معنا."
                                        else "Message submitted successfully! Thank you for reaching out.",
                                        color = AvaSuccess,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Button(
                            onClick = {
                                if (supportMessage.isNotBlank()) {
                                    viewModel.submitReport(
                                        targetType = "SUPPORT_INQUIRY",
                                        targetId = "help_desk",
                                        reason = "GENERAL_SUPPORT",
                                        details = supportMessage
                                    )
                                    supportSubmitted = true
                                    supportMessage = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("send_support_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AvaPurplePrimary)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAr) "إرسال الرسالة" else "Send Message",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
