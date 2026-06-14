package com.vocational.orientador.controller;


import com.vocational.orientador.entity.Axis;
import com.vocational.orientador.repository.AxisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/axes")
@RequiredArgsConstructor
public class AxisController {

    private final AxisRepository axisRepository;

    @GetMapping
    public List<Axis> findAll() {
        return axisRepository.findAll();
    }
}