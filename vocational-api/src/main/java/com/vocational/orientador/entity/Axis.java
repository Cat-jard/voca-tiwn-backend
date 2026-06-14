package com.vocational.orientador.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "axes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Axis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    private String category;
}