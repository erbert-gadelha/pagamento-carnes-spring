package com.ebgr.pagamento_carnes.efi.dto;

import static com.ebgr.pagamento_carnes.efi.dto.DTO_efi.*;

public record CobrancaImediata() {
    public record Request(
            CalendarioSemCriacao calendario,
            Devedor devedor,
            Valor valor,
            String chave,
            String solicitacaoPagador
    ) {
        public Request {
            if(solicitacaoPagador == null)
                solicitacaoPagador = "Fatura mensal.";
        }
    }
    public record Response(
            Calendario calendario,
            String txid,
            String revisao,
            String status,
            Valor valor,
            String chave,
            Devedor devedor,
            String solicitacaoPagador,
            Loc loc,
            String location,
            String pixCopiaECola
    ){};
}
