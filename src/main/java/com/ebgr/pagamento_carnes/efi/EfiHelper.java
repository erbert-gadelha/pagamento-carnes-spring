package com.ebgr.pagamento_carnes.efi;

import com.ebgr.pagamento_carnes.efi.dto.CobrancaImediata;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import com.ebgr.pagamento_carnes.efi.dto.GerarQRCode;

public interface EfiHelper {

    public void exibirListaDeCobrancas();
    public CobrancaImediata.Response criarCobrancaImediata (DTO_efi.Devedor devedor);
    public CobrancaImediata.Response criarCobrancaImediata (DTO_efi.Devedor devedor, double valor, String txid);
    public GerarQRCode.Response criarQrCode (CobrancaImediata.Response cobrancaImediata);
    public void criarWebhook(String txid);
}
