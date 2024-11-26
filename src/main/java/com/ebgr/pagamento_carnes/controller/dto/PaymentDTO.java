package com.ebgr.pagamento_carnes.controller.dto;

import java.time.LocalDateTime;

public record PaymentDTO(
        String copiaECola,
        LocalDateTime expiresAt
) {
}
