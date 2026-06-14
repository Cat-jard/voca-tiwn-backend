package com.vocational.orientador.repository;

import com.vocational.orientador.entity.CareerAxisProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CareerAxisProfileRepository
        extends JpaRepository<CareerAxisProfile, Long> {

    List<CareerAxisProfile> findByCareerId(Long careerId);
    List<CareerAxisProfile> findAll();

}