package com.jornada.produtor.dto;

import lombok.Data;

@Data
public class MensagemDTO {
    private TipoMensagem tipoMensagem;
    private String destinatario;
    private String assunto;
    private String mensagem;
}
