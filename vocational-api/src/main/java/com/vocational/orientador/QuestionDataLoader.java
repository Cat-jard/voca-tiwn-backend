package com.vocational.orientador;

import com.vocational.orientador.entity.Option;
import com.vocational.orientador.entity.Question;
import com.vocational.orientador.repository.OptionRepository;
import com.vocational.orientador.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionDataLoader implements CommandLineRunner {

    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    @Override
    public void run(String... args) {

        if (questionRepository.count() > 0) {
            return;
        }

        loadQuestions();
    }

    private void loadQuestions() {

        createQuestion(
                1,
                "¿Qué área del conocimiento te genera más curiosidad genuina?",
                "Todo lo relacionado con física, energía, materiales y construcción",
                "La biología, química, el cuerpo humano y los seres vivos",
                "La programación, los datos, las redes y los sistemas digitales",
                "Las personas, la sociedad, el lenguaje y la cultura",
                "El diseño, la forma, el espacio y la comunicación visual",
                "La economía, los negocios, la estrategia y las organizaciones"
        );

        createQuestion(
                2,
                "¿Qué tipo de impacto quieres generar con tu trabajo?",
                "Mejorar la salud y salvar vidas directamente",
                "Construir infraestructura, tecnología o sistemas que duren décadas",
                "Transformar organizaciones, mercados o economías",
                "Defender derechos, hacer justicia o cambiar leyes",
                "Comunicar, inspirar o crear cultura visual",
                "Proteger el entorno, la seguridad o el medio ambiente"
        );

        createQuestion(
                3,
                "¿Qué materias del colegio se te daban mejor sin esforzarte mucho?",
                "Matemáticas y física",
                "Biología y química",
                "Computación e informática",
                "Lenguaje, literatura e historia",
                "Arte, dibujo y educación visual",
                "Economía, gestión o matemática financiera"
        );

        createQuestion(
                4,
                "¿En qué tipo de entorno de trabajo te imaginas más cómodo/a?",
                "En campo: obras, minas, sitios de construcción al aire libre",
                "En hospitales, clínicas o centros de atención médica",
                "En oficinas, empresas o entidades financieras",
                "En estudios de diseño, agencias creativas o medios de comunicación",
                "En laboratorios, plantas industriales o talleres técnicos",
                "En aulas, centros educativos o atendiendo personas directamente"
        );

        createQuestion(
                5,
                "¿Cómo describes tu forma natural de trabajar?",
                "Técnico/a y manual: me gusta usar equipos, máquinas y herramientas",
                "Analítico/a: me gustan los datos, los números y la lógica pura",
                "Con personas: escuchar, acompañar, enseñar o tratar a otros",
                "Creativo/a: diseñar, imaginar, construir desde cero algo visual",
                "Estratégico/a: planificar, organizar, tomar decisiones de largo plazo",
                "Normativo/a: aplicar reglas, protocolos, estándares y procedimientos"
        );

        createQuestion(
                6,
                "¿Cómo reaccionas ante situaciones de alta presión o emergencia?",
                "Me activo, pienso rápido y soy muy efectivo/a bajo presión extrema",
                "Funciono bien bajo presión técnica, aunque prefiero no arriesgar vidas",
                "Soy calmado/a: eso me ayuda a escuchar y decidir con claridad",
                "Prefiero entornos estables y predecibles para trabajar mejor",
                "Me adapto al nivel de presión que la situación requiera",
                "Prefiero evitar entornos de alto riesgo físico o vital"
        );

        createQuestion(
                7,
                "Si tuvieras que resolver un problema ahora mismo, ¿cuál elegirías?",
                "Un sistema mecánico o eléctrico que no funciona",
                "Un paciente o persona que necesita atención",
                "Un algoritmo, app o sistema de información con errores",
                "Un conflicto legal, social o comunicacional complejo",
                "Un espacio o producto que necesita mejor diseño visual",
                "Un negocio, presupuesto o estrategia con pérdidas"
        );

        createQuestion(
                8,
                "¿Cuál de estas frases describe mejor tu motivación principal?",
                "Quiero curar enfermedades y mejorar la calidad de vida humana",
                "Quiero construir cosas que duren: edificios, máquinas, redes",
                "Quiero entender y transformar la economía o los mercados",
                "Quiero proteger derechos, educar o cambiar la sociedad",
                "Quiero crear ideas, marcas, imágenes o experiencias que emocionen",
                "Quiero liderar organizaciones y generar impacto económico y empleo"
        );

        createQuestion(
                9,
                "¿Qué habilidad destacarían tus profesores o amigos en ti?",
                "Razonamiento lógico y matemático",
                "Creatividad e imaginación visual",
                "Empatía, escucha activa y apoyo emocional",
                "Organización, precisión y atención al detalle",
                "Liderazgo, comunicación y capacidad de convencer",
                "Curiosidad científica y pensamiento investigador"
        );

        createQuestion(
                10,
                "¿Cómo te relacionas con los números y el análisis cuantitativo?",
                "Me encantan: cálculo, estadística, simulaciones numéricas",
                "Los manejo bien como herramienta, no son mi pasión",
                "Los uso lo indispensable, prefiero el razonamiento cualitativo",
                "Prefiero trabajar con personas, relaciones e ideas abstractas",
                "Prefiero trabajar con formas, espacios y composiciones visuales",
                "Prefiero trabajar con organismos, reacciones o procesos naturales"
        );

        createQuestion(
                11,
                "¿Cómo prefieres trabajar la mayor parte del tiempo?",
                "Solo/a, con concentración profunda y autonomía",
                "En equipo técnico pequeño y especializado",
                "Con muchas personas: pacientes, alumnos, clientes, público",
                "Liderando equipos y tomando decisiones estratégicas",
                "En terreno, campo o entornos variados y no rutinarios",
                "Mezclando trabajo creativo individual con presentaciones y colaboración"
        );

        createQuestion(
                12,
                "¿Cuánta responsabilidad estás dispuesto/a a asumir?",
                "Máxima: decisiones críticas donde está en juego la vida de personas",
                "Alta: decisiones técnicas de gran impacto pero no de vida o muerte",
                "Media: contribuir como experto/a sin ser el único responsable",
                "Quiero ser el mejor experto/a de mi área sin gestionar equipos",
                "Quiero liderar organizaciones completas o crear mi empresa",
                "No lo tengo claro todavía, me adapto"
        );

        createQuestion(
                13,
                "¿Cuánto te importa el nivel de ingresos al elegir tu carrera?",
                "Es la prioridad principal: quiero uno de los salarios más altos",
                "Importa mucho, pero junto con un trabajo que me apasione",
                "Prefiero vocación sobre salario, aunque gane lo suficiente",
                "Busco equilibrio: buena remuneración sin sacrificar calidad de vida",
                "Priorizo la estabilidad y seguridad laboral sobre el salario",
                "Quiero crear mi propio negocio y generar riqueza propia"
        );

        createQuestion(
                14,
                "¿Cuántos años de estudio estás dispuesto/a a invertir?",
                "7 o más años: quiero la carrera más completa y de mayor prestigio",
                "5-6 años: una carrera universitaria completa es lo que busco",
                "4-5 años: eficiencia sin sacrificar calidad académica",
                "No importa si las oportunidades laborales son excelentes",
                "Quiero poder trabajar mientras estudio desde temprano",
                "Prefiero la carrera más corta que me dé salida laboral rápida"
        );

        createQuestion(
                15,
                "¿Dónde te visualizas trabajando en 10 años?",
                "En una empresa multinacional o corporación global",
                "En el sector público: hospital, colegio, municipio o estado",
                "Como consultor/a o profesional independiente con mis propios clientes",
                "En una startup o empresa de tecnología e innovación",
                "En el extranjero o con proyectos internacionales",
                "Fundando y dirigiendo mi propia empresa"
        );

        createQuestion(
                16,
                "¿Qué tipo de actividad extracurricular has disfrutado más?",
                "Robótica, electrónica, programación o ferias de ciencias",
                "Voluntariado en salud, trabajo social o tutorías académicas",
                "Debates, moot court, periodismo estudiantil u oratoria",
                "Arte, fotografía, diseño, teatro o medios audiovisuales",
                "Liderazgo estudiantil, deportes en equipo o emprendimientos",
                "Proyectos de negocio, ferias de emprendimiento o finanzas"
        );

        createQuestion(
                17,
                "Cuando algo importante falla, ¿cuál es tu reacción natural?",
                "Analizo metódicamente qué falló y corrijo con un plan claro",
                "Me frustro un momento, pero sigo con más energía que antes",
                "Consulto con mi equipo y buscamos la solución juntos",
                "Busco una solución alternativa creativa e innovadora",
                "Reviso normas, protocolos y estándares para identificar la causa",
                "Lo tomo con calma, reflexiono y vuelvo a intentarlo con paciencia"
        );

        createQuestion(
                18,
                "¿Qué sector te parece más relevante para el futuro del mundo?",
                "Tecnología: inteligencia artificial, ciberseguridad y software",
                "Salud y biotecnología: medicina personalizada, genómica",
                "Sostenibilidad: energías renovables, medio ambiente y minería responsable",
                "Economía digital: fintech, comercio global, criptomonedas",
                "Educación y desarrollo humano: psicología, pedagogía y bienestar",
                "Infraestructura e industria: manufactura avanzada, construcción, aviación"
        );

        createQuestion(
                19,
                "¿Cómo aprendes mejor las cosas nuevas?",
                "Practicando directamente: armando, tocando y experimentando",
                "Leyendo, investigando y elaborando mis propias conclusiones",
                "Programando, resolviendo ejercicios en pantalla o con simuladores",
                "Debatiendo, escuchando experiencias y aprendiendo de otros",
                "Dibujando, maquetando, visualizando o creando prototipos",
                "En laboratorio, con experimentos, muestras y análisis científico"
        );

        createQuestion(
                20,
                "¿Cuál de estos proyectos te gustaría liderar?",
                "Desarrollar un sistema de IA o aplicación que use millones de personas",
                "Diseñar y construir un aeropuerto, puente o planta de energía",
                "Crear una campaña que cambie la percepción de una marca o causa social",
                "Investigar y desarrollar un tratamiento o medicamento nuevo",
                "Fundar y escalar una empresa que genere cientos de empleos",
                "Diseñar un hospital, escuela, vivienda social o espacio público"
        );

        createQuestion(
                21,
                "¿Qué frase te describe mejor como persona?",
                "Soy muy metódico/a, me gustan las reglas claras y la precisión",
                "Soy curioso/a y siempre necesito entender el porqué de las cosas",
                "Soy empático/a y me importa mucho el bienestar de los demás",
                "Soy ambicioso/a y me motivan las metas grandes y los logros",
                "Soy creativo/a y me incomoda hacer siempre lo mismo",
                "Soy práctico/a: valoro los resultados concretos y tangibles"
        );

        createQuestion(
                22,
                "¿Qué tan cómodo/a te sientes con la tecnología digital?",
                "Soy apasionado/a: siempre al día con lo último en tech",
                "La domino bien para lo que necesito en mi trabajo",
                "La uso lo necesario, pero no es lo que más me atrae",
                "Me interesa cómo la tecnología transforma la medicina y la salud",
                "Me interesa cómo la tecnología mejora los negocios y mercados",
                "Me interesa cómo la tecnología transforma estructuras físicas y ciudades"
        );

        createQuestion(
                23,
                "¿Cuál de estos problemas del mundo actual te parece más urgente resolver?",
                "La crisis de salud mental y bienestar psicológico en jóvenes",
                "La brecha tecnológica y la exclusión digital en países en desarrollo",
                "El cambio climático y la destrucción de ecosistemas",
                "La corrupción, la injusticia y la desigualdad social",
                "La falta de infraestructura: hospitales, vivienda, carreteras",
                "La competitividad económica y la generación de empleo"
        );

        createQuestion(
                24,
                "¿Cuál de estas actividades realizarías en un día ideal de trabajo?",
                "Diseñar planos, modelos 3D o esquemas de construcción",
                "Atender pacientes, hacer diagnósticos o realizar procedimientos clínicos",
                "Escribir código, depurar algoritmos o diseñar arquitecturas de software",
                "Negociar contratos, presentar propuestas o analizar mercados globales",
                "Crear piezas gráficas, campañas visuales o conceptos de marca",
                "Supervisar procesos en planta, analizar fallas o gestionar la seguridad"
        );

        createQuestion(
                25,
                "Si pudieras ver un documental ahora mismo, ¿cuál elegirías?",
                "Cómo se construyen aviones, cohetes o infraestructura de gran escala",
                "Descubrimientos médicos, epidemias o el futuro de la biotecnología",
                "Grandes emprendedores, economías globales o el mundo de las finanzas",
                "Diseño, arquitectura, ciudades del futuro o grandes marcas",
                "Justicia social, psicología humana, educación o derechos humanos",
                "Inteligencia artificial, ciberseguridad o el futuro digital"
        );



    }


    private void createQuestion(
            int order,
            String text,
            String... options
    ) {
        Question question = questionRepository.save(
                Question.builder()
                        .questionOrder(order)
                        .questionText(text)
                        .build()
        );

        char letter = 'A';

        for (String optionText : options) {
            optionRepository.save(
                    Option.builder()
                            .question(question)
                            .optionLetter(String.valueOf(letter++))
                            .optionText(optionText)
                            .build()
            );
        }
    }
}