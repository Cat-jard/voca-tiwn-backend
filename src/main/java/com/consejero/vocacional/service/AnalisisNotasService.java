package com.consejero.vocacional.service;

import com.consejero.vocacional.dto.DatosConstancia;
import com.consejero.vocacional.dto.ResultadoAnalisis;
import com.consejero.vocacional.entity.BeneficioEntity;
import com.consejero.vocacional.repository.BeneficioRepository;
import org.springframework.stereotype.Service;
import java.time.Year;
import java.util.*;

@Service
public class AnalisisNotasService {

    private final BeneficioRepository beneficioRepository;

    public AnalisisNotasService(BeneficioRepository beneficioRepository) {
        this.beneficioRepository = beneficioRepository;
    }

    public ResultadoAnalisis analizar(DatosConstancia datos) {
        ResultadoAnalisis resultado = new ResultadoAnalisis();
        resultado.setNombreEstudiante(datos.getNombreEstudiante());
        resultado.setPromedio4to(Math.round(datos.getPromedio4to() * 10.0) / 10.0);
        resultado.setPromedio5to(Math.round(datos.getPromedio5to() * 10.0) / 10.0);
        resultado.setPromedioGeneral(Math.round(datos.getPromedioGeneral() * 10.0) / 10.0);

        // Identificar áreas de mejora (notas < 14)
        Map<String, Double> areasMejora = new LinkedHashMap<>();
        datos.getNotas4to().forEach((curso, nota) -> {
            if (nota < 14) areasMejora.put(curso + " (4to)", nota);
        });
        datos.getNotas5to().forEach((curso, nota) -> {
            if (nota < 14) areasMejora.put(curso + " (5to)", nota);
        });
        resultado.setAreasMejora(areasMejora);

        // Evaluar beneficios
        List<BeneficioEntity> beneficios = beneficioRepository.findAll();
        int anioActual = Year.now().getValue();
        Integer anioEgreso = datos.getAnioEgreso();

        for (BeneficioEntity b : beneficios) {
            Map<String, Object> cond = b.getCondicionesMap();
            boolean cumple = false;

            String nombre = b.getNombre().toLowerCase();

            if (nombre.contains("tercio")) {
                cumple = datos.getPromedio4to() > 16 && datos.getPromedio5to() > 16;
            } else if (nombre.contains("buen rendimiento")) {
                cumple = datos.getPromedio4to() >= 14 && datos.getPromedio5to() >= 14;
            } else if (nombre.contains("preferencial")) {
                cumple = anioEgreso != null && (anioActual - anioEgreso) == 1;
            } else if (nombre.contains("regular")) {
                cumple = anioEgreso != null && (anioActual - anioEgreso) >= 2;
            } else {
                cumple = evaluarCondicionGenerica(cond, datos, anioActual, anioEgreso);
            }

            if (cumple) {
                resultado.setBeneficioAplicable(b.getNombre());
                resultado.setModalidadIngreso("Ingreso " + b.getNombre());

                List<String> descripciones = new ArrayList<>();
                descripciones.add(b.getDescripcion());
                if (cond.containsKey("promedio_minimo")) {
                    descripciones.add("Promedio mínimo requerido: " + cond.get("promedio_minimo") + "/20");
                }
                if (cond.containsKey("diferencia_anios")) {
                    descripciones.add("Egreso: hace " + cond.get("diferencia_anios") + " año(s)");
                }
                if (cond.containsKey("anios")) {
                    descripciones.add("Años considerados: " + cond.get("anios"));
                }
                resultado.setBeneficios(descripciones);

                resultado.setRequisitos(b.getRequisitosList());
                break;
            }
        }

        if (resultado.getBeneficioAplicable() == null) {
            resultado.setBeneficioAplicable("Ninguno");
            resultado.setModalidadIngreso("Examen de Admisión Regular");
            resultado.setBeneficios(List.of("Acceso al examen de admisión"));
            resultado.setRequisitos(List.of("Ficha de inscripción", "Fotocopia DNI", "Constancia de pago", "Certificado de estudios"));
        }

        return resultado;
    }

    private boolean evaluarCondicionGenerica(Map<String, Object> cond, DatosConstancia datos, int anioActual, Integer anioEgreso) {
        if (cond.containsKey("promedio_minimo")) {
            double min = ((Number) cond.get("promedio_minimo")).doubleValue();
            return datos.getPromedioGeneral() >= min;
        }
        if (cond.containsKey("diferencia_anios") && anioEgreso != null) {
            Object val = cond.get("diferencia_anios");
            if (val instanceof Number) {
                return (anioActual - anioEgreso) == ((Number) val).intValue();
            }
            if (val instanceof String s && s.startsWith(">=")) {
                return (anioActual - anioEgreso) >= Integer.parseInt(s.substring(2));
            }
        }
        return false;
    }

}
