package com.vocational.orientador.controller;

import com.vocational.orientador.dto.QuestionResponseDTO;
import com.vocational.orientador.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@CrossOrigin("*")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public List<QuestionResponseDTO> getQuestions() {
        return questionService.getAllQuestions();
    }
}
