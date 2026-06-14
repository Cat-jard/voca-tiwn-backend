package com.vocational.orientador.repository;

import com.vocational.orientador.entity.Axis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AxisRepository
        extends JpaRepository<Axis, Long> {

    Optional<Axis> findByCode(String code);
}