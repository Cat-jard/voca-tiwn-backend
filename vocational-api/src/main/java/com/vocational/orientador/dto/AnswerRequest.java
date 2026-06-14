package com.vocational.orientador.dto;

import lombok.Data;

@Data
public class AnswerRequest {

    private Long questionId;
    private Long optionId;

}