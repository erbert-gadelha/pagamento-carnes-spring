package com.ebgr.pagamento_carnes.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PaymentMonthDTO(
        int month,
        int year,
        LocalDateTime expiresAt,
        LocalDateTime closedAt,
        String pixUrl
) {
}
