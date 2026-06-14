package com.vocational.orientador.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionResponseDTO {

    private Long id;
    private String optionLetter;
    private String optionText;
}