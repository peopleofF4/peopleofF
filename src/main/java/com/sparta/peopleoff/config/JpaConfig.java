package com.sparta.peopleoff.config;

import com.sparta.peopleoff.common.auditing.SoftDeleteEntityListener;
import com.sparta.peopleoff.common.auditing.SpringSecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

  @Bean
  public AuditorAware<String> auditorProvider() {
    return new SpringSecurityAuditorAware(); // 현재 사용자 정보 제공
  }

  @Bean
  public SoftDeleteEntityListener softDeleteEntityListener(AuditorAware<String> auditorAware) {
    return new SoftDeleteEntityListener(auditorAware);
  }

}
