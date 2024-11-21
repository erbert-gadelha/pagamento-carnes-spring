package com.ebgr.pagamento_carnes.controller.dto;

public record PaymentsSummary(Info[] payments, int open, int closed) {

    public record Info( int key, String name, String closedAt, String pixUrl){};
}
