package com.ebgr.pagamento_carnes.configuration;

import com.ebgr.pagamento_carnes.efi.EfiHelper;
import com.ebgr.pagamento_carnes.efi.EfiHelperMock;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    /*@Bean
    public EfiHelper efiHelperImpl() throws Exception{
        return new EfiHelperImpl(client_id, client_secret, url);
    }*/

    @Bean
    public EfiHelper efiHelperMock() {
        return new EfiHelperMock();
    }

}
