package com.consejero.vocacional.repository;

import com.consejero.vocacional.entity.CarreraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarreraRepository extends JpaRepository<CarreraEntity, Long> {

    Optional<CarreraEntity> findByNombreIgnoreCase(String nombre);

    List<CarreraEntity> findAllByOrderByCategoriaAscNombreAsc();

    List<CarreraEntity> findByCategoria(String categoria);

}
