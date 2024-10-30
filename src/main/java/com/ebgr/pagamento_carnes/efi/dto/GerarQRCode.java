package com.ebgr.pagamento_carnes.efi.dto;

public record GerarQRCode() {
    public record Request(){};
    public record Response(
            String qrcode,
            String imagemQrcode,
            String linkVisualizacao
    ){};
}
