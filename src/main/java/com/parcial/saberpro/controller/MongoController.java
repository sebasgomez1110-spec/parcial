package com.parcial.saberpro.controller;

import com.parcial.saberpro.entity.Alumno;
import com.parcial.saberpro.entity.ResultadoSaberPro;
import com.parcial.saberpro.entity.Usuario;
import com.parcial.saberpro.entity.Facultad;
import com.parcial.saberpro.entity.ResolucionBeneficio;
import com.parcial.saberpro.repository.AlumnoRepository;
import com.parcial.saberpro.repository.FacultadRepository;
import com.parcial.saberpro.repository.UsuarioRepository;
import com.parcial.saberpro.repository.ResolucionBeneficioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MongoController {

    private final AlumnoRepository alumnoRepository;
    private final FacultadRepository facultadRepository;
    private final UsuarioRepository usuarioRepository;
    private final ResolucionBeneficioRepository resolucionRepository;

    public MongoController(AlumnoRepository alumnoRepository,
                           FacultadRepository facultadRepository,
                           UsuarioRepository usuarioRepository,
                           ResolucionBeneficioRepository resolucionRepository) {
        this.alumnoRepository    = alumnoRepository;
        this.facultadRepository  = facultadRepository;
        this.usuarioRepository   = usuarioRepository;
        this.resolucionRepository = resolucionRepository;
    }

    // ALUMNOS
    @GetMapping("/alumnos")
    public ResponseEntity<List<Alumno>> listarAlumnos() {
        return ResponseEntity.ok(alumnoRepository.findAll());
    }

    @GetMapping("/alumnos/{id}")
    public ResponseEntity<?> obtenerAlumno(@PathVariable String id) {
        return alumnoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/alumnos/cedula/{cedula}")
    public ResponseEntity<?> buscarPorCedula(@PathVariable String cedula) {
        return alumnoRepository.findFirstByCedulaOrderByFechaCreacionDesc(cedula)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/alumnos/facultad/{facultadId}")
    public ResponseEntity<List<Alumno>> alumnosPorFacultad(@PathVariable String facultadId) {
        return ResponseEntity.ok(alumnoRepository.findByFacultadId(facultadId));
    }

    @GetMapping("/alumnos/estado/{estado}")
    public ResponseEntity<List<Alumno>> alumnosPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(alumnoRepository.findByEstadoSaberPro(estado));
    }

    @PostMapping("/alumnos")
    public ResponseEntity<Alumno> crearAlumno(@RequestBody Alumno alumno) {
        alumno.setFechaCreacion(LocalDateTime.now());
        return ResponseEntity.ok(alumnoRepository.save(alumno));
    }

    @PutMapping("/alumnos/{id}")
    public ResponseEntity<?> actualizarAlumno(@PathVariable String id, @RequestBody Alumno datos) {
        return alumnoRepository.findById(id).map(alumno -> {
            datos.setId(id);
            datos.setFechaActualizacion(LocalDateTime.now());
            return ResponseEntity.ok(alumnoRepository.save(datos));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/alumnos/{id}")
    public ResponseEntity<?> eliminarAlumno(@PathVariable String id) {
        return alumnoRepository.findById(id).map(a -> {
            a.setActivo(false);
            a.setFechaActualizacion(LocalDateTime.now());
            alumnoRepository.save(a);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/alumnos/{id}/resultado-unico")
    public ResponseEntity<?> registrarResultadoUnico(@PathVariable String id,
                                                      @RequestBody ResultadoSaberPro resultado) {
        return alumnoRepository.findById(id).map(alumno -> {
            alumno.setResultadoUnico(resultado);
            alumno.setEstadoSaberPro("PRESENTADO");
            alumno.setFechaActualizacion(LocalDateTime.now());
            return ResponseEntity.ok(alumnoRepository.save(alumno));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/alumnos/{id}/resultado-total")
    public ResponseEntity<?> registrarResultadoTotal(@PathVariable String id,
                                                      @RequestBody ResultadoSaberPro resultado) {
        return alumnoRepository.findById(id).map(alumno -> {
            alumno.setResultadoTotal(resultado);
            alumno.setFechaActualizacion(LocalDateTime.now());
            return ResponseEntity.ok(alumnoRepository.save(alumno));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/alumnos/{id}/aprobar")
    public ResponseEntity<?> aprobarAlumno(@PathVariable String id,
                                            @RequestParam(defaultValue = "sistema") String aprobadoPor) {
        return alumnoRepository.findById(id).map(alumno -> {
            alumno.setAprobadoPorCoordinacion(true);
            alumno.setEstadoSaberPro("APROBADO");
            alumno.setFechaAprobacion(LocalDateTime.now());
            alumno.setAprobadoPor(aprobadoPor);
            alumno.setFechaActualizacion(LocalDateTime.now());
            return ResponseEntity.ok(alumnoRepository.save(alumno));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/alumnos/{id}/rechazar")
    public ResponseEntity<?> rechazarAlumno(@PathVariable String id) {
        return alumnoRepository.findById(id).map(alumno -> {
            alumno.setAprobadoPorCoordinacion(false);
            alumno.setEstadoSaberPro("RECHAZADO");
            alumno.setFechaActualizacion(LocalDateTime.now());
            return ResponseEntity.ok(alumnoRepository.save(alumno));
        }).orElse(ResponseEntity.notFound().build());
    }

    // USUARIOS
    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/usuarios/rol/{rol}")
    public ResponseEntity<List<Usuario>> usuariosPorRol(@PathVariable String rol) {
        return ResponseEntity.ok(usuarioRepository.findByRol(rol.toUpperCase()));
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable String id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "El email ya está registrado"));
        }
        usuario.setFechaCreacion(LocalDateTime.now());
        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable String id, @RequestBody Usuario datos) {
        return usuarioRepository.findById(id).map(u -> {
            datos.setId(id);
            datos.setFechaActualizacion(LocalDateTime.now());
            return ResponseEntity.ok(usuarioRepository.save(datos));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String id) {
        return usuarioRepository.findById(id).map(u -> {
            u.setActivo(false);
            u.setFechaActualizacion(LocalDateTime.now());
            usuarioRepository.save(u);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // FACULTADES
    @GetMapping("/facultades")
    public ResponseEntity<List<Facultad>> listarFacultades(@RequestParam(required = false) Boolean activa) {
        if (activa != null) return ResponseEntity.ok(facultadRepository.findByActiva(activa));
        return ResponseEntity.ok(facultadRepository.findAll());
    }

    @GetMapping("/facultades/{id}")
    public ResponseEntity<?> obtenerFacultad(@PathVariable String id) {
        return facultadRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/facultades")
    public ResponseEntity<Facultad> crearFacultad(@RequestBody Facultad facultad) {
        facultad.setFechaCreacion(LocalDateTime.now());
        return ResponseEntity.ok(facultadRepository.save(facultad));
    }

    @PutMapping("/facultades/{id}")
    public ResponseEntity<?> actualizarFacultad(@PathVariable String id, @RequestBody Facultad datos) {
        return facultadRepository.findById(id).map(f -> {
            datos.setId(id);
            return ResponseEntity.ok(facultadRepository.save(datos));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/facultades/{id}")
    public ResponseEntity<?> eliminarFacultad(@PathVariable String id) {
        return facultadRepository.findById(id).map(f -> {
            f.setActiva(false);
            facultadRepository.save(f);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // RESOLUCIONES
    @GetMapping("/resoluciones")
    public ResponseEntity<List<ResolucionBeneficio>> listarResoluciones(
            @RequestParam(required = false) String area,
            @RequestParam(required = false) Boolean vigente) {
        if (area != null && vigente != null)
            return ResponseEntity.ok(resolucionRepository.findByVigenteAndArea(vigente, area));
        if (area != null)
            return ResponseEntity.ok(resolucionRepository.findByArea(area));
        if (vigente != null)
            return ResponseEntity.ok(resolucionRepository.findByVigente(vigente));
        return ResponseEntity.ok(resolucionRepository.findAll());
    }

    @GetMapping("/resoluciones/{id}")
    public ResponseEntity<?> obtenerResolucion(@PathVariable String id) {
        return resolucionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/resoluciones")
    public ResponseEntity<ResolucionBeneficio> crearResolucion(@RequestBody ResolucionBeneficio resolucion) {
        resolucion.setFechaCreacion(LocalDateTime.now());
        return ResponseEntity.ok(resolucionRepository.save(resolucion));
    }

    @PutMapping("/resoluciones/{id}")
    public ResponseEntity<?> actualizarResolucion(@PathVariable String id,
                                                   @RequestBody ResolucionBeneficio datos) {
        return resolucionRepository.findById(id).map(r -> {
            datos.setId(id);
            return ResponseEntity.ok(resolucionRepository.save(datos));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/resoluciones/{id}")
    public ResponseEntity<?> eliminarResolucion(@PathVariable String id) {
        return resolucionRepository.findById(id).map(r -> {
            r.setVigente(false);
            resolucionRepository.save(r);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // ESTADÍSTICAS
    @GetMapping("/estadisticas/dashboard")
    public ResponseEntity<Map<String, Object>> estadisticasDashboard() {
        Map<String, Object> stats = Map.of(
            "totalAlumnos",    alumnoRepository.count(),
            "aprobados",       alumnoRepository.countByEstadoSaberPro("APROBADO"),
            "pendientes",      alumnoRepository.countByEstadoSaberPro("PENDIENTE"),
            "conBeneficio",    alumnoRepository.findByTieneBeneficio(true).size(),
            "totalFacultades", facultadRepository.findByActiva(true).size(),
            "resoluciones",    resolucionRepository.findByVigente(true).size()
        );
        return ResponseEntity.ok(stats);
    }
}