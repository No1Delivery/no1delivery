package com.sparta.no1delivery.global.infrastructure.persistence;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.no1delivery.global.infrastructure.security.SecurityUserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@RequiredArgsConstructor
public class JPAConfig {

        @PersistenceContext
        private EntityManager em;

        private final SecurityUserDetails securityUserDetails;

        @Bean
        public JPAQueryFactory jpaQueryFactory() {
            return new JPAQueryFactory(em);
        }

        @Bean
        public AuditorAware<Long> auditorProvider() {
                return ()-> Optional.ofNullable(securityUserDetails.getId());
        }
}

