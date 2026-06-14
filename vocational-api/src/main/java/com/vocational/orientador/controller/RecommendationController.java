package com.vocational.orientador.controller;

import com.vocational.orientador.dto.CareerResultDTO;
import com.vocational.orientador.dto.TestRequest;
import com.vocational.orientador.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping
    public List<CareerResultDTO> calculate(
            @RequestBody TestRequest request) {

        return recommendationService.calculate(request);
    }
}