package com.ebgr.pagamento_carnes.efi;

import com.ebgr.pagamento_carnes.efi.dto.CobrancaImediata;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import com.ebgr.pagamento_carnes.efi.dto.GerarQRCode;

public interface EfiHelper {

    public void exibirListaDeCobrancas();
    public CobrancaImediata.Response criarCobrancaImediata (DTO_efi.Devedor devedor, double valor);
    public GerarQRCode.Response criarQrCode (CobrancaImediata.Response cobrancaImediata);
}
