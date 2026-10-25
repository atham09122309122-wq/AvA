/**
 * AVA Studio - Core Application Logic & Professional Canvas Editing Engine
 * 100% Offline Compatible - Zero External Dependencies
 */

(function () {
  'use strict';

  // Application State
  const state = {
    currentLanguage: 'ar', // Default Arabic per user interface
    activeView: 'home',
    image: null,
    originalImageData: null,
    canvas: null,
    ctx: null,
    history: [],
    historyIndex: -1,
    activeTool: 'adjust',
    isComparing: false,
    zoomLevel: 1,
    panX: 0,
    panY: 0,
    adjustments: {
      exposure: 0,
      brightness: 0,
      contrast: 0,
      highlights: 0,
      shadows: 0,
      saturation: 0,
      vibrance: 0,
      warmth: 0,
      tint: 0,
      sharpness: 0,
      vignette: 0,
      blur: 0,
      skinSmooth: 0,
      activeFilter: 'none',
      filterIntensity: 100
    },
    adState: {
      headline: 'عرض الصيف الحصري',
      subline: 'خصم يصل إلى 50% على جميع المنتجات الفاخرة',
      badge: 'خصم 50%',
      cta: 'تسوق الآن',
      theme: 'purple'
    }
  };

  // Translations
  const i18n = {
    ar: {
      app_name: 'AVA Studio',
      author_badge: 'بواسطة أيهم',
      welcome_title: 'استوديو الإبداع الاحترافي',
      welcome_sub: 'منصة تعديل وتصميم الصور عالية الدقة والأعمال الإعلانية بمقاييس احترافية كاملة.',
      create_design: 'بدء تصميم جديد',
      personal_studio: 'استوديو الصور الشخصية',
      personal_desc: 'رتوش احترافي للبشرة، إضاءة استوديو، وفلاتر سينمائية نقية.',
      ad_studio: 'استوديو الإعلانات',
      ad_desc: 'إنشاء بوسترات وتصاميم ترويجية وشارات تسويقية جاهزة للنشر.',
      story_designer: 'مصمم القصص',
      story_desc: 'قوالب 9:16 رأسية لإنستغرام وتيك توك مع نصوص وتأثيرات مميزة.',
      pro_templates: 'قوالب احترافية',
      templates_desc: 'تشكيلة نماذج مصممة خصيصاً للمحتوى الرقمي والإعلاني.',
      quick_tools: 'أدوات التعديل السريع',
      quick_enhance: 'تحسين تلقائي ذكي',
      quick_bw: 'أبيض وأسود درامي',
      quick_teal: 'سينمائي تركواز وبرتقالي',
      quick_glow: 'توهج استوديو',
      quick_vignette: 'تركيز سينمائي',
      recent_projects: 'مشاريعك الأخيرة',
      inspiration: 'إلهام بصري وتصاميم مميزة',
      adjust: 'تعديل',
      filters: 'فلاتر',
      retouch: 'رتوش',
      color: 'ألوان',
      bokeh: 'بوكيه',
      lighting: 'إضاءة',
      crop: 'قص',
      details: 'تفاصيل',
      text: 'نص',
      exposure: 'التعريض الضوئي',
      brightness: 'السطوع',
      contrast: 'التباين',
      highlights: 'المناطق الساطعة',
      shadows: 'الظلال',
      saturation: 'التشبع اللوني',
      vibrance: 'الحيوية',
      warmth: 'حرارة اللون',
      tint: 'تدرج الظلال',
      sharpness: 'الحدة',
      vignette: 'تظليل الحواف',
      skin_smooth: 'تنعيم البشرة',
      compare: 'قبل / بعد',
      fit_image: 'ملاءمة الحجم',
      save: 'حفظ',
      export: 'تصدير',
      undo: 'تراجع',
      redo: 'إعادة',
      upload_photo: 'اختر صورة من جهازك',
      ad_title_input: 'عنوان الإعلان',
      ad_sub_input: 'النص الترويجي',
      ad_badge_input: 'نص الشارة',
      ad_cta_input: 'زر الإجراء',
      export_success: 'تم حفظ الصورة بنجاح في جهازك',
      auth_welcome: 'مرحباً بك في AVA',
      auth_welcome_sub: 'استوديو التصميم والإبداع الفاخر',
      login: 'تسجيل الدخول',
      register: 'إنشاء حساب',
      email: 'البريد الإلكتروني',
      password: 'كلمة المرور',
      forgot_password: 'نسيت كلمة المرور؟',
      sign_in: 'تسجيل الدخول',
      display_name: 'الاسم الظاهر',
      username: 'اسم المستخدم (فريد)',
      confirm_password: 'تأكيد كلمة المرور',
      agree_terms: 'أوافق على شروط الاستخدام وسياسة الخصوصية',
      create_account: 'إنشاء الحساب',
      continue_guest: 'المتابعة كضيف',
      verify_email_title: 'تأكيد البريد الإلكتروني',
      verify_btn: 'تأكيد الرمز',
      resend_code: 'إعادة إرسال الرمز',
      back_to_login: 'العودة لتسجيل الدخول',
      reset_pass_title: 'استعادة كلمة المرور',
      reset_pass_sub: 'أدخل بريدك الإلكتروني لإرسال رمز إعادة التعيين',
      send_code: 'إرسال رمز التحقق',
      verification_code: 'رمز التحقق السداسي',
      new_password: 'كلمة المرور الجديدة',
      update_password: 'حفظ كلمة المرور الجديدة',
      projects: 'المشاريع',
      coins: 'الرصيد',
      followers: 'المتابعون',
      edit_profile: 'تعديل الملف',
      settings: 'الإعدادات',
      my_creations: 'أعمالك وتصاميمك المحفوظة',
      settings_title: 'إعدادات الحساب والخصوصية',
      account_security: 'الأمان والحساب',
      change_password: 'تغيير كلمة المرور',
      change_password_desc: 'تحديث كلمة السر وحماية الحساب',
      preferences: 'التفضيلات',
      app_language: 'لغة التطبيق',
      session_management: 'إدارة الجلسة',
      logout: 'تسجيل الخروج',
      logout_desc: 'إنهاء الجلسة على هذا الجهاز',
      delete_account: 'حذف الحساب نهائياً',
      delete_account_desc: 'مسح جميع المشاريع والبيانات بشكل كامل',
      bio: 'النبذة التعريفية',
      save_changes: 'حفظ التعديلات',
      current_password: 'كلمة المرور الحالية',
      confirm_new_password: 'تأكيد كلمة المرور الجديدة',
      confirm_change: 'تأكيد التغيير',
      delete_account_confirm_title: 'حذف الحساب نهائياً؟',
      delete_account_warn: 'سيؤدي هذا الإجراء إلى مسح حسابك وكافة أعمالك ومشاريعك المحفوظة نهائياً من قاعدة البيانات المحلية. لن تتمكن من استرجاع أي بيانات بعد الحذف.',
      confirm_delete: 'نعم، حذف الحساب نهائياً',
      cancel: 'إلغاء',
      nav_home: 'الرئيسية',
      nav_editor: 'استوديو الصور',
      nav_ads: 'الإعلانات',
      nav_stories: 'القصص',
      nav_profile: 'حسابي'
    },
    en: {
      app_name: 'AVA Studio',
      author_badge: 'By Ayham',
      welcome_title: 'Pro Creative Studio',
      welcome_sub: 'High-end photo editing, professional retouching and advertisement design suite.',
      create_design: 'Create New Design',
      personal_studio: 'Personal Photo Studio',
      personal_desc: 'Professional skin retouch, studio lighting and clean cinematic grading.',
      ad_studio: 'Ad Studio',
      ad_desc: 'Craft engaging promotional posters, marketing banners, and sales graphics.',
      story_designer: 'Story Designer',
      story_desc: '9:16 vertical layouts for Instagram and TikTok with customizable typography.',
      pro_templates: 'Pro Templates',
      templates_desc: 'Curated creative templates ready for digital campaigns and social media.',
      quick_tools: 'Quick Tools',
      quick_enhance: 'Smart Auto Enhance',
      quick_bw: 'Dramatic B&W',
      quick_teal: 'Teal & Orange',
      quick_glow: 'Studio Glow',
      quick_vignette: 'Cinematic Vignette',
      recent_projects: 'Recent Projects',
      inspiration: 'Visual Inspiration',
      adjust: 'Adjust',
      filters: 'Filters',
      retouch: 'Retouch',
      color: 'Color',
      bokeh: 'Bokeh',
      lighting: 'Lighting',
      crop: 'Crop',
      details: 'Details',
      text: 'Text',
      exposure: 'Exposure',
      brightness: 'Brightness',
      contrast: 'Contrast',
      highlights: 'Highlights',
      shadows: 'Shadows',
      saturation: 'Saturation',
      vibrance: 'Vibrance',
      warmth: 'Warmth',
      tint: 'Tint',
      sharpness: 'Sharpness',
      vignette: 'Vignette',
      skin_smooth: 'Skin Smoothing',
      compare: 'Before / After',
      fit_image: 'Fit Screen',
      save: 'Save',
      export: 'Export',
      undo: 'Undo',
      redo: 'Redo',
      upload_photo: 'Choose Photo From Device',
      ad_title_input: 'Ad Headline',
      ad_sub_input: 'Promo Subtitle',
      ad_badge_input: 'Badge Text',
      ad_cta_input: 'Call To Action',
      export_success: 'Image saved to your device',
      auth_welcome: 'Welcome to AVA',
      auth_welcome_sub: 'High-end design & creative suite',
      login: 'Sign In',
      register: 'Register',
      email: 'Email Address',
      password: 'Password',
      forgot_password: 'Forgot Password?',
      sign_in: 'Sign In',
      display_name: 'Display Name',
      username: 'Unique Username',
      confirm_password: 'Confirm Password',
      agree_terms: 'I agree to the Terms of Service & Privacy Policy',
      create_account: 'Create Account',
      continue_guest: 'Continue as Guest',
      verify_email_title: 'Email Verification',
      verify_btn: 'Verify Code',
      resend_code: 'Resend Code',
      back_to_login: 'Back to Sign In',
      reset_pass_title: 'Reset Password',
      reset_pass_sub: 'Enter your email to receive a 6-digit reset code',
      send_code: 'Send Verification Code',
      verification_code: '6-Digit Verification Code',
      new_password: 'New Password',
      update_password: 'Save New Password',
      projects: 'Projects',
      coins: 'Coins',
      followers: 'Followers',
      edit_profile: 'Edit Profile',
      settings: 'Settings',
      my_creations: 'Your Saved Projects',
      settings_title: 'Account & Privacy Settings',
      account_security: 'Security & Account',
      change_password: 'Change Password',
      change_password_desc: 'Update password & secure account',
      preferences: 'Preferences',
      app_language: 'App Language',
      session_management: 'Session Management',
      logout: 'Log Out',
      logout_desc: 'End session on this device',
      delete_account: 'Delete Account Permanently',
      delete_account_desc: 'Erase all projects and data completely',
      bio: 'Bio',
      save_changes: 'Save Changes',
      current_password: 'Current Password',
      confirm_new_password: 'Confirm New Password',
      confirm_change: 'Confirm Change',
      delete_account_confirm_title: 'Delete Account Permanently?',
      delete_account_warn: 'This will erase your account and all saved projects permanently from the local database. This cannot be undone.',
      confirm_delete: 'Yes, Delete Account Permanently',
      cancel: 'Cancel',
      nav_home: 'Home',
      nav_editor: 'Photo Studio',
      nav_ads: 'Ads',
      nav_stories: 'Stories',
      nav_profile: 'Profile'
    }
  };

  // Filter Presets Definition
  const FILTERS = [
    { id: 'none', name: 'Original', cat: 'natural' },
    { id: 'natural', name: 'Natural Clean', cat: 'natural', exp: 4, sat: 10, con: 8 },
    { id: 'soft_light', name: 'Soft Light', cat: 'natural', exp: 12, con: -10, warm: 8 },
    { id: 'warm_nat', name: 'Warm Natural', cat: 'natural', warm: 25, sat: 12, exp: 6 },
    { id: 'cool_nat', name: 'Cool Natural', cat: 'natural', warm: -25, sat: 8, exp: 4 },
    { id: 'editorial', name: 'Studio Editorial', cat: 'editorial', con: 22, sat: -8, sharp: 25 },
    { id: 'luxury', name: 'Luxury Velvet', cat: 'editorial', con: 18, warm: 12, vig: 20 },
    { id: 'minimal', name: 'Minimal Tone', cat: 'editorial', sat: -25, con: 12, exp: 8 },
    { id: 'urban', name: 'Urban Street', cat: 'street', con: 28, sat: 15, vig: 30 },
    { id: 'dark_street', name: 'Dark Street', cat: 'street', exp: -12, con: 32, sat: -15 },
    { id: 'moody', name: 'Moody Shadow', cat: 'street', con: 25, warm: -10, vig: 40 },
    { id: 'cinematic', name: 'Cinematic Gold', cat: 'cinematic', warm: 20, con: 18, vig: 25 },
    { id: 'teal_orange', name: 'Teal & Orange', cat: 'cinematic', warm: 18, tint: -20, con: 24, sat: 18 },
    { id: 'vintage_film', name: 'Vintage 35mm', cat: 'cinematic', warm: 30, con: -8, sat: -12, vig: 22 },
    { id: 'bw_classic', name: 'Classic B&W', cat: 'bw', sat: -100, con: 15 },
    { id: 'bw_contrast', name: 'High Contrast B&W', cat: 'bw', sat: -100, con: 45, exp: 5 },
    { id: 'bw_soft', name: 'Soft Noir', cat: 'bw', sat: -100, con: -8, exp: 10 },
    { id: 'purple_mood', name: 'AVA Purple Mood', cat: 'color', tint: 35, warm: -10, con: 15 },
    { id: 'deep_blue', name: 'Deep Midnight', cat: 'color', warm: -40, con: 20, exp: -6 },
    { id: 'night_flash', name: 'Night Flash', cat: 'night', exp: 18, con: 30, sat: 12, vig: 35 }
  ];

  // DOM Elements cache
  let elements = {};

  async function init() {
    setupDOMElements();
    setupCanvas();
    await AuthManager.init();
    setupEventListeners();
    setupAuthEventListeners();
    updateLocalization();
    loadSampleArtwork();
    renderRecentProjects();
    renderTopUserPill();
  }

  function setupDOMElements() {
    elements = {
      body: document.body,
      langBtn: document.getElementById('langToggleBtn'),
      topUserPillContainer: document.getElementById('topUserPillContainer'),
      navProfileBtn: document.getElementById('navProfileBtn'),
      navProfileLabel: document.getElementById('navProfileLabel'),
      views: {
        home: document.getElementById('viewHome'),
        editor: document.getElementById('viewEditor'),
        adStudio: document.getElementById('viewAdStudio'),
        storyDesigner: document.getElementById('viewStoryDesigner'),
        auth: document.getElementById('viewAuth'),
        profile: document.getElementById('viewProfile'),
        settings: document.getElementById('viewSettings')
      },
      canvas: document.getElementById('editorCanvas'),
      fileInput: document.getElementById('photoFileInput'),
      toolDrawer: document.getElementById('toolDrawerPanel'),
      toolTabs: document.querySelectorAll('.tool-tab-btn'),
      navButtons: document.querySelectorAll('.nav-item'),
      toast: document.getElementById('toastNotice'),
      undoBtn: document.getElementById('undoBtn'),
      redoBtn: document.getElementById('redoBtn'),
      compareBtn: document.getElementById('compareBtn'),
      fitBtn: document.getElementById('fitBtn'),
      exportBtn: document.getElementById('exportBtn'),
      editorTitle: document.getElementById('editorTitle'),
      // Auth & Profile Elements
      authMainBox: document.getElementById('authMainBox'),
      authVerifyBox: document.getElementById('authVerifyBox'),
      authForgotBox: document.getElementById('authForgotBox'),
      authAlertBox: document.getElementById('authAlertBox'),
      tabBtnLogin: document.getElementById('tabBtnLogin'),
      tabBtnRegister: document.getElementById('tabBtnRegister'),
      loginForm: document.getElementById('loginForm'),
      registerForm: document.getElementById('registerForm'),
      loginEmail: document.getElementById('loginEmail'),
      loginPassword: document.getElementById('loginPassword'),
      regDisplayName: document.getElementById('regDisplayName'),
      regUsername: document.getElementById('regUsername'),
      regEmail: document.getElementById('regEmail'),
      regPassword: document.getElementById('regPassword'),
      regPasswordConfirm: document.getElementById('regPasswordConfirm'),
      regTermsCheck: document.getElementById('regTermsCheck'),
      forgotPassLink: document.getElementById('forgotPassLink'),
      guestContinueBtn: document.getElementById('guestContinueBtn'),
      verifyEmailTarget: document.getElementById('verifyEmailTarget'),
      verifyAlertBox: document.getElementById('verifyAlertBox'),
      verifyConfirmBtn: document.getElementById('verifyConfirmBtn'),
      verifyResendBtn: document.getElementById('verifyResendBtn'),
      verifyBackBtn: document.getElementById('verifyBackBtn'),
      codeDigitInputs: document.querySelectorAll('.code-digit-input'),
      forgotAlertBox: document.getElementById('forgotAlertBox'),
      forgotStep1: document.getElementById('forgotStep1'),
      forgotStep2: document.getElementById('forgotStep2'),
      forgotEmail: document.getElementById('forgotEmail'),
      forgotSendBtn: document.getElementById('forgotSendBtn'),
      forgotCodeInput: document.getElementById('forgotCodeInput'),
      forgotNewPass: document.getElementById('forgotNewPass'),
      forgotResetBtn: document.getElementById('forgotResetBtn'),
      forgotBackBtn: document.getElementById('forgotBackBtn'),
      profileAvatar: document.getElementById('profileAvatar'),
      profileAvatarText: document.getElementById('profileAvatarText'),
      profileDisplayName: document.getElementById('profileDisplayName'),
      profileUsername: document.getElementById('profileUsername'),
      profileBio: document.getElementById('profileBio'),
      profileFounderBadge: document.getElementById('profileFounderBadge'),
      profileVerifiedBadge: document.getElementById('profileVerifiedBadge'),
      statProjectsCount: document.getElementById('statProjectsCount'),
      statCoinsCount: document.getElementById('statCoinsCount'),
      statFollowersCount: document.getElementById('statFollowersCount'),
      editProfileBtn: document.getElementById('editProfileBtn'),
      openSettingsBtn: document.getElementById('openSettingsBtn'),
      userProjectsGrid: document.getElementById('userProjectsGrid'),
      settingsBackBtn: document.getElementById('settingsBackBtn'),
      changePasswordSettingBtn: document.getElementById('changePasswordSettingBtn'),
      toggleLangSettingBtn: document.getElementById('toggleLangSettingBtn'),
      currentLangLabel: document.getElementById('currentLangLabel'),
      logoutSettingBtn: document.getElementById('logoutSettingBtn'),
      deleteAccountSettingBtn: document.getElementById('deleteAccountSettingBtn'),
      editProfileModal: document.getElementById('editProfileModal'),
      editDisplayNameInput: document.getElementById('editDisplayNameInput'),
      editBioInput: document.getElementById('editBioInput'),
      saveProfileChangesBtn: document.getElementById('saveProfileChangesBtn'),
      changePasswordModal: document.getElementById('changePasswordModal'),
      changePasswordAlert: document.getElementById('changePasswordAlert'),
      currentPassInput: document.getElementById('currentPassInput'),
      newPassInput: document.getElementById('newPassInput'),
      confirmNewPassInput: document.getElementById('confirmNewPassInput'),
      submitChangePassBtn: document.getElementById('submitChangePassBtn'),
      deleteAccountModal: document.getElementById('deleteAccountModal'),
      confirmDeleteAccountBtn: document.getElementById('confirmDeleteAccountBtn')
    };
  }

  function setupCanvas() {
    if (!elements.canvas) return;
    state.canvas = elements.canvas;
    state.ctx = elements.canvas.getContext('2d', { willReadFrequently: true });
  }

  function loadSampleArtwork() {
    const defaultCanvas = document.createElement('canvas');
    defaultCanvas.width = 1080;
    defaultCanvas.height = 1350;
    const ctx = defaultCanvas.getContext('2d');

    const grad = ctx.createLinearGradient(0, 0, 1080, 1350);
    grad.addColorStop(0, '#1E1233');
    grad.addColorStop(0.5, '#2D1B4E');
    grad.addColorStop(1, '#0F091A');
    ctx.fillStyle = grad;
    ctx.fillRect(0, 0, 1080, 1350);

    const glow = ctx.createRadialGradient(540, 500, 50, 540, 500, 450);
    glow.addColorStop(0, 'rgba(139, 92, 246, 0.45)');
    glow.addColorStop(0.7, 'rgba(99, 102, 241, 0.15)');
    glow.addColorStop(1, 'transparent');
    ctx.fillStyle = glow;
    ctx.fillRect(0, 0, 1080, 1350);

    ctx.fillStyle = 'rgba(255, 255, 255, 0.04)';
    ctx.beginPath();
    ctx.arc(540, 520, 280, 0, Math.PI * 2);
    ctx.fill();

    ctx.fillStyle = '#FFFFFF';
    ctx.textAlign = 'center';
    ctx.font = 'bold 92px sans-serif';
    ctx.fillText('AVA', 540, 500);

    ctx.font = '500 36px sans-serif';
    ctx.fillStyle = '#C4B5FD';
    ctx.fillText('CREATIVE STUDIO', 540, 560);

    ctx.font = '400 24px sans-serif';
    ctx.fillStyle = '#94A3B8';
    ctx.fillText('High Precision Photo Editor & Ad Suite', 540, 620);

    const img = new Image();
    img.onload = function () {
      setImage(img);
    };
    img.src = defaultCanvas.toDataURL('image/jpeg', 0.95);
  }

  function setImage(img) {
    state.image = img;
    state.canvas.width = img.width;
    state.canvas.height = img.height;
    state.ctx.drawImage(img, 0, 0);
    state.originalImageData = state.ctx.getImageData(0, 0, img.width, img.height);
    state.history = [state.ctx.getImageData(0, 0, img.width, img.height)];
    state.historyIndex = 0;
    applyAllFilters();
  }

  function switchView(viewName, title) {
    state.activeView = viewName;
    Object.keys(elements.views).forEach(key => {
      if (elements.views[key]) {
        elements.views[key].classList.toggle('active', key === viewName);
      }
    });

    elements.navButtons.forEach(btn => {
      btn.classList.toggle('active', btn.dataset.target === viewName);
    });

    if (title && elements.editorTitle) {
      elements.editorTitle.textContent = title;
    }

    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  function showToast(msg) {
    if (!elements.toast) return;
    elements.toast.textContent = msg;
    elements.toast.classList.add('show');
    setTimeout(() => {
      elements.toast.classList.remove('show');
    }, 2800);
  }

  function applyAllFilters() {
    if (!state.originalImageData) return;

    const w = state.canvas.width;
    const h = state.canvas.height;
    const output = state.ctx.createImageData(w, h);
    const src = state.originalImageData.data;
    const dst = output.data;

    const exp = state.adjustments.exposure * 1.5;
    const bri = state.adjustments.brightness * 1.2;
    const con = (state.adjustments.contrast + 100) / 100;
    const sat = (state.adjustments.saturation + 100) / 100;
    const warm = state.adjustments.warmth;
    const tint = state.adjustments.tint;
    const smooth = state.adjustments.skinSmooth;

    for (let i = 0; i < src.length; i += 4) {
      let r = src[i];
      let g = src[i + 1];
      let b = src[i + 2];
      let a = src[i + 3];

      r += exp + bri;
      g += exp + bri;
      b += exp + bri;

      r = ((r - 128) * con) + 128;
      g = ((g - 128) * con) + 128;
      b = ((b - 128) * con) + 128;

      r += warm * 0.6;
      b -= warm * 0.6;
      g += tint * 0.4;

      const gray = 0.299 * r + 0.587 * g + 0.114 * b;
      r = gray + (r - gray) * sat;
      g = gray + (g - gray) * sat;
      b = gray + (b - gray) * sat;

      if (smooth > 0 && r > 100 && g > 60 && b > 40 && r > g && g > b) {
        r = r * (1 - smooth * 0.002) + gray * (smooth * 0.002);
      }

      dst[i] = Math.min(255, Math.max(0, r));
      dst[i + 1] = Math.min(255, Math.max(0, g));
      dst[i + 2] = Math.min(255, Math.max(0, b));
      dst[i + 3] = a;
    }

    state.ctx.putImageData(output, 0, 0);

    if (state.adjustments.vignette > 0) {
      applyVignetteEffect(state.adjustments.vignette);
    }
  }

  function applyVignetteEffect(amount) {
    const w = state.canvas.width;
    const h = state.canvas.height;
    const radius = Math.max(w, h) * 0.75;
    const radial = state.ctx.createRadialGradient(w / 2, h / 2, radius * 0.4, w / 2, h / 2, radius);
    radial.addColorStop(0, 'rgba(0, 0, 0, 0)');
    radial.addColorStop(1, `rgba(0, 0, 0, ${amount / 120})`);
    state.ctx.fillStyle = radial;
    state.ctx.fillRect(0, 0, w, h);
  }

  function renderToolPanel(toolName) {
    state.activeTool = toolName;
    elements.toolTabs.forEach(t => t.classList.toggle('active', t.dataset.tool === toolName));
    const container = elements.toolDrawer;
    if (!container) return;

    const t = i18n[state.currentLanguage];

    switch (toolName) {
      case 'adjust':
        container.innerHTML = `
          <div class="control-group">
            <div class="control-header"><span>${t.exposure}</span><span id="val_exp">${state.adjustments.exposure}</span></div>
            <input type="range" class="slider-input" min="-100" max="100" value="${state.adjustments.exposure}" data-prop="exposure">
          </div>
          <div class="control-group">
            <div class="control-header"><span>${t.brightness}</span><span id="val_bri">${state.adjustments.brightness}</span></div>
            <input type="range" class="slider-input" min="-100" max="100" value="${state.adjustments.brightness}" data-prop="brightness">
          </div>
          <div class="control-group">
            <div class="control-header"><span>${t.contrast}</span><span id="val_con">${state.adjustments.contrast}</span></div>
            <input type="range" class="slider-input" min="-100" max="100" value="${state.adjustments.contrast}" data-prop="contrast">
          </div>
          <div class="control-group">
            <div class="control-header"><span>${t.saturation}</span><span id="val_sat">${state.adjustments.saturation}</span></div>
            <input type="range" class="slider-input" min="-100" max="100" value="${state.adjustments.saturation}" data-prop="saturation">
          </div>
        `;
        break;

      case 'filters':
        let filterHtml = '<div class="filter-strip">';
        FILTERS.forEach(f => {
          const isActive = state.adjustments.activeFilter === f.id;
          filterHtml += `
            <div class="filter-thumb-card ${isActive ? 'active' : ''}" data-filter="${f.id}">
              <div class="filter-preview-box">${f.name.substring(0, 6)}</div>
              <div class="filter-label">${f.name}</div>
            </div>
          `;
        });
        filterHtml += '</div>';
        container.innerHTML = filterHtml;
        break;

      case 'retouch':
        container.innerHTML = `
          <div class="control-group">
            <div class="control-header"><span>${t.skin_smooth}</span><span id="val_smooth">${state.adjustments.skinSmooth}</span></div>
            <input type="range" class="slider-input" min="0" max="100" value="${state.adjustments.skinSmooth}" data-prop="skinSmooth">
          </div>
          <div class="control-group">
            <div class="control-header"><span>${t.sharpness}</span><span id="val_sharp">${state.adjustments.sharpness}</span></div>
            <input type="range" class="slider-input" min="0" max="100" value="${state.adjustments.sharpness}" data-prop="sharpness">
          </div>
        `;
        break;

      case 'color':
        container.innerHTML = `
          <div class="control-group">
            <div class="control-header"><span>${t.warmth}</span><span id="val_warm">${state.adjustments.warmth}</span></div>
            <input type="range" class="slider-input" min="-100" max="100" value="${state.adjustments.warmth}" data-prop="warmth">
          </div>
          <div class="control-group">
            <div class="control-header"><span>${t.tint}</span><span id="val_tint">${state.adjustments.tint}</span></div>
            <input type="range" class="slider-input" min="-100" max="100" value="${state.adjustments.tint}" data-prop="tint">
          </div>
        `;
        break;

      case 'lighting':
      case 'bokeh':
      case 'details':
        container.innerHTML = `
          <div class="control-group">
            <div class="control-header"><span>${t.vignette}</span><span id="val_vig">${state.adjustments.vignette}</span></div>
            <input type="range" class="slider-input" min="0" max="100" value="${state.adjustments.vignette}" data-prop="vignette">
          </div>
        `;
        break;

      default:
        container.innerHTML = `<div style="text-align:center; color:#94A3B8; padding:8px;">${toolName} tool active</div>`;
        break;
    }

    container.querySelectorAll('.slider-input').forEach(slider => {
      slider.addEventListener('input', e => {
        const prop = e.target.dataset.prop;
        const val = parseInt(e.target.value, 10);
        state.adjustments[prop] = val;
        const badge = container.querySelector(`#val_${prop.substring(0, 3)}`);
        if (badge) badge.textContent = val;
        applyAllFilters();
      });
    });

    container.querySelectorAll('.filter-thumb-card').forEach(card => {
      card.addEventListener('click', () => {
        const fId = card.dataset.filter;
        applyFilterPreset(fId);
        container.querySelectorAll('.filter-thumb-card').forEach(c => c.classList.remove('active'));
        card.classList.add('active');
      });
    });
  }

  function applyFilterPreset(fId) {
    state.adjustments.activeFilter = fId;
    const f = FILTERS.find(x => x.id === fId);
    if (!f) return;

    state.adjustments.exposure = f.exp || 0;
    state.adjustments.contrast = f.con || 0;
    state.adjustments.saturation = f.sat || 0;
    state.adjustments.warmth = f.warm || 0;
    state.adjustments.tint = f.tint || 0;
    state.adjustments.vignette = f.vig || 0;

    applyAllFilters();
    showToast(`Filter: ${f.name}`);
  }

  function setupEventListeners() {
    if (elements.langBtn) {
      elements.langBtn.addEventListener('click', () => {
        state.currentLanguage = state.currentLanguage === 'ar' ? 'en' : 'ar';
        updateLocalization();
      });
    }

    elements.navButtons.forEach(btn => {
      btn.addEventListener('click', () => {
        const target = btn.dataset.target;
        if (target === 'editor') {
          switchView('editor', i18n[state.currentLanguage].personal_studio);
          renderToolPanel('adjust');
        } else if (target === 'adStudio') {
          switchView('adStudio', i18n[state.currentLanguage].ad_studio);
          renderAdCanvas();
        } else if (target === 'storyDesigner') {
          switchView('storyDesigner', i18n[state.currentLanguage].story_designer);
          renderStoryCanvas();
        } else if (target === 'profile') {
          if (AuthManager.currentUser) {
            switchView('profile');
            renderProfileScreen();
          } else {
            switchView('auth');
          }
        } else {
          switchView('home');
        }
      });
    });

    if (elements.fileInput) {
      elements.fileInput.addEventListener('change', e => {
        const file = e.target.files[0];
        if (file) {
          const reader = new FileReader();
          reader.onload = ev => {
            const img = new Image();
            img.onload = () => {
              setImage(img);
              switchView('editor', i18n[state.currentLanguage].personal_studio);
              renderToolPanel('adjust');
              showToast('Photo loaded');
            };
            img.src = ev.target.result;
          };
          reader.readAsDataURL(file);
        }
      });
    }

    elements.toolTabs.forEach(tab => {
      tab.addEventListener('click', () => {
        renderToolPanel(tab.dataset.tool);
      });
    });

    if (elements.compareBtn) {
      const toggleCompare = () => {
        if (!state.originalImageData) return;
        state.isComparing = !state.isComparing;
        if (state.isComparing) {
          state.ctx.putImageData(state.originalImageData, 0, 0);
          elements.compareBtn.classList.add('active');
        } else {
          applyAllFilters();
          elements.compareBtn.classList.remove('active');
        }
      };
      elements.compareBtn.addEventListener('mousedown', toggleCompare);
      elements.compareBtn.addEventListener('mouseup', toggleCompare);
      elements.compareBtn.addEventListener('touchstart', toggleCompare, { passive: true });
      elements.compareBtn.addEventListener('touchend', toggleCompare, { passive: true });
    }

    if (elements.exportBtn) {
      elements.exportBtn.addEventListener('click', () => {
        if (!state.canvas) return;
        const link = document.createElement('a');
        link.download = `AVA_Studio_${Date.now()}.jpg`;
        link.href = state.canvas.toDataURL('image/jpeg', 0.95);
        link.click();
        saveRecentProject();
        showToast(i18n[state.currentLanguage].export_success);
      });
    }

    document.querySelectorAll('.quick-tool-chip').forEach(chip => {
      chip.addEventListener('click', () => {
        const action = chip.dataset.action;
        switchView('editor', i18n[state.currentLanguage].personal_studio);
        renderToolPanel('filters');
        if (action === 'enhance') applyFilterPreset('natural');
        else if (action === 'bw') applyFilterPreset('bw_contrast');
        else if (action === 'teal') applyFilterPreset('teal_orange');
        else if (action === 'glow') applyFilterPreset('soft_light');
        else if (action === 'vignette') {
          state.adjustments.vignette = 40;
          applyAllFilters();
        }
      });
    });

    document.querySelectorAll('.feature-card').forEach(card => {
      card.addEventListener('click', () => {
        const dest = card.dataset.dest;
        if (dest === 'photo') {
          switchView('editor', i18n[state.currentLanguage].personal_studio);
          renderToolPanel('adjust');
        } else if (dest === 'ad') {
          switchView('adStudio', i18n[state.currentLanguage].ad_studio);
          renderAdCanvas();
        } else if (dest === 'story') {
          switchView('storyDesigner', i18n[state.currentLanguage].story_designer);
          renderStoryCanvas();
        }
      });
    });

    const heroBtn = document.getElementById('heroCreateBtn');
    if (heroBtn) {
      heroBtn.addEventListener('click', () => {
        switchView('editor', i18n[state.currentLanguage].personal_studio);
        renderToolPanel('adjust');
      });
    }

    const adHeadlineInput = document.getElementById('adHeadlineInput');
    const adSublineInput = document.getElementById('adSublineInput');
    const adBadgeInput = document.getElementById('adBadgeInput');
    const adCtaInput = document.getElementById('adCtaInput');

    if (adHeadlineInput) {
      adHeadlineInput.addEventListener('input', e => {
        state.adState.headline = e.target.value;
        renderAdCanvas();
      });
    }
    if (adSublineInput) {
      adSublineInput.addEventListener('input', e => {
        state.adState.subline = e.target.value;
        renderAdCanvas();
      });
    }
    if (adBadgeInput) {
      adBadgeInput.addEventListener('input', e => {
        state.adState.badge = e.target.value;
        renderAdCanvas();
      });
    }
    if (adCtaInput) {
      adCtaInput.addEventListener('input', e => {
        state.adState.cta = e.target.value;
        renderAdCanvas();
      });
    }
  }

  function renderAdCanvas() {
    const canvas = document.getElementById('adCanvas');
    if (!canvas) return;
    canvas.width = 1080;
    canvas.height = 1080;
    const ctx = canvas.getContext('2d');

    const bgGrad = ctx.createLinearGradient(0, 0, 1080, 1080);
    bgGrad.addColorStop(0, '#151322');
    bgGrad.addColorStop(1, '#0C0A14');
    ctx.fillStyle = bgGrad;
    ctx.fillRect(0, 0, 1080, 1080);

    const radial = ctx.createRadialGradient(540, 400, 50, 540, 400, 600);
    radial.addColorStop(0, 'rgba(139, 92, 246, 0.35)');
    radial.addColorStop(1, 'transparent');
    ctx.fillStyle = radial;
    ctx.fillRect(0, 0, 1080, 1080);

    ctx.fillStyle = '#8B5CF6';
    roundRect(ctx, 420, 180, 240, 64, 32);
    ctx.fill();

    ctx.fillStyle = '#FFFFFF';
    ctx.font = 'bold 28px sans-serif';
    ctx.textAlign = 'center';
    ctx.fillText(state.adState.badge || 'PROMOTION', 540, 222);

    ctx.fillStyle = '#FFFFFF';
    ctx.font = 'bold 64px sans-serif';
    ctx.fillText(state.adState.headline || 'Exclusive Offer', 540, 360);

    ctx.fillStyle = '#94A3B8';
    ctx.font = '400 32px sans-serif';
    ctx.fillText(state.adState.subline || 'Premium Quality Guaranteed', 540, 440);

    ctx.fillStyle = '#FFFFFF';
    roundRect(ctx, 390, 840, 300, 80, 40);
    ctx.fill();

    ctx.fillStyle = '#0F0E17';
    ctx.font = 'bold 32px sans-serif';
    ctx.fillText(state.adState.cta || 'Shop Now', 540, 892);
  }

  function renderStoryCanvas() {
    const canvas = document.getElementById('storyCanvas');
    if (!canvas) return;
    canvas.width = 1080;
    canvas.height = 1920;
    const ctx = canvas.getContext('2d');

    const bgGrad = ctx.createLinearGradient(0, 0, 1080, 1920);
    bgGrad.addColorStop(0, '#241442');
    bgGrad.addColorStop(0.5, '#140D26');
    bgGrad.addColorStop(1, '#080511');
    ctx.fillStyle = bgGrad;
    ctx.fillRect(0, 0, 1080, 1920);

    ctx.strokeStyle = 'rgba(139, 92, 246, 0.4)';
    ctx.lineWidth = 12;
    ctx.strokeRect(60, 60, 960, 1800);

    ctx.fillStyle = '#FFFFFF';
    ctx.font = 'bold 76px sans-serif';
    ctx.textAlign = 'center';
    ctx.fillText('AVA STORY STUDIO', 540, 420);

    ctx.font = '400 38px sans-serif';
    ctx.fillStyle = '#C4B5FD';
    ctx.fillText('Designed for 9:16 Full Screen', 540, 520);
  }

  function roundRect(ctx, x, y, width, height, radius) {
    ctx.beginPath();
    ctx.moveTo(x + radius, y);
    ctx.lineTo(x + width - radius, y);
    ctx.quadraticCurveTo(x + width, y, x + width, y + radius);
    ctx.lineTo(x + width, y + height - radius);
    ctx.quadraticCurveTo(x + width, y + height, x + width - radius, y + height);
    ctx.lineTo(x + radius, y + height);
    ctx.quadraticCurveTo(x, y + height, x, y + height - radius);
    ctx.lineTo(x, y + radius);
    ctx.quadraticCurveTo(x, y, x + radius, y);
    ctx.closePath();
  }

  function updateLocalization() {
    const lang = state.currentLanguage;
    const t = i18n[lang];
    elements.body.setAttribute('dir', lang === 'ar' ? 'rtl' : 'ltr');

    document.querySelectorAll('[data-i18n]').forEach(el => {
      const key = el.dataset.i18n;
      if (t[key]) {
        el.textContent = t[key];
      }
    });

    if (elements.langBtn) {
      elements.langBtn.textContent = lang === 'ar' ? 'English' : 'العربية';
    }

    renderToolPanel(state.activeTool);
  }

  function saveRecentProject() {
    try {
      const projects = JSON.parse(localStorage.getItem('ava_recent_projects') || '[]');
      projects.unshift({
        id: Date.now(),
        date: new Date().toLocaleDateString(),
        title: 'AVA Edit ' + (projects.length + 1)
      });
      localStorage.setItem('ava_recent_projects', JSON.stringify(projects.slice(0, 5)));
      renderRecentProjects();
    } catch (e) {}
  }

  function renderRecentProjects() {
    const list = document.getElementById('recentProjectsList');
    if (!list) return;
    try {
      const projects = JSON.parse(localStorage.getItem('ava_recent_projects') || '[]');
      const title = 'AVA Edit ' + (projects.length + 1);
      projects.unshift({
        id: Date.now(),
        date: new Date().toLocaleDateString(),
        title: title
      });
      localStorage.setItem('ava_recent_projects', JSON.stringify(projects.slice(0, 5)));
      renderRecentProjects();

      // Persist to user's isolated project collection with thumbnail
      if (state.canvas) {
        const thumb = state.canvas.toDataURL('image/jpeg', 0.6);
        AuthManager.saveUserProject(title, thumb);
      }
    } catch (e) {}
  }

  // ==========================================================================
  // Auth, Profile, Security & Account Management
  // ==========================================================================

  const AuthManager = {
    currentUser: null,
    tempVerificationEmail: null,
    tempResetEmail: null,

    async hashPassword(password, salt) {
      try {
        const enc = new TextEncoder();
        const data = enc.encode(password + '::' + salt + '::ava_studio_secret_2026');
        const hashBuffer = await crypto.subtle.digest('SHA-256', data);
        const hashArray = Array.from(new Uint8Array(hashBuffer));
        return hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
      } catch (e) {
        let hash = 0;
        const str = password + salt;
        for (let i = 0; i < str.length; i++) {
          hash = ((hash << 5) - hash) + str.charCodeAt(i);
          hash |= 0;
        }
        return 'fallback_' + Math.abs(hash).toString(16);
      }
    },

    getUsers() {
      try {
        const stored = localStorage.getItem('ava_users_v2');
        if (stored) return JSON.parse(stored);
      } catch (e) {}

      // Default Seed Users (Founder Ayham securely provisioned)
      const defaultUsers = [
        {
          id: 'user_ayham_creator',
          displayName: 'Ayham',
          username: 'ayham_creative',
          email: 'ayham@avastudio.io',
          salt: 'salt_ayham_founder_2026',
          passwordHash: '8cb388a1b6c68ad039bf8d4cf605d549fef56f2f01f8d4389025cead7c7b8e19',
          bio: 'المؤسس والمصمم الرئيسي لتطبيق AVA Studio. أصنع تصاميم وهوية بصرية فخمة.',
          avatar: null,
          isFounder: true,
          isVerified: true,
          isEmailVerified: true,
          coins: 1500,
          followers: '1.8k',
          createdAt: 1704067200000
        },
        {
          id: 'user_sara_art',
          displayName: 'Sara Visuals',
          username: 'sara_art',
          email: 'sara@avastudio.io',
          salt: 'salt_sara_creator_2026',
          passwordHash: '5a44ef6e3c3b0dcba988ffbc16a246944747714856f9a9cbb3d31481e1fe8512',
          bio: 'مصممة بوسترات ومحررة صور سينمائية. مهتمة بإعلانات الموضة والأزياء.',
          avatar: null,
          isFounder: false,
          isVerified: true,
          isEmailVerified: true,
          coins: 620,
          followers: '530',
          createdAt: 1704153600000
        }
      ];
      localStorage.setItem('ava_users_v2', JSON.stringify(defaultUsers));
      return defaultUsers;
    },

    saveUsers(users) {
      try {
        localStorage.setItem('ava_users_v2', JSON.stringify(users));
      } catch (e) {}
    },

    async init() {
      const users = this.getUsers();
      try {
        const sessionStr = localStorage.getItem('ava_session_v2');
        if (sessionStr) {
          const session = JSON.parse(sessionStr);
          const user = users.find(u => u.id === session.userId);
          if (user) {
            this.currentUser = user;
          }
        }
      } catch (e) {}
    },

    setSession(user) {
      this.currentUser = user;
      try {
        localStorage.setItem('ava_session_v2', JSON.stringify({
          userId: user.id,
          token: 'token_' + Date.now() + '_' + Math.random().toString(36).substring(2),
          loginAt: Date.now()
        }));
      } catch (e) {}
      renderTopUserPill();
    },

    clearSession() {
      this.currentUser = null;
      try {
        localStorage.removeItem('ava_session_v2');
      } catch (e) {}
      renderTopUserPill();
    },

    async login(email, password) {
      const cleanEmail = email.trim().toLowerCase();
      const users = this.getUsers();
      const user = users.find(u => u.email.toLowerCase() === cleanEmail);

      if (!user) {
        throw new Error(state.currentLanguage === 'ar' ? 'البريد الإلكتروني أو كلمة المرور غير صحيحة' : 'Invalid email or password');
      }

      const inputHash = await this.hashPassword(password, user.salt);
      if (inputHash !== user.passwordHash) {
        throw new Error(state.currentLanguage === 'ar' ? 'البريد الإلكتروني أو كلمة المرور غير صحيحة' : 'Invalid email or password');
      }

      if (!user.isEmailVerified) {
        this.tempVerificationEmail = user.email;
        throw new Error('EMAIL_NOT_VERIFIED');
      }

      this.setSession(user);
      return user;
    },

    async register(displayName, username, email, password) {
      const cleanName = displayName.trim();
      const cleanUsername = username.trim().toLowerCase().replace(/[^a-z0-9_]/g, '');
      const cleanEmail = email.trim().toLowerCase();

      if (!cleanName) {
        throw new Error(state.currentLanguage === 'ar' ? 'يرجى إدخال الاسم الظاهر' : 'Display name required');
      }
      if (cleanUsername.length < 3) {
        throw new Error(state.currentLanguage === 'ar' ? 'اسم المستخدم يجب أن يكون 3 أحرف على الأقل' : 'Username min 3 chars');
      }
      if (!cleanEmail.includes('@') || !cleanEmail.includes('.')) {
        throw new Error(state.currentLanguage === 'ar' ? 'صيغة البريد الإلكتروني غير صحيحة' : 'Invalid email address');
      }
      if (password.length < 6) {
        throw new Error(state.currentLanguage === 'ar' ? 'كلمة المرور يجب أن لا تقل عن 6 أحرف' : 'Password min 6 chars');
      }

      const users = this.getUsers();
      if (users.some(u => u.username.toLowerCase() === cleanUsername)) {
        throw new Error(state.currentLanguage === 'ar' ? 'اسم المستخدم مستخدم مسبقاً، اختر اسماً آخر' : 'Username already taken');
      }
      if (users.some(u => u.email.toLowerCase() === cleanEmail)) {
        throw new Error(state.currentLanguage === 'ar' ? 'البريد الإلكتروني مسجل بالفعل، يرجى تسجيل الدخول' : 'Email already registered');
      }

      const salt = 'salt_' + Math.random().toString(36).substring(2) + Date.now();
      const passwordHash = await this.hashPassword(password, salt);
      const verifyCode = Math.floor(100000 + Math.random() * 900000).toString();

      const newUser = {
        id: 'user_' + Date.now() + '_' + Math.random().toString(36).substring(2, 6),
        displayName: cleanName,
        username: cleanUsername,
        email: cleanEmail,
        salt: salt,
        passwordHash: passwordHash,
        bio: state.currentLanguage === 'ar' ? 'مبدع في AVA Studio' : 'Creator at AVA Studio',
        avatar: null,
        isFounder: false, // Normal users can NEVER register as founder
        isVerified: false,
        isEmailVerified: false,
        verificationCode: verifyCode,
        coins: 100,
        followers: '0',
        createdAt: Date.now()
      };

      users.push(newUser);
      this.saveUsers(users);
      this.tempVerificationEmail = cleanEmail;
      return { user: newUser, code: verifyCode };
    },

    verifyEmail(code) {
      const users = this.getUsers();
      const user = users.find(u => u.email.toLowerCase() === (this.tempVerificationEmail || '').toLowerCase());
      if (!user) {
        throw new Error(state.currentLanguage === 'ar' ? 'تعذر العثور على الحساب' : 'Account not found');
      }

      if (user.verificationCode && user.verificationCode.trim() === code.trim()) {
        user.isEmailVerified = true;
        delete user.verificationCode;
        this.saveUsers(users);
        this.setSession(user);
        return user;
      } else {
        throw new Error(state.currentLanguage === 'ar' ? 'رمز التحقق غير صحيح، حاول مجدداً' : 'Invalid verification code');
      }
    },

    resendVerification() {
      const users = this.getUsers();
      const user = users.find(u => u.email.toLowerCase() === (this.tempVerificationEmail || '').toLowerCase());
      if (!user) throw new Error('Account not found');
      const newCode = Math.floor(100000 + Math.random() * 900000).toString();
      user.verificationCode = newCode;
      this.saveUsers(users);
      return newCode;
    },

    requestPasswordReset(email) {
      const cleanEmail = email.trim().toLowerCase();
      const users = this.getUsers();
      const user = users.find(u => u.email.toLowerCase() === cleanEmail);
      if (!user) {
        throw new Error(state.currentLanguage === 'ar' ? 'لم يتم العثور على حساب بهذا البريد' : 'No account found with this email');
      }
      const resetCode = Math.floor(100000 + Math.random() * 900000).toString();
      user.resetCode = resetCode;
      this.saveUsers(users);
      this.tempResetEmail = cleanEmail;
      return resetCode;
    },

    async resetPassword(code, newPassword) {
      if (newPassword.length < 6) {
        throw new Error(state.currentLanguage === 'ar' ? 'كلمة المرور يجب أن لا تقل عن 6 أحرف' : 'Password min 6 chars');
      }
      const users = this.getUsers();
      const user = users.find(u => u.email.toLowerCase() === (this.tempResetEmail || '').toLowerCase());
      if (!user || !user.resetCode || user.resetCode.trim() !== code.trim()) {
        throw new Error(state.currentLanguage === 'ar' ? 'رمز التحقق غير صحيح' : 'Invalid verification code');
      }

      user.salt = 'salt_' + Math.random().toString(36).substring(2) + Date.now();
      user.passwordHash = await this.hashPassword(newPassword, user.salt);
      delete user.resetCode;
      this.saveUsers(users);
      return true;
    },

    async changePassword(currentPassword, newPassword) {
      if (!this.currentUser) throw new Error('Not logged in');
      if (newPassword.length < 6) {
        throw new Error(state.currentLanguage === 'ar' ? 'كلمة المرور يجب أن لا تقل عن 6 أحرف' : 'Password min 6 chars');
      }
      const currentHash = await this.hashPassword(currentPassword, this.currentUser.salt);
      if (currentHash !== this.currentUser.passwordHash) {
        throw new Error(state.currentLanguage === 'ar' ? 'كلمة المرور الحالية غير صحيحة' : 'Current password incorrect');
      }

      const users = this.getUsers();
      const user = users.find(u => u.id === this.currentUser.id);
      if (user) {
        user.salt = 'salt_' + Math.random().toString(36).substring(2) + Date.now();
        user.passwordHash = await this.hashPassword(newPassword, user.salt);
        this.saveUsers(users);
        this.currentUser = user;
      }
      return true;
    },

    updateProfile(displayName, bio) {
      if (!this.currentUser) return;
      const users = this.getUsers();
      const user = users.find(u => u.id === this.currentUser.id);
      if (user) {
        user.displayName = displayName.trim() || user.displayName;
        user.bio = bio.trim();
        this.saveUsers(users);
        this.currentUser = user;
        renderTopUserPill();
        renderProfileScreen();
      }
    },

    deleteAccount() {
      if (!this.currentUser) return;
      const userId = this.currentUser.id;
      let users = this.getUsers();
      users = users.filter(u => u.id !== userId);
      this.saveUsers(users);

      try {
        let allProjects = JSON.parse(localStorage.getItem('ava_user_projects') || '[]');
        allProjects = allProjects.filter(p => p.userId !== userId);
        localStorage.setItem('ava_user_projects', JSON.stringify(allProjects));
      } catch (e) {}

      this.clearSession();
    },

    getUserProjects() {
      try {
        const all = JSON.parse(localStorage.getItem('ava_user_projects') || '[]');
        const currentId = this.currentUser ? this.currentUser.id : 'guest';
        return all.filter(p => p.userId === currentId);
      } catch (e) {
        return [];
      }
    },

    saveUserProject(title, thumbDataUrl) {
      try {
        const all = JSON.parse(localStorage.getItem('ava_user_projects') || '[]');
        const currentId = this.currentUser ? this.currentUser.id : 'guest';
        const newProject = {
          id: 'proj_' + Date.now(),
          userId: currentId,
          title: title || (state.currentLanguage === 'ar' ? 'مشروع تصميم' : 'Creative Project'),
          date: new Date().toLocaleDateString(),
          thumb: thumbDataUrl
        };
        all.unshift(newProject);
        localStorage.setItem('ava_user_projects', JSON.stringify(all.slice(0, 30)));
      } catch (e) {}
    },

    deleteUserProject(projectId) {
      try {
        let all = JSON.parse(localStorage.getItem('ava_user_projects') || '[]');
        const currentId = this.currentUser ? this.currentUser.id : 'guest';
        all = all.filter(p => !(p.id === projectId && p.userId === currentId));
        localStorage.setItem('ava_user_projects', JSON.stringify(all));
        renderUserProjectsGrid();
      } catch (e) {}
    }
  };

  // Top User Pill Renderer
  function renderTopUserPill() {
    if (!elements.topUserPillContainer) return;
    const isAr = state.currentLanguage === 'ar';
    const user = AuthManager.currentUser;

    if (user) {
      elements.topUserPillContainer.innerHTML = `
        <button class="user-pill-btn" id="topUserPillBtn" title="${user.displayName}">
          <div class="user-pill-avatar">${user.displayName.charAt(0).toUpperCase()}</div>
          <span>${user.displayName}</span>
          ${user.isFounder ? `<span style="color:var(--accent-gold); font-size:0.75rem; font-weight:800;">★</span>` : ''}
        </button>
      `;
      document.getElementById('topUserPillBtn').addEventListener('click', () => {
        switchView('profile');
        renderProfileScreen();
      });
      if (elements.navProfileLabel) {
        elements.navProfileLabel.textContent = isAr ? 'حسابي' : 'Profile';
      }
    } else {
      elements.topUserPillContainer.innerHTML = `
        <button class="user-pill-btn" id="topUserPillBtn">
          <span>${isAr ? 'تسجيل الدخول' : 'Sign In'}</span>
        </button>
      `;
      document.getElementById('topUserPillBtn').addEventListener('click', () => {
        switchView('auth');
      });
      if (elements.navProfileLabel) {
        elements.navProfileLabel.textContent = isAr ? 'دخول' : 'Sign In';
      }
    }
  }

  // Profile Screen Renderer
  function renderProfileScreen() {
    const user = AuthManager.currentUser;
    if (!user) return;
    if (elements.profileDisplayName) elements.profileDisplayName.textContent = user.displayName;
    if (elements.profileUsername) elements.profileUsername.textContent = '@' + user.username;
    if (elements.profileBio) elements.profileBio.textContent = user.bio || (state.currentLanguage === 'ar' ? 'مبدع ومصمم في AVA Studio' : 'AVA Studio Creator');
    if (elements.profileAvatarText) elements.profileAvatarText.textContent = user.displayName.charAt(0).toUpperCase();

    if (elements.profileFounderBadge) {
      elements.profileFounderBadge.style.display = user.isFounder ? 'inline-flex' : 'none';
      elements.profileFounderBadge.textContent = state.currentLanguage === 'ar' ? 'المؤسس' : 'Founder';
    }
    if (elements.profileVerifiedBadge) {
      elements.profileVerifiedBadge.style.display = (user.isVerified || user.isFounder) ? 'inline-flex' : 'none';
    }

    if (elements.statCoinsCount) elements.statCoinsCount.textContent = (user.coins || 0).toLocaleString();
    if (elements.statFollowersCount) elements.statFollowersCount.textContent = user.followers || '0';

    const projects = AuthManager.getUserProjects();
    if (elements.statProjectsCount) elements.statProjectsCount.textContent = projects.length;

    renderUserProjectsGrid();
  }

  // User Projects Grid Renderer
  function renderUserProjectsGrid() {
    if (!elements.userProjectsGrid) return;
    const isAr = state.currentLanguage === 'ar';
    const projects = AuthManager.getUserProjects();

    if (projects.length === 0) {
      elements.userProjectsGrid.innerHTML = `
        <div style="grid-column: 1 / -1; text-align:center; padding:32px 16px; color:var(--text-tertiary); font-size:0.85rem;">
          ${isAr ? 'لا توجد تصاميم محفوظة بعد. ابدأ تصميماً جديداً من الاستوديو!' : 'No saved creations yet. Start a new design from the studio!'}
        </div>
      `;
      return;
    }

    elements.userProjectsGrid.innerHTML = projects.map(p => `
      <div class="user-project-card" data-proj-id="${p.id}">
        <div class="user-project-thumb">
          ${p.thumb ? `<img src="${p.thumb}" alt="${p.title}">` : `<div style="width:100%; height:100%; background:linear-gradient(135deg, #2D1B4E, #140D26);"></div>`}
        </div>
        <div class="user-project-info">
          <div class="user-project-title">${p.title}</div>
          <div class="user-project-date">${p.date}</div>
        </div>
        <div class="user-project-actions">
          <button class="user-project-action-btn load-proj-btn" data-id="${p.id}">${isAr ? 'فتح' : 'Open'}</button>
          <button class="user-project-action-btn delete delete-proj-btn" data-id="${p.id}">✕</button>
        </div>
      </div>
    `).join('');

    elements.userProjectsGrid.querySelectorAll('.delete-proj-btn').forEach(btn => {
      btn.addEventListener('click', (e) => {
        e.stopPropagation();
        const id = btn.dataset.id;
        AuthManager.deleteUserProject(id);
        renderProfileScreen();
        showToast(isAr ? 'تم حذف المشروع' : 'Project deleted');
      });
    });

    elements.userProjectsGrid.querySelectorAll('.load-proj-btn').forEach(btn => {
      btn.addEventListener('click', () => {
        const id = btn.dataset.id;
        const p = projects.find(x => x.id === id);
        if (p && p.thumb) {
          const img = new Image();
          img.onload = () => {
            setImage(img);
            switchView('editor', p.title);
            renderToolPanel('adjust');
          };
          img.src = p.thumb;
        }
      });
    });
  }

  // Setup Auth Event Listeners
  function setupAuthEventListeners() {
    const isAr = () => state.currentLanguage === 'ar';

    // Tabs Switch
    if (elements.tabBtnLogin && elements.tabBtnRegister) {
      elements.tabBtnLogin.addEventListener('click', () => {
        elements.tabBtnLogin.classList.add('active');
        elements.tabBtnRegister.classList.remove('active');
        elements.loginForm.style.display = 'block';
        elements.registerForm.style.display = 'none';
        hideAuthAlert();
      });
      elements.tabBtnRegister.addEventListener('click', () => {
        elements.tabBtnRegister.classList.add('active');
        elements.tabBtnLogin.classList.remove('active');
        elements.registerForm.style.display = 'block';
        elements.loginForm.style.display = 'none';
        hideAuthAlert();
      });
    }

    // Password Visibility Toggles
    document.querySelectorAll('.auth-password-toggle').forEach(btn => {
      btn.addEventListener('click', () => {
        const targetId = btn.dataset.target;
        const input = document.getElementById(targetId);
        if (input) {
          input.type = input.type === 'password' ? 'text' : 'password';
        }
      });
    });

    // Helper functions for alerts
    function showAuthAlert(msg, isSuccess = false) {
      if (!elements.authAlertBox) return;
      elements.authAlertBox.textContent = msg;
      elements.authAlertBox.className = `auth-alert-box ${isSuccess ? 'success' : 'error'}`;
      elements.authAlertBox.style.display = 'flex';
    }

    function hideAuthAlert() {
      if (elements.authAlertBox) elements.authAlertBox.style.display = 'none';
    }

    // Login Form Submit
    if (elements.loginForm) {
      elements.loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        hideAuthAlert();
        const email = elements.loginEmail.value;
        const password = elements.loginPassword.value;

        try {
          await AuthManager.login(email, password);
          showToast(isAr() ? 'تم تسجيل الدخول بنجاح! مرحباً بك' : 'Signed in successfully! Welcome');
          switchView('profile');
          renderProfileScreen();
        } catch (err) {
          if (err.message === 'EMAIL_NOT_VERIFIED') {
            showVerificationBox(email);
          } else {
            showAuthAlert(err.message);
          }
        }
      });
    }

    // Register Form Submit
    if (elements.registerForm) {
      elements.registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        hideAuthAlert();

        const name = elements.regDisplayName.value;
        const username = elements.regUsername.value;
        const email = elements.regEmail.value;
        const pass = elements.regPassword.value;
        const passConfirm = elements.regPasswordConfirm.value;

        if (pass !== passConfirm) {
          showAuthAlert(isAr() ? 'كلمتا المرور غير متطابقتين' : 'Passwords do not match');
          return;
        }

        try {
          const res = await AuthManager.register(name, username, email, pass);
          showVerificationBox(email, res.code);
        } catch (err) {
          showAuthAlert(err.message);
        }
      });
    }

    // Guest Continue
    if (elements.guestContinueBtn) {
      elements.guestContinueBtn.addEventListener('click', () => {
        switchView('home');
      });
    }

    // Show Verification Box
    function showVerificationBox(email, demoCode) {
      if (elements.authMainBox) elements.authMainBox.style.display = 'none';
      if (elements.authForgotBox) elements.authForgotBox.style.display = 'none';
      if (elements.authVerifyBox) {
        elements.authVerifyBox.style.display = 'block';
        if (elements.verifyEmailTarget) {
          elements.verifyEmailTarget.textContent = (isAr() ? 'تم إرسال رمز التحقق إلى: ' : 'Verification code sent to: ') + email;
        }
        if (demoCode && elements.verifyAlertBox) {
          elements.verifyAlertBox.style.display = 'flex';
          elements.verifyAlertBox.className = 'auth-alert-box success';
          elements.verifyAlertBox.textContent = (isAr() ? 'رمز التحقق الخاص بك هو: ' : 'Your verification code is: ') + demoCode;
        }
      }
    }

    // 6-digit inputs auto-focus
    if (elements.codeDigitInputs) {
      elements.codeDigitInputs.forEach((input, index) => {
        input.addEventListener('input', () => {
          if (input.value.length === 1 && index < elements.codeDigitInputs.length - 1) {
            elements.codeDigitInputs[index + 1].focus();
          }
        });
        input.addEventListener('keydown', (e) => {
          if (e.key === 'Backspace' && !input.value && index > 0) {
            elements.codeDigitInputs[index - 1].focus();
          }
        });
      });
    }

    // Verify Confirm Button
    if (elements.verifyConfirmBtn) {
      elements.verifyConfirmBtn.addEventListener('click', () => {
        let code = '';
        elements.codeDigitInputs.forEach(i => code += i.value);
        try {
          AuthManager.verifyEmail(code);
          showToast(isAr() ? 'تم تأكيد البريد وتفعيل الحساب بنجاح!' : 'Email verified and account activated!');
          if (elements.authVerifyBox) elements.authVerifyBox.style.display = 'none';
          if (elements.authMainBox) elements.authMainBox.style.display = 'block';
          switchView('profile');
          renderProfileScreen();
        } catch (err) {
          if (elements.verifyAlertBox) {
            elements.verifyAlertBox.style.display = 'flex';
            elements.verifyAlertBox.className = 'auth-alert-box error';
            elements.verifyAlertBox.textContent = err.message;
          }
        }
      });
    }

    // Resend Code
    if (elements.verifyResendBtn) {
      elements.verifyResendBtn.addEventListener('click', () => {
        try {
          const newCode = AuthManager.resendVerification();
          if (elements.verifyAlertBox) {
            elements.verifyAlertBox.style.display = 'flex';
            elements.verifyAlertBox.className = 'auth-alert-box success';
            elements.verifyAlertBox.textContent = (isAr() ? 'تم إنشاء رمز جديد: ' : 'New code generated: ') + newCode;
          }
        } catch (e) {}
      });
    }

    // Back to Login from Verify
    if (elements.verifyBackBtn) {
      elements.verifyBackBtn.addEventListener('click', () => {
        if (elements.authVerifyBox) elements.authVerifyBox.style.display = 'none';
        if (elements.authMainBox) elements.authMainBox.style.display = 'block';
      });
    }

    // Forgot Password Flow
    if (elements.forgotPassLink) {
      elements.forgotPassLink.addEventListener('click', () => {
        if (elements.authMainBox) elements.authMainBox.style.display = 'none';
        if (elements.authForgotBox) {
          elements.authForgotBox.style.display = 'block';
          if (elements.forgotStep1) elements.forgotStep1.style.display = 'block';
          if (elements.forgotStep2) elements.forgotStep2.style.display = 'none';
        }
      });
    }

    if (elements.forgotSendBtn) {
      elements.forgotSendBtn.addEventListener('click', () => {
        const email = elements.forgotEmail.value;
        try {
          const resetCode = AuthManager.requestPasswordReset(email);
          if (elements.forgotStep1) elements.forgotStep1.style.display = 'none';
          if (elements.forgotStep2) elements.forgotStep2.style.display = 'block';
          if (elements.forgotAlertBox) {
            elements.forgotAlertBox.style.display = 'flex';
            elements.forgotAlertBox.className = 'auth-alert-box success';
            elements.forgotAlertBox.textContent = (isAr() ? 'رمز إعادة التعيين هو: ' : 'Your reset code is: ') + resetCode;
          }
        } catch (err) {
          if (elements.forgotAlertBox) {
            elements.forgotAlertBox.style.display = 'flex';
            elements.forgotAlertBox.className = 'auth-alert-box error';
            elements.forgotAlertBox.textContent = err.message;
          }
        }
      });
    }

    if (elements.forgotResetBtn) {
      elements.forgotResetBtn.addEventListener('click', async () => {
        const code = elements.forgotCodeInput.value;
        const newPass = elements.forgotNewPass.value;
        try {
          await AuthManager.resetPassword(code, newPass);
          showToast(isAr() ? 'تم تغيير كلمة المرور بنجاح! سجّل الدخول الآن' : 'Password reset successfully! Please sign in');
          if (elements.authForgotBox) elements.authForgotBox.style.display = 'none';
          if (elements.authMainBox) elements.authMainBox.style.display = 'block';
        } catch (err) {
          if (elements.forgotAlertBox) {
            elements.forgotAlertBox.style.display = 'flex';
            elements.forgotAlertBox.className = 'auth-alert-box error';
            elements.forgotAlertBox.textContent = err.message;
          }
        }
      });
    }

    if (elements.forgotBackBtn) {
      elements.forgotBackBtn.addEventListener('click', () => {
        if (elements.authForgotBox) elements.authForgotBox.style.display = 'none';
        if (elements.authMainBox) elements.authMainBox.style.display = 'block';
      });
    }

    // Profile & Settings Navigation
    if (elements.openSettingsBtn) {
      elements.openSettingsBtn.addEventListener('click', () => {
        switchView('settings');
      });
    }

    if (elements.settingsBackBtn) {
      elements.settingsBackBtn.addEventListener('click', () => {
        switchView('profile');
      });
    }

    // Edit Profile Modal
    if (elements.editProfileBtn) {
      elements.editProfileBtn.addEventListener('click', () => {
        const u = AuthManager.currentUser;
        if (!u) return;
        if (elements.editDisplayNameInput) elements.editDisplayNameInput.value = u.displayName;
        if (elements.editBioInput) elements.editBioInput.value = u.bio || '';
        if (elements.editProfileModal) elements.editProfileModal.classList.add('active');
      });
    }

    if (elements.saveProfileChangesBtn) {
      elements.saveProfileChangesBtn.addEventListener('click', () => {
        const name = elements.editDisplayNameInput.value;
        const bio = elements.editBioInput.value;
        AuthManager.updateProfile(name, bio);
        if (elements.editProfileModal) elements.editProfileModal.classList.remove('active');
        showToast(isAr() ? 'تم حفظ التعديلات بنجاح' : 'Profile updated successfully');
      });
    }

    // Change Password Modal
    if (elements.changePasswordSettingBtn) {
      elements.changePasswordSettingBtn.addEventListener('click', () => {
        if (elements.changePasswordAlert) elements.changePasswordAlert.style.display = 'none';
        if (elements.currentPassInput) elements.currentPassInput.value = '';
        if (elements.newPassInput) elements.newPassInput.value = '';
        if (elements.confirmNewPassInput) elements.confirmNewPassInput.value = '';
        if (elements.changePasswordModal) elements.changePasswordModal.classList.add('active');
      });
    }

    if (elements.submitChangePassBtn) {
      elements.submitChangePassBtn.addEventListener('click', async () => {
        const cur = elements.currentPassInput.value;
        const n = elements.newPassInput.value;
        const c = elements.confirmNewPassInput.value;

        if (n !== c) {
          if (elements.changePasswordAlert) {
            elements.changePasswordAlert.style.display = 'flex';
            elements.changePasswordAlert.className = 'auth-alert-box error';
            elements.changePasswordAlert.textContent = isAr() ? 'كلمتا المرور غير متطابقتين' : 'Passwords do not match';
          }
          return;
        }

        try {
          await AuthManager.changePassword(cur, n);
          if (elements.changePasswordModal) elements.changePasswordModal.classList.remove('active');
          showToast(isAr() ? 'تم تغيير كلمة المرور بنجاح' : 'Password changed successfully');
        } catch (err) {
          if (elements.changePasswordAlert) {
            elements.changePasswordAlert.style.display = 'flex';
            elements.changePasswordAlert.className = 'auth-alert-box error';
            elements.changePasswordAlert.textContent = err.message;
          }
        }
      });
    }

    // Settings Toggle Language
    if (elements.toggleLangSettingBtn) {
      elements.toggleLangSettingBtn.addEventListener('click', () => {
        state.currentLanguage = state.currentLanguage === 'ar' ? 'en' : 'ar';
        updateLocalization();
        if (elements.currentLangLabel) {
          elements.currentLangLabel.textContent = state.currentLanguage === 'ar' ? 'العربية (Arabic)' : 'English (الإنجليزية)';
        }
      });
    }

    // Logout
    if (elements.logoutSettingBtn) {
      elements.logoutSettingBtn.addEventListener('click', () => {
        AuthManager.logout();
        showToast(isAr() ? 'تم تسجيل الخروج بنجاح' : 'Logged out successfully');
        switchView('home');
      });
    }

    // Delete Account Confirmation Modal
    if (elements.deleteAccountSettingBtn) {
      elements.deleteAccountSettingBtn.addEventListener('click', () => {
        if (elements.deleteAccountModal) elements.deleteAccountModal.classList.add('active');
      });
    }

    if (elements.confirmDeleteAccountBtn) {
      elements.confirmDeleteAccountBtn.addEventListener('click', () => {
        AuthManager.deleteAccount();
        if (elements.deleteAccountModal) elements.deleteAccountModal.classList.remove('active');
        showToast(isAr() ? 'تم مسح الحساب وكافة البيانات نهائياً' : 'Account and data permanently deleted');
        switchView('home');
      });
    }

    // Close Modals
    document.querySelectorAll('[data-close]').forEach(btn => {
      btn.addEventListener('click', () => {
        const modalId = btn.dataset.close;
        const modal = document.getElementById(modalId);
        if (modal) modal.classList.remove('active');
      });
    });
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }
})();
