package com.parcial.saberpro.controller;

import com.parcial.saberpro.entity.*;
import com.parcial.saberpro.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
public class WebController {

    private final AlumnoRepository alumnoRepository;
    private final FacultadRepository facultadRepository;
    private final UsuarioRepository usuarioRepository;
    private final ResolucionBeneficioRepository resolucionRepository;
    private static final String UPLOAD_DIR = "uploads/comprobantes/";

    public WebController(AlumnoRepository a, FacultadRepository f,
                         UsuarioRepository u, ResolucionBeneficioRepository r) {
        this.alumnoRepository = a; this.facultadRepository = f;
        this.usuarioRepository = u; this.resolucionRepository = r;
    }

    // ── HOME / LOGIN ──────────────────────────────────────────────────────────
    @GetMapping("/") public String index() { return "index"; }
    @GetMapping("/login") public String loginForm() { return "login"; }

    @PostMapping("/login")
    public String loginProcess(@RequestParam String username, @RequestParam String password,
                               HttpSession session, RedirectAttributes ra) {
        return usuarioRepository.findByEmail(username)
                .filter(u -> u.isActivo() && u.getPassword().equals(password))
                .map(u -> {
                    session.setAttribute("usuarioId",     u.getId());
                    session.setAttribute("usuarioRol",    u.getRol());
                    session.setAttribute("usuarioNombre", u.getNombreCompleto());
                    session.setAttribute("usuarioEmail",  u.getEmail());
                    session.setAttribute("usuarioCedula", u.getCedula());
                    return switch (u.getRol()) {
                        case "ADMIN"        -> "redirect:/admin/dashboard";
                        case "COORDINACION" -> "redirect:/coordinacion/dashboard";
                        case "DOCENTE"      -> "redirect:/docente/dashboard";
                        case "ESTUDIANTE"   -> "redirect:/estudiante/dashboard";
                        default             -> "redirect:/estudiante/dashboard";
                    };
                })
                .orElseGet(() -> { ra.addFlashAttribute("error","Credenciales inválidas"); return "redirect:/login?error"; });
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) { session.invalidate(); return "redirect:/login?logout=true"; }

    // ── ADMIN ─────────────────────────────────────────────────────────────────
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalCoordinaciones", usuarioRepository.findByRol("COORDINACION").size());
        model.addAttribute("totalDocentes",       usuarioRepository.findByRol("DOCENTE").size());
        model.addAttribute("totalFacultades",     facultadRepository.findByActiva(true).size());
        model.addAttribute("totalResoluciones",   resolucionRepository.findByVigente(true).size());
        model.addAttribute("facultades",          facultadRepository.findAll());
        model.addAttribute("resoluciones",        resolucionRepository.findAll());
        return "admin/dashboard";
    }
    @GetMapping("/admin/directores")
    public String listarDirectores(Model model) {
        model.addAttribute("directores", usuarioRepository.findByRol("COORDINACION"));
        model.addAttribute("director", new Usuario());
        return "admin/directores";
    }

    // ── ADMIN: COORDINACIONES ────────────────────────────────────────────────
    @GetMapping("/admin/coordinaciones")
    public String listarCoordinaciones(Model model) {
        model.addAttribute("coordinaciones", usuarioRepository.findByRol("COORDINACION"));
        model.addAttribute("coordinacion",   new Usuario());
        return "admin/coordinaciones";
    }

    @PostMapping("/admin/coordinaciones/guardar")
    public String guardarCoordinacion(@ModelAttribute Usuario coordinacion, RedirectAttributes ra) {
        coordinacion.setRol("COORDINACION");
        if (coordinacion.getId() == null || coordinacion.getId().isBlank()) {
            coordinacion.setId(null);
            coordinacion.setFechaCreacion(LocalDateTime.now());
        } else {
            usuarioRepository.findById(coordinacion.getId()).ifPresent(u -> {
                if (coordinacion.getPassword() == null || coordinacion.getPassword().isBlank())
                    coordinacion.setPassword(u.getPassword());
            });
            coordinacion.setFechaActualizacion(LocalDateTime.now());
        }
        usuarioRepository.save(coordinacion);
        ra.addFlashAttribute("mensaje", "Coordinación guardada exitosamente");
        ra.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/admin/coordinaciones";
    }

    @GetMapping("/admin/coordinaciones/editar/{id}")
    public String editarCoordinacion(@PathVariable String id, Model model) {
        model.addAttribute("coordinaciones", usuarioRepository.findByRol("COORDINACION"));
        model.addAttribute("coordinacion",   usuarioRepository.findById(id).orElseThrow());
        return "admin/coordinaciones";
    }

    @GetMapping("/admin/coordinaciones/eliminar/{id}")
    public String eliminarCoordinacion(@PathVariable String id, RedirectAttributes ra) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setActivo(false); u.setFechaActualizacion(LocalDateTime.now()); usuarioRepository.save(u);
        });
        ra.addFlashAttribute("mensaje", "Coordinación desactivada");
        ra.addFlashAttribute("tipoMensaje", "warning");
        return "redirect:/admin/coordinaciones";
    }

    // ── RF-06: Restablecer contraseña ─────────────────────────────────────────
    @PostMapping("/admin/usuarios/reset-password/{id}")
    public String resetPassword(@PathVariable String id,
                                @RequestParam String nuevaPassword,
                                @RequestParam(required = false) String returnUrl,
                                RedirectAttributes ra) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setPassword(nuevaPassword.isBlank() ? u.getCedula() : nuevaPassword);
            u.setFechaActualizacion(LocalDateTime.now());
            usuarioRepository.save(u);
        });
        ra.addFlashAttribute("mensaje", "Contraseña restablecida correctamente");
        ra.addFlashAttribute("tipoMensaje", "success");
        return "redirect:" + (returnUrl != null && !returnUrl.isBlank() ? returnUrl : "/admin/coordinaciones");
    }

    // ── ADMIN: DOCENTES ───────────────────────────────────────────────────────
    @GetMapping("/admin/docentes")
    public String listarDocentes(Model model) {
        model.addAttribute("docentes",   usuarioRepository.findByRol("DOCENTE"));
        model.addAttribute("facultades", facultadRepository.findByActiva(true));
        model.addAttribute("docente",    new Usuario());
        return "admin/docentes";
    }

    @PostMapping("/admin/docentes/guardar")
    public String guardarDocente(@ModelAttribute Usuario docente, RedirectAttributes ra) {
        docente.setRol("DOCENTE");
        if (docente.getId() == null || docente.getId().isBlank()) {
            docente.setId(null); docente.setFechaCreacion(LocalDateTime.now());
        } else {
            usuarioRepository.findById(docente.getId()).ifPresent(u -> {
                if (docente.getPassword() == null || docente.getPassword().isBlank()) docente.setPassword(u.getPassword());
            });
            docente.setFechaActualizacion(LocalDateTime.now());
        }
        usuarioRepository.save(docente);
        ra.addFlashAttribute("mensaje", "Docente guardado exitosamente");
        ra.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/admin/docentes";
    }

    @GetMapping("/admin/docentes/editar/{id}")
    public String editarDocente(@PathVariable String id, Model model) {
        model.addAttribute("docentes",   usuarioRepository.findByRol("DOCENTE"));
        model.addAttribute("facultades", facultadRepository.findByActiva(true));
        model.addAttribute("docente",    usuarioRepository.findById(id).orElseThrow());
        return "admin/docentes";
    }

    @GetMapping("/admin/docentes/eliminar/{id}")
    public String eliminarDocente(@PathVariable String id, RedirectAttributes ra) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setActivo(false); u.setFechaActualizacion(LocalDateTime.now()); usuarioRepository.save(u);
        });
        ra.addFlashAttribute("mensaje", "Docente desactivado");
        ra.addFlashAttribute("tipoMensaje", "warning");
        return "redirect:/admin/docentes";
    }

    // ── ADMIN: FACULTADES ─────────────────────────────────────────────────────
    @GetMapping("/admin/facultades")
    public String listarFacultades(Model model) {
        model.addAttribute("facultades", facultadRepository.findAll());
        model.addAttribute("directores", usuarioRepository.findByRol("COORDINACION"));
        model.addAttribute("facultad",   new Facultad());
        return "admin/facultades";
    }

    @PostMapping("/admin/facultades/guardar")
    public String guardarFacultad(@ModelAttribute Facultad facultad, RedirectAttributes ra) {
        if (facultad.getId() == null || facultad.getId().isBlank()) {
            facultad.setId(null); facultad.setFechaCreacion(LocalDateTime.now());
        }
        facultadRepository.save(facultad);
        ra.addFlashAttribute("mensaje", "Facultad guardada exitosamente");
        ra.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/admin/facultades";
    }

    @GetMapping("/admin/facultades/editar/{id}")
    public String editarFacultad(@PathVariable String id, Model model) {
        model.addAttribute("facultades", facultadRepository.findAll());
        model.addAttribute("directores", usuarioRepository.findByRol("COORDINACION"));
        model.addAttribute("facultad",   facultadRepository.findById(id).orElseThrow());
        return "admin/facultades";
    }

    @GetMapping("/admin/facultades/eliminar/{id}")
    public String eliminarFacultad(@PathVariable String id, RedirectAttributes ra) {
        facultadRepository.findById(id).ifPresent(f -> { f.setActiva(false); facultadRepository.save(f); });
        ra.addFlashAttribute("mensaje", "Facultad desactivada");
        ra.addFlashAttribute("tipoMensaje", "warning");
        return "redirect:/admin/facultades";
    }

    // ── ADMIN: RESOLUCIONES ───────────────────────────────────────────────────
    @GetMapping("/admin/resoluciones")
    public String listarResolucionesAdmin(Model model) {
        model.addAttribute("resoluciones", resolucionRepository.findAll());
        model.addAttribute("resolucion",   new ResolucionBeneficio());
        return "admin/resoluciones";
    }

    @PostMapping("/admin/resoluciones/guardar")
    public String guardarResolucionAdmin(@ModelAttribute ResolucionBeneficio resolucion, RedirectAttributes ra) {
        if (resolucion.getId() == null || resolucion.getId().isBlank()) {
            resolucion.setId(null); resolucion.setFechaCreacion(LocalDateTime.now());
        }
        resolucionRepository.save(resolucion);
        ra.addFlashAttribute("mensaje", "Resolución guardada exitosamente");
        ra.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/admin/resoluciones";
    }

    @GetMapping("/admin/resoluciones/editar/{id}")
    public String editarResolucionAdmin(@PathVariable String id, Model model) {
        model.addAttribute("resoluciones", resolucionRepository.findAll());
        model.addAttribute("resolucion",   resolucionRepository.findById(id).orElseThrow());
        return "admin/resoluciones";
    }

    @GetMapping("/admin/resoluciones/eliminar/{id}")
    public String eliminarResolucionAdmin(@PathVariable String id, RedirectAttributes ra) {
        resolucionRepository.findById(id).ifPresent(r -> { r.setVigente(false); resolucionRepository.save(r); });
        ra.addFlashAttribute("mensaje", "Resolución desactivada");
        ra.addFlashAttribute("tipoMensaje", "warning");
        return "redirect:/admin/resoluciones";
    }

    // ── COORDINACIÓN ──────────────────────────────────────────────────────────
    @GetMapping("/coordinacion/dashboard")
    public String coordinacionDashboard(Model model) {
        model.addAttribute("totalAlumnos",   alumnoRepository.count());
        model.addAttribute("aprobados",      alumnoRepository.countByEstadoSaberPro("APROBADO"));
        model.addAttribute("pendientes",     alumnoRepository.countByEstadoSaberPro("PENDIENTE"));
        model.addAttribute("conBeneficio",   alumnoRepository.findByTieneBeneficio(true).size());
        model.addAttribute("ultimosAlumnos", alumnoRepository.findByEstadoSaberPro("PENDIENTE"));
        return "coordinacion/dashboard";
    }

    @GetMapping("/coordinacion/alumnos")
    public String listarAlumnosCoord(Model model) {
        model.addAttribute("alumnos",    alumnoRepository.findAll());
        model.addAttribute("alumno",     new Alumno());
        model.addAttribute("facultades", facultadRepository.findByActiva(true));
        return "coordinacion/alumnos";
    }

    @PostMapping("/coordinacion/alumnos/guardar")
    public String guardarAlumnoCoord(@ModelAttribute Alumno alumno,
                                      @RequestParam(required = false) String passwordEstudiante,
                                      RedirectAttributes ra) {
        if (alumno.getFacultadId() != null && !alumno.getFacultadId().isBlank())
            facultadRepository.findById(alumno.getFacultadId()).ifPresent(f -> alumno.setFacultadNombre(f.getNombre()));
        if (alumno.getEmail() == null || alumno.getEmail().isBlank())
            alumno.setEmail(normalize(alumno.getNombre()) + "." + alumno.getApellido().toLowerCase() + "@uts.edu.co");
        boolean esNuevo = (alumno.getId() == null || alumno.getId().isBlank());
        if (esNuevo) { alumno.setId(null); alumno.setFechaCreacion(LocalDateTime.now()); }
        else alumno.setFechaActualizacion(LocalDateTime.now());
        Alumno saved = alumnoRepository.save(alumno);
        String pwd = (passwordEstudiante != null && !passwordEstudiante.isBlank())
                ? passwordEstudiante : saved.getCedula();
        crearOActualizarUsuarioEstudianteConPassword(saved, pwd);
        ra.addFlashAttribute("mensaje", "Estudiante guardado. Credenciales → Email: " + saved.getEmail() + " | Contraseña: " + pwd);
        ra.addFlashAttribute("tipoMensaje", "success");
        return "redirect:/coordinacion/alumnos";
    }

    private void crearOActualizarUsuarioEstudianteConPassword(Alumno alumno, String password) {
        usuarioRepository.findByEmail(alumno.getEmail()).ifPresentOrElse(
            u -> { u.setNombre(alumno.getNombre()); u.setApellido(alumno.getApellido());
                   u.setCedula(alumno.getCedula()); u.setPassword(password);
                   u.setFechaActualizacion(LocalDateTime.now()); usuarioRepository.save(u); },
            () -> { Usuario u = new Usuario(); u.setNombre(alumno.getNombre()); u.setApellido(alumno.getApellido());
                    u.setEmail(alumno.getEmail()); u.setPassword(password); u.setCedula(alumno.getCedula());
                    u.setTelefono(alumno.getTelefono()); u.setRol("ESTUDIANTE"); u.setActivo(true);
                    u.setFechaCreacion(LocalDateTime.now()); usuarioRepository.save(u); }
        );
    }

    private String normalize(String s) {
        if (s == null) return "";
        return s.toLowerCase().replace("é","e").replace("á","a").replace("í","i")
                .replace("ó","o").replace("ú","u").replace("ñ","n");
    }

    @GetMapping("/coordinacion/alumnos/editar/{id}")
    public String editarAlumnoCoord(@PathVariable String id, Model model) {
        model.addAttribute("alumnos",    alumnoRepository.findAll());
        model.addAttribute("alumno",     alumnoRepository.findById(id).orElseThrow());
        model.addAttribute("facultades", facultadRepository.findByActiva(true));
        return "coordinacion/alumnos";
    }

    @GetMapping("/coordinacion/alumnos/eliminar/{id}")
    public String eliminarAlumnoCoord(@PathVariable String id, RedirectAttributes ra) {
        alumnoRepository.findById(id).ifPresent(a -> {
            a.setActivo(false); alumnoRepository.save(a);
            usuarioRepository.findByEmail(a.getEmail()).ifPresent(u -> {
                u.setActivo(false); u.setFechaActualizacion(LocalDateTime.now()); usuarioRepository.save(u);
            });
        });
        ra.addFlashAttribute("mensaje","Estudiante eliminado"); ra.addFlashAttribute("tipoMensaje","warning");
        return "redirect:/coordinacion/alumnos";
    }

    @GetMapping("/coordinacion/aprobar")
    public String listarParaAprobar(Model model) {
        model.addAttribute("pendientes", alumnoRepository.findByEstadoSaberPro("PENDIENTE"));
        model.addAttribute("aprobados",  alumnoRepository.findByEstadoSaberPro("APROBADO"));
        model.addAttribute("rechazados", alumnoRepository.findByEstadoSaberPro("RECHAZADO"));
        return "coordinacion/aprobar";
    }

    @PostMapping("/coordinacion/aprobar/{id}")
    public String aprobarAlumnoCoord(@PathVariable String id,
                                      @RequestParam(defaultValue = "coordinacion") String aprobadoPor,
                                      RedirectAttributes ra) {
        alumnoRepository.findById(id).ifPresent(a -> {
            a.setAprobadoPorCoordinacion(true); a.setEstadoSaberPro("APROBADO");
            a.setFechaAprobacion(LocalDateTime.now()); a.setAprobadoPor(aprobadoPor);
            a.setFechaActualizacion(LocalDateTime.now()); alumnoRepository.save(a);
        });
        ra.addFlashAttribute("mensaje","Estudiante aprobado"); ra.addFlashAttribute("tipoMensaje","success");
        return "redirect:/coordinacion/aprobar";
    }

    @PostMapping("/coordinacion/rechazar/{id}")
    public String rechazarAlumnoCoord(@PathVariable String id, RedirectAttributes ra) {
        alumnoRepository.findById(id).ifPresent(a -> {
            a.setAprobadoPorCoordinacion(false); a.setEstadoSaberPro("RECHAZADO");
            a.setFechaActualizacion(LocalDateTime.now()); alumnoRepository.save(a);
        });
        ra.addFlashAttribute("mensaje","Solicitud rechazada"); ra.addFlashAttribute("tipoMensaje","danger");
        return "redirect:/coordinacion/aprobar";
    }

    @GetMapping("/coordinacion/informe/alumnos")
    public String informeAlumnosCoord(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String facultadId,
            @RequestParam(required = false) String estado,
            Model model) {
        java.util.List<com.parcial.saberpro.entity.Alumno> alumnos;
        if (facultadId != null && !facultadId.isBlank() && estado != null && !estado.isBlank())
            alumnos = alumnoRepository.findByFacultadIdAndEstadoSaberPro(facultadId, estado);
        else if (facultadId != null && !facultadId.isBlank())
            alumnos = alumnoRepository.findByFacultadId(facultadId);
        else if (estado != null && !estado.isBlank())
            alumnos = alumnoRepository.findByEstadoSaberPro(estado);
        else
            alumnos = alumnoRepository.findAll();
        model.addAttribute("alumnos",      alumnos);
        model.addAttribute("tipo",         tipo != null ? tipo : "TOTAL");
        model.addAttribute("facultades",   facultadRepository.findByActiva(true));
        model.addAttribute("totalAlumnos", alumnoRepository.count());
        model.addAttribute("aprobados",    alumnoRepository.countByEstadoSaberPro("APROBADO"));
        model.addAttribute("pendientes",   alumnoRepository.countByEstadoSaberPro("PENDIENTE"));
        model.addAttribute("conBeneficio", alumnoRepository.findByTieneBeneficio(true).size());
        return "coordinacion/informe-alumnos";
    }

    @GetMapping("/coordinacion/informe/beneficios")
    public String informeBeneficiosCoord(Model model) {
        model.addAttribute("alumnosConBeneficio", alumnoRepository.findByTieneBeneficio(true));
        model.addAttribute("resoluciones",        resolucionRepository.findByVigente(true));
        return "coordinacion/informe-beneficios";
    }

    @GetMapping("/coordinacion/resoluciones")
    public String resolucionesCoord(Model model) {
        model.addAttribute("resoluciones", resolucionRepository.findAll());
        return "coordinacion/resoluciones";
    }

    // ── DOCENTE ───────────────────────────────────────────────────────────────
    @GetMapping("/docente/dashboard")
    public String docenteDashboard(Model model) {
        model.addAttribute("facultades",   facultadRepository.findByActiva(true));
        model.addAttribute("totalAlumnos", alumnoRepository.count());
        model.addAttribute("aprobados",    alumnoRepository.countByEstadoSaberPro("APROBADO"));
        return "docente/dashboard";
    }

    @GetMapping("/docente/alumnos/facultad/{facultadId}")
    public String alumnosPorFacultadDocente(@PathVariable String facultadId, Model model) {
        model.addAttribute("alumnos",    alumnoRepository.findByFacultadId(facultadId));
        model.addAttribute("facultad",   facultadRepository.findById(facultadId).orElseThrow());
        model.addAttribute("facultades", facultadRepository.findByActiva(true));
        return "docente/alumnos-facultad";
    }

    @GetMapping("/docente/alumnos/cedula")
    public String buscarPorCedulaDocente(@RequestParam(required = false) String cedula, Model model) {
        if (cedula != null && !cedula.isBlank())
            // ✅ CORREGIDO: findFirstByCedulaOrderByFechaCreacionDesc evita excepción por duplicados
            alumnoRepository.findFirstByCedulaOrderByFechaCreacionDesc(cedula)
                            .ifPresent(a -> model.addAttribute("alumno", a));
        return "docente/buscar-cedula";
    }

    @GetMapping("/docente/informe/alumnos")
    public String informeAlumnosDocente(@RequestParam(required = false) String tipo, Model model) {
        model.addAttribute("alumnos",      alumnoRepository.findAll());
        model.addAttribute("tipo",         tipo != null ? tipo : "TOTAL");
        model.addAttribute("totalAlumnos", alumnoRepository.count());
        model.addAttribute("aprobados",    alumnoRepository.countByEstadoSaberPro("APROBADO"));
        model.addAttribute("conBeneficio", alumnoRepository.findByTieneBeneficio(true).size());
        return "docente/informe-alumnos";
    }

    @GetMapping("/docente/informe/beneficios")
    public String informeBeneficiosDocente(Model model) {
        model.addAttribute("alumnosConBeneficio", alumnoRepository.findByTieneBeneficio(true));
        model.addAttribute("resoluciones",        resolucionRepository.findByVigente(true));
        return "docente/informe-beneficios";
    }

    @GetMapping("/docente/resoluciones")
    public String resolucionesDocente(Model model) {
        model.addAttribute("resoluciones",           resolucionRepository.findAll());
        model.addAttribute("resolucionesTecnologia", resolucionRepository.findByArea("TECNOLOGIA"));
        model.addAttribute("resolucionesIngenieria",  resolucionRepository.findByArea("INGENIERIA"));
        return "docente/resoluciones";
    }

    // ── ESTUDIANTE — todas las rutas usan sesión ──────────────────────────────

    // ✅ CORREGIDO: usa findFirstByCedulaOrderByFechaCreacionDesc en lugar de findByCedula
    //    para evitar IncorrectResultSizeDataAccessException cuando hay duplicados en BD
    private Alumno getAlumnoFromSession(HttpSession session) {
        String cedula = (String) session.getAttribute("usuarioCedula");
        if (cedula == null) return null;
        return alumnoRepository.findFirstByCedulaOrderByFechaCreacionDesc(cedula).orElse(null);
    }

    @GetMapping("/estudiante/dashboard")
    public String estudianteDashboard(HttpSession session, Model model) {
        Alumno alumno = getAlumnoFromSession(session);
        if (alumno != null) model.addAttribute("alumno", alumno);
        model.addAttribute("resoluciones", resolucionRepository.findByVigente(true));
        return "estudiante/dashboard";
    }

    @GetMapping("/estudiante/identificacion")
    public String identificacion(HttpSession session, Model model) {
        Alumno alumno = getAlumnoFromSession(session);
        if (alumno != null) model.addAttribute("alumno", alumno);
        return "estudiante/identificacion";
    }

    @GetMapping("/estudiante/pago")
    public String pagoPagina(HttpSession session, Model model) {
        Alumno alumno = getAlumnoFromSession(session);
        if (alumno != null) model.addAttribute("alumno", alumno);
        return "estudiante/pago";
    }

    @PostMapping("/estudiante/pago/cargar")
    public String cargarPago(HttpSession session, @RequestParam MultipartFile comprobante, RedirectAttributes ra) {
        Alumno alumno = getAlumnoFromSession(session);
        if (alumno == null) return "redirect:/login";
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            String nombreArchivo = UUID.randomUUID() + "_" + comprobante.getOriginalFilename();
            Files.copy(comprobante.getInputStream(), uploadPath.resolve(nombreArchivo), StandardCopyOption.REPLACE_EXISTING);
            alumno.setComprobantePagoPath(UPLOAD_DIR + nombreArchivo);
            alumno.setFechaCargoPago(LocalDateTime.now()); alumno.setEstadoSaberPro("PAGADO");
            alumno.setFechaActualizacion(LocalDateTime.now()); alumnoRepository.save(alumno);
            ra.addFlashAttribute("mensaje","Comprobante cargado exitosamente."); ra.addFlashAttribute("tipoMensaje","success");
        } catch (IOException e) {
            ra.addFlashAttribute("mensaje","Error al cargar: " + e.getMessage()); ra.addFlashAttribute("tipoMensaje","danger");
        }
        return "redirect:/estudiante/pago";
    }

    @GetMapping("/estudiante/resultado/unico")
    public String resultadoUnico(HttpSession session, Model model) {
        Alumno alumno = getAlumnoFromSession(session);
        if (alumno != null) { model.addAttribute("alumno", alumno); model.addAttribute("resultado", alumno.getResultadoUnico()); }
        return "estudiante/resultado-unico";
    }

    @GetMapping("/estudiante/resultado/total")
    public String resultadoTotal(HttpSession session, Model model) {
        Alumno alumno = getAlumnoFromSession(session);
        if (alumno != null) { model.addAttribute("alumno", alumno); model.addAttribute("resultado", alumno.getResultadoTotal()); }
        return "estudiante/resultado-total";
    }

    @GetMapping("/estudiante/resoluciones")
    public String resolucionesEstudiante(HttpSession session, Model model) {
        Alumno alumno = getAlumnoFromSession(session);
        if (alumno != null) model.addAttribute("alumno", alumno);
        model.addAttribute("resoluciones",           resolucionRepository.findByVigente(true));
        model.addAttribute("resolucionesTecnologia", resolucionRepository.findByArea("TECNOLOGIA"));
        model.addAttribute("resolucionesIngenieria",  resolucionRepository.findByArea("INGENIERIA"));
        return "estudiante/resoluciones";
        
    }
}