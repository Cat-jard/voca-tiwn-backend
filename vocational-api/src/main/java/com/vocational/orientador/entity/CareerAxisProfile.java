package com.vocational.orientador.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "career_axis_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerAxisProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id")
    private Career career;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "axis_id")
    private Axis axis;

    @Column(nullable = false)
    private Integer weight;
}