package com.ebgr.pagamento_carnes.efi;

import com.ebgr.pagamento_carnes.efi.dto.CobrancaImediata;
import com.ebgr.pagamento_carnes.efi.dto.GerarQRCode;
import com.ebgr.pagamento_carnes.efi.dto.DTO_efi;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EfiHelperImpl implements EfiHelper {

    private final String client_id;
    private final String client_secret;
    private final String url;
    private String acessToken;
    private final int pixLifetime;


    private LocalDateTime expirationTime = LocalDateTime.MIN;

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public EfiHelperImpl (String client_id, String client_secret, String url, String base64P12, int pixLifetime) throws Exception {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.url = url;
        this.pixLifetime = pixLifetime;

        System.out.printf("*\n*\n* EFI [IMPL]:\n*\turl - %s\n*\tpixLifetime - %d\n*\n*\n", url, pixLifetime);


        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        byte[] decodedBytes = Base64.getDecoder().decode(base64P12);
        keyStore.load(new ByteArrayInputStream(decodedBytes), null);


        //try (InputStream keyStoreStream = new FileInputStream("src/main/resources/producao-616112-pagamento-carnes.p12")) {   keyStore.load(keyStoreStream, null);    }

        // Criar SSLContext com o keystore
        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(keyStore, null) // KeyStore sem senha
                .loadTrustMaterial((X509Certificate[] chain, String authType) -> true) // Ignorar validação de certificado (apenas para teste)
                .build();

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
        HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(socketFactory).build();
        this.httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
    }



    private void tryAuthenticate () {

        if(expirationTime.isBefore(LocalDateTime.now())) {
            System.err.println("Necessario realizar autenticacao!");
            try {
                authenticate();
                System.out.println("Autenticado com sucesso!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void authenticate() throws Exception {

        try {
            HttpPost request = new HttpPost(url + "/oauth/token");

            // Credenciais codificadas em Base64
            String auth = client_id + ":" + client_secret;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            request.setHeader("Authorization", "Basic " + encodedAuth);
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");

            // Corpo da requisição (form parameters)
            final List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("grant_type", "client_credentials"));
            request.setEntity(new UrlEncodedFormEntity(params));

            // Executar a requisição e obter a resposta
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity httpEntity = response.getEntity();
                String responseString = EntityUtils.toString(httpEntity, "UTF-8");

                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> responseMap = objectMapper.readValue(responseString, new TypeReference<Map<String, String>>() {});
                responseMap.forEach((key, value) -> System.out.println(key + ": " + value));



                int statusCode = response.getCode();
                //System.out.println("Response Code: " + statusCode);
                if (statusCode >= 200 && statusCode < 300) {
                    //System.out.println("Request was successful!");
                    this.acessToken = responseMap.get("access_token");
                    int expiresIn = Integer.parseInt(responseMap.get("expires_in"));
                    this.expirationTime = LocalDateTime.now().plusSeconds(expiresIn);
                    //System.out.println(expirationTime.toString());
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private String formatDate(LocalDateTime local) {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }

    private String objectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> decodeResponse(CloseableHttpResponse response) {
        try {
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println("responseString: " + responseString);
            return objectMapper.readValue(responseString, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ClassicHttpRequest createHttpsRequest(Method method, String query, String body) {
        ClassicHttpRequest request = ClassicRequestBuilder
                .create(method.name())
                .setUri(url + query)
                .setHeader("Authorization", "Bearer " + acessToken)
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        if (method == Method.POST || method == Method.PUT) {
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(body));
        }

        return request;
    }


    @Override
    public void exibirListaDeCobrancas() {
        tryAuthenticate();
        ClassicHttpRequest request = createHttpsRequest(
                Method.GET,
                "/v2/cobv?inicio=2020-10-22T16:01:35Z&fim=" + formatDate(LocalDateTime.now()),
                null);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.out.println("responseString: " + responseString);

            System.out.println("Response Code (consultarListaDeCobrancas): " + response.getCode());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }



    @Override
    public CobrancaImediata.Response criarCobrancaImediata (DTO_efi.Devedor devedor, double valor, String txid) {
        CobrancaImediata.Request cobrancaImediata = new CobrancaImediata.Request(
                new DTO_efi.CalendarioSemCriacao(this.pixLifetime),
                devedor,
                new DTO_efi.Valor (String.format("%.2f", valor)),
                "48c34d56-f0f7-44e8-bf0d-07e242ef98e7",
                null
        );

        System.out.println("txid: " + txid);

        tryAuthenticate();
        ClassicHttpRequest request = createHttpsRequest(
                Method.PUT,
                "/v2/cob/"+txid,
                //"/v2/cob",
                objectToJson(cobrancaImediata));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getCode();
            System.out.println("Response Code (criarCobrancaImediata): " + statusCode);
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
            System.err.println("responseString: " + responseString);
            if(statusCode >= 200 && statusCode < 300) {
                CobrancaImediata.Response responseDto = objectMapper.readValue(responseString, CobrancaImediata.Response.class);
                System.out.println(responseDto);
                return responseDto;
            } else {
                System.err.println("responseString: " + responseString);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public GerarQRCode.Response criarQrCode (CobrancaImediata.Response cobrancaImediata) {

        int loc_id = cobrancaImediata.loc().id();

        tryAuthenticate();
        ClassicHttpRequest request = createHttpsRequest(
                Method.GET,
                String.format("/v2/loc/%s/qrcode", loc_id)                ,
                objectToJson(cobrancaImediata));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getCode();
            System.out.println("Response Code (criarQrCode): " + statusCode);
            String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

            if(statusCode >= 200 && statusCode < 300) {
                GerarQRCode.Response responseDto = objectMapper.readValue(responseString, GerarQRCode.Response.class);
                System.out.println(responseDto);
                return responseDto;
            } else {
                System.err.println("criarQrCode: " + responseString);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
