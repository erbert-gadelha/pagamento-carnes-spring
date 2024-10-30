package com.ebgr.pagamento_carnes.efi;

import com.ebgr.pagamento_carnes.efi.dto.CobrancaImediata;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import com.ebgr.pagamento_carnes.efi.dto.GerarQRCode;

public interface EfiHelper {

    /*public String getClient_id();
    public void setClient_id(String client_id);
    public String getClient_secret();
    public void setClient_secret(String client_secret);
    public String getCertificate();
    public void setCertificate(String certificate);
    public void setSandbox(boolean sandbox);
    public String getUrl();
    public void setUrl(String url);
    public String getAcessToken();
    public void setAcessToken(String acessToken);*/

    public void consultarListaDeCobrancas ();
    public CobrancaImediata.Response criarCobrancaImediata (DTO_efi.Devedor devedor, double valor, int expiraEm);
    public GerarQRCode.Response criarQrCode (CobrancaImediata.Response cobrancaImediata);
}
