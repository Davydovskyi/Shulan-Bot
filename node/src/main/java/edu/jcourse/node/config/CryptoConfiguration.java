package edu.jcourse.node.config;

import edu.jcourse.util.CryptoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptoConfiguration {
    @Value("${app.doc-secret}")
    private String docSecret;
    @Value("${app.photo-secret}")
    private String photoSecret;
    @Value("${app.user-secret}")
    private String userSecret;

    @Bean
    public CryptoUtil docCryptoUtil() {
        return new CryptoUtil(docSecret);
    }

    @Bean
    public CryptoUtil photoCryptoUtil() {
        return new CryptoUtil(photoSecret);
    }

    @Bean
    public CryptoUtil userCryptoUtil() {
        return new CryptoUtil(userSecret);
    }
}