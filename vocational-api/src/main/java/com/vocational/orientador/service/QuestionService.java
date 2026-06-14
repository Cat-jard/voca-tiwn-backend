package com.vocational.orientador.service;

import com.vocational.orientador.dto.QuestionResponseDTO;
import com.vocational.orientador.dto.OptionResponseDTO;
import com.vocational.orientador.entity.Option;
import com.vocational.orientador.entity.Question;
import com.vocational.orientador.repository.OptionRepository;
import com.vocational.orientador.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public List<QuestionResponseDTO> getAllQuestions() {

        return questionRepository
                .findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private QuestionResponseDTO mapToDTO(
            Question question
    ) {

        List<OptionResponseDTO> options =
                optionRepository
                        .findByQuestion_Id(question.getId())
                        .stream()
                        .map(this::mapOption)
                        .toList();

        return QuestionResponseDTO.builder()
                .id(question.getId())
                .questionOrder(question.getQuestionOrder())
                .questionText(question.getQuestionText())
                .options(options)
                .build();
    }

    private OptionResponseDTO mapOption(
            Option option
    ) {

        return OptionResponseDTO.builder()
                .id(option.getId())
                .optionLetter(option.getOptionLetter())
                .optionText(option.getOptionText())
                .build();
    }
}