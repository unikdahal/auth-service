package com.unik.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.unik.auth.adapters.persistence")
public class PersistenceConfig {}
