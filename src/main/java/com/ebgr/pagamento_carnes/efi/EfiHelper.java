package com.ebgr.pagamento_carnes.efi;

import com.ebgr.pagamento_carnes.efi.dto.CobrancaImediata;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import com.ebgr.pagamento_carnes.efi.dto.GerarQRCode;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;

public interface EfiHelper {

    public void imprimirWebhooks();
    public void exibirListaDeCobrancas();
    public CobrancaImediata.Response criarCobrancaImediata (DTO_efi.Devedor devedor);
    public CobrancaImediata.Response criarCobrancaImediata (DTO_efi.Devedor devedor, double valor);
    public GerarQRCode.Response criarQrCode (CobrancaImediata.Response cobrancaImediata);
    public void criarWebhook(CobrancaImediata.Response cobrancaImediata);
    /// Verificar wasd
    public LocalDateTime verificarCobranca(String txid);

    }
