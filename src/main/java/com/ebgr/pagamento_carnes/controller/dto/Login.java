package com.ebgr.pagamento_carnes.controller.dto;

public record Login() {
    public record Request(String login, String password){};
}
