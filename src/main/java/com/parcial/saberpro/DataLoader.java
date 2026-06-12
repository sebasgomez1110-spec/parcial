package com.parcial.saberpro;

import com.parcial.saberpro.entity.*;
import com.parcial.saberpro.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final UsuarioRepository usuarioRepository;
    private final FacultadRepository facultadRepository;
    private final AlumnoRepository alumnoRepository;
    private final ResolucionBeneficioRepository resolucionRepository;

    public DataLoader(UsuarioRepository u, FacultadRepository f, AlumnoRepository a, ResolucionBeneficioRepository r) {
        this.usuarioRepository = u; this.facultadRepository = f;
        this.alumnoRepository = a; this.resolucionRepository = r;
    }

    @Override
    public void run(String... args) {

        // USUARIOS
        crearUsuario("admin@uts.edu.co",        "admin123",  "Administrador",  "Sistema",   "0000000001","ADMIN");
        crearUsuario("coordinacion@uts.edu.co", "coord123",  "Coordinación",   "Saber Pro", "0000000002","COORDINACION");
        crearUsuario("coord2@uts.edu.co",       "coord456",  "Laura",          "Martínez",  "0000000005","COORDINACION");
        crearUsuario("docente@uts.edu.co",      "doc123",    "Carlos",         "Pérez",     "0000000003","DOCENTE");
        crearUsuario("docente2@uts.edu.co",     "doc456",    "Andrés",         "Gómez",     "0000000006","DOCENTE");

        // FACULTADES UTS — 29 programas oficiales
        List<Facultad> facActuales = facultadRepository.findAll();
        boolean sinArea = !facActuales.isEmpty() && facActuales.stream()
                .allMatch(f -> f.getDirectorNombre() == null
                        || (!f.getDirectorNombre().equals("TECNOLOGIA")
                        && !f.getDirectorNombre().equals("INGENIERIA")
                        && !f.getDirectorNombre().equals("ADMINISTRATIVA")
                        && !f.getDirectorNombre().equals("DEPORTES")));

        if (facActuales.isEmpty() || sinArea) {
            if (sinArea) {
                log.info("Facultades antiguas sin área detectadas — eliminando y recreando las 29 UTS");
                facultadRepository.deleteAll();
            }

            crearFacultad("Tecnología en Desarrollo de Sistemas Informáticos", "TDSI",
                    "Diseño y desarrollo de software, bases de datos y sistemas de información", "TECNOLOGIA");
            crearFacultad("Ingeniería de Sistemas", "IS",
                    "Gestión, diseño y arquitectura de sistemas computacionales", "INGENIERIA");
            crearFacultad("Tecnología en Implementación de Sistemas Electrónicos Industriales", "TISEI",
                    "Electrónica aplicada a procesos industriales y automatización", "TECNOLOGIA");
            crearFacultad("Ingeniería Electrónica", "IE",
                    "Diseño de sistemas electrónicos, telecomunicaciones y control", "INGENIERIA");
            crearFacultad("Tecnología en Electricidad Industrial", "TEI",
                    "Instalaciones eléctricas industriales y sistemas de potencia", "TECNOLOGIA");
            crearFacultad("Ingeniería Eléctrica", "IEL",
                    "Generación, transmisión y distribución de energía eléctrica", "INGENIERIA");
            crearFacultad("Tecnología en Gestión de Sistemas de Telecomunicaciones", "TGST",
                    "Redes de telecomunicaciones, fibra óptica y comunicaciones digitales", "TECNOLOGIA");
            crearFacultad("Ingeniería de Telecomunicaciones", "IT",
                    "Diseño e implementación de infraestructuras de telecomunicaciones", "INGENIERIA");
            crearFacultad("Tecnología en Producción Industrial", "TPI",
                    "Gestión de procesos productivos, calidad y manufactura", "TECNOLOGIA");
            crearFacultad("Ingeniería Industrial", "II",
                    "Optimización de procesos industriales y cadenas de suministro", "INGENIERIA");
            crearFacultad("Tecnología en Operación y Mantenimiento Electromecánico", "TOME",
                    "Mantenimiento de maquinaria eléctrica y mecánica industrial", "TECNOLOGIA");
            crearFacultad("Ingeniería Electromecánica", "IEM",
                    "Integración de sistemas eléctricos y mecánicos en la industria", "INGENIERIA");
            crearFacultad("Tecnología en Manejo de Recursos Ambientales", "TMRA",
                    "Gestión ambiental, saneamiento y conservación de recursos naturales", "TECNOLOGIA");
            crearFacultad("Ingeniería Ambiental", "IA",
                    "Evaluación de impacto ambiental, tratamiento de aguas y residuos", "INGENIERIA");
            crearFacultad("Tecnología en Levantamientos Topográficos", "TLT",
                    "Medición y representación del terreno con instrumentos topográficos", "TECNOLOGIA");
            crearFacultad("Ingeniería en Topografía", "ITOP",
                    "Geodesia, cartografía y sistemas de información geográfica (SIG)", "INGENIERIA");
            crearFacultad("Tecnología en Gestión Empresarial", "TGE",
                    "Administración de organizaciones, liderazgo y gestión del talento", "ADMINISTRATIVA");
            crearFacultad("Administración de Empresas", "AE",
                    "Dirección estratégica, finanzas y gestión organizacional", "ADMINISTRATIVA");
            crearFacultad("Tecnología en Mercadeo y Gestión Comercial", "TMGC",
                    "Estrategias de ventas, marketing digital y gestión comercial", "ADMINISTRATIVA");
            crearFacultad("Profesional en Mercadeo", "PM",
                    "Investigación de mercados, branding y comportamiento del consumidor", "ADMINISTRATIVA");
            crearFacultad("Tecnología en Gestión Bancaria y Financiera", "TGBF",
                    "Operaciones bancarias, crédito y gestión de portafolios financieros", "ADMINISTRATIVA");
            crearFacultad("Administración Financiera", "AF",
                    "Análisis financiero, inversión y gestión de riesgos corporativos", "ADMINISTRATIVA");
            crearFacultad("Tecnología en Manejo de la Información Contable", "TMIC",
                    "Contabilidad, costos y gestión tributaria en las organizaciones", "ADMINISTRATIVA");
            crearFacultad("Contaduría Pública", "CP",
                    "Auditoría, revisoría fiscal, normas NIIF y gestión contable", "ADMINISTRATIVA");
            crearFacultad("Tecnología en Gestión Agroindustrial", "TGA",
                    "Procesamiento y comercialización de productos agropecuarios", "ADMINISTRATIVA");
            crearFacultad("Tecnología en Gestión de la Moda", "TGMOD",
                    "Diseño, producción y comercialización en la industria de la moda", "ADMINISTRATIVA");
            crearFacultad("Profesional en Diseño de Moda", "PDM",
                    "Creación de colecciones, patronaje y gestión de marcas de moda", "ADMINISTRATIVA");
            crearFacultad("Tecnología en Entrenamiento Deportivo", "TED",
                    "Planificación del entrenamiento, rendimiento deportivo y salud", "DEPORTES");
            crearFacultad("Profesional en Cultura Física y Deporte", "PCFD",
                    "Actividad física, recreación, deporte y bienestar comunitario", "DEPORTES");

            log.info("29 facultades UTS creadas");
        }

        // RESOLUCIONES
        if (resolucionRepository.count() == 0) {
            crearResolucion("RES-001-2025","Beneficio Excelencia — Tecnología",
                "Descuento 30% matrícula para puntaje global >= 160","TECNOLOGIA",160.0,
                List.of("Diseño de Software","Comunicación Escrita"),"DESCUENTO_MATRICULA","30%");
            crearResolucion("RES-002-2025","Beneficio Destacado — Ingeniería",
                "Descuento 25% matrícula para puntaje global >= 150","INGENIERIA",150.0,
                List.of("Formulación de Proyectos de Ingeniería","Razonamiento Cuantitativo"),"DESCUENTO_MATRICULA","25%");
            crearResolucion("RES-003-2025","Reconocimiento Bilingüismo",
                "Reconocimiento especial para nivel inglés B1 o superior","GENERAL",0.0,
                List.of("Inglés"),"RECONOCIMIENTO","N/A");
            log.info("3 resoluciones de beneficios creadas");
        }

        // ALUMNOS — ✅ CORREGIDO: se verifica existsByCedula antes de insertar
        List<Facultad> facultades = facultadRepository.findAll();
        String idTDSI = facultades.stream().filter(f -> "TDSI".equals(f.getCodigo())).map(Facultad::getId).findFirst().orElse("");
        String idIS   = facultades.stream().filter(f -> "IS".equals(f.getCodigo())).map(Facultad::getId).findFirst().orElse("");

        Object[][] data = {
            {"BARBOSA",   "EK20183007722",200d,128d,182d,202d,206d,183d,185d,160d,"B1","INGENIERIA"},
            {"QUINTERO",  "EK20183140703",165d,125d,151d,179d,163d,205d,182d,144d,"B2","TECNOLOGIA"},
            {"PARRA",     "EK20183040545",164d,159d,172d,182d,142d,165d,167d,132d,"A2","TECNOLOGIA"},
            {"ANAYA",     "EK20183025381",160d,146d,199d,157d,149d,147d,174d,127d,"A2","INGENIERIA"},
            {"FLOR",      "EK20183025335",160d,198d,153d,147d,157d,146d,168d,114d,"A2","TECNOLOGIA"},
            {"GARCIA",    "EK20183122648",157d,179d,172d,158d,140d,136d,128d,121d,"A1","INGENIERIA"},
            {"MANOSALVA", "EK20183064605",153d,115d,152d,159d,172d,165d,142d,118d,"A2","TECNOLOGIA"},
            {"MENDOZA",   "EK20183187351",151d,132d,123d,125d,169d,204d,173d,127d,"B2","INGENIERIA"},
            {"BELTRAN",   "EK20183233820",150d, 86d,187d,160d,171d,148d,162d,125d,"A2","TECNOLOGIA"},
            {"SANTAMARIA","EK20183030016",150d,175d,149d,145d,158d,125d,162d, 76d,"A1","INGENIERIA"},
            {"SANCHEZ",   "EK20183047073",149d,209d,143d,117d,129d,147d,137d,125d,"A2","TECNOLOGIA"},
            {"ROMERO",    "EK20183236451",146d, 93d,183d,155d,164d,133d,174d,130d,"A1","INGENIERIA"},
            {"LUNA",      "EK20183041714",141d,125d,157d,138d,135d,152d,176d,128d,"A2","TECNOLOGIA"},
            {"TRIANA",    "EK20183187801",141d,150d,136d,145d,150d,126d,148d,129d,"A1","INGENIERIA"},
            {"SUAREZ",    "EK20183176566",140d,128d,146d,146d,132d,147d,130d,110d,"A2","TECNOLOGIA"},
            {"GARCIA",    "EK20183204427",139d,129d,138d,148d,146d,135d,109d,107d,"A1","INGENIERIA"},
            {"PINZON",    "EK20183196280",138d,153d,123d,127d,147d,140d,145d,143d,"A1","TECNOLOGIA"},
            {"JAIMES",    "EK20183173799",137d,166d,157d,124d,100d,140d,100d,105d,"A1","INGENIERIA"},
            {"NINO",      "EK20183009565",134d,165d,137d,136d,118d,116d,146d,122d,"A0","TECNOLOGIA"},
            {"FABIAN",    "EK20183117756",133d,139d, 93d,168d,150d,114d,102d,123d,"A0","INGENIERIA"},
            {"HERNANDEZ", "EK20183044579",132d,116d,166d,136d,104d,140d,158d,125d,"A1","TECNOLOGIA"},
            {"LARIOS",    "EK20183045760",131d,149d,123d,129d,121d,131d,101d,102d,"A1","INGENIERIA"},
            {"CALDERON",  "EK20183034044",130d,127d,147d,134d,111d,131d, 65d,112d,"A1","TECNOLOGIA"},
            {"VILLARREAL","EK20183041521",129d, 96d,162d,114d,131d,144d,122d,112d,"A1","INGENIERIA"},
            {"RESTREPO",  "EK20183027436",126d, 81d,134d,126d,149d,139d,127d,136d,"A1","TECNOLOGIA"},
            {"CACERES",   "EK20183031592",125d,124d,135d,108d, 92d,165d,132d,104d,"A2","INGENIERIA"},
            {"TABARES",   "EK20183004153",124d,131d,131d,107d, 88d,162d,136d,112d,"A2","TECNOLOGIA"},
            {"NARANJO",   "EK20183030783",122d,166d,113d,113d,112d,106d,135d,117d,"A0","INGENIERIA"},
            {"PRADA",     "EK20183024754",122d,119d,125d,137d,107d,123d, 83d,104d,"A1","TECNOLOGIA"},
            {"VARGAS",    "EK20183186200",114d, 95d,120d,151d, 86d,119d,149d,103d,"A0","INGENIERIA"},
            {"TORRES",    "EK20183182410",113d,109d,105d,104d,103d,142d,102d,135d,"A1","TECNOLOGIA"},
            {"ORTIZ",     "EK20183213735",107d,128d, 81d,107d,102d,119d,130d,111d,"A0","INGENIERIA"},
            {"VILLAMIZAR","EK20183065220",106d,134d, 96d, 92d,110d, 97d, 83d,107d,"A0","TECNOLOGIA"},
            {"RESTREPO",  "EK20183028123", 96d,  0d,117d,122d,105d,137d,157d, 96d,"A1","INGENIERIA"},
        };

        String[] nombresM = {"Andrés","Carlos","Miguel","David","Daniel","Juan","Sebastián","Felipe","Santiago","Camilo","Diego","Alejandro","Nicolás","Mateo","Cristian","Jhon","Luis","Sergio"};
        String[] nombresF = {"Laura","María","Andrea","Valentina","Daniela","Natalia","Camila","Paola","Juliana","Carolina","Luisa","Diana","Marcela","Viviana","Alejandra","Catalina","Adriana","Lorena"};

        int alumnosCreados = 0;
        for (int i = 0; i < data.length; i++) {
            Object[] d = data[i];
            String apellido = (String) d[0];
            String registro = (String) d[1];
            double global   = (Double) d[2];
            double commEsc  = (Double) d[3];
            double razCuant = (Double) d[4];
            double lecCrit  = (Double) d[5];
            double compCiud = (Double) d[6];
            double ingles   = (Double) d[7];
            double formulac = (Double) d[8];
            double pensam   = (Double) d[9];
            String nivelIng = (String) d[10];
            String area     = (String) d[11];

            boolean esTec   = "TECNOLOGIA".equals(area);
            String facId    = esTec ? idTDSI : idIS;
            String facNom   = esTec ? "Tecnología en Desarrollo de Sistemas Informáticos" : "Ingeniería de Sistemas";
            String modEspNombre  = esTec ? "Diseño de Software" : "Formulación de Proyectos de Ingeniería";
            double modEspPuntaje = esTec ? pensam : formulac;

            String clasi = global >= 170 ? "SUPERIOR" : global >= 145 ? "ALTO" : global >= 120 ? "MEDIO" : "BAJO";

            boolean tieneBen = global >= 150 || "B1".equals(nivelIng) || "B2".equals(nivelIng);
            String tipoBen = null;
            if ("B1".equals(nivelIng) || "B2".equals(nivelIng)) tipoBen = "RECONOCIMIENTO";
            else if (global >= 160) tipoBen = "DESCUENTO_MATRICULA";
            else if (global >= 150) tipoBen = "DESCUENTO_MATRICULA";

            String nombre = (i % 2 == 0) ? nombresM[i % nombresM.length] : nombresF[i % nombresF.length];
            String cedula = String.valueOf(1090000000L + (long) i * 13791L);
            String email  = normalize(nombre) + "." + apellido.toLowerCase() + "@uts.edu.co";

            // ✅ CORRECCIÓN: no insertar si ya existe un alumno con esa cédula
            if (alumnoRepository.existsByCedula(cedula)) {
                continue;
            }

            ResultadoSaberPro resultado = new ResultadoSaberPro(
                    LocalDate.of(2025, 10, 15), "2025-2",
                    razCuant, lecCrit, commEsc, compCiud, ingles,
                    modEspNombre, modEspPuntaje, global, clasi,
                    "Período 2025-2 | Registro: " + registro
            );

            Alumno alumno = new Alumno();
            alumno.setNombre(nombre); alumno.setApellido(apellido);
            alumno.setCedula(cedula); alumno.setEmail(email);
            alumno.setTelefono("317" + String.format("%07d", (i * 1234567) % 10000000));
            alumno.setFacultadId(facId); alumno.setFacultadNombre(facNom);
            alumno.setProgramaAcademico(facNom); alumno.setSemestre(String.valueOf(8 + (i % 3)));
            alumno.setCodigoEstudiante(registro);
            alumno.setEstadoSaberPro("APROBADO"); alumno.setAprobadoPorCoordinacion(true);
            alumno.setFechaAprobacion(LocalDateTime.of(2025, 9, 1, 8, 0));
            alumno.setAprobadoPor("coordinacion@uts.edu.co"); alumno.setPagoVerificado(true);
            alumno.setResultadoUnico(resultado); alumno.setResultadoTotal(resultado);
            alumno.setTieneBeneficio(tieneBen); alumno.setTipoBeneficio(tipoBen);
            alumno.setActivo(true); alumno.setFechaCreacion(LocalDateTime.of(2025, 8, 15, 10, 0));
            alumnoRepository.save(alumno);
            alumnosCreados++;

            // ✅ CORRECCIÓN: ya tenía la guarda existsByEmail, se mantiene
            if (!usuarioRepository.existsByEmail(email)) {
                Usuario usr = new Usuario();
                usr.setNombre(nombre); usr.setApellido(apellido);
                usr.setEmail(email); usr.setPassword(cedula);
                usr.setCedula(cedula); usr.setTelefono(alumno.getTelefono());
                usr.setRol("ESTUDIANTE"); usr.setActivo(true);
                usr.setFechaCreacion(LocalDateTime.of(2025, 8, 15, 10, 0));
                usuarioRepository.save(usr);
            }
        }

        if (alumnosCreados > 0) {
            log.info("{} alumnos del Excel cargados en MongoDB", alumnosCreados);
        } else {
            log.info("Alumnos ya existentes — no se insertaron duplicados");
        }
    }

    private String normalize(String s) {
        return s.toLowerCase()
                .replace("é","e").replace("á","a").replace("í","i")
                .replace("ó","o").replace("ú","u").replace("ñ","n");
    }

    private void crearUsuario(String email, String pass, String nombre, String apellido, String cedula, String rol) {
        if (!usuarioRepository.existsByEmail(email)) {
            Usuario u = new Usuario();
            u.setNombre(nombre); u.setApellido(apellido); u.setEmail(email);
            u.setPassword(pass); u.setCedula(cedula); u.setRol(rol);
            u.setActivo(true); u.setFechaCreacion(LocalDateTime.now());
            usuarioRepository.save(u);
            log.info("Usuario: {} / {} [{}]", email, pass, rol);
        }
    }

    private void crearFacultad(String nombre, String codigo, String desc, String area) {
        Facultad f = new Facultad();
        f.setNombre(nombre); f.setCodigo(codigo);
        f.setDescripcion(desc + " — UTS Bucaramanga");
        f.setDirectorNombre(area);
        f.setActiva(true); f.setFechaCreacion(LocalDateTime.now());
        facultadRepository.save(f);
    }

    private void crearResolucion(String num, String titulo, String desc, String area,
                                  double puntMin, List<String> modulos, String tipo, String pct) {
        ResolucionBeneficio r = new ResolucionBeneficio();
        r.setNumeroResolucion(num); r.setTitulo(titulo); r.setDescripcion(desc);
        r.setArea(area); r.setPuntajeMinimo(puntMin); r.setModulosAplicables(modulos);
        r.setTipoBeneficio(tipo); r.setPorcentajeDescuento(pct);
        r.setFechaEmision(LocalDate.of(2025, 1, 15));
        r.setFechaVigencia(LocalDate.of(2026, 12, 31));
        r.setVigente(true); r.setCreadoPor("admin@uts.edu.co");
        r.setFechaCreacion(LocalDateTime.now());
        resolucionRepository.save(r);
    }
}