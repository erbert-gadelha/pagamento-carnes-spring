package com.ebgr.pagamento_carnes.controller.dto;

public record UserDTO(
        Integer id,
        String login,
        String name,
        String password) {
}
