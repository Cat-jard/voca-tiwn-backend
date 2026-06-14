package com.vocational.orientador.service;

import com.vocational.orientador.dto.*;
import com.vocational.orientador.entity.Career;
import com.vocational.orientador.entity.CareerAxisProfile;
import com.vocational.orientador.entity.OptionAxisScore;
import com.vocational.orientador.repository.CareerAxisProfileRepository;
import com.vocational.orientador.repository.OptionAxisScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final OptionAxisScoreRepository optionAxisScoreRepository;
    private final CareerAxisProfileRepository careerAxisProfileRepository;

    public List<CareerResultDTO> calculate(TestRequest request) {

        Map<Long, Integer> studentProfile = new HashMap<>();

        for (AnswerRequest answer : request.getAnswers()) {

            List<OptionAxisScore> scores =
                    optionAxisScoreRepository.findByOption_Id(answer.getOptionId());

            for (OptionAxisScore score : scores) {

                Long axisId = score.getAxis().getId();

                studentProfile.merge(
                        axisId,
                        score.getPoints(),
                        Integer::sum
                );
            }
        }

        Map<Long, Integer> careerScores = new HashMap<>();
        Map<Long, String> careerNames = new HashMap<>();

        List<CareerAxisProfile> profiles =
                careerAxisProfileRepository.findAll();

        for (CareerAxisProfile profile : profiles) {

            Long axisId = profile.getAxis().getId();

            Integer studentPoints =
                    studentProfile.getOrDefault(axisId, 0);

            Integer contribution =
                    studentPoints * profile.getWeight();

            Career career = profile.getCareer();

            careerScores.merge(
                    career.getId(),
                    contribution,
                    Integer::sum
            );

            careerNames.put(
                    career.getId(),
                    career.getName()
            );
        }

        return careerScores.entrySet()
                .stream()
                .sorted((a,b) -> b.getValue().compareTo(a.getValue()))
                .limit(3)
                .map(entry ->
                        new CareerResultDTO(
                                entry.getKey(),
                                careerNames.get(entry.getKey()),
                                entry.getValue()
                        )
                )
                .toList();
    }
}