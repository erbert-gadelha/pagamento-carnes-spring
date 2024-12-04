package com.ebgr.pagamento_carnes.efi.dto;

public record CriarWebhook() {
    public record Request(String webhookUrl) {}

}
