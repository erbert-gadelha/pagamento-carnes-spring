package com.ebgr.pagamento_carnes.controller.dto;

import java.time.ZonedDateTime;

public record WebhookDTO(
        Pix [] pix
) {

    public record Pix(
        String endToEndId,
        String txid,
        String chave,
        String valor,
        ZonedDateTime horario,
        String infoPagador,
        GnExtras gnExtras
    ) {}

    public record GnExtras (
            Pagador pagador,
            String tarifa
    ){}

    public record Pagador(
            String nome,
            String cnpj,
            String codigoBanco
    ){}

    public record Split(String id, int revisao){}
}
