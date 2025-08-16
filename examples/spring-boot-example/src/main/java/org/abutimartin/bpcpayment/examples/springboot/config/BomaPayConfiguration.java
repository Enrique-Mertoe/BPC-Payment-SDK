package org.abutimartin.bpcpayment.examples.springboot.config;

import org.abutimartin.bpcpayment.BomaPayClient;
import org.abutimartin.bpcpayment.config.BomaPayConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot configuration for BomaPay SDK.
 */
@Configuration
@EnableConfigurationProperties(BomaPayProperties.class)
public class BomaPayConfiguration {

    /**
     * Creates a BomaPay client bean configured with application properties.
     *
     * @param properties the BomaPay properties
     * @return configured BomaPay client
     */
    @Bean
    public BomaPayClient bomaPayClient(BomaPayProperties properties) {
        BomaPayConfig config = BomaPayConfig.builder()
                .username(properties.getUsername())
                .password(properties.getPassword())
                .clientId(properties.getClientId())
                .merchantLogin(properties.getMerchantLogin())
                .baseUrl(properties.getBaseUrl())
                .currency(properties.getCurrency())
                .language(properties.getLanguage())
                .timeout(properties.getTimeout())
                .build();

        return new BomaPayClient(config);
    }
}