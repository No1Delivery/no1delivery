package com.sparta.no1delivery.global.infrastructure.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.no1delivery.global.domain.service.UserDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JPAConfig {

        private final Long SYSTEM_ID = 0L;

        @PersistenceContext
        private EntityManager em;

        @Bean
        public JPAQueryFactory jpaQueryFactory() {
                return new JPAQueryFactory(em);
        }

        @Bean
        public AuditorAware<Long> auditorProvider(UserDetails userDetails) {
                return () -> Optional.ofNullable(userDetails == null ? SYSTEM_ID : userDetails.getId()); // 0L
        }
}