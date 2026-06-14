package com.vocational.orientador.repository;

import com.vocational.orientador.entity.OptionAxisScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionAxisScoreRepository
        extends JpaRepository<OptionAxisScore, Long> {
    List<OptionAxisScore> findByOption_Id(Long optionId);
}