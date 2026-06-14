package com.vocational.orientador.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponseDTO {

    private Long id;
    private Integer questionOrder;
    private String questionText;
    private List<OptionResponseDTO> options;
}