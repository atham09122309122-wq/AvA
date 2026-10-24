package com.example.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

enum class AppLanguage(val code: String, val title: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    ARABIC("ar", "Arabic", "العربية")
}

val LocalAppLanguage = compositionLocalOf { AppLanguage.ENGLISH }

object Strings {
    private val en = mapOf(
        "app_name" to "AVA Studio",
        "brand_signature" to "Made by Ayham",
        "welcome_back" to "Welcome back",
        "create_extraordinary" to "Create something extraordinary.",
        "search_hint" to "Search styles, templates, tools...",
        
        // Navigation
        "nav_home" to "Home",
        "nav_editor" to "Photo Editor",
        "nav_create" to "Create",
        "nav_templates" to "Templates",
        "nav_projects" to "My Projects",
        "nav_explore" to "Explore",
        "nav_characters" to "Characters",
        "nav_profile" to "Profile",
        
        // Main Cards
        "personal_photo_title" to "Personal Photo",
        "personal_photo_desc" to "Transform your photos while keeping your identity authentic.",
        "edit_my_photo" to "Edit My Photo",
        "ad_studio_title" to "Advertisement Studio",
        "ad_studio_desc" to "Create professional promotional visuals for products, brands, and social media.",
        "create_advertisement" to "Create Advertisement",
        
        // Quick Tools
        "quick_tools" to "Quick Creative Tools",
        "tool_bg_removal" to "Background Removal",
        "tool_blur" to "Depth & Blur",
        "tool_crop" to "Crop & Fit",
        "tool_color" to "Color & Light",
        "tool_text" to "Typography",
        "tool_filters" to "Filters",
        
        // Sections
        "continue_editing" to "Continue Editing",
        "my_characters" to "My Characters",
        "trending_templates" to "Trending Templates",
        "recent_projects" to "Recent Projects",
        "ava_coins_balance" to "AVA Coins Balance",
        "claim_daily_reward" to "Claim +50 Coins",
        "coins_claimed" to "Daily reward claimed!",
        "view_all" to "View All",
        "use_template" to "Use Template",
        
        // Auth
        "sign_in" to "Sign In",
        "sign_up" to "Create Account",
        "sign_out" to "Sign Out",
        "forgot_password" to "Forgot Password?",
        "reset_password" to "Reset Password",
        "display_name" to "Display Name",
        "username" to "Username",
        "email" to "Email Address",
        "password" to "Password",
        "confirm_password" to "Confirm Password",
        "accept_terms" to "I accept the Terms of Service & Privacy Policy",
        "already_have_account" to "Already have an account? Sign In",
        "dont_have_account" to "Don't have an account? Create one",
        "or_divider" to "OR",
        "google_sign_in" to "Sign in with Google",
        "account_created_success" to "Account created successfully",
        "invalid_email" to "Please enter a valid email address",
        "password_too_short" to "Password must be at least 6 characters",
        "passwords_do_not_match" to "Passwords do not match",
        "username_taken" to "This username is already taken",
        "fill_all_fields" to "Please fill in all fields",
        "auth_error" to "Invalid credentials. Please verify your details.",
        
        // Ad Studio Wizard Steps
        "ad_step_1_title" to "1. Advertisement Type",
        "ad_step_2_title" to "2. Product & Brand Details",
        "ad_step_3_title" to "3. Canvas Format",
        "ad_step_4_title" to "4. Visual Direction",
        "brand_name" to "Brand Name",
        "product_name" to "Product Name",
        "product_desc" to "Product Description",
        "main_headline" to "Main Headline",
        "supporting_text" to "Supporting Text / Subtitle",
        "call_to_action" to "Call to Action",
        "website_handle" to "Website or Social Handle",
        "target_platform" to "Target Platform",
        "upload_image" to "Select Product Image",
        "open_in_editor" to "Open in Studio Editor",
        "next_step" to "Next Step",
        "previous_step" to "Back",
        
        // Personal Photo Studio
        "identity_safe_badge" to "Identity Preservation Guaranteed",
        "identity_safe_desc" to "Facial geometry and authenticity are strictly preserved.",
        "upload_photo" to "Select / Change Photo",
        "intended_style" to "Creative Direction",
        "adjustments" to "Fine Adjustments",
        "lighting_bg" to "Lighting & Backdrop",
        "before_after" to "Before / After",
        "brightness" to "Brightness",
        "contrast" to "Contrast",
        "saturation" to "Saturation",
        "temperature" to "Color Temperature",
        "exposure" to "Exposure",
        "sharpness" to "Sharpness",
        "vignette" to "Vignette",
        "grain" to "Film Grain",
        "bg_blur" to "Backdrop Blur",
        
        // Editor
        "editor_title" to "Studio Canvas",
        "undo" to "Undo",
        "redo" to "Redo",
        "save_project" to "Save",
        "export" to "Export",
        "export_png" to "Export PNG",
        "export_jpg" to "Export JPG",
        "add_text" to "Add Text",
        "add_shape" to "Add Shape",
        "layers" to "Layers",
        "canvas_size" to "Canvas Size",
        "unsaved_dialog_title" to "Discard Unsaved Work?",
        "unsaved_dialog_msg" to "You have unsaved changes in this editing session. Are you sure you want to leave?",
        "keep_editing" to "Keep Editing",
        "discard_leave" to "Discard & Leave",
        "export_success" to "Design exported and saved successfully!",
        
        // Character Studio
        "character_studio_title" to "Character Studio",
        "character_studio_sub" to "Design your personal virtual identity in AVA.",
        "save_character" to "Save Character",
        "set_as_avatar" to "Set as Profile Avatar",
        "character_name" to "Character Name",
        "skin_tone" to "Skin Tone",
        "face_features" to "Face & Eyes",
        "hair_style" to "Hairstyle",
        "tops_clothing" to "Top Wear",
        "bottoms_clothing" to "Bottom Wear",
        "shoes" to "Sneakers & Shoes",
        "accessories" to "Accessories",
        "character_pose" to "Pose",
        "character_backdrop" to "Backdrop",
        "wardrobe_store" to "Wardrobe Catalog",
        "try_on" to "Try On",
        "unlock_for" to "Unlock for",
        "owned" to "Owned",
        "insufficient_coins" to "Not enough AVA coins. Claim daily reward or earn more!",
        
        // Profile & Settings
        "edit_profile" to "Edit Profile",
        "share_profile" to "Share Profile",
        "settings" to "Settings",
        "bio" to "Biography",
        "designs_count" to "Designs",
        "characters_count" to "Characters",
        "coins_wallet" to "AVA Coins",
        "tab_designs" to "My Designs",
        "tab_photos" to "My Photos",
        "tab_characters" to "Characters",
        "tab_saved" to "Saved",
        "language_settings" to "Language & Region",
        "about_ava" to "About AVA Studio",
        "help_feedback" to "Help & Feedback",
        "notifications" to "Notifications",
        "save_changes" to "Save Changes",
        "cancel" to "Cancel",
        "delete_account" to "Delete Account",
        "empty_projects" to "No projects yet. Create something extraordinary!",
        "empty_characters" to "No saved characters yet. Build your first avatar!"
    )

    private val ar = mapOf(
        "app_name" to "AVA ستوديو",
        "brand_signature" to "صنع بواسطة أيهم",
        "welcome_back" to "مرحباً بعودتك",
        "create_extraordinary" to "اصنع شيئاً استثنائياً.",
        "search_hint" to "ابحث عن أنماط، قوالب، أدوات...",
        
        // Navigation
        "nav_home" to "الرئيسية",
        "nav_editor" to "محرر الصور",
        "nav_create" to "إنشاء",
        "nav_templates" to "القوالب",
        "nav_projects" to "مشاريعي",
        "nav_explore" to "استكشاف",
        "nav_characters" to "الشخصيات",
        "nav_profile" to "الملف الشخصي",
        
        // Main Cards
        "personal_photo_title" to "الصور الشخصية",
        "personal_photo_desc" to "حوّل صورك بطريقة احترافية مع الحفاظ على هويتك وملامحك الحقيقية.",
        "edit_my_photo" to "تعديل صورتي",
        "ad_studio_title" to "ستوديو الإعلانات",
        "ad_studio_desc" to "أنشئ تصاميم ترويجية احترافية للمنتجات والماركات ومواقع التواصل.",
        "create_advertisement" to "إنشاء إعلان",
        
        // Quick Tools
        "quick_tools" to "أدوات إبداعية سريعة",
        "tool_bg_removal" to "إزالة الخلفية",
        "tool_blur" to "العمق والضبابية",
        "tool_crop" to "القص والملاءمة",
        "tool_color" to "اللون والإضاءة",
        "tool_text" to "الخطوط والنصوص",
        "tool_filters" to "الفلاتر",
        
        // Sections
        "continue_editing" to "متابعة التعديل",
        "my_characters" to "شخصياتي",
        "trending_templates" to "القوالب الشائعة",
        "recent_projects" to "المشاريع الأخيرة",
        "ava_coins_balance" to "رصيد عملات AVA",
        "claim_daily_reward" to "استلام +50 عملة",
        "coins_claimed" to "تم استلام المكافأة اليومية!",
        "view_all" to "عرض الكل",
        "use_template" to "استخدام القالب",
        
        // Auth
        "sign_in" to "تسجيل الدخول",
        "sign_up" to "إنشاء حساب",
        "sign_out" to "تسجيل الخروج",
        "forgot_password" to "نسيت كلمة المرور؟",
        "reset_password" to "إعادة ضبط كلمة المرور",
        "display_name" to "الاسم المعروض",
        "username" to "اسم المستخدم",
        "email" to "البريد الإلكتروني",
        "password" to "كلمة المرور",
        "confirm_password" to "تأكيد كلمة المرور",
        "accept_terms" to "أوافق على شروط الخدمة وسياسة الخصوصية",
        "already_have_account" to "لديك حساب بالفعل؟ تسجيل الدخول",
        "dont_have_account" to "ليس لديك حساب؟ أنشئ حساباً جديداً",
        "or_divider" to "أو",
        "google_sign_in" to "الدخول بواسطة جوجل",
        "account_created_success" to "تم إنشاء الحساب بنجاح",
        "invalid_email" to "يرجى إدخال بريد إلكتروني صحيح",
        "password_too_short" to "يجب ألا تقل كلمة المرور عن 6 أحرف",
        "passwords_do_not_match" to "كلمات المرور غير متطابقة",
        "username_taken" to "اسم المستخدم هذا مأخوذ بالفعل",
        "fill_all_fields" to "يرجى تعبئة جميع الحقول",
        "auth_error" to "بيانات الدخول غير صحيحة. يرجى التحقق.",
        
        // Ad Studio Wizard Steps
        "ad_step_1_title" to "1. نوع الإعلان",
        "ad_step_2_title" to "2. بيانات المنتج والعلامة",
        "ad_step_3_title" to "3. أبعاد لوحة التصميم",
        "ad_step_4_title" to "4. التوجه البصري والنمط",
        "brand_name" to "اسم العلامة التجارية",
        "product_name" to "اسم المنتج",
        "product_desc" to "وصف المنتج",
        "main_headline" to "العنوان الرئيسي",
        "supporting_text" to "النص الداعم / الوصف الفرعي",
        "call_to_action" to "زر الحث على اتخاذ إجراء (CTA)",
        "website_handle" to "الموقع أو حساب التواصل",
        "target_platform" to "المنصة المستهدفة",
        "upload_image" to "اختيار صورة المنتج",
        "open_in_editor" to "فتح في محرر التصميم",
        "next_step" to "الخطوة التالية",
        "previous_step" to "رجوع",
        
        // Personal Photo Studio
        "identity_safe_badge" to "حفظ الهوية والملامح مضمون",
        "identity_safe_desc" to "يتم الحفاظ على ملامح الوجه والأصالة تماماً وبشكل طبيعي.",
        "upload_photo" to "اختيار / تغيير الصورة",
        "intended_style" to "النمط الإبداعي",
        "adjustments" to "الضبط الدقيق",
        "lighting_bg" to "الإضاءة والخلفية",
        "before_after" to "قبل / بعد",
        "brightness" to "السطوع",
        "contrast" to "التباين",
        "saturation" to "التشبع اللوني",
        "temperature" to "حرارة اللون",
        "exposure" to "التعريض",
        "sharpness" to "الحدة",
        "vignette" to "تأطير الحواف",
        "grain" to "حبيبات الفيلم",
        "bg_blur" to "ضبابية الخلفية",
        
        // Editor
        "editor_title" to "محرر التصميم",
        "undo" to "تراجع",
        "redo" to "إعادة",
        "save_project" to "حفظ",
        "export" to "تصدير",
        "export_png" to "تصدير PNG",
        "export_jpg" to "تصدير JPG",
        "add_text" to "إضافة نص",
        "add_shape" to "إضافة شكل",
        "layers" to "الطبقات",
        "canvas_size" to "مقاس اللوحة",
        "unsaved_dialog_title" to "إلغاء التعديلات غير المحفوظة؟",
        "unsaved_dialog_msg" to "لديك تعديلات لم يتم حفظها في جلسة العمل هذه. هل أنت متأكد من المغادرة؟",
        "keep_editing" to "متابعة التعديل",
        "discard_leave" to "تجاهل ومغادرة",
        "export_success" to "تم تصدير وحفظ التصميم بنجاح!",
        
        // Character Studio
        "character_studio_title" to "ستوديو الشخصيات",
        "character_studio_sub" to "صمم هويتك الافتراضية الخاصة في AVA.",
        "save_character" to "حفظ الشخصية",
        "set_as_avatar" to "تعيين كصورة للملف الشخصي",
        "character_name" to "اسم الشخصية",
        "skin_tone" to "لون البشرة",
        "face_features" to "ملامح الوجه والعيون",
        "hair_style" to "تسريحة الشعر",
        "tops_clothing" to "الملابس العلوية",
        "bottoms_clothing" to "الملابس السفلية",
        "shoes" to "الأحذية",
        "accessories" to "الإكسسوارات",
        "character_pose" to "الوضعية",
        "character_backdrop" to "الخلفية",
        "wardrobe_store" to "كتالوج الأزياء",
        "try_on" to "تجربة",
        "unlock_for" to "فتح مقابل",
        "owned" to "مملوك",
        "insufficient_coins" to "رصيد عملات AVA غير كافٍ. استلم المكافأة اليومية!",
        
        // Profile & Settings
        "edit_profile" to "تعديل الملف",
        "share_profile" to "مشاركة الملف",
        "settings" to "الإعدادات",
        "bio" to "السيرة الشخصية",
        "designs_count" to "التصاميم",
        "characters_count" to "الشخصيات",
        "coins_wallet" to "عملات AVA",
        "tab_designs" to "تصاميمي",
        "tab_photos" to "صوري",
        "tab_characters" to "الشخصيات",
        "tab_saved" to "المحفوظات",
        "language_settings" to "اللغة والمنطقة",
        "about_ava" to "حول AVA ستوديو",
        "help_feedback" to "المساعدة والملاحظات",
        "notifications" to "الإشعارات",
        "save_changes" to "حفظ التغييرات",
        "cancel" to "إلغاء",
        "delete_account" to "حذف الحساب",
        "empty_projects" to "لا توجد مشاريع حتى الآن. اصنع شيئاً رائعاً!",
        "empty_characters" to "لا توجد شخصيات محفوظة. ابنِ صورتك الرمزية الأولى!"
    )

    fun get(key: String, lang: AppLanguage = AppLanguage.ENGLISH): String {
        val map = if (lang == AppLanguage.ARABIC) ar else en
        return map[key] ?: en[key] ?: key
    }
}

@Composable
fun stringRes(key: String): String {
    val currentLang = LocalAppLanguage.current
    return Strings.get(key, currentLang)
}

@Composable
fun ProvideAppLanguage(
    language: AppLanguage,
    content: @Composable () -> Unit
) {
    val layoutDirection = if (language == AppLanguage.ARABIC) LayoutDirection.Rtl else LayoutDirection.Ltr
    CompositionLocalProvider(
        LocalAppLanguage provides language,
        LocalLayoutDirection provides layoutDirection
    ) {
        content()
    }
}
