package com.vocational.orientador.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "option_axis_scores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionAxisScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "axis_id")
    private Axis axis;

    private Integer points;
}