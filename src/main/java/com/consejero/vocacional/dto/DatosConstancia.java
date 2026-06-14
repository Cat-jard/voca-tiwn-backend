package com.consejero.vocacional.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class DatosConstancia {

    private String nombreEstudiante;
    private String dni;
    private String colegio;
    private Integer anioEgreso;
    private Map<String, Double> notas4to = new LinkedHashMap<>();
    private Map<String, Double> notas5to = new LinkedHashMap<>();

    public double getPromedio4to() {
        return notas4to.isEmpty() ? 0.0 :
                notas4to.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public double getPromedio5to() {
        return notas5to.isEmpty() ? 0.0 :
                notas5to.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public double getPromedioGeneral() {
        double suma = 0;
        int count = 0;
        for (double n : notas4to.values()) { suma += n; count++; }
        for (double n : notas5to.values()) { suma += n; count++; }
        return count == 0 ? 0.0 : suma / count;
    }

    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getColegio() { return colegio; }
    public void setColegio(String colegio) { this.colegio = colegio; }
    public Integer getAnioEgreso() { return anioEgreso; }
    public void setAnioEgreso(Integer anioEgreso) { this.anioEgreso = anioEgreso; }
    public Map<String, Double> getNotas4to() { return notas4to; }
    public void setNotas4to(Map<String, Double> notas4to) { this.notas4to = notas4to; }
    public Map<String, Double> getNotas5to() { return notas5to; }
    public void setNotas5to(Map<String, Double> notas5to) { this.notas5to = notas5to; }

}
