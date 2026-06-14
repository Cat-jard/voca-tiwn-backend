package com.consejero.vocacional.controller;

import com.consejero.vocacional.service.ConsejeroService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ConsejeroService consejeroService;

    public ChatController(ConsejeroService consejeroService) {
        this.consejeroService = consejeroService;
    }

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> conversarStreaming(@RequestBody ChatRequest request) {
        return consejeroService.conversarStreaming(request.mensaje(), request.conversationId());
    }

    @PostMapping(value = "/stream-with-file", produces = MediaType.TEXT_EVENT_STREAM_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Flux<String> conversarConArchivo(
            @RequestParam("mensaje") String mensaje,
            @RequestParam("conversationId") String conversationId,
            @RequestParam("archivo") MultipartFile archivo) {
        return consejeroService.conversarConArchivo(mensaje, conversationId, archivo);
    }

    @GetMapping("/influencer/{nombre}")
    public String obtenerInfluencer(@PathVariable String nombre) {
        return consejeroService.obtenerInfluencer(nombre);
    }

    record ChatRequest(String mensaje, String conversationId) {}

}
