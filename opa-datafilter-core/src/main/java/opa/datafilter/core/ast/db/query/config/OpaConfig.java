package opa.datafilter.core.ast.db.query.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author joffryferrater
 */
@Configuration
@ConfigurationProperties(prefix = "opa.authorization")
@Data
public class OpaConfig {

    /**
     * Enable OPA data filter authorization
     */
    private boolean dataFilterEnabled = true;
    /**
     * The OPA compile API endpoint. Set default for localhost suitable for sidecar pattern deployment
     */
    private String url = "http://localhost:8181/v1/compile";
}
