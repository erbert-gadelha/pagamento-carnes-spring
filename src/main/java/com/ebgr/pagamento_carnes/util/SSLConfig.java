package com.ebgr.pagamento_carnes.util;


import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

public class SSLConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    /*public RestTemplate restTemplate() throws Exception {

        // Carregar o keystore a partir do arquivo .p12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(new File("/home/ebgr/Desktop/pagamento-carnes/src/main/resources/producao-616112-pagamento-carnes.p12"))) {
            keyStore.load(fis, new char[0]);
        }

        // Configurar SSLContext com o keystore
        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(keyStore, "senha_do_certificado".toCharArray())
                .build();

        // Configurar o HttpClient com SSL
        CloseableHttpClient httpClient = HttpClients.custom()
                .setUserAgent(String.valueOf(sslContext))
                //.setSSLContext(sslContext)
                .build();

        // Usar HttpComponentsClientHttpRequestFactory para injetar o HttpClient no RestTemplate
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }*/
}
