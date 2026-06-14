package com.consejero.vocacional.service;

import com.consejero.vocacional.dto.DatosConstancia;
import com.consejero.vocacional.dto.ResultadoAnalisis;
import com.consejero.vocacional.entity.BeneficioEntity;
import com.consejero.vocacional.entity.CarreraEntity;
import com.consejero.vocacional.repository.BeneficioRepository;
import com.consejero.vocacional.repository.CarreraRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConsejeroService {

    private final ChatClient chatClient;
    private final CarreraRepository carreraRepository;
    private final BeneficioRepository beneficioRepository;
    private final ProcesadorConstanciaService procesadorConstanciaService;
    private final AnalisisNotasService analisisNotasService;

    public ConsejeroService(ChatClient.Builder chatClientBuilder,
                            CarreraRepository carreraRepository,
                            BeneficioRepository beneficioRepository,
                            ProcesadorConstanciaService procesadorConstanciaService,
                            AnalisisNotasService analisisNotasService) {
        this.carreraRepository = carreraRepository;
        this.beneficioRepository = beneficioRepository;
        this.procesadorConstanciaService = procesadorConstanciaService;
        this.analisisNotasService = analisisNotasService;

        MessageChatMemoryAdvisor advisor = MessageChatMemoryAdvisor.builder(
                        MessageWindowChatMemory.builder().maxMessages(100).build())
                .build();

        this.chatClient = chatClientBuilder
                .defaultSystem(new ClassPathResource("prompts/sistema-prompt.txt"))
                .defaultAdvisors(advisor)
                .build();
    }

    public Flux<String> conversarStreaming(String mensaje, String conversationId) {
        String msg = mensaje.toLowerCase().trim();

        Optional<CarreraEntity> carrera = buscarCarreraPorNombre(msg);
        if (carrera.isPresent()) {
            return Flux.just(formatearRespuestaCarrera(carrera.get()));
        }

        Optional<List<CarreraEntity>> porCategoria = buscarCarrerasPorCategoria(msg);
        if (porCategoria.isPresent()) {
            return Flux.just(formatearListaPorCategoria(porCategoria.get()));
        }

        if (msg.contains("beneficio") || msg.contains("ingreso") || msg.contains("admisión")) {
            return Flux.just(listarBeneficios());
        }

        if (msg.contains("requisito") || msg.contains("nota") || msg.contains("promedio")) {
            return Flux.just(explicarRequisitos());
        }

        return this.chatClient.prompt()
                .user(mensaje)
                .advisors(a -> a.param("chat_memory_conversation_id", conversationId))
                .stream()
                .content();
    }

    public Flux<String> conversarConArchivo(String mensaje, String conversationId, MultipartFile archivo) {
        try {
            DatosConstancia datos = procesadorConstanciaService.procesar(archivo);
            ResultadoAnalisis resultado = analisisNotasService.analizar(datos);

            String respuesta = formatearResultadoAnalisis(resultado);
            return Flux.just(respuesta);

        } catch (Exception e) {
            return Flux.just("❌ No pude procesar el archivo. Asegúrate de que sea una constancia de logros en formato PDF válido.\n\nError: " + e.getMessage());
        }
    }

    private String formatearResultadoAnalisis(ResultadoAnalisis r) {
        StringBuilder sb = new StringBuilder();
        sb.append("📄 ANÁLISIS DE CONSTANCIA\n");
        sb.append("\n");
        sb.append("👤 Estudiante: ").append(r.getNombreEstudiante() != null ? r.getNombreEstudiante() : "No identificado").append("\n");
        sb.append("📊 Promedio 4to: ").append(r.getPromedio4to()).append("/20 | ");
        sb.append("Promedio 5to: ").append(r.getPromedio5to()).append("/20 | ");
        sb.append("General: ").append(r.getPromedioGeneral()).append("/20\n");
        sb.append("\n");
        sb.append("🏆 Beneficio aplicable: ").append(r.getBeneficioAplicable()).append("\n");
        sb.append("🎯 Modalidad de ingreso: ").append(r.getModalidadIngreso()).append("\n");
        sb.append("\n");

        if (r.getBeneficios() != null && !r.getBeneficios().isEmpty()) {
            sb.append("📋 Beneficios:\n");
            for (String b : r.getBeneficios()) {
                sb.append("  ✅ ").append(b).append("\n");
            }
            sb.append("\n");
        }

        if (r.getRequisitos() != null && !r.getRequisitos().isEmpty()) {
            sb.append("📄 Requisitos:\n");
            for (String req : r.getRequisitos()) {
                sb.append("  • ").append(req).append("\n");
            }
            sb.append("\n");
        }

        if (!r.getAreasMejora().isEmpty()) {
            sb.append("📚 Áreas de mejora sugeridas:\n");
            r.getAreasMejora().forEach((curso, nota) ->
                    sb.append("  • ").append(curso).append(" → ").append(nota).append("/20\n"));
            sb.append("\n");
            sb.append("💡 Puedes repasar esos cursos con los recursos que tengo.\n");
            sb.append("\n");
        }

        sb.append("---\n");
        sb.append("💡 ¿Quieres saber qué carreras puedes postular según tu perfil?");
        return sb.toString();
    }

    public String obtenerInfluencer(String nombre) {
        Optional<CarreraEntity> carrera = carreraRepository.findByNombreIgnoreCase(nombre);
        if (carrera.isEmpty()) {
            return "❌ No encontré información de la carrera \"" + nombre + "\". ¿Puedes verificar el nombre?";
        }
        CarreraEntity c = carrera.get();
        if (c.getInfluencerNombre() == null || c.getInfluencerUrl() == null) {
            return "📚 Aún no tengo contenido recomendado para " + c.getNombre() + ". ¿Te gustaría conocer más sobre otra carrera?";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("👨‍🏫 ").append(c.getInfluencerNombre()).append("\n");
        sb.append("🔗 ").append(c.getInfluencerUrl()).append("\n");
        sb.append("\n");
        sb.append("✨ Este creador de contenido comparte información valiosa sobre ").append(c.getNombre()).append(".\n");
        sb.append("Recuerda que estos recursos son complementarios; lo más importante es tu dedicación y esfuerzo.");
        return sb.toString();
    }

    private Optional<CarreraEntity> buscarCarreraPorNombre(String mensaje) {
        List<CarreraEntity> todas = carreraRepository.findAllByOrderByCategoriaAscNombreAsc();
        for (CarreraEntity c : todas) {
            if (mensaje.contains(c.getNombre().toLowerCase())) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    private Optional<List<CarreraEntity>> buscarCarrerasPorCategoria(String mensaje) {
        String[] categorias = {"ingeniería", "negocios", "derecho", "educación", "psicología",
                "comunicaciones", "arquitectura", "salud", "medicina"};
        for (String cat : categorias) {
            if (mensaje.contains(cat)) {
                String catDb = Character.toUpperCase(cat.charAt(0)) + cat.substring(1);
                return Optional.of(carreraRepository.findByCategoria(catDb));
            }
        }
        return Optional.empty();
    }

    private String formatearRespuestaCarrera(CarreraEntity c) {
        StringBuilder sb = new StringBuilder();
        sb.append("🎓 ").append(c.getNombre()).append("\n");
        sb.append("\n");
        sb.append("📖 ").append(c.getDescripcion()).append("\n");
        sb.append("\n");

        if (c.getNotaMinimaRecomendada() != null) {
            sb.append("📊 Nota mínima recomendada: ").append(c.getNotaMinimaRecomendada()).append("/20\n");
        }
        sb.append("🏷️ Categoría: ").append(c.getCategoria()).append("\n");
        sb.append("\n");

        if (c.getInfluencerNombre() != null) {
            sb.append("📺 Contenido recomendado:\n");
            sb.append("👨‍🏫 ").append(c.getInfluencerNombre()).append("\n");
            sb.append("🔗 ").append(c.getInfluencerUrl()).append("\n");
            sb.append("\n");
        }

        sb.append("💡 ¿Quieres saber más sobre requisitos, beneficios o comparar con otra carrera?");
        return sb.toString();
    }

    private String formatearListaPorCategoria(List<CarreraEntity> carreras) {
        if (carreras.isEmpty()) return "No encontré carreras en esa categoría.";
        String cat = carreras.get(0).getCategoria();
        StringBuilder sb = new StringBuilder();
        sb.append("📂 ").append(cat).append(" (").append(carreras.size()).append(" carreras)\n");
        sb.append("\n");
        for (CarreraEntity c : carreras) {
            sb.append("• ").append(c.getNombre());
            if (c.getNotaMinimaRecomendada() != null) {
                sb.append(" (nota min: ").append(c.getNotaMinimaRecomendada()).append("/20)");
            }
            sb.append("\n");
        }
        sb.append("\n");
        sb.append("💡 ¿Te interesa alguna? Escríbeme el nombre y te cuento más detalles.");
        return sb.toString();
    }

    private String listarBeneficios() {
        List<BeneficioEntity> beneficios = beneficioRepository.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("🏆 Beneficios de Ingreso\n");
        sb.append("\n");

        for (BeneficioEntity b : beneficios) {
            sb.append(b.getNombre()).append("\n");
            sb.append("📝 ").append(b.getDescripcion()).append("\n");

            Map<String, Object> cond = b.getCondicionesMap();
            if (!cond.isEmpty()) {
                sb.append("📋 Condiciones:\n");
                cond.forEach((key, value) -> sb.append("  • ").append(key).append(": ").append(value).append("\n"));
            }

            List<String> reqs = b.getRequisitosList();
            if (!reqs.isEmpty()) {
                sb.append("📄 Requisitos:\n");
                reqs.forEach(r -> sb.append("  • ").append(r).append("\n"));
            }
            sb.append("\n");
        }

        sb.append("💡 ¿Quieres saber si cumples con algún beneficio? Puedes subir tu constancia de notas (PDF) y la analizo.");
        return sb.toString();
    }

    private String explicarRequisitos() {
        List<CarreraEntity> carreras = carreraRepository.findAllByOrderByCategoriaAscNombreAsc();
        StringBuilder sb = new StringBuilder();
        sb.append("📊 Notas mínimas recomendadas por carrera\n");
        sb.append("\n");

        Map<String, List<CarreraEntity>> agrupadas = carreras.stream()
                .collect(Collectors.groupingBy(CarreraEntity::getCategoria));

        agrupadas.forEach((cat, lista) -> {
            sb.append("📂 ").append(cat).append("\n");
            for (CarreraEntity c : lista) {
                sb.append("  • ").append(c.getNombre());
                sb.append(" → ").append(c.getNotaMinimaRecomendada() != null ?
                        c.getNotaMinimaRecomendada() + "/20" : "Consultar");
                sb.append("\n");
            }
            sb.append("\n");
        });

        sb.append("💡 ¿Te interesa alguna carrera en específico para saber si cumples?");
        return sb.toString();
    }

}