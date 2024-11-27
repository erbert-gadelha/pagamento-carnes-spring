package com.ebgr.pagamento_carnes.controller.dto;

import java.util.Date;

public record TokenDTO (
        String value,
        Date issuedAt,
        Date expiresAt,
        int maxAge
) {
}
