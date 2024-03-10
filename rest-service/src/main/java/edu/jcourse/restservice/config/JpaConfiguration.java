package edu.jcourse.restservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({
        "edu.jcourse.jpa.entity"
})
@EnableJpaRepositories(basePackages = {
        "edu.jcourse.jpa.repository"
})
public class JpaConfiguration {
}