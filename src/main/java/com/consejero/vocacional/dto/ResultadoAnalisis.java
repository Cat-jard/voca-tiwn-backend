package com.consejero.vocacional.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResultadoAnalisis {

    private String nombreEstudiante;
    private double promedio4to;
    private double promedio5to;
    private double promedioGeneral;
    private String beneficioAplicable;
    private String modalidadIngreso;
    private List<String> beneficios;
    private List<String> requisitos;
    private Map<String, Double> areasMejora = new LinkedHashMap<>();

    public String getNombreEstudiante() { return nombreEstudiante; }
    public void setNombreEstudiante(String nombreEstudiante) { this.nombreEstudiante = nombreEstudiante; }
    public double getPromedio4to() { return promedio4to; }
    public void setPromedio4to(double promedio4to) { this.promedio4to = promedio4to; }
    public double getPromedio5to() { return promedio5to; }
    public void setPromedio5to(double promedio5to) { this.promedio5to = promedio5to; }
    public double getPromedioGeneral() { return promedioGeneral; }
    public void setPromedioGeneral(double promedioGeneral) { this.promedioGeneral = promedioGeneral; }
    public String getBeneficioAplicable() { return beneficioAplicable; }
    public void setBeneficioAplicable(String beneficioAplicable) { this.beneficioAplicable = beneficioAplicable; }
    public String getModalidadIngreso() { return modalidadIngreso; }
    public void setModalidadIngreso(String modalidadIngreso) { this.modalidadIngreso = modalidadIngreso; }
    public List<String> getBeneficios() { return beneficios; }
    public void setBeneficios(List<String> beneficios) { this.beneficios = beneficios; }
    public List<String> getRequisitos() { return requisitos; }
    public void setRequisitos(List<String> requisitos) { this.requisitos = requisitos; }
    public Map<String, Double> getAreasMejora() { return areasMejora; }
    public void setAreasMejora(Map<String, Double> areasMejora) { this.areasMejora = areasMejora; }

}
