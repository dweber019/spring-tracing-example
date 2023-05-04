package ch.basler.experimental.tracingexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Slf4j
@Configuration
public class LoggingConfig {

  @Bean
  public CommonsRequestLoggingFilter requestLoggingFilter() {
    CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
    loggingFilter.setIncludeHeaders(true);
    return loggingFilter;
  }

  @Bean
  public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
    return builder.additionalInterceptors((request, body, execution) -> {
          log.info("Method: {}, URI: {}, Headers: {}", request.getMethod(), request.getURI(), request.getHeaders());
          return execution.execute(request, body);
        })
        .build();
  }

  @Bean
  public RestTemplateCustomizer getRestTemplateCustomizer() {
    return new ProxyCustomizer();
  }
}
