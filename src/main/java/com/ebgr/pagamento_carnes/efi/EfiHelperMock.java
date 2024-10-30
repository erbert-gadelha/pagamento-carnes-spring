package com.ebgr.pagamento_carnes.efi;

import com.ebgr.pagamento_carnes.efi.dto.CobrancaImediata;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import com.ebgr.pagamento_carnes.efi.dto.GerarQRCode;

public class EfiHelperMock implements EfiHelper {
    @Override
    public void consultarListaDeCobrancas() {
        System.out.println("""
                {
                    "parametros": {
                        "inicio": "2020-10-22T16:01:35.000Z",
                        "fim": "2025-11-30T20:10:00.000Z",
                        "paginacao": {
                            "paginaAtual": 0,
                            "itensPorPagina": 100,
                            "quantidadeDePaginas": 1,
                            "quantidadeTotalDeItens": 13
                        }
                    },
                    "cobs": [
                        {
                            "calendario": {
                                "criacao": "2024-10-22T16:09:16.000Z",
                                "expiracao": 3600
                            },
                            "txid": "919ba6d042a74fc18f44e1e7118af81a",
                            "revisao": 0,
                            "status": "ATIVA",
                            "valor": {
                                "original": "0.25"
                            },
                            "chave": "48c34d56-f0f7-44e8-bf0d-07e242ef98e7",
                            "devedor": {
                                "cpf": "70292933479",
                                "nome": "Erbert Gadelha"
                            },
                            "solicitacaoPagador": "Fatura mensal.",
                            "loc": {
                                "id": 13,
                                "location": "qrcodespix.sejaefi.com.br/v2/3e9d97fa15354b66b13ca72f880169c6",
                                "tipoCob": "cob",
                                "criacao": "2024-10-22T16:09:16.000Z"
                            },
                            "location": "qrcodespix.sejaefi.com.br/v2/3e9d97fa15354b66b13ca72f880169c6",
                            "pixCopiaECola": "00020101021226830014BR.GOV.BCB.PIX2561qrcodespix.sejaefi.com.br/v2/3e9d97fa15354b66b13ca72f880169c65204000053039865802BR5905EFISA6008SAOPAULO62070503***63048813"
                        },
                        {
                            "calendario": {
                                "criacao": "2024-10-22T16:03:18.000Z",
                                "expiracao": 3600
                            },
                            "txid": "0a307dacacc54e268e99582de45e11ba",
                            "revisao": 0,
                            "status": "ATIVA",
                            "valor": {
                                "original": "0.25"
                            },
                            "chave": "48c34d56-f0f7-44e8-bf0d-07e242ef98e7",
                            "devedor": {
                                "cpf": "70292933479",
                                "nome": "Erbert Gadelha"
                            },
                            "solicitacaoPagador": "Fatura mensal.",
                            "loc": {
                                "id": 12,
                                "location": "qrcodespix.sejaefi.com.br/v2/f85a92bd3e324d99a69d8e35667f80da",
                                "tipoCob": "cob",
                                "criacao": "2024-10-22T16:03:18.000Z"
                            },
                            "location": "qrcodespix.sejaefi.com.br/v2/f85a92bd3e324d99a69d8e35667f80da",
                            "pixCopiaECola": "00020101021226830014BR.GOV.BCB.PIX2561qrcodespix.sejaefi.com.br/v2/f85a92bd3e324d99a69d8e35667f80da5204000053039865802BR5905EFISA6008SAOPAULO62070503***6304D4F5"
                        }
                ]
                }""");
    }

    @Override
    public CobrancaImediata.Response criarCobrancaImediata(DTO_efi.Devedor devedor, double valor, int expiraEm) {
        return new CobrancaImediata.Response(
                new DTO_efi.Calendario("2024-10-23T17:10:09.564Z", 3600),
                "1e5c5b8fd6a84e7eb3dafe5f08804596",
                "0",
                "ATIVA",
                new DTO_efi.Valor(valor),
                "48c34d56-f0f7-44e8-bf0d-07e242ef98e7",
                devedor,
                "",
                new DTO_efi.Loc(14, "qrcodespix.sejaefi.com.br/v2/3dd60461c1434e598c26b6ea6dc4772a", "COB", "2024-10-23T17:10:09.586Z"),
                "qrcodespix.sejaefi.com.br/v2/3dd60461c1434e598c26b6ea6dc4772a",
                "00020101021226830014BR.GOV.BCB.PIX2561qrcodespix.sejaefi.com.br/v2/3dd60461c1434e598c26b6ea6dc4772a5204000053039865802BR5905EFISA6008SAOPAULO62070503***630445E6"
        );
    }

    @Override
    public GerarQRCode.Response criarQrCode(CobrancaImediata.Response cobrancaImediata) {
        return new GerarQRCode.Response(
            "00020101021226830014BR.GOV.BCB.PIX2561qrcodespix.sejaefi.com.br/v2/3dd60461c1434e598c26b6ea6dc4772a5204000053039865802BR5905EFISA6008SAOPAULO62070503***630445E6",
            "data:image/png;base64,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",
            "https://pix.sejaefi.com.br/cob/pagar/3dd60461c1434e598c26b6ea6dc4772a"
        );
    }
}
