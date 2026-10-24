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
      export_success: 'تم حفظ الصورة بنجاح في جهازك'
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
      export_success: 'Image saved to your device'
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

  function init() {
    setupDOMElements();
    setupCanvas();
    setupEventListeners();
    updateLocalization();
    loadSampleArtwork();
    renderRecentProjects();
  }

  function setupDOMElements() {
    elements = {
      body: document.body,
      langBtn: document.getElementById('langToggleBtn'),
      views: {
        home: document.getElementById('viewHome'),
        editor: document.getElementById('viewEditor'),
        adStudio: document.getElementById('viewAdStudio'),
        storyDesigner: document.getElementById('viewStoryDesigner')
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
      editorTitle: document.getElementById('editorTitle')
    };
  }

  function setupCanvas() {
    if (!elements.canvas) return;
    state.canvas = elements.canvas;
    state.ctx = elements.canvas.getContext('2d', { willReadFrequently: true });
  }

  // Load an initial elegant aesthetic sample image so the user immediately has content
  function loadSampleArtwork() {
    const defaultCanvas = document.createElement('canvas');
    defaultCanvas.width = 1080;
    defaultCanvas.height = 1350;
    const ctx = defaultCanvas.getContext('2d');

    // Draw rich gradient background
    const grad = ctx.createLinearGradient(0, 0, 1080, 1350);
    grad.addColorStop(0, '#1E1233');
    grad.addColorStop(0.5, '#2D1B4E');
    grad.addColorStop(1, '#0F091A');
    ctx.fillStyle = grad;
    ctx.fillRect(0, 0, 1080, 1350);

    // Subtle aesthetic light circle
    const glow = ctx.createRadialGradient(540, 500, 50, 540, 500, 450);
    glow.addColorStop(0, 'rgba(139, 92, 246, 0.45)');
    glow.addColorStop(0.7, 'rgba(99, 102, 241, 0.15)');
    glow.addColorStop(1, 'transparent');
    ctx.fillStyle = glow;
    ctx.fillRect(0, 0, 1080, 1350);

    // Draw geometric stylish art shapes
    ctx.fillStyle = 'rgba(255, 255, 255, 0.04)';
    ctx.beginPath();
    ctx.arc(540, 520, 280, 0, Math.PI * 2);
    ctx.fill();

    // Central elegant monogram
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

    // Update bottom nav
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

  // Pixel Processing Engine (Canvas 2D Manipulations)
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

      // Exposure & Brightness
      r += exp + bri;
      g += exp + bri;
      b += exp + bri;

      // Contrast
      r = ((r - 128) * con) + 128;
      g = ((g - 128) * con) + 128;
      b = ((b - 128) * con) + 128;

      // Warmth & Tint
      r += warm * 0.6;
      b -= warm * 0.6;
      g += tint * 0.4;

      // Saturation
      const gray = 0.299 * r + 0.587 * g + 0.114 * b;
      r = gray + (r - gray) * sat;
      g = gray + (g - gray) * sat;
      b = gray + (b - gray) * sat;

      // Skin Smoothing approximation
      if (smooth > 0 && r > 100 && g > 60 && b > 40 && r > g && g > b) {
        r = r * (1 - smooth * 0.002) + gray * (smooth * 0.002);
      }

      dst[i] = Math.min(255, Math.max(0, r));
      dst[i + 1] = Math.min(255, Math.max(0, g));
      dst[i + 2] = Math.min(255, Math.max(0, b));
      dst[i + 3] = a;
    }

    state.ctx.putImageData(output, 0, 0);

    // Apply Vignette if enabled
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

  // Tool Drawer Dynamic UI Rendering
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

    // Attach slider listeners
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

    // Attach filter listeners
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
    // Language Toggle
    if (elements.langBtn) {
      elements.langBtn.addEventListener('click', () => {
        state.currentLanguage = state.currentLanguage === 'ar' ? 'en' : 'ar';
        updateLocalization();
      });
    }

    // Navigation Items
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
        } else {
          switchView('home');
        }
      });
    });

    // File Input Upload
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

    // Tool Category Tabs
    elements.toolTabs.forEach(tab => {
      tab.addEventListener('click', () => {
        renderToolPanel(tab.dataset.tool);
      });
    });

    // Before/After Compare Button
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

    // Export Button
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

    // Quick Tool chips on Home
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

    // Feature Cards on Home
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

    // Hero CTA Button
    const heroBtn = document.getElementById('heroCreateBtn');
    if (heroBtn) {
      heroBtn.addEventListener('click', () => {
        switchView('editor', i18n[state.currentLanguage].personal_studio);
        renderToolPanel('adjust');
      });
    }

    // Ad Studio Form Inputs
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

  // Advertisement Canvas Generator
  function renderAdCanvas() {
    const canvas = document.getElementById('adCanvas');
    if (!canvas) return;
    canvas.width = 1080;
    canvas.height = 1080;
    const ctx = canvas.getContext('2d');

    // Rich Dark Ad Gradient
    const bgGrad = ctx.createLinearGradient(0, 0, 1080, 1080);
    bgGrad.addColorStop(0, '#151322');
    bgGrad.addColorStop(1, '#0C0A14');
    ctx.fillStyle = bgGrad;
    ctx.fillRect(0, 0, 1080, 1080);

    // Decorative Purple glow
    const radial = ctx.createRadialGradient(540, 400, 50, 540, 400, 600);
    radial.addColorStop(0, 'rgba(139, 92, 246, 0.35)');
    radial.addColorStop(1, 'transparent');
    ctx.fillStyle = radial;
    ctx.fillRect(0, 0, 1080, 1080);

    // Badge Pill
    ctx.fillStyle = '#8B5CF6';
    roundRect(ctx, 420, 180, 240, 64, 32);
    ctx.fill();

    ctx.fillStyle = '#FFFFFF';
    ctx.font = 'bold 28px sans-serif';
    ctx.textAlign = 'center';
    ctx.fillText(state.adState.badge || 'PROMOTION', 540, 222);

    // Headline
    ctx.fillStyle = '#FFFFFF';
    ctx.font = 'bold 64px sans-serif';
    ctx.fillText(state.adState.headline || 'Exclusive Offer', 540, 360);

    // Subtitle
    ctx.fillStyle = '#94A3B8';
    ctx.font = '400 32px sans-serif';
    ctx.fillText(state.adState.subline || 'Premium Quality Guaranteed', 540, 440);

    // CTA Button
    ctx.fillStyle = '#FFFFFF';
    roundRect(ctx, 390, 840, 300, 80, 40);
    ctx.fill();

    ctx.fillStyle = '#0F0E17';
    ctx.font = 'bold 32px sans-serif';
    ctx.fillText(state.adState.cta || 'Shop Now', 540, 892);
  }

  // Story Designer Canvas Generator
  function renderStoryCanvas() {
    const canvas = document.getElementById('storyCanvas');
    if (!canvas) return;
    canvas.width = 1080;
    canvas.height = 1920;
    const ctx = canvas.getContext('2d');

    // 9:16 background
    const bgGrad = ctx.createLinearGradient(0, 0, 1080, 1920);
    bgGrad.addColorStop(0, '#241442');
    bgGrad.addColorStop(0.5, '#140D26');
    bgGrad.addColorStop(1, '#080511');
    ctx.fillStyle = bgGrad;
    ctx.fillRect(0, 0, 1080, 1920);

    // Modern frame border
    ctx.strokeStyle = 'rgba(139, 92, 246, 0.4)';
    ctx.lineWidth = 12;
    ctx.strokeRect(60, 60, 960, 1800);

    // Headline
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
    } catch (e) {
      // Local storage fallback
    }
  }

  function renderRecentProjects() {
    const list = document.getElementById('recentProjectsList');
    if (!list) return;
    try {
      const projects = JSON.parse(localStorage.getItem('ava_recent_projects') || '[]');
      if (projects.length === 0) {
        list.innerHTML = `<div style="color:var(--text-tertiary); font-size:0.8rem; padding:8px 0;">لا توجد مشاريع سابقة بعد</div>`;
        return;
      }
      list.innerHTML = projects.map(p => `
        <div style="background:var(--bg-card); padding:10px 14px; border-radius:12px; border:1px solid var(--border-subtle); display:flex; justify-content:space-between; align-items:center; margin-bottom:8px;">
          <span style="font-size:0.85rem; font-weight:600; color:var(--text-primary);">${p.title}</span>
          <span style="font-size:0.75rem; color:var(--text-tertiary);">${p.date}</span>
        </div>
      `).join('');
    } catch (e) {}
  }

  // Run on DOM Ready
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }
})();
