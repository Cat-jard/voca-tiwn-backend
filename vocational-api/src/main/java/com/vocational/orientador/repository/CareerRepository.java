package com.vocational.orientador.repository;

import com.vocational.orientador.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerRepository extends JpaRepository<Career, Long> {
}
