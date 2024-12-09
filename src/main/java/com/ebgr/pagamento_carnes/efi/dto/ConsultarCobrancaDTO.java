package com.ebgr.pagamento_carnes.efi.dto;

import java.time.ZonedDateTime;

public record ConsultarCobrancaDTO (
        String status,
        Calendario calendario,
        String location,
        String txid,
        int revisao,
        Devedor devedor,
        Valor valor,
        String chave,
        String solicitacaoPagador,
        Pix pix,
        String pixCopiaECola
) {
    public record Pix(
      String endToEndId,
      String txid,
      String valor,
      String horario,
      String infoPagador,
      Devolucoes [] devolucoes) {
        public record Devolucoes(
                String id,
                String rtrId,
                String valor,
                Horario horario,
                String status
        ) {
            public record Horario(String solicitacao){}
        }
    }

    public record Calendario(
            ZonedDateTime criacao,
            String expiracao
    ) {}

    public record Devedor(
            String cnpj,
            String nome
    ) {}

    public record Valor(
            String original
    ) {}
}