package com.jornada.produtor.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jornada.produtor.dto.MensagemDTO;
import com.jornada.produtor.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class EnvioController {
    private final ProdutoService produtoService;

    @PostMapping
    public void enviarMensagem(@RequestBody MensagemDTO mensagem) throws JsonProcessingException {
        produtoService.enviarMensagemAoTopico(mensagem, mensagem.getTipoMensagem().ordinal());
    }
}
