package com.consejero.vocacional.repository;

import com.consejero.vocacional.entity.BeneficioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BeneficioRepository extends JpaRepository<BeneficioEntity, Long> {

    Optional<BeneficioEntity> findByNombreIgnoreCase(String nombre);

}
