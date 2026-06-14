package com.consejero.vocacional.service;

import com.consejero.vocacional.dto.DatosConstancia;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProcesadorConstanciaService {

    private static final Pattern GRADE_AT_END = Pattern.compile("(\\d{1,2}(?:\\.\\d+)?)\\s*$");
    private static final Pattern STUDENT_NAME = Pattern.compile(
            "(?i)(?:estudiante|alumno|alumna)\\s*[:\\s]+([A-ZÁÉÍÓÚÑa-záéíóúñ\\s]+?)(?:,|\\s+con\\s+|$)");
    private static final Pattern DNI_PATTERN = Pattern.compile("\\b\\d{8}\\b");
    private static final Pattern ANIO_PATTERN = Pattern.compile("\\b(20\\d{2})\\b");
    private static final Pattern THREE_PLUS_DIGITS = Pattern.compile("\\d{3,}");

    private static final Set<String> NON_COURSE_TOKENS = Set.of(
            "código", "codigo", "modular", "dni", "constancia", "certificado",
            "registra", "calificativo", "calificativos", "institución", "institucion",
            "educativa", "ugel", "dirección", "direccion", "teléfono", "telefono",
            "correo", "firma", "sello", "director", "secretario", "página", "pagina",
            "web", "fecha", "nota", "promedio", "área", "area"
    );

    public DatosConstancia procesar(MultipartFile archivo) throws Exception {
        try (PDDocument document = Loader.loadPDF(archivo.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String texto = stripper.getText(document);
            System.out.println("===== TEXTO EXTRAÍDO DEL PDF =====");
            System.out.println(texto);
            System.out.println("==================================");
            return parsearTexto(texto);
        }
    }

    private DatosConstancia parsearTexto(String texto) {
        DatosConstancia datos = new DatosConstancia();
        String[] lineas = texto.split("\\r?\\n");
        String seccionActual = "";
        String ultimoPosibleCurso = null;

        for (String linea : lineas) {
            String limpia = linea.trim();
            if (limpia.isEmpty()) continue;

            String lower = limpia.toLowerCase();

            // --- Student name ---
            if ((lower.contains("estudiante") || lower.contains("alumno")) && datos.getNombreEstudiante() == null) {
                String nombre = extraerNombre(limpia);
                if (nombre != null) datos.setNombreEstudiante(nombre);
            }

            // --- DNI ---
            if (datos.getDni() == null) {
                Matcher dniM = DNI_PATTERN.matcher(limpia);
                if (dniM.find()) datos.setDni(dniM.group());
            }

            // --- Graduation year ---
            if (datos.getAnioEgreso() == null) {
                Matcher anioM = ANIO_PATTERN.matcher(limpia);
                if (anioM.find()) datos.setAnioEgreso(Integer.parseInt(anioM.group()));
            }

            // --- Section detection (state machine) ---
            String nuevaSeccion = detectarSeccion(lower);
            if (nuevaSeccion != null) {
                seccionActual = nuevaSeccion;
                continue;
            }

            // --- Skip non-course lines ---
            if (esLineaNoCurso(lower)) continue;

            // --- Try to extract grade from current line ---
            if (!seccionActual.isEmpty()) {
                extraerNotaDeLinea(limpia, seccionActual, datos);
            }

            // --- State-based pairing: if line looks like a subject and next might be the grade ---
            if (esPosibleNombreCurso(limpia) && !terminaConNumero(limpia)) {
                ultimoPosibleCurso = limpia;
            } else if (ultimoPosibleCurso != null && esNotaAislada(limpia)) {
                double nota = Double.parseDouble(limpia.trim());
                if (nota >= 0 && nota <= 20) {
                    if ("4to".equals(seccionActual)) {
                        datos.getNotas4to().put(ultimoPosibleCurso, nota);
                    } else if ("5to".equals(seccionActual)) {
                        datos.getNotas5to().put(ultimoPosibleCurso, nota);
                    }
                }
                ultimoPosibleCurso = null;
            } else {
                ultimoPosibleCurso = null;
            }
        }

        return datos;
    }

    private String extraerNombre(String linea) {
        String limpia = linea.replaceAll("(?i)(?:estudiante|alumno|alumna)\\s*[:\\s]+", "").trim();
        int comma = limpia.indexOf(',');
        if (comma > 0) limpia = limpia.substring(0, comma);
        int con = limpia.toLowerCase().indexOf(" con ");
        if (con > 0) limpia = limpia.substring(0, con);
        return limpia.trim().isEmpty() || limpia.length() < 4 ? null : limpia.trim();
    }

    private String detectarSeccion(String lower) {
        Set<String> cuatroTokens = Set.of("4°", "4to", "cuarto", "cuarta", "4 grado", "4to grado", "cuarto grado");
        Set<String> cincoTokens = Set.of("5°", "5to", "quinto", "quinta", "5 grado", "5to grado", "quinto grado");

        boolean es4 = cuatroTokens.stream().anyMatch(lower::contains);
        boolean es5 = cincoTokens.stream().anyMatch(lower::contains);

        if (es4 && !es5) return "4to";
        if (es5 && !es4) return "5to";
        if (es4 && es5) {
            boolean after4 = cuatroTokens.stream().anyMatch(t -> lower.indexOf(t) > lower.indexOf("y"));
            boolean after5 = cincoTokens.stream().anyMatch(t -> lower.indexOf(t) > lower.indexOf("y"));
            if (after4 && !after5) return "4to";
            if (after5 && !after4) return "5to";
        }
        return null;
    }

    private boolean esLineaNoCurso(String lower) {
        for (String token : NON_COURSE_TOKENS) {
            if (lower.contains(token)) return true;
        }
        // Skip lines with 3+ digit numbers (likely codes/IDs)
        if (THREE_PLUS_DIGITS.matcher(lower).find()) return true;
        return false;
    }

    private void extraerNotaDeLinea(String linea, String seccion, DatosConstancia datos) {
        Matcher m = GRADE_AT_END.matcher(linea);
        if (!m.find()) return;

        double nota;
        try {
            nota = Double.parseDouble(m.group(1));
        } catch (NumberFormatException e) {
            return;
        }
        if (nota < 0 || nota > 20) return;

        String posibleCurso = linea.substring(0, m.start()).trim();

        // Validate course name
        if (posibleCurso.length() < 3 || posibleCurso.length() > 80) return;
        if (THREE_PLUS_DIGITS.matcher(posibleCurso).find()) return;
        if (posibleCurso.replaceAll("[^A-Za-zÁÉÍÓÚÑáéíóúñ]", "").length() < 2) return;

        if ("4to".equals(seccion)) {
            datos.getNotas4to().put(posibleCurso, nota);
        } else if ("5to".equals(seccion)) {
            datos.getNotas5to().put(posibleCurso, nota);
        }
    }

    private boolean esPosibleNombreCurso(String linea) {
        String clean = linea.replaceAll("[^A-Za-zÁÉÍÓÚÑáéíóúñ\\s]", "").trim();
        return clean.length() >= 4 && clean.length() <= 80 && !THREE_PLUS_DIGITS.matcher(linea).find();
    }

    private boolean terminaConNumero(String linea) {
        return GRADE_AT_END.matcher(linea).find();
    }

    private boolean esNotaAislada(String linea) {
        String t = linea.trim();
        return t.matches("\\d{1,2}(\\.\\d+)?") && !t.matches("\\d{3,}");
    }
}
