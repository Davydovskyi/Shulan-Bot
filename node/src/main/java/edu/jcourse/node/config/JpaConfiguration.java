package edu.jcourse.node.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({
        "edu.jcourse.jpa.entity",
        "edu.jcourse.node.entity"
})
@EnableJpaRepositories(basePackages = {
        "edu.jcourse.jpa.repository",
        "edu.jcourse.node.repository"
})
public class JpaConfiguration {
}