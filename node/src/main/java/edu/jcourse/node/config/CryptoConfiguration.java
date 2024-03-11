package edu.jcourse.node.config;

import edu.jcourse.util.CryptoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptoConfiguration {
    @Value("${app.doc-salt}")
    private String docSalt;
    @Value("${app.photo-salt}")
    private String photoSalt;

    @Bean
    public CryptoUtil docCryptoUtil() {
        return new CryptoUtil(docSalt);
    }

    @Bean
    public CryptoUtil photoCryptoUtil() {
        return new CryptoUtil(photoSalt);
    }
}