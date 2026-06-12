package com.parcial.saberpro.controller;

import com.parcial.saberpro.entity.Alumno;
import com.parcial.saberpro.entity.ResultadoSaberPro;
import com.parcial.saberpro.repository.AlumnoRepository;
import com.parcial.saberpro.repository.ResolucionBeneficioRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/export")
public class ExportController {

    private final AlumnoRepository alumnoRepository;
    private final ResolucionBeneficioRepository resolucionRepository;

    public ExportController(AlumnoRepository a, ResolucionBeneficioRepository r) {
        this.alumnoRepository = a;
        this.resolucionRepository = r;
    }

    // ── RF-14/18: Excel alumnos (Coordinación y Docente) ─────────────────────
    @GetMapping("/alumnos/excel")
    public void exportarAlumnosExcel(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String facultadId,
            @RequestParam(required = false) String estado,
            HttpServletResponse response) throws IOException {

        List<Alumno> alumnos = obtenerAlumnos(facultadId, estado);
        boolean isTotal = !"UNICO".equalsIgnoreCase(tipo);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String filename = "Informe_Alumnos_" + (isTotal ? "Total" : "Unico") + "_"
                + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Alumnos Saber Pro");

            // Estilos
            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font hFont = wb.createFont();
            hFont.setColor(IndexedColors.WHITE.getIndex());
            hFont.setBold(true);
            hFont.setFontHeightInPoints((short) 11);
            headerStyle.setFont(hFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            CellStyle titleStyle = wb.createCellStyle();
            Font tFont = wb.createFont();
            tFont.setBold(true);
            tFont.setFontHeightInPoints((short) 14);
            tFont.setColor(IndexedColors.DARK_GREEN.getIndex());
            titleStyle.setFont(tFont);

            CellStyle altStyle = wb.createCellStyle();
            altStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            altStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle numStyle = wb.createCellStyle();
            DataFormat fmt = wb.createDataFormat();
            numStyle.setDataFormat(fmt.getFormat("0.0"));

            // Título
            Row titleRow = sheet.createRow(0);
            Cell tc = titleRow.createCell(0);
            tc.setCellValue("Sistema Saber Pro — UTS Bucaramanga");
            tc.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 12));

            Row subRow = sheet.createRow(1);
            subRow.createCell(0).setCellValue("Informe " + (isTotal ? "Total" : "Único")
                    + " | Generado: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 12));

            // Cabecera
            String[] baseHeaders = {"N°", "Nombre", "Apellido", "Cédula", "Email", "Facultad",
                    "Semestre", "Código Est.", "Estado SP", "Aprobado", "Beneficio", "Tipo Beneficio"};
            String[] resultHeaders = {"Puntaje Global", "Clasif.", "Comm. Escrita",
                    "Raz. Cuantit.", "Lect. Crítica", "Comp. Ciudadanas", "Inglés",
                    "Módulo Esp.", "Puntaje Esp."};

            Row header = sheet.createRow(3);
            int col = 0;
            for (String h : baseHeaders) {
                Cell c = header.createCell(col++);
                c.setCellValue(h);
                c.setCellStyle(headerStyle);
            }
            if (isTotal) {
                for (String h : resultHeaders) {
                    Cell c = header.createCell(col++);
                    c.setCellValue(h);
                    c.setCellStyle(headerStyle);
                }
            }

            // Datos
            int rowNum = 4;
            for (int i = 0; i < alumnos.size(); i++) {
                Alumno a = alumnos.get(i);
                Row row = sheet.createRow(rowNum++);
                if (i % 2 == 1) {
                    for (int j = 0; j < (isTotal ? 21 : 12); j++) {
                        row.createCell(j).setCellStyle(altStyle);
                    }
                }
                int c2 = 0;
                row.createCell(c2++).setCellValue(i + 1);
                row.createCell(c2++).setCellValue(a.getNombre());
                row.createCell(c2++).setCellValue(a.getApellido());
                row.createCell(c2++).setCellValue(a.getCedula());
                row.createCell(c2++).setCellValue(a.getEmail() != null ? a.getEmail() : "");
                row.createCell(c2++).setCellValue(a.getFacultadNombre() != null ? a.getFacultadNombre() : "");
                row.createCell(c2++).setCellValue(a.getSemestre() != null ? a.getSemestre() : "");
                row.createCell(c2++).setCellValue(a.getCodigoEstudiante() != null ? a.getCodigoEstudiante() : "");
                row.createCell(c2++).setCellValue(a.getEstadoSaberPro() != null ? a.getEstadoSaberPro() : "");
                row.createCell(c2++).setCellValue(a.isAprobadoPorCoordinacion() ? "Sí" : "No");
                row.createCell(c2++).setCellValue(a.isTieneBeneficio() ? "Sí" : "No");
                row.createCell(c2++).setCellValue(a.getTipoBeneficio() != null ? a.getTipoBeneficio() : "");

                if (isTotal) {
                    ResultadoSaberPro r = a.getResultadoTotal();
                    if (r != null) {
                        setNum(row, c2++, r.getPuntajeGlobal(), numStyle);
                        row.createCell(c2++).setCellValue(r.getClasificacion() != null ? r.getClasificacion() : "");
                        setNum(row, c2++, r.getComunicacionEscrita(), numStyle);
                        setNum(row, c2++, r.getRazonamientoCuantitativo(), numStyle);
                        setNum(row, c2++, r.getLecturasCritica(), numStyle);
                        setNum(row, c2++, r.getCompetenciasCiudadanas(), numStyle);
                        setNum(row, c2++, r.getIngles(), numStyle);
                        row.createCell(c2++).setCellValue(r.getModuloEspecificoNombre() != null ? r.getModuloEspecificoNombre() : "");
                        setNum(row, c2++, r.getModuloEspecificoPuntaje(), numStyle);
                    }
                }
            }

            // Autosize columnas
            int maxCol = isTotal ? 21 : 12;
            for (int j = 0; j < maxCol; j++) sheet.autoSizeColumn(j);

            // Fila de totales
            Row totRow = sheet.createRow(rowNum + 1);
            CellStyle totStyle = wb.createCellStyle();
            Font totFont = wb.createFont();
            totFont.setBold(true);
            totStyle.setFont(totFont);
            Cell totLabel = totRow.createCell(0);
            totLabel.setCellValue("Total: " + alumnos.size() + " alumnos");
            totLabel.setCellStyle(totStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, 0, 3));

            wb.write(response.getOutputStream());
        }
    }

    // ── RF-15/18: Excel beneficios ────────────────────────────────────────────
    @GetMapping("/beneficios/excel")
    public void exportarBeneficiosExcel(HttpServletResponse response) throws IOException {
        List<Alumno> beneficiados = alumnoRepository.findByTieneBeneficio(true);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String filename = "Informe_Beneficios_"
                + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Beneficios Saber Pro");

            CellStyle hs = wb.createCellStyle();
            hs.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
            hs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font hf = wb.createFont();
            hf.setColor(IndexedColors.WHITE.getIndex());
            hf.setBold(true);
            hs.setFont(hf);

            Row title = sheet.createRow(0);
            title.createCell(0).setCellValue("Informe de Beneficios — Sistema Saber Pro UTS");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));

            Row header = sheet.createRow(2);
            String[] heads = {"Nombre Completo","Cédula","Facultad","Código Est.","Estado SP","Tipo Beneficio","Puntaje Global"};
            for (int i = 0; i < heads.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(heads[i]);
                c.setCellStyle(hs);
            }

            int rn = 3;
            for (Alumno a : beneficiados) {
                Row row = sheet.createRow(rn++);
                row.createCell(0).setCellValue(a.getNombreCompleto());
                row.createCell(1).setCellValue(a.getCedula());
                row.createCell(2).setCellValue(a.getFacultadNombre() != null ? a.getFacultadNombre() : "");
                row.createCell(3).setCellValue(a.getCodigoEstudiante() != null ? a.getCodigoEstudiante() : "");
                row.createCell(4).setCellValue(a.getEstadoSaberPro());
                row.createCell(5).setCellValue(a.getTipoBeneficio() != null ? a.getTipoBeneficio() : "");
                double pg = 0;
                if (a.getResultadoTotal() != null && a.getResultadoTotal().getPuntajeGlobal() != null)
                    pg = a.getResultadoTotal().getPuntajeGlobal();
                row.createCell(6).setCellValue(pg);
            }
            for (int i = 0; i < 7; i++) sheet.autoSizeColumn(i);
            wb.write(response.getOutputStream());
        }
    }

    // ── RF-14/18: Vista impresión PDF (Thymeleaf + CSS print) ─────────────────
    @GetMapping("/alumnos/print")
    public String printAlumnos(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String facultadId,
            @RequestParam(required = false) String estado,
            Model model) {
        List<Alumno> alumnos = obtenerAlumnos(facultadId, estado);
        model.addAttribute("alumnos", alumnos);
        model.addAttribute("tipo", tipo != null ? tipo : "TOTAL");
        model.addAttribute("fecha", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        model.addAttribute("totalAlumnos", alumnos.size());
        model.addAttribute("aprobados", alumnos.stream().filter(a -> "APROBADO".equals(a.getEstadoSaberPro())).count());
        model.addAttribute("conBeneficio", alumnos.stream().filter(Alumno::isTieneBeneficio).count());
        return "export/print-alumnos";
    }

    @GetMapping("/beneficios/print")
    public String printBeneficios(Model model) {
        model.addAttribute("alumnos",     alumnoRepository.findByTieneBeneficio(true));
        model.addAttribute("resoluciones",resolucionRepository.findByVigente(true));
        model.addAttribute("fecha", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        return "export/print-beneficios";
    }

    // Helper
    private List<Alumno> obtenerAlumnos(String facultadId, String estado) {
        if (facultadId != null && !facultadId.isBlank() && estado != null && !estado.isBlank())
            return alumnoRepository.findByFacultadIdAndEstadoSaberPro(facultadId, estado);
        if (facultadId != null && !facultadId.isBlank())
            return alumnoRepository.findByFacultadId(facultadId);
        if (estado != null && !estado.isBlank())
            return alumnoRepository.findByEstadoSaberPro(estado);
        return alumnoRepository.findAll();
    }

    private void setNum(Row row, int col, Double val, CellStyle style) {
        Cell c = row.createCell(col);
        if (val != null) { c.setCellValue(val); c.setCellStyle(style); }
        else c.setCellValue("");
    }
}
