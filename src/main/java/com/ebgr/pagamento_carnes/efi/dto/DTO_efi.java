package com.ebgr.pagamento_carnes.efi.dto;

public record DTO_efi() {
    public record Calendario (String criacao, int expiracao) {}
    public record CalendarioSemCriacao (int expiracao) {}
    public record Devedor (String cpf, String nome){}
    public record Valor(String original) {
        public Valor(double value) {
            this(String.format("%.2f", value));
        }
    }
    public record Loc (
            int id,
            String location,
            String tipoCob,
            String criacao
    ) {};

}
