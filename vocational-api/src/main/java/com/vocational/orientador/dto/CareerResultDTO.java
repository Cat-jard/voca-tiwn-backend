package com.vocational.orientador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CareerResultDTO {

    private Long careerId;
    private String careerName;
    private Integer score;

}