package com.vocational.orientador;


import com.vocational.orientador.entity.Axis;
import com.vocational.orientador.entity.Option;
import com.vocational.orientador.entity.OptionAxisScore;
import com.vocational.orientador.repository.AxisRepository;
import com.vocational.orientador.repository.OptionAxisScoreRepository;
import com.vocational.orientador.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptionAxisScoreDataLoader
        implements CommandLineRunner {

    private final OptionAxisScoreRepository repository;
    private final OptionRepository optionRepository;
    private final AxisRepository axisRepository;

    @Override
    public void run(String... args) {

        if (repository.count() > 0) {
            return;
        }

        loadQuestion1();
        loadQuestion2();
        loadQuestion3();
        loadQuestion4();
        loadQuestion5();
        loadQuestion6();
        loadQuestion7();
        loadQuestion8();
        loadQuestion9();
        loadQuestion10();
        loadQuestion11();
        loadQuestion12();
        loadQuestion13();
        loadQuestion14();
        loadQuestion15();
        loadQuestion16();
        loadQuestion17();
        loadQuestion18();
        loadQuestion19();
        loadQuestion20();
        loadQuestion21();
        loadQuestion22();
        loadQuestion23();
        loadQuestion24();
        loadQuestion25();
    }

    private void save(
            int question,
            String letter,
            String axisCode,
            int points
    ) {

        Option option =
                optionRepository
                        .findByQuestionQuestionOrderAndOptionLetter(
                                question,
                                letter
                        )
                        .orElseThrow();

        Axis axis =
                axisRepository
                        .findByCode(axisCode)
                        .orElseThrow();

        repository.save(
                OptionAxisScore.builder()
                        .option(option)
                        .axis(axis)
                        .points(points)
                        .build()
        );
    }

    private void loadQuestion1() {
        save(1, "A", "FM", 3);
        save(1, "B", "BQ", 3);
        save(1, "C", "LD", 3);
        save(1, "D", "SH", 3);
        save(1, "E", "EE", 3);
        save(1, "F", "EO", 3);
    }

    private void loadQuestion2() {
        save(2, "A", "PER", 3);
        save(2, "B", "SIS", 3);
        save(2, "C", "ORG", 3);
        save(2, "D", "SOC", 3);
        save(2, "E", "OBJ", 3);
        save(2, "F", "NAT", 3);
    }

    private void loadQuestion3() {
        save(3, "A", "FM", 3);
        save(3, "B", "BQ", 3);
        save(3, "C", "LD", 3);
        save(3, "D", "SH", 3);
        save(3, "E", "EE", 3);
        save(3, "F", "EO", 3);
    }

    private void loadQuestion4() {

        save(4, "A", "CAM", 3);
        save(4, "A", "OBJ", 1);

        save(4, "B", "PERS", 3);
        save(4, "B", "PER", 1);

        save(4, "C", "ORG", 3);
        save(4, "C", "EST", 1);

        save(4, "D", "CRE", 3);
        save(4, "D", "SOC", 1);

        save(4, "E", "TEC", 3);
        save(4, "E", "ANA", 1);

        save(4, "F", "PERS", 3);
        save(4, "F", "PER", 2);
    }

    private void loadQuestion5() {

        save(5, "A", "TEC", 3);
        save(5, "B", "ANA", 3);
        save(5, "C", "PERS", 3);
        save(5, "D", "CRE", 3);
        save(5, "E", "EST", 3);
        save(5, "F", "NOR", 3);
    }

    private void loadQuestion6() {

        save(6, "A", "MA", 3);

        save(6, "B", "AL", 3);

        save(6, "C", "ME", 3);
        save(6, "C", "PERS", 1);

        save(6, "D", "BM", 3);

        save(6, "E", "ME", 2);
        save(6, "E", "AL", 1);

        save(6, "F", "BM", 3);
    }

    private void loadQuestion7() {

        save(7, "A", "FM", 2);
        save(7, "A", "SIS", 2);

        save(7, "B", "BQ", 2);
        save(7, "B", "PER", 2);

        save(7, "C", "LD", 3);

        save(7, "D", "SH", 2);
        save(7, "D", "SOC", 2);

        save(7, "E", "EE", 2);
        save(7, "E", "OBJ", 2);

        save(7, "F", "EO", 2);
        save(7, "F", "ORG", 2);
    }

    private void loadQuestion8() {

        save(8, "A", "PER", 3);

        save(8, "B", "OBJ", 3);

        save(8, "C", "ORG", 3);

        save(8, "D", "SOC", 3);

        save(8, "E", "CRE", 2);
        save(8, "E", "SOC", 1);

        save(8, "F", "EST", 2);
        save(8, "F", "ORG", 1);
    }

    private void loadQuestion9() {

        save(9, "A", "ANA", 3);

        save(9, "B", "CRE", 3);

        save(9, "C", "PERS", 3);

        save(9, "D", "NOR", 3);

        save(9, "E", "EST", 3);

        save(9, "F", "BQ", 2);
        save(9, "F", "ANA", 1);
    }

    private void loadQuestion10() {

        save(10, "A", "ANA", 3);
        save(10, "A", "FM", 1);

        save(10, "B", "ANA", 2);

        save(10, "C", "SH", 1);

        save(10, "D", "PERS", 2);

        save(10, "E", "EE", 3);

        save(10, "F", "BQ", 3);
    }

    private void loadQuestion11() {

        save(11, "A", "ANA", 3);

        save(11, "B", "ANA", 2);
        save(11, "B", "TEC", 1);

        save(11, "C", "PERS", 3);

        save(11, "D", "EST", 3);

        save(11, "E", "CAM", 3);

        save(11, "F", "CRE", 2);
        save(11, "F", "PERS", 1);
    }

    private void loadQuestion12() {

        save(12, "A", "MA", 3);

        save(12, "B", "AL", 3);

        save(12, "C", "ME", 3);

        save(12, "D", "ANA", 2);

        save(12, "E", "EST", 3);

        save(12, "F", "ME", 1);
    }

    private void loadQuestion13() {

        save(13, "A", "EST", 2);
        save(13, "A", "ORG", 1);

        save(13, "B", "EST", 1);

        save(13, "C", "PER", 1);

        save(13, "D", "ME", 1);

        save(13, "E", "BM", 1);

        save(13, "F", "EST", 2);
        save(13, "F", "ORG", 2);
    }

    private void loadQuestion14() {

        save(14, "A", "MA", 1);
        save(14, "A", "BQ", 1);

        save(14, "B", "AL", 1);

        save(14, "C", "ME", 1);

        save(14, "D", "EST", 1);

        save(14, "E", "ORG", 1);

        save(14, "F", "BM", 1);
    }

    private void loadQuestion15() {

        save(15, "A", "ORG", 2);

        save(15, "B", "PER", 2);

        save(15, "C", "EST", 2);

        save(15, "D", "LD", 2);

        save(15, "E", "SOC", 2);

        save(15, "F", "EST", 3);
    }

    private void loadQuestion16() {

        save(16, "A", "LD", 2);
        save(16, "A", "TEC", 2);

        save(16, "B", "PER", 2);
        save(16, "B", "PERS", 2);

        save(16, "C", "SOC", 2);
        save(16, "C", "NOR", 2);

        save(16, "D", "CRE", 2);
        save(16, "D", "EE", 2);

        save(16, "E", "EST", 2);
        save(16, "E", "ORG", 2);

        save(16, "F", "EO", 2);
        save(16, "F", "ORG", 2);
    }

    private void loadQuestion17() {

        save(17, "A", "ANA", 3);

        save(17, "B", "MA", 1);

        save(17, "C", "PERS", 2);

        save(17, "D", "CRE", 2);

        save(17, "E", "NOR", 2);

        save(17, "F", "ME", 1);
    }

    private void loadQuestion18() {

        save(18, "A", "LD", 3);

        save(18, "B", "BQ", 3);

        save(18, "C", "NAT", 3);

        save(18, "D", "EO", 3);

        save(18, "E", "PER", 2);
        save(18, "E", "SH", 1);

        save(18, "F", "FM", 2);
        save(18, "F", "OBJ", 1);
    }

    private void loadQuestion19() {

        save(19, "A", "TEC", 3);

        save(19, "B", "ANA", 2);

        save(19, "C", "LD", 3);

        save(19, "D", "PERS", 2);

        save(19, "E", "CRE", 2);
        save(19, "E", "EE", 1);

        save(19, "F", "BQ", 2);
    }

    private void loadQuestion20() {

        save(20, "A", "LD", 3);

        save(20, "B", "OBJ", 3);

        save(20, "C", "CRE", 2);
        save(20, "C", "SOC", 1);

        save(20, "D", "BQ", 3);

        save(20, "E", "EST", 2);
        save(20, "E", "ORG", 2);

        save(20, "F", "EE", 2);
        save(20, "F", "PER", 1);
    }

    private void loadQuestion21() {

        save(21, "A", "NOR", 3);

        save(21, "B", "ANA", 2);
        save(21, "B", "BQ", 1);

        save(21, "C", "PERS", 2);
        save(21, "C", "PER", 1);

        save(21, "D", "EST", 3);

        save(21, "E", "CRE", 3);

        save(21, "F", "TEC", 2);
        save(21, "F", "OBJ", 1);
    }

    private void loadQuestion22() {

        save(22, "A", "LD", 3);

        save(22, "B", "LD", 1);

        save(22, "C", "BM", 1);

        save(22, "D", "BQ", 2);
        save(22, "D", "LD", 1);

        save(22, "E", "EO", 2);
        save(22, "E", "LD", 1);

        save(22, "F", "FM", 2);
        save(22, "F", "LD", 1);
    }

    private void loadQuestion23() {

        save(23, "A", "PER", 2);
        save(23, "A", "SH", 1);

        save(23, "B", "LD", 3);

        save(23, "C", "NAT", 3);

        save(23, "D", "SOC", 3);

        save(23, "E", "OBJ", 2);
        save(23, "E", "FM", 1);

        save(23, "F", "ORG", 2);
        save(23, "F", "EO", 1);
    }

    private void loadQuestion24() {

        save(24, "A", "EE", 2);
        save(24, "A", "OBJ", 1);

        save(24, "B", "PER", 2);
        save(24, "B", "BQ", 1);

        save(24, "C", "LD", 3);

        save(24, "D", "ORG", 2);
        save(24, "D", "EST", 1);

        save(24, "E", "CRE", 3);

        save(24, "F", "NOR", 2);
        save(24, "F", "TEC", 1);
    }

    private void loadQuestion25() {

        save(25, "A", "FM", 3);

        save(25, "B", "BQ", 3);

        save(25, "C", "EO", 3);

        save(25, "D", "EE", 3);

        save(25, "E", "SH", 3);

        save(25, "F", "LD", 3);
    }





}