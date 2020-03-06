package com.joffer.opa.ast.to.sql.query.it;

import com.joffer.opa.ast.to.sql.query.config.OpaConfig;
import com.joffer.opa.ast.to.sql.query.service.OpaClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public OpaClientService opaClientService() {
        OpaConfig opaConfig = new OpaConfig();
        opaConfig.setUrl("http://localhost:8181/v1/compile");
        opaConfig.setDataFilterEnabled(true);
        return new OpaClientService(opaConfig);
    }
}
