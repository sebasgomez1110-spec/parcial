// SABER PRO — UTS | JS Rediseño
document.addEventListener('DOMContentLoaded', () => {
  // ── Sidebar toggle (icono ↔ expandido) ──
  const sidebar = document.getElementById('sidebar');
  const mainEl  = document.querySelector('.sp-main');
  const toggleBtn = document.getElementById('sidebarToggle');

  if (toggleBtn && sidebar) {
    toggleBtn.addEventListener('click', () => {
      const isMobile = window.innerWidth <= 768;
      if (isMobile) {
        sidebar.classList.toggle('open');
      } else {
        sidebar.classList.toggle('expanded');
        if (mainEl) {
          mainEl.style.marginLeft = sidebar.classList.contains('expanded') ? '240px' : 'var(--sidebar-w)';
        }
      }
    });
    // Cerrar con clic afuera en móvil
    document.addEventListener('click', (e) => {
      if (window.innerWidth <= 768 && sidebar.classList.contains('open') &&
          !sidebar.contains(e.target) && !toggleBtn.contains(e.target)) {
        sidebar.classList.remove('open');
      }
    });
  }

  // ── Marcar nav link activo ──
  const links = document.querySelectorAll('.sp-nav-link');
  const cur = window.location.pathname;
  links.forEach(a => {
    if (a.getAttribute('href') && cur.includes(a.getAttribute('href').replace(/\?.*/,''))) {
      a.classList.add('active');
    }
  });

  // ── Toggle contraseña (login) ──
  window.togglePassword = function() {
    const pwd = document.getElementById('password');
    const icon = document.getElementById('pwdIcon');
    if (!pwd) return;
    pwd.type = pwd.type === 'password' ? 'text' : 'password';
    icon.className = pwd.type === 'password' ? 'bi bi-eye-fill' : 'bi bi-eye-slash-fill';
  };

  // ── Bootstrap tooltips ──
  document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(el => {
    new bootstrap.Tooltip(el);
  });

  // ── Auto-hide alerts ──
  document.querySelectorAll('.alert.auto-hide').forEach(el => {
    setTimeout(() => { el.style.opacity = '0'; setTimeout(() => el.remove(), 300); }, 4000);
  });
});
