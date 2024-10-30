package com.ebgr.pagamento_carnes.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.KeyManagerFactory;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.util.Base64;

public class InterApiClient {

    public static void main(String[] args) throws Exception {

        for(String arg : args) {
            System.out.println(arg);
        }

        // Carregando o arquivo .p12
        char[] password = "".toCharArray(); // senha do seu certificado .p12
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        String cert_path = "/home/ebgr/Desktop/pagamento-carnes/src/main/resources/producao-616112-pagamento-carnes.p12";
        try (FileInputStream fis = new FileInputStream(cert_path)) {
            keyStore.load(fis, password);
        }

        // Configurando KeyManagerFactory e TrustManagerFactory
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keyStore, password);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        // Criando o SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        // Configurando o HttpClient com SSL
        HttpClient client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();

        // Configurar URL da API do Banco Inter

        //URI uri = new URI("https://api.bancointer.com.br/oauth/v2/token");
        URI uri = new URI("https://pix.api.efipay.com.br/oauth/token");

        // Configurar client_id e client_secret para autenticação
        String clientId = "Client_Id_ba252dc29671b01ca35f75f324927170a0770df5";
        String clientSecret = "Client_Secret_ea66aaa23b4fc5b85c964ddc00e32d12bceec18b";

        // Montando o Authorization Header
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        // Criando a requisição HTTP POST
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Basic " + encodedAuth)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                .build();

        // Enviando a requisição e recebendo a resposta
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Exibir a resposta
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
}
