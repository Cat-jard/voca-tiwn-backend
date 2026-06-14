package com.consejero.vocacional.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "carrera")
public class CarreraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "nota_minima_recomendada")
    private String notaMinimaRecomendada;

    @Column(name = "influencer_nombre")
    private String influencerNombre;

    @Column(name = "influencer_plataforma")
    private String influencerPlataforma = "YouTube";

    @Column(name = "influencer_url")
    private String influencerUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getNotaMinimaRecomendada() { return notaMinimaRecomendada; }
    public void setNotaMinimaRecomendada(String notaMinimaRecomendada) { this.notaMinimaRecomendada = notaMinimaRecomendada; }
    public String getInfluencerNombre() { return influencerNombre; }
    public void setInfluencerNombre(String influencerNombre) { this.influencerNombre = influencerNombre; }
    public String getInfluencerPlataforma() { return influencerPlataforma; }
    public void setInfluencerPlataforma(String influencerPlataforma) { this.influencerPlataforma = influencerPlataforma; }
    public String getInfluencerUrl() { return influencerUrl; }
    public void setInfluencerUrl(String influencerUrl) { this.influencerUrl = influencerUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

}
