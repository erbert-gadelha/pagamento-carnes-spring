package com.ebgr.pagamento_carnes.controller.dto;

import java.util.Date;

public record TokenDTO (
        String token,
        Date issuedAt,
        Date expiresAt,
        int maxAge
) {
}
