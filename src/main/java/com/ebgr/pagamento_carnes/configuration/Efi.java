package com.ebgr.pagamento_carnes.configuration;

import com.ebgr.pagamento_carnes.efi.EfiHelper;
import com.ebgr.pagamento_carnes.efi.EfiHelperImpl;
import com.ebgr.pagamento_carnes.efi.EfiHelperMock;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class Efi {

    @Getter
    @Value("${efi.client_id}")
    String client_id;
    @Getter
    @Value("${efi.client_secret}")
    String client_secret;
    @Getter
    @Value("${efi.url}")
    String url;
    @Value("${efi.base64p12}")
    private String base64P12;
    @Value("${efi.pixLifetime}")
    private int pixLifetime;

    @Bean
    @Profile("prd")
    public EfiHelper efiHelperImpl() throws Exception {
        return new EfiHelperImpl(client_id, client_secret, url, base64P12, pixLifetime);
    }

    @Bean
    @Profile("dev")
    public EfiHelper efiHelperMock() {
        return new EfiHelperMock(pixLifetime);
    }
}
