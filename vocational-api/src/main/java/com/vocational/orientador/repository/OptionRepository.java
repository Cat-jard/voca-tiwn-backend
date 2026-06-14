package com.vocational.orientador.repository;

import com.vocational.orientador.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OptionRepository
        extends JpaRepository<Option, Long> {

    List<Option> findByQuestion_Id(Long questionId);

    Optional<Option>
    findByQuestionQuestionOrderAndOptionLetter(
            Integer questionOrder,
            String optionLetter
    );
}
