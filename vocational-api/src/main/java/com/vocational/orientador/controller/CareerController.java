package com.vocational.orientador.controller;

import com.vocational.orientador.entity.Career;
import com.vocational.orientador.repository.CareerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/careers")
@RequiredArgsConstructor
public class CareerController {

    private final CareerRepository careerRepository;

    @GetMapping
    public List<Career> findAll() {
        return careerRepository.findAll();
    }
}
